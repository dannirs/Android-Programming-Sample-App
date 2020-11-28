package com.example.androidassignments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MessageFragment extends Fragment {
    //    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    ChatWindow chat;
    TextView view1, view2;
    Button delete;
    String idVal;

    public MessageFragment() {
        // Required empty public constructor
    }


    public static MessageFragment newInstance(ChatWindow chat) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.chat = chat;
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_message, container, false);
        Bundle bundle = this.getArguments();
        String messageVal = bundle.getString("message");
        idVal = bundle.getString("messageId");

        delete= myView.findViewById(R.id.delButton);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chat==null) {
                    Intent intent = new Intent(getActivity(), ChatWindow.class);
                    if (idVal != null) {
                        getActivity().setResult(Integer.parseInt(idVal), intent);
                    }
                    getActivity().finish();
                }
                else
                {
                    chat.tabletDeleteMsg(Integer.parseInt(idVal));
                }
            }
        });

        view1 = (TextView) myView.findViewById(R.id.msgTextView);
        //view1.setText("1");
        view1.setText(messageVal);

        view2 = (TextView) myView.findViewById(R.id.idTextView);
        //view2.setText("2");
        view2.setText(idVal);

        return myView;
    }


}

//
//import android.os.Bundle;
//
//
//import android.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link MessageFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class MessageFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public MessageFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment MessageFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static MessageFragment newInstance(String param1, String param2) {
//        MessageFragment fragment = new MessageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_message, container, false);
//
//        return view;
//    }
//
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Button delete = (Button) view.findViewById(R.id.delButton);
//        delete.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                MessageDetails msg = new MessageDetails();
//                msg.buttonDo();
//            }
//        });
//
//
//    }
//
//
//
//}
