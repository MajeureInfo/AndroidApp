package com.example.pedrp.roommanagement;


import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class RoomContextHttpManager {

    //static Context mCtx;
    //static RequestQueue queue;
    //String url = CONTEXT_SERVER_URL + "/" + room + "/";
    static String url_api = "https://pure-island-76277.herokuapp.com/api/rooms/";

    public RoomContextHttpManager(Context context){
        //this.mCtx = context;
        //this.queue = Volley.newRequestQueue(mCtx.getApplicationContext());
    }

    public static void retrieveRoomContextState(String room, final ContextManagementActivity aCtx, Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        System.out.println("Retrieve");
        String url = url_api + room;
        System.out.println(url);
        //get room sensed context
        JsonObjectRequest contextRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id = Integer.parseInt(response.getString("id").toString());
                            int lightLevel = Integer.parseInt(response.getJSONObject("light").get("level").toString());
                            String lightStatus = response.getJSONObject("light").get("status").toString();
                            int noiseLevel = Integer.parseInt(response.getJSONObject("noise").get("level").toString());
                            String ringerStatus = response.getJSONObject("noise").get("status").toString();

                            RoomContextState roomCtx = new RoomContextState(id, lightStatus, ringerStatus, lightLevel, noiseLevel);
                            aCtx.onUpdate(roomCtx);
                            // notify main activity for update...
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       error.printStackTrace(); // Some error to access URL : Room may not exists...
                    }
                });
        queue.add(contextRequest);
    }

    public static void switchLight(final String room, final ContextManagementActivity aCtx, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        System.out.println("Switch Light " + room);
        String url = url_api + room + "/switch-light/";

        //Switch Light

        JsonArrayRequest contextRequest = new JsonArrayRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj = new JSONObject();
                            for (int i = 0; i < response.length(); i++){
                                if (response.getJSONObject(i).getString("id").toString().equals(room)){
                                    obj = response.getJSONObject(i);
                                }
                            }
                            int id = Integer.parseInt(obj.getString("id").toString());
                            int lightLevel = Integer.parseInt(obj.getJSONObject("light").get("level").toString());
                            String lightStatus = obj.getJSONObject("light").get("status").toString();
                            int noiseLevel = Integer.parseInt(obj.getJSONObject("noise").get("level").toString());
                            String ringerStatus = obj.getJSONObject("noise").get("status").toString();
                            RoomContextState roomCtx = new RoomContextState(id, lightStatus, ringerStatus, lightLevel, noiseLevel);
                            aCtx.onUpdate(roomCtx);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); // Some error to access URL : Room may not exists...
                    }
                });
        queue.add(contextRequest);

    }

    public static void switchRinger(final String room, final ContextManagementActivity aCtx, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        System.out.println("Switch Ringer " + room);
        String url = url_api + room + "/switch-ringer/";

        //Switch Ringer

        JsonArrayRequest contextRequest = new JsonArrayRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject obj = new JSONObject();
                            for (int i = 0; i < response.length(); i++){
                                if (response.getJSONObject(i).getString("id").toString().equals(room)){
                                    obj = response.getJSONObject(i);
                                }
                            }
                            int id = Integer.parseInt(obj.getString("id").toString());
                            int lightLevel = Integer.parseInt(obj.getJSONObject("light").get("level").toString());
                            String lightStatus = obj.getJSONObject("light").get("status").toString();
                            int noiseLevel = Integer.parseInt(obj.getJSONObject("noise").get("level").toString());
                            String ringerStatus = obj.getJSONObject("noise").get("status").toString();
                            RoomContextState roomCtx = new RoomContextState(id, lightStatus, ringerStatus, lightLevel, noiseLevel);
                            aCtx.onUpdate(roomCtx);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); // Some error to access URL : Room may not exists...
                    }
                });
        queue.add(contextRequest);

    }

    public static void retrieveRooms(final ContextManagementActivity aCtx, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = url_api;
        System.out.println(url_api);

        //Switch Ringer

        JsonArrayRequest contextRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("RESPONSE");
                        System.out.println(response);
                        try {
                            List<String> list = new ArrayList<String>();
                            for (int i = 0; i < response.length(); i++){
                                list.add(response.getJSONObject(i).getString("id").toString());
                                System.out.println("id " + list.get(i));
                            }
                            aCtx.fillSpinner(list);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); // Some error to access URL : Room may not exists...
                    }
                });
        queue.add(contextRequest);

    }
}
