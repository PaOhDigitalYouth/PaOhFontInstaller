package com.paohdigitalyouth.paohfontinstaller;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.snatik.storage.Storage;

import java.io.File;

public class Huawei extends AppCompatActivity {
    Generator generator;
    String workPath;
    Storage storage;
    String font,name;
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei);
        getSupportActionBar().setElevation(0);

        storage = new Storage(this);
        font = "paoh.ttf";
        name = "PaOh Zawgyi";
        workPath = Environment.getExternalStorageDirectory()+"/Android/data/"+getPackageName()+"/.cache/";
        storage = new Storage(this);
        storage.createDirectory(workPath);
        new FontChanger(this).setFont(this,"paoh.ttf",true);
        adRequest = new AdRequest.Builder().build();
        banner = findViewById(R.id.adView);
        banner.loadAd(adRequest);

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

    public void change(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention!");
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.bv3help,null);
        WebView webView = view1.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/huawei/change.html");
        builder.setView(view1);
        builder.setPositiveButton("Change Font", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
showAD();
                try{
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName("com.huawei.android.thememanager","com.huawei.android.thememanager/.HwThemeManagerActivity"));
                    startActivity(intent);
                }catch (Exception e){
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.huawei.android.thememanager");
                    startActivity(intent);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void install(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention!");
        builder.setMessage("You want to install "+name+" Font ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                install();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showAD();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void install() {
        showAD();
        String version = "1.0";
        String osversion = Build.VERSION.RELEASE;
        String screen = "HD";

        if (new File(workPath+font).exists()==true){
            generator = new Generator(Huawei.this,workPath+font,name,"PaOh Digital Youth",screen,version,osversion);
            String text = generator.build();
            Toast.makeText(Huawei.this, text, Toast.LENGTH_LONG).show();
        }else{
            generator = new Generator(Huawei.this,workPath+font,name,"PaOh Digital Youth",screen,version,osversion);
            generator.assets2SD(Huawei.this,"huawei/paoh.ttf",workPath,"paoh.ttf");
            if (new File(workPath+font).exists()==true) {
                generator = new Generator(Huawei.this, workPath + font, name, "PaOh Digital Youth", screen, version, osversion);
                String text = generator.build();
                Toast.makeText(Huawei.this, text, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void reset(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WARNING!");
        builder.setMessage("This function will deleted\nall your font styles!");
        builder.setPositiveButton("Process anyway", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showAD();
                try {
                    File file = new File(Environment.getExternalStorageDirectory()+"/HWThemes/PaOh Zawgyi.hwt");
                    file.delete();
                    File file1[] = new File(Environment.getExternalStorageDirectory()+"/Android/data/"+getPackageName()).listFiles();
                    for (int l=0;l<file1.length;l++){
                        if (file1[l].isDirectory()){
                            storage.deleteDirectory(file1[l].toString());
                        }else{
                            storage.deleteFile(file1[l].toString());
                        }
                    }
                    Toast.makeText(Huawei.this, "Completed!\nDeleted all fonts :)", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(Huawei.this, "Error!\nSomething was wrong :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showAD();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void dev(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://paohdigitalyouth.com")));
    }
}
