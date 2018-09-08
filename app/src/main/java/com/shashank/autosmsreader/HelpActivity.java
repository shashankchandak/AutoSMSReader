package com.shashank.autosmsreader;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    ListView listView;
    List<String> brands;
    List<String> brand_val;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView= (ListView) findViewById(R.id.listview);
        brands=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.brand)));
        brand_val=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.brand_val)));
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,brands);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // Toast.makeText(HelpActivity.this,brands.get(position),Toast.LENGTH_LONG).show();
                dialogBuilder=new AlertDialog.Builder(HelpActivity.this);
                View v =getLayoutInflater().inflate(R.layout.help,null);
                dialogBuilder.setView(v);
                dialogBuilder.setCancelable(false);
                dialog=dialogBuilder.create();
                dialog.show();
                TextView helpTextView= (TextView) v.findViewById(R.id.helptextview);
                helpTextView.setText(brand_val.get(position));
                Button okayButton= (Button) v.findViewById(R.id.okButton);
                okayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
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
