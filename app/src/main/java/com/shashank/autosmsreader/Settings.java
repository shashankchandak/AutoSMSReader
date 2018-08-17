package com.shashank.autosmsreader;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;

public class Settings extends AppCompatActivity {

    ArrayList<String> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listItems = new ArrayList<>();
        try {
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                listItems.add(cursor.getString(cursor.getColumnIndex("display_name")));
            }
            cursor.close();
            Collections.sort(listItems);
        } catch (Exception e) {
        }
        listItems.add(0, "Unknown numbers");

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment(listItems)).commit();
    }


    @SuppressLint("ValidFragment")
    public static class MyPreferenceFragment extends PreferenceFragment
    {   ArrayList<String> listItems;

        public MyPreferenceFragment(ArrayList<String> listItems){
            this.listItems=listItems;

        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
            CharSequence[] entry = listItems.toArray(new CharSequence[listItems.size()]);
            MultiSelectListPreference ml = (MultiSelectListPreference) findPreference("filter_sender");
            ml.setEntries(entry);
            ml.setEntryValues(entry);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return  true;
    }
}
