package com.paohdigitalyouth.paohfontinstaller.noti;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.paohdigitalyouth.paohfontinstaller.R;

public class Link extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        String intent = getIntent().getStringExtra("url");
        if (!intent.equals(null) || !intent.isEmpty()){
            finish();
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(intent)));
        }else{
            finish();
        }
    }
}
