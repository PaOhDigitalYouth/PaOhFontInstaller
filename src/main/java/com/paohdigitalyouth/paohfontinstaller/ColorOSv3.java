package com.paohdigitalyouth.paohfontinstaller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by HtetzNaing on 4/5/2018.
 */


public class ColorOSv3 extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bv3help);

        webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/oppo/v3.html");
        new FontChanger(this).setFont(this,"paoh.ttf",true);
    }
}