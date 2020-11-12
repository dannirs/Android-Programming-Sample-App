package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

    public static SQLiteDatabase database;
    private static ChatDatabaseHelper db;
    ArrayList<String> arr;
    protected static final String ACTIVITY_NAME = "ChatWindow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        final EditText chatBox = findViewById(R.id.editText);
        final ListView chat = findViewById(R.id.listView);
        final Button sendButton = findViewById(R.id.button);
        arr=new ArrayList<String>();

        final ChatAdapter messageAdapter = new ChatAdapter( this );
        chat.setAdapter (messageAdapter);

        db = new ChatDatabaseHelper(this);
        database = db.getWritableDatabase();
        String[] columns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
        Cursor cursor = database.query(ChatDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor == null) {
            Log.i(ACTIVITY_NAME, "null cursor");
        }
        else if (cursor.getCount() == 0){
            Log.i(ACTIVITY_NAME, "count = 0");
        }
        else {
            while(!cursor.isAfterLast() ) {
                arr.add(cursor.getString(cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
                cursor.moveToNext();
            }

            Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount() );
            int columnsQty = cursor.getColumnCount();
            for (int idx = 0; idx < columnsQty; ++idx) {
                String name = cursor.getColumnName(idx);
                Log.i(ACTIVITY_NAME, "Cursor column name: " + name);
            }
        }

        messageAdapter.notifyDataSetChanged();
        cursor.close();

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                arr.add(chatBox.getText().toString());

                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, chatBox.getText().toString());
                database.insert(ChatDatabaseHelper.TABLE_NAME, null,
                        values);

                messageAdapter.notifyDataSetChanged();
                //this restarts the process of getCount()/getView()
                chatBox.setText("");
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "Calling onDestroy()");
        database.close();
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