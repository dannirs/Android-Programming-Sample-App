package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        final Button button = findViewById(R.id.button);
        // when the user clicks the button in MainActivity:
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // opens ListItemsActivity
                Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
                // sets requestCode = 10
                startActivityForResult(intent, 10);
            }
        });
        final Button chatButton = findViewById(R.id.startButton);
        // when the user clicks the button in MainActivity:
        chatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(MainActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });
        final Button toolbarButton = findViewById(R.id.testToolbar);
        // when the user clicks the button in MainActivity:
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User started TestToolbar");
                Intent intent = new Intent(MainActivity.this, TestToolbar.class);
                startActivity(intent);
            }
        });
    }


    protected void onActivityResult (int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        // checks to see if requestCode is 10 and if checkbox has been checked and user clicked 'Yes'
        if(requestCode==10 && responseCode == ListItemsActivity.RESULT_OK)
        {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
            // gets the string from extra data container of the intent
            String messagePassed = data.getStringExtra("Response");
            if (messagePassed != null) {
                CharSequence text = "ListItemsActivity passed: My information to share";
                // creates a Toast that will display upon returning to MainActivity
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(MainActivity.this, text, duration);
                toast.show();
            }
        }
        // if checkbox hasn't been checked and user hasn't clicked yes, then no Toast appears
        else if (requestCode==10) {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
        }
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