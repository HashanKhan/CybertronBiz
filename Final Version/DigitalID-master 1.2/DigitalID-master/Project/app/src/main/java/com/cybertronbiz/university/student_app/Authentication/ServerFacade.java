package com.cybertronbiz.university.student_app.Authentication;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ServerFacade {


    public static void sendRegisterUserToQRCode(final Context context, String username, String qrCode) throws UnsupportedEncodingException {

        String url ="http://"+Configuration.getInstance().getChatServerIp()+":"+Configuration.getInstance().getChatServerPort()+"/session/auth?mobileid="+
                URLEncoder.encode( username, "UTF-8")
                +"&qrCode="+
                URLEncoder.encode(qrCode, "UTF-8");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(context, "Sent DATA !!!", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "That didn't work", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void sendVerificationToServer(final Context context, String username, String qrCode,boolean s,String userNameWeb,String passwordWeb) throws UnsupportedEncodingException {

        String verification;
        if(s){
            verification = "success";
        }
        else {
            verification = "failed";
        }

        String url ="http://"+Configuration.getInstance().getChatServerIp()+":"+Configuration.getInstance().getChatServerPort()+"/session/verify?mobileid="+
                URLEncoder.encode( username, "UTF-8")
                +"&qrCode="+
                URLEncoder.encode(qrCode, "UTF-8")
                +"&verification="+
                URLEncoder.encode(verification, "UTF-8")
                +"&userNameWeb="+
                URLEncoder.encode(userNameWeb, "UTF-8")
                +"&passwordWeb="+
                URLEncoder.encode(passwordWeb, "UTF-8");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(context, "Sent DATA !!!", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "That didn't work", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
