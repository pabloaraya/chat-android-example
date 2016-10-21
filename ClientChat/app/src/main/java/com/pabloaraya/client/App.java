package com.pabloaraya.client;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by pablo on 10/17/16.
 */

public class App extends Application {

    /* Socket Constant */
    final public static String URL_SOCKET  = "http://10.0.2.42/";
    final public static String URL_LOGIN = URL_SOCKET.concat("login");

    /* Events Constants */
    final public static String EVENT_MESSAGE = "message";

    /* Variables Constant */
    final public static String VAR_MESSAGE = "message";
    final public static String VAR_USERNAME = "username";
    final public static String VAR_ERROR = "error";
    final public static String VAR_STATUS = "status";
    final public static String VAR_OK = "ok";
    final public static String VAR_EMPTY = "";

    /* SharedPreference */
    private static SharedPreferences sharedPreferences;

    /* Queue Volley */
    private static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();

        /* SharedPreference */
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        /* Queue Volley */
        queue = Volley.newRequestQueue(this);
    }

    public static SharedPreferences getSession(){
        return sharedPreferences;
    }

    public static RequestQueue getQueue() {
        return queue;
    }
}
