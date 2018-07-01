package com.shashank.autosmsreader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class InboxFragment extends Fragment{


    private RecyclerView rv;
    private ArrayList<Messages> messageList;
    private CustomAdapter customAdapter;
    HashMap<String,String> Contacts=new HashMap<>();
    Uri inboxURI;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.messages,container,false);

        rv=view.findViewById(R.id.rv);
        messageList=new ArrayList<>();
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        customAdapter=new CustomAdapter(getActivity(),messageList);
        rv.setAdapter(customAdapter);

        readContacts();
        readMessages();
        showMessages();

        return view;

    }

    public void readContacts(){

        //TODO:DUPLICATE VALUES AND HOW TO EXACTLY USE HASHMAP
        //TODO:WHAT IS NORMALIZED NUMBER AND +91 VS 0 VS NORMAL
        //TODO:TO LOAD ALL MESSAGES IN BACKGROUND THREAD
        Cursor c=getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.Contacts.DISPLAY_NAME+" ASC");

        while(c.moveToNext()){

            String contactName=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contacts.put(phoneNumber,contactName);
           // Log.i(contactName,phoneNumber);
        }
        c.close();
        //Log.i("Hashmap",Contacts.toString());
    }

    public void readMessages(){
        inboxURI= Uri.parse("content://sms/inbox");

        ContentResolver cr=getActivity().getContentResolver();

        Cursor c=cr.query(inboxURI,null,null,null,null);

        while (c.moveToNext()){
            String senderNumber=c.getString(c.getColumnIndexOrThrow("address"));
            String messageBody=c.getString(c.getColumnIndexOrThrow("body"));
            String Date=c.getString(c.getColumnIndexOrThrow("date"));

            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
            String messageDate=dateFormat.format(new Date(Long.valueOf(Date)));

            Messages message=new Messages(senderNumber,messageBody,messageDate);
            messageList.add(message);

        }
        c.close();

    }

    public void showMessages(){
        Log.i("This",messageList.get(0).senderName);
        for(Messages message:messageList){
            if(Contacts.containsKey(message.getSenderName())){
                message.setSenderName(Contacts.get(message.getSenderName()));
            }
        }
        Log.i("This",messageList.get(0).senderName);

        customAdapter.notifyDataSetChanged();
    }
}
