package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        // create sharedpreferences object
        final SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        EditText editText = findViewById(R.id.plain_text_input);
        // set the default value that will be displayed if "Email" key is null
        String defaultEmail = sharedPref.getString("Email", "email@domain.com");
        editText.setText(defaultEmail);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // write to shared preferences
                SharedPreferences.Editor editor = sharedPref.edit();
                EditText editText = findViewById(R.id.plain_text_input);
                editor.putString("Email", editText.getText()+"");
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onStart(){

        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onResume() {

        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onPause(){

        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop(){

        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    protected void onDestroy(){

        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");

    }

}


