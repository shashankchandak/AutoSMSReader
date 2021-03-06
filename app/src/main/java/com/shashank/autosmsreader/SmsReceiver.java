package com.shashank.autosmsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

           // Toast.makeText(context,"Broadcast recieved",Toast.LENGTH_LONG).show();
            String displayName="";
            String senderNumber="";
            String message="";
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdus.length; i++) {

                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    senderNumber = sms.getOriginatingAddress();

                    message += sms.getDisplayMessageBody();

                }

                Uri lookUpUri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(senderNumber));
                Cursor c=context.getContentResolver().query(lookUpUri,new String[]{ContactsContract.Data.DISPLAY_NAME},null,null,null);

                if(c.moveToFirst()) {
                    c.moveToFirst();
                    displayName = c.getString(0);
                }
                else
                    displayName=senderNumber;

                SharedPreferences shref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

                if(new CheckSettings(context,displayName).read()&&shref.getBoolean("main",true)) {

                    Intent smsIntent = new Intent(context, ReadMessage.class);
                    smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    smsIntent.putExtra("senderName", displayName);
                    smsIntent.putExtra("messageBody", message);
                    context.startActivity(smsIntent);
                }

//                Log.i(senderNumber, message);
  //              Log.i(displayName,message);
                //Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            }
        }
    }

}

