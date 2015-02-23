package com.pabloaraya.client;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    /* Socket Constant */
    final private static String SOCKET_URL  = "http://192.168.56.1/";
    final private static String EVENT_MESSAGE = "message";

    /* Variables Constant */
    final private static String VAR_MESSAGE = "message";
    final private static String VAR_NAME = "name";
    final private static String VAR_EMPTY = "unknown";

    /* UI Elements */
    Toolbar toolbar;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    LinearLayoutManager linearLayoutManager;

    /* Socket */
    Socket socket;

    /* SharedPreference */
    SharedPreferences userPreference;
    SharedPreferences.Editor userEditorPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Set actionbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        /* SharedPreference */
        userPreference = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        userEditorPreference = userPreference.edit();

        /* Initialize recycler view */
        recyclerView = (RecyclerView)findViewById(R.id.listViewChat);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        /* Set adapter */
        messageAdapter = new MessageAdapter(this);
        recyclerView.setAdapter(messageAdapter);

        try {
            socket = IO.socket(SOCKET_URL);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                }
            });
            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                }
            });
            socket.on(EVENT_MESSAGE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject messageJson = new JSONObject(args[0].toString());
                        final MessageModel messageModel = new MessageModel(
                                messageJson.getString(VAR_MESSAGE),
                                messageJson.getString(VAR_NAME));

                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messageAdapter.addMessage(messageModel);
                                    }
                                });
                            }
                        };
                        thread.start();
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        final EditText editTextMessage = (EditText)findViewById(R.id.editTextMessage);
        ImageButton btnSend = (ImageButton)findViewById(R.id.btnSendMessage);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextMessage.getText().toString().isEmpty()){
                    JSONObject messageJson = new JSONObject();
                    try {
                        messageJson.put(VAR_MESSAGE, editTextMessage.getText().toString());
                        messageJson.put(VAR_NAME, userPreference.getString(VAR_NAME, VAR_EMPTY));

                        socket.emit(EVENT_MESSAGE, messageJson);

                        MessageModel messageModel =
                                new MessageModel(editTextMessage.getText().toString(),
                                        userPreference.getString(VAR_NAME, VAR_EMPTY));
                        messageAdapter.addMessage(messageModel);

                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                        editTextMessage.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
