package com.example.androidassignments;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    public static SQLiteDatabase database;
    private static ChatDatabaseHelper chatDbHelper;
    ArrayList<String> chatMessage = new ArrayList<String>();
    Cursor cursor;
    ChatAdapter messageAdapter;
    MessageFragment messageFragment;
    private ListView listView;
    private EditText chatbox;
    private Button chatbutton;
    Boolean bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        listView = (ListView)findViewById(R.id.listView);
        chatbox   = (EditText) findViewById(R.id.editText);
        messageAdapter =new ChatAdapter( this );
        listView.setAdapter (messageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    bool = true;
                    Log.i(ACTIVITY_NAME, "true");
                } else {
                    bool = false;
                    Log.i(ACTIVITY_NAME, "false");
                }
                String messa=(String)adapter.getItemAtPosition(position);
                long mId  = messageAdapter.getItemId(position);
                String messageId =String.valueOf( mId);
                if(bool == true)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("message", messa );
                    bundle.putString("messageId",messageId);

                    messageFragment = new MessageFragment();
                    messageFragment.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.frame2, messageFragment).commit();

                } else {
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtra("message", messa);
                    intent.putExtra("messageId", messageId);
                    startActivityForResult(intent, 10);
                }
            }
        });

        chatDbHelper = new ChatDatabaseHelper(this);
        database = chatDbHelper.getWritableDatabase();
        String[] columns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
        cursor = database.query(ChatDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                chatMessage.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                cursor.moveToNext();
            }
        }

        Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount() );

        for(int i = 0; i < cursor.getColumnCount(); i++){
            System.out.println(cursor.getColumnName(i));
        }

        chatbutton = (Button)findViewById(R.id.button);
        chatbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newString1 = chatbox.getText().toString();
                chatMessage.add(newString1);
                chatbox.setText("");
                ContentValues cValues = new ContentValues();
                cValues.put(ChatDatabaseHelper.KEY_MESSAGE, newString1);
                database.insert(ChatDatabaseHelper.TABLE_NAME, null, cValues);
                String[] columns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
                cursor = database.query(ChatDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
                chatMessage.clear();
                if(cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                        chatMessage.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                        cursor.moveToNext();
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }
        });
    }


    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return chatMessage.size();
        }

        public String getItem(int position) {
            return chatMessage.get(position);
        }

        public long getItemId(int position) {
            cursor.moveToPosition(position);
            long dbId = 0;
            if (cursor.getCount() > position) {
                dbId = cursor.getLong(0);
            }
            return dbId;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }

    public void deleteMsg(int id) {
        database = chatDbHelper.getWritableDatabase();
        database.delete(ChatDatabaseHelper.TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        String[] columns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
        cursor = database.query(ChatDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        chatMessage.clear();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                chatMessage.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                cursor.moveToNext();
            }
        }
    }

    public void tabletDeleteMsg(int id) {
        deleteMsg(id);
        messageAdapter.notifyDataSetChanged();
        getFragmentManager().beginTransaction().remove(messageFragment).commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            int a = resultCode;
            deleteMsg(a);
            messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        database.close();
    }
}
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentTransaction;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//public class ChatWindow extends AppCompatActivity {
//
//    public static SQLiteDatabase database;
//    private static ChatDatabaseHelper db;
//    ArrayList<String> arr;
//    protected static final String ACTIVITY_NAME = "ChatWindow";
//    private static Boolean layout;
//    public Cursor cursor;
//    public static String BUNDLE_NAME = "Details";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat_window);
//        if (findViewById(R.id.frame2) != null) {
//            Log.i(ACTIVITY_NAME, "Using tablet layout; framelayout loaded");
//            layout = true;
//        }
//        else {
//            Log.i(ACTIVITY_NAME, "Using phone layout; framelayout not loaded");
//            layout = false;
//        }
//
//        final EditText chatBox = findViewById(R.id.editText);
//        final ListView chat = findViewById(R.id.listView);
//        final Button sendButton = findViewById(R.id.button);
//        arr=new ArrayList<String>();
//
//        final ChatAdapter messageAdapter = new ChatAdapter( this );
//        chat.setAdapter (messageAdapter);
//
//        db = new ChatDatabaseHelper(this);
//        database = db.getWritableDatabase();
//        String[] columns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
//        cursor = database.query(ChatDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
//        cursor.moveToFirst();
//        if (cursor == null) {
//            Log.i(ACTIVITY_NAME, "null cursor");
//        }
//        else if (cursor.getCount() == 0){
//            Log.i(ACTIVITY_NAME, "count = 0");
//        }
//        else {
//            while(!cursor.isAfterLast() ) {
//                arr.add(cursor.getString(cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ));
//                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
//                cursor.moveToNext();
//            }
//
//            Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount() );
//            int columnsQty = cursor.getColumnCount();
//            for (int idx = 0; idx < columnsQty; ++idx) {
//                String name = cursor.getColumnName(idx);
//                Log.i(ACTIVITY_NAME, "Cursor column name: " + name);
//            }
//        }
//
//        chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                String message = messageAdapter.getItem(position);
//                Bundle msgBundle = new Bundle();
//                msgBundle.putString(ChatDatabaseHelper.KEY_MESSAGE, message);
//                msgBundle.putLong(ChatDatabaseHelper.KEY_ID, messageAdapter.getItemId(position));
//                if (findViewById(R.id.frame2) != null) {
//                    Log.i(ACTIVITY_NAME, "Using tablet layout; framelayout loaded");
//                    layout = true;
//                }
//                else {
//                    Log.i(ACTIVITY_NAME, "Using phone layout; framelayout not loaded");
//                    layout = false;
//                }
//                if (layout == true) {
//                    MessageFragment fragment = new MessageFragment();
//                    FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//                    fragment.setArguments(msgBundle);
//                    trans.replace(R.id.frame2, fragment);
//                    trans.commit();
//                }
//                else {
//                    Intent details = new Intent(ChatWindow.this, MessageDetails.class);
//                    details.putExtra(BUNDLE_NAME, msgBundle);
//                    startActivityForResult(details, 10);
//                }
//            }
//        });
//
//        messageAdapter.notifyDataSetChanged();
////        cursor.close();
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                arr.add(chatBox.getText().toString());
//
//                ContentValues values = new ContentValues();
//                values.put(ChatDatabaseHelper.KEY_MESSAGE, chatBox.getText().toString());
//                database.insert(ChatDatabaseHelper.TABLE_NAME, null,
//                        values);
//
//                messageAdapter.notifyDataSetChanged();
//                //this restarts the process of getCount()/getView()
//                chatBox.setText("");
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        Log.i(ACTIVITY_NAME, "Calling onDestroy()");
//        database.close();
//    }
//
//    private class ChatAdapter extends ArrayAdapter<String> {
//
//        Context ctx;
//        public ChatAdapter(Context ctx) {
//            super(ctx, 0);
//        }
//
//        public int getCount() {
//            return arr.size();
//        }
//
//        public String getItem(int position) {
//            return arr.get(position);
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
//            View result = null;
//            if(position%2 == 0)
//                result = inflater.inflate(R.layout.chat_row_incoming, null);
//            else
//                result = inflater.inflate(R.layout.chat_row_outgoing, null);
//
//            TextView message = (TextView)result.findViewById(R.id.message_text);
//            message.setText(getItem(position)); // get the string at position
//            return result;
//        }
//
//        public long getItemId(int position) {
//            long id;
//            cursor.moveToPosition(position);
//            id = cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
//            return id;
//        }
//
//    }
//
//}