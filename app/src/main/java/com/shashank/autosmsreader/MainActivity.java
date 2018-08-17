package com.shashank.autosmsreader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager mViewPager;
    public static final int PERMISSION=101;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==PERMISSION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                setupViewPager(mViewPager);
                TabLayout tabLayout=findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);
            }
            else{
                Toast.makeText(this,"Give permissions to read sms",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        startActivity(new Intent(MainActivity.this, Settings.class));
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sectionsPageAdapter=new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager=findViewById(R.id.container);


        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED))
            requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS}, PERMISSION);
        else {
            setupViewPager(mViewPager);

            TabLayout tabLayout = findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        }

    }

    private void setupViewPager(ViewPager viewPager){
        sectionsPageAdapter.addFragment(new InboxFragment(),"INBOX");
        sectionsPageAdapter.addFragment(new OutboxFragment(),"OUTBOX");
        viewPager.setAdapter(sectionsPageAdapter);
    }
}
