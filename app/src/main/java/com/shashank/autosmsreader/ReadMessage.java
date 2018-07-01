package com.shashank.autosmsreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReadMessage extends Activity {

    private TextView senderName;
    private TextView messageBody;
    private Button stopButton;

    String Sender="From: ";
    String Message="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        senderName=findViewById(R.id.senderName);
        messageBody=findViewById(R.id.messageBody);
        stopButton=findViewById(R.id.stopButton);

        Intent intent=getIntent();
        Sender+=intent.getStringExtra("senderName");
        Message=intent.getStringExtra("messageBody");
        senderName.setText(Sender);
        messageBody.setText(Message);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
