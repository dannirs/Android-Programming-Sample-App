package com.example.androidassignments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "TestToolbar";
    Snackbar snackbar;
    String message;
    ImageButton btnImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        /*btnImg = findViewById(R.id.btn_img);*/
        View background = findViewById(R.id.background);
        setSupportActionBar(toolbar);
        message = getString(R.string.snkb_message);
        snackbar = Snackbar.make(background, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String displayMessage = getString(R.string.fab);
                Snackbar.make(view, displayMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public boolean onCreateOptionsMenu (Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
       int objId = mi.getItemId();
        switch (objId) {
            case R.id.action_one:
                Log.d(ACTIVITY_NAME, "Option 1 selected");
                snackbar.show();
                return true;
            case R.id.action_two:
                //Start an activity…
                Log.d(ACTIVITY_NAME, "Option 2 selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
                builder.setTitle(R.string.returnTitle);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.action_three:
                Log.d(ACTIVITY_NAME, "Option 3 selected");
                LayoutInflater inflater = TestToolbar.this.getLayoutInflater();
                AlertDialog.Builder builder2 = new AlertDialog.Builder(TestToolbar.this);
                builder2.setTitle(R.string.enterMsg);
                final View view = inflater.inflate(R.layout.custom_dialog, null);
                builder2.setView(view);
                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText edit = view.findViewById(R.id.dialog_message_box);
                        String message = edit.getText().toString();
                        snackbar.setText(message);
                    }
                });
                builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder2.create();
                builder2.show();
                break;
            case R.id.about:
                Log.d(ACTIVITY_NAME, "About selected");
                CharSequence text = "Version 1.0, by ";
                String name = getString(R.string.name);
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(TestToolbar.this, text + name, duration);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
        return true;
    }

}