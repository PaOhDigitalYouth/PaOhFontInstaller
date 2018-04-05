package com.paohdigitalyouth.paohfontinstaller;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;

public class ASUS extends AppCompatActivity implements View.OnClickListener {
    String name,font,path;
    Button btnInstall,btnChange;
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asus);

        name = "PaOh Zawgyi";
        font = "PaOhOppo";
        path = Environment.getExternalStorageDirectory()+"/PaOhFontInstaller/";
        makeDir();
        setTitle(name);

        btnChange = findViewById(R.id.btnChange);
        btnInstall = findViewById(R.id.btnInstall);
        btnInstall.setOnClickListener(this);
        btnChange.setOnClickListener(this);

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

    public void makeDir(){
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }else{
            File f [] = new File(path).listFiles();
            for (int i=0;i<f.length;i++){
                f[i].delete();
            }
        }
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
        }
    }

    public void install(final String font){
        LOL lol = new LOL();
            boolean b = lol.Assets2SD(this, font, path, font);
            if (b == true) {
                View view = getLayoutInflater().inflate(R.layout.bv3help,null);
                WebView webView = view.findViewById(R.id.webView);
                webView.loadUrl("file:///android_asset/asus/i.html");
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Attention!")
                        .setView(view)
                        .setPositiveButton("Install Font", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showAD();
                                File toInstall = new File(path+ font);
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
                showAD();
                Toast.makeText(this, "Write Storage Permission Error!", Toast.LENGTH_SHORT).show();
            }
        }

    public void changeFont(){
        File [] files = new File(path).listFiles();
        for (int o=0;o<files.length;o++){
            if (files[o].isDirectory()){
                deleteDirectory(files[o].toString());
            }else{
                files[o].delete();
            }
        }

        View view = getLayoutInflater().inflate(R.layout.bv3help,null);
        WebView webView = view.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/asus/change.html");
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
                        Toast.makeText(ASUS.this, "Font Style > "+name+"(iFont) > Apply :)", Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog dialog =builder.create();
        dialog.show();
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

    public void dev(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://paohdigitalyouth.com")));
    }
}
