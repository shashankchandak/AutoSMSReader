package com.shashank.autosmsreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class CheckSettings {

    Context context;
    String senderName;
    AudioManager manager;
    SharedPreferences sharedPrefs;

    public CheckSettings(Context context, String displayName) {
        this.context=context;
        this.senderName=displayName;
    }

    public boolean read(){

        manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        if(manager.getMode()==AudioManager.MODE_IN_CALL)
            return false;
        else{
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Set<String> selected = sharedPrefs.getStringSet("filter_sender", null);
            if(selected!=null) {
                ArrayList<String> selectedContacts = new ArrayList<>(selected);

                boolean isUnknown=false;
                Uri lookUpUri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(senderName));
                Cursor c=context.getContentResolver().query(lookUpUri,new String[]{ContactsContract.Data.DISPLAY_NAME},null,null,null);
                if(c==null)
                    isUnknown=true;

                if (selectedContacts.contains(senderName)||(selected.contains("Unknown numbers")&&isUnknown))
                    return false;
                else{
                    return otherCheck();
                }
            }
            else{
                return otherCheck();
            }

        }
    }

    public boolean otherCheck(){
        boolean onlyHeadSet=sharedPrefs.getBoolean("headset",false);

        if(onlyHeadSet){
            if(!isHeadSetOn())
                return false;
            return true;
        }
        else{
            if(isHeadSetOn())
                return true;
            else {
                boolean playOnSilent = sharedPrefs.getBoolean("silent", false);
                boolean isSilent = false;

                if (manager.getRingerMode() == AudioManager.RINGER_MODE_SILENT || manager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE)
                    isSilent = true;

                if (isSilent) {
                    if (playOnSilent == false)
                        return false;
                    return true;
                }
                return true;
            }
        }
    }


    public boolean isHeadSetOn(){
         manager= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return manager.isWiredHeadsetOn() ;
        }
        else{
        AudioDeviceInfo[] devices=manager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        for(int i=0;i<devices.length;i++){
            AudioDeviceInfo device=devices[i];
            if(device.getType()==AudioDeviceInfo.TYPE_WIRED_HEADSET
                    ||device.getType()==AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                    ||device.getType()==AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
                    ||device.getType()==AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {


                return true;

                }
            }
        }
        return false;
    }
}
