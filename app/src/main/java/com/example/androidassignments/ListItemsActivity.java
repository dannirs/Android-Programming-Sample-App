package com.example.androidassignments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    static final int REQUEST_IMAGE_CAPTURE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        // check if permission is granted to use the camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
             //Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                setResult(ListItemsActivity.RESULT_OK, takePictureIntent);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

        });

        Switch switch1 = findViewById(R.id.switch1);
        if (switch1 != null) {
            switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        // if the switch is on, display Toast message saying it's on in current activity
                        String strText = "@string/OnMsg";
                        CharSequence text = "Switch is On";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                        toast.show();
                    } else {
                        // if the switch if off, display Toast message it's off in current activity
                        CharSequence text = "Switch is Off";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                        toast.show();
                    }
                }
            });
        }

        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // creates a dialog box
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                    // sets the dialog box's message, title, and the 2 buttons (yes and no)
                    builder.setMessage(R.string.dialog_message)
                            .setTitle(R.string.dialog_title)
                            // if user clicks yes, then finish() is called
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog2, int id) {
                                    Intent resultIntent = new Intent(  );
                                    resultIntent.putExtra("Response", "Here is my response");
                                    setResult(ListItemsActivity.RESULT_OK, resultIntent);
                                    finish();
                                    Log.i(ACTIVITY_NAME, "In onFinish()");
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // closes dialog box, nothing happens
                                }
                            })
                            .show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                // gets the captured picture and sets it as the imagebutton display
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageButton imageButton = findViewById(R.id.imageButton);
                imageButton.setImageBitmap(imageBitmap);
            }
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