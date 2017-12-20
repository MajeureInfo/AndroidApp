package com.example.pedrp.roommanagement;


import android.content.Context;
import android.provider.Settings;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


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
                            // do something with results...
                            System.out.println("here!");
                            RoomContextState roomCtx = new RoomContextState(id, lightStatus, lightLevel, noiseLevel);
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

}
