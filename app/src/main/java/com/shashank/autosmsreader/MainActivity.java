package com.shashank.autosmsreader;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager mViewPager;
    public static final int PERMISSION=101;
    SharedPreferences shref;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==PERMISSION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                setupViewPager(mViewPager);
                TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);
            }
            else{
                Toast.makeText(this, R.string.permission_error,Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);

        MenuItem item = (MenuItem) menu.findItem(R.id.switchId);
        item.setActionView(R.layout.switch_layout);
        final Switch switchAB = (Switch) item.getActionView().findViewById(R.id.switchAB);
        shref = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        switchAB.setChecked(shref.getBoolean("main",true));

        switchAB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                shref = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor;
                editor = shref.edit();
                editor.putBoolean("main",isChecked);
                editor.commit();

                if (isChecked) {
                    Toast.makeText(getApplication(), getString(R.string.switch_on_toast), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), getString(R.string.switch_off_toast), Toast.LENGTH_LONG).show();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, Settings.class));
                break;
            case R.id.rateUs:
                try{
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id="+getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }
                break;
            case R.id.help:
                startActivity(new Intent(MainActivity.this,HelpActivity.class));

        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sectionsPageAdapter=new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager=(ViewPager) findViewById(R.id.container);


        if(Build.VERSION.SDK_INT < 23){
            setupViewPager(mViewPager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        }
        else {

            if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)||
                    (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED)) {

                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS}, PERMISSION);
            }
            else{
                setupViewPager(mViewPager);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);
            }
        }

    }

    private void setupViewPager(ViewPager viewPager){
        sectionsPageAdapter.addFragment(new InboxFragment(),getString(R.string.inbox_tab));
        sectionsPageAdapter.addFragment(new OutboxFragment(),getString(R.string.outbox_tab));
        viewPager.setAdapter(sectionsPageAdapter);
    }
}
