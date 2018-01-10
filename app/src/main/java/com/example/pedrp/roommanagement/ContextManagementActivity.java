package com.example.pedrp.roommanagement;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;

import helpers.MqttHelper;
import helpers.MqttSubscriber;

public class ContextManagementActivity extends AppCompatActivity {

    private String room = null;
    private RoomContextState state;
    MqttHelper mqttHelper;
    MqttSubscriber mqttSubscriber;
    /*
    MemoryPersistence memPer = new MemoryPersistence();

    final MqttAndroidClient client = new MqttAndroidClient(
            this, "wss://m23.cloudmqtt.com:34160", "roomPublisher", memPer);
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_management);

        //initialize the MQTT Client
        startMqtt();
        startSubscriber();

        //Retrieve the Rooms
        retrieveRooms();

        //Retrieve Room
        ((Button) findViewById(R.id.buttonCheck)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                room = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
                if (room != null)
                    retrieveRoomContextState(room);
            }
        });

        //Switch Light
        ((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("SwitchLight");
                if (room != null) {
                    switchLight(room);
                    publish(room, "light");
                }

            }
        });

        //Switch Ringer
        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("SwitchRinger");
                if (room != null) {
                    switchRinger(room);
                    publish(room, "ringer");
                }
            }
        });

    }

    protected void onUpdate(RoomContextState context){
        this.state = context;
        updateContextView();
    }

    private void updateContextView() {
        System.out.println("update");
        //if (this.state != null) {
            //contextView.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textViewLightValue)).setText(Integer
                    .toString(state.getLightLevel()));
            ((TextView) findViewById(R.id.textViewNoiseValue)).setText(Float
                    .toString(state.getNoiseLevel()));
            ImageView image = (ImageView)findViewById(R.id.imageView1);
            ImageView image_ringer = (ImageView)findViewById(R.id.imageView2);
            System.out.println(state.getLightStatus());
            if (state.getLightStatus().equals("ON")) {
                image.setImageResource(R.drawable.ic_bulb_on);
            }else
                image.setImageResource(R.drawable.ic_bulb_off);
            if (state.getRingerStatus().equals("ON")) {
                image_ringer.setImageResource(R.drawable.ic_ringer_on);
            }else
                image_ringer.setImageResource(R.drawable.ic_ringer_off);
        //} else {
        //    initView();
        //}
    }

    protected void retrieveRoomContextState(String room){
        RoomContextHttpManager.retrieveRoomContextState(room, this, this.getApplicationContext());
    }

    protected void switchLight(String room){
        RoomContextHttpManager.switchLight(room, this, this.getApplicationContext());
    }

    protected void switchRinger(String room){
        RoomContextHttpManager.switchRinger(room, this, this.getApplicationContext());
    }

    protected void retrieveRooms(){
        System.out.println("Retrieve Rooms");
        RoomContextHttpManager.retrieveRooms(this, this.getApplicationContext());
    }

    protected void fillSpinner(List<String> list){

        //Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void startMqtt(){
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                System.out.println("Connect Complete");

            }

            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Connect Lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
                System.out.println(mqttMessage.toString());
                System.out.println("Message Received");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    private void startSubscriber(){
        mqttSubscriber = new MqttSubscriber(getApplicationContext());
        mqttSubscriber.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                System.out.println("Connect Complete");

            }

            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Connect Lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
                System.out.println(mqttMessage.toString());
                updateMqtt(topic, mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    private void publish(String room, String lightnoise) {
        MqttMessage message = new MqttMessage("switch,appClient".getBytes());
        try {
            mqttHelper.mqttAndroidClient.publish("rooms/" + room + "/" + lightnoise, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void updateMqtt(String topic, String message){
        System.out.println("Update MQTT");
        if (!message.toString().contains(",")) {
            System.out.println("Bad message received.");
            return;
        }
        String sender = message.toString().split(",")[1];
        message = message.toString().split(",")[0];
        System.out.println("[mqtt] " + topic + " : " + message + " from " + sender);
        //String type = topic.split("/")[2];
        String roomId = topic.split("/")[1];
        System.out.println(roomId);
        if (message.equals("switch") && (sender.equals("arduinoClient") || sender.equals("webClient")) && room.equals(roomId)) {
            retrieveRoomContextState(roomId);
        }
    }
}
