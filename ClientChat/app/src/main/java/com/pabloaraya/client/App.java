package com.pabloaraya.client;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by pablo on 10/17/16.
 */

public class App extends Application {

    /* Socket Constant */
    final public static String SOCKET_URL  = "http://192.168.56.1/";
    final public static String EVENT_MESSAGE = "message";

    /* Variables Constant */
    final public static String VAR_MESSAGE = "message";
    final public static String VAR_EMPTY = "unknown";
    final public static String VAR_USERNAME = "name";

    /* SharedPreference */
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        /* SharedPreference */
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
    }

    public static SharedPreferences getSession(){
        return sharedPreferences;
    }
}
