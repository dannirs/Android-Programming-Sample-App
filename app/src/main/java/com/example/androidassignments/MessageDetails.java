package com.example.androidassignments;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        String message = getIntent().getStringExtra("message");
        String messageId = getIntent().getStringExtra("messageId");

        Bundle bundle = new Bundle();
        bundle.putString("message", message );
        bundle.putString("messageId", messageId );

        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.detailsFrame, messageFragment).commit();

    }
}

//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentTransaction;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//public class MessageDetails extends AppCompatActivity {
//
//    Intent messageDetailsIntent;
//    Bundle passBackBundle;
//    public static String PASS_BACK = "Passbackbundle";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_message_details);
//
//        Bundle bundle = getIntent().getExtras().getBundle(ChatWindow.BUNDLE_NAME);
//        MessageFragment fragment = new MessageFragment();
//        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//        fragment.setArguments(bundle);
//        trans.replace(R.id.detailsFrame, fragment);
//        trans.commit();
//        long msgId= bundle.getLong(ChatDatabaseHelper.KEY_ID);
//        passBackBundle.putLong("ID", msgId);
//        messageDetailsIntent = new Intent(MessageDetails.this, ChatWindow.class);
//        messageDetailsIntent.putExtra(PASS_BACK, passBackBundle);
//    }
//
//    public void buttonDo() {
//        Log.d("test", "working");
//        setResult(12, messageDetailsIntent);
//        finish();
//    }
//}