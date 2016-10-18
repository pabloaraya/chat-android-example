package com.pabloaraya.client;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import static com.pabloaraya.client.App.EVENT_MESSAGE;
import static com.pabloaraya.client.App.VAR_EMPTY;


public class MainActivity extends AppCompatActivity {

    /* UI Elements */
    Toolbar toolbar;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    LinearLayoutManager linearLayoutManager;

    /* Socket */
    Socket socket;

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

        /* Initialize recycler view */
        recyclerView = (RecyclerView)findViewById(R.id.listViewChat);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        /* Set adapter */
        messageAdapter = new MessageAdapter(this);
        recyclerView.setAdapter(messageAdapter);

        try {
            socket = IO.socket(App.SOCKET_URL);
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
                                messageJson.getString(App.VAR_MESSAGE),
                                messageJson.getString(App.VAR_USERNAME));

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
                        messageJson.put(App.VAR_MESSAGE, editTextMessage.getText().toString());
                        messageJson.put(App.VAR_USERNAME, App.getSession().getString(App.VAR_USERNAME, VAR_EMPTY));

                        socket.emit(EVENT_MESSAGE, messageJson);

                        MessageModel messageModel =
                                new MessageModel(editTextMessage.getText().toString(),
                                        App.getSession().getString(App.VAR_USERNAME, VAR_EMPTY));
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
