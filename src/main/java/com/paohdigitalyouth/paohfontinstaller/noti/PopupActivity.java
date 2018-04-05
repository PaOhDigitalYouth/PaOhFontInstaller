package com.paohdigitalyouth.paohfontinstaller.noti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.paohdigitalyouth.paohfontinstaller.News;
import com.paohdigitalyouth.paohfontinstaller.R;
import com.squareup.picasso.Picasso;

public class PopupActivity extends Activity {
    TextView title,message;
    ImageView image;
    Button download;
    InterstitialAd interstitialAd;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        image = findViewById(R.id.image);
        download = findViewById(R.id.download);

        adRequest = new AdRequest.Builder().build();
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-1325188641119577/2310379200");
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                loadAD();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                loadAD();
            }
        });

        Intent intent = getIntent();
        String mTitle = intent.getStringExtra("title");
        String mMessage = intent.getStringExtra("message");
        final String mURL = intent.getStringExtra("url");
        String mImage = intent.getStringExtra("image");

        if (mTitle!=null&&!mTitle.isEmpty()){
            title.setText(mTitle);
        }else{
            title.setText("PaOh Keyboard");
        }


        message.setText(mMessage);
        Picasso.with(this)
                .load(mImage)
                .placeholder(R.drawable.icon)
                .fit()
                .centerCrop()
                .into(image);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mm = new Intent(PopupActivity.this,News.class)
                        .putExtra("url",mURL)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                showAD();
                startActivity(mm);
                finish();
            }
        });
    }

    private void loadAD() {
        if (!interstitialAd.isLoaded()){
            interstitialAd.loadAd(adRequest);
        }
    }

    private void showAD() {
        if (!interstitialAd.isLoaded()){
            interstitialAd.loadAd(adRequest);
        }else{
            interstitialAd.show();
        }
    }

    public void close(View view) {
        showAD();
        finish();
    }
}
