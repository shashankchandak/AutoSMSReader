package com.shashank.autosmsreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
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

        setTitle(getString(R.string.settings_actvity_title));

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
        listItems.add(0, getString(R.string.unknown_number));

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
            MultiSelectListPreferenceFix contacts = (MultiSelectListPreferenceFix) findPreference("filter_sender");
            contacts.setEntries(entry);
            contacts.setEntryValues(entry);

            Preference resetButton = findPreference("resetButton");
            resetButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //PreferenceManager.setDefaultValues(getActivity(), R.xml.settings, true);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
//                    Intent intent=getActivity().getIntent();
//                    getActivity().finish();
//                    startActivity(intent);
                    //getActivity().recreate();
                    Intent intent = getActivity().getIntent();
                    getActivity().overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(intent);


                    return true;
                }
            });

            Preference filter_time=findPreference("filter_time");
            filter_time.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent=new Intent(getActivity(),TimeActivity.class);
                    startActivity(intent);

                    return true;
                }
            });


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
