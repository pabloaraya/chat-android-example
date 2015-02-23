package com.pabloaraya.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends ActionBarActivity {

    final private static String VAR_USERNAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences userPreference = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        final SharedPreferences.Editor userEditorPreference = userPreference.edit();

        final EditText editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        Button buttonJoin = (Button)findViewById(R.id.buttonJoin);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextUsername.getText().toString().isEmpty()){
                    userEditorPreference.putString(VAR_USERNAME, editTextUsername.getText().toString());
                    userEditorPreference.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
