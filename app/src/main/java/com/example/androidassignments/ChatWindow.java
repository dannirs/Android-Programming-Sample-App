package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        final EditText chatBox = findViewById(R.id.editText);
        final ListView chat = findViewById(R.id.listView);
        final Button sendButton = findViewById(R.id.button);
        arr=new ArrayList<String>();
        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter( this );
        chat.setAdapter (messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                arr.add(chatBox.getText().toString());
                messageAdapter.notifyDataSetChanged();
                //this restarts the process of getCount()/getView()
                chatBox.setText("");
            }
        });
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        Context ctx;
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return arr.size();
        }

        public String getItem(int position) {
            return arr.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }

    }

}