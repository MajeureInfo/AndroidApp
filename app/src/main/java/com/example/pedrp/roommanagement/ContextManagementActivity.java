package com.example.pedrp.roommanagement;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ContextManagementActivity extends AppCompatActivity {

    private String room = "";
    private RoomContextState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("created!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_management);
        //Retrieve Room
        ((Button) findViewById(R.id.buttonCheck)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Click");
                room = ((EditText) findViewById(R.id.editText1))
                        .getText().toString();
                retrieveRoomContextState(room);
            }
        });

        //Switch Light
        ((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("SwitchLight");
                switchLight(room);
            }
        });

        //Switch Ringer
        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("SwitchRinger");
                switchRinger(room);
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

}
