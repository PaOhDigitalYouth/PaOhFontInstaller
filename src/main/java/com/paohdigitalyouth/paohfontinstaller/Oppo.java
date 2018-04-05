package com.paohdigitalyouth.paohfontinstaller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;

public class Oppo extends AppCompatActivity implements View.OnClickListener {
    String name = "PaOh Zawgyi",font="PaOhOppo",myPath;
    Button btnInstall,btnChange,btnChangeV3;
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oppo);

        myPath = Environment.getExternalStorageDirectory()+"/PaOhFontInstaller/";
        createDir();

        btnChange = findViewById(R.id.btnChange);
        btnInstall = findViewById(R.id.btnInstall);
        btnChangeV3 = findViewById(R.id.btnChangeV3);
        btnInstall.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnChangeV3.setOnClickListener(this);

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

    public boolean createDir(){
        boolean b = false;
        File file =new File(myPath);
        file.mkdirs();
        if (file.exists()){
            b=true;
        }else{
            File nn[] = new File(myPath).listFiles();
            for (int i=0;i<nn.length;i++){
                nn[i].delete();
            }
        }

        return b;
    }

    public void install(final String font){
        LOL lol = new LOL();
        if (createDir()==true) {
            boolean b = lol.Assets2SD(this, font, myPath, font);
            if (b == true) {
                View view = getLayoutInflater().inflate(R.layout.bv3help,null);
                WebView webView = view.findViewById(R.id.webView);
                webView.loadUrl("file:///android_asset/oppo/i.html");
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Attention!")
                        .setView(view)
                        .setPositiveButton("Install Font", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showAD();
                                File toInstall = new File(myPath+ font);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    Uri apkUri = Uri.fromFile(toInstall);
                                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                                    intent.setData(apkUri);
                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(intent);
                                } else {
                                    Uri apkUri = Uri.fromFile(toInstall);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                        });
                AlertDialog dialog =builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "Write Storage Permission Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void changeFont(){
        File [] files = new File(myPath).listFiles();
        for (int o=0;o<files.length;o++){
            if (files[o].isDirectory()){
                deleteDirectory(files[o].toString());
            }else{
                files[o].delete();
            }
        }
        View view = getLayoutInflater().inflate(R.layout.bv3help,null);
        WebView webView = view.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/oppo/change.html");
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Attention!")
                .setView(view)
                .setPositiveButton("Change Font", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAD();
                        Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(Oppo.this, "Font > "+name+"(iFont) > Apply :)", Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog dialog =builder.create();
        dialog.show();

    }

    public void v3(){
        if (ogay()==true){
            View view = getLayoutInflater().inflate(R.layout.bv3help,null);
            WebView webView = view.findViewById(R.id.webView);
            webView.loadUrl("file:///android_asset/oppo/v3already.html");
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Attention!")
                    .setView(view)
                    .setPositiveButton("Change Font", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showAD();
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.nearme.themespace");
                            startActivity(intent);
                        }
                    });
            AlertDialog dialog =builder.create();
            dialog.show();
        }else{
            showAD();
            startActivity(new Intent(this,ColorOSv3.class));
        }
    }

    public boolean ogay(){
        boolean b =false;
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo("com.nearme.themespace", 0);
            int version = packageInfo.versionCode;
            Log.d("ThemeVersion",String.valueOf(version));
            if (version>=463){
                b=true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    public boolean deleteDirectory(String path) {
        return deleteDirectoryImpl(path);
    }

    private boolean deleteDirectoryImpl(String path) {
        File directory = new File(path);

        // If the directory exists then delete
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files == null) {
                return true;
            }
            // Run on all sub files and folders and delete them
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectoryImpl(files[i].getAbsolutePath());
                } else {
                    files[i].delete();
                }
            }
        }
        return directory.delete();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInstall:
                install(font);
                break;
            case R.id.btnChange:
                changeFont();
                break;
            case R.id.btnChangeV3:
                v3();
                break;
        }
    }

    public void dev(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://paohdigitalyouth.com")));
    }
}
