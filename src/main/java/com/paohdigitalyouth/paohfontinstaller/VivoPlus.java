package com.paohdigitalyouth.paohfontinstaller;

/**
 * Created by HtetzNaing on 4/4/2018.
 */

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class VivoPlus extends AppCompatActivity implements View.OnClickListener {
    String fontPath;
    String keyPaht;
    LOL fucker;
    String title = "PaOh Zawgyi";
    String font = "PaOh.itz";
    String key = "4_key_1517144975000PaOh.itz";
    ActivityManager am;
    Button btnInstall,btnChange;
    List<ActivityManager.RunningAppProcessInfo> processes;
    ImageButton fab;
    Storage storage;
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vivo);
        getSupportActionBar().setElevation(0);

        fontPath = Environment.getExternalStorageDirectory()+"/.dwd/c/o/m/b/b/k/t/h/e/m/e/F/";
        keyPaht = Environment.getExternalStorageDirectory()+"/下载/i主题/.Font/cache/";
        fucker = new LOL();
        am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        storage = new Storage(this);
        createDir();

        btnInstall = findViewById(R.id.btnInstall);
        btnChange = findViewById(R.id.btnChange);
        btnInstall.setOnClickListener(this);
        btnChange.setOnClickListener(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VivoPlus.this);
                builder.setTitle("Attention!");
                builder.setMessage("This function will deleted your all fonts");
                builder.setPositiveButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            File f1 = new File(fontPath);
                            File f2 [] = f1.listFiles();
                            for (int a=0;a<f2.length;a++){
                                if (f2[a].isDirectory()){
                                }else{
                                    f2[a].delete();
                                }
                            }
                        }catch (Exception e){
                            Toast.makeText(VivoPlus.this, "Something was wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

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
        storage.createDirectory(fontPath,false);
        storage.createDirectory(keyPaht,false);
        if (storage.isDirectoryExists(fontPath)==true && storage.isDirectoryExists(keyPaht)==true){
            b=true;
        }
        return b;
    }

    public void error(String font,String key){
        fucker.Assets2SD(this,font,fontPath,font);
        fucker.Assets2SD(this,key,keyPaht,key);
        processes = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processes) {
            if (info.processName.equalsIgnoreCase("com.bbk.theme")) {
                android.os.Process.killProcess(info.pid);
                android.os.Process.sendSignal(info.pid, android.os.Process.SIGNAL_KILL);
                am.killBackgroundProcesses(info.processName);
                am.restartPackage("com.bbk.theme");
            }
        }

        Toast.makeText(this, "Please Wait..", Toast.LENGTH_SHORT).show();
        try {
            Intent localIntent = new Intent(Intent.ACTION_MAIN);
            localIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            localIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            localIntent.setComponent(new ComponentName("com.bbk.theme", "com.bbk.theme.Theme"));
            startActivity(localIntent);
        }catch (Exception e){
            startActivity(getPackageManager().getLaunchIntentForPackage("com.bbk.theme"));
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File file = new File(keyPaht);
        File file1 [] = file.listFiles();
        Arrays.sort( file1, new Comparator(){
            public int compare(Object o1, Object o2) {
                if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                    return -1;
                } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });

        ArrayList<String> lol = new ArrayList<>();
        for (int i=0;i<file1.length;i++){
            if (file1[i].isDirectory()){
            }else{
                if (file1[i].getName().endsWith(".itz") && file1[i].getName().startsWith("4_v4")) {
                    lol.add(file1[i].getName());
                }
            }
        }

        try {
            String s1 = lol.get(0);
            s1 = s1.substring(s1.indexOf("v4"),s1.lastIndexOf("_v4"));
            s1 = s1.replace("v4_","");
            Log.d("Files","v4 "+s1);

            String s2 = key;
            s2 = s2.substring(s2.indexOf("key"));
            s2 = s2.replaceAll("\\D+","");
            Log.d("Files","key "+ s2);

            String newKeyName = key.replace(s2,s1);
            storage.move(keyPaht+key,keyPaht+newKeyName);
            Toast.makeText(this, "Installation finished\n" + "Now, you can change font!", Toast.LENGTH_LONG).show();
            Intent newIntent = new Intent(VivoPlus.this,getClass());
            newIntent.putExtra("title",title);
            newIntent.putExtra("font",font);
            newIntent.putExtra("key",key);
            startActivity(newIntent);
            finish();
        }catch (Exception e){
            Toast.makeText(this, "Please try again :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInstall:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Attention!");
                builder.setMessage("Do you want to Install\n"+title+" Font ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        error(font,key);
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
                break;
            case R.id.btnChange:
                processes = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo info : processes) {
                    if (info.processName.equalsIgnoreCase("com.bbk.theme")) {
                        android.os.Process.killProcess(info.pid);
                        android.os.Process.sendSignal(info.pid, android.os.Process.SIGNAL_KILL);
                        am.killBackgroundProcesses(info.processName);
                        am.restartPackage("com.bbk.theme");
                    }
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Attention!");
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view1 = layoutInflater.inflate(R.layout.bv3help,null);
                WebView webView = view1.findViewById(R.id.webView);
                webView.loadUrl("file:///android_asset/v3plus.html");
                builder1.setView(view1);
                builder1.setPositiveButton("Change Font", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAD();
                        try {
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.bbk.theme");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        }catch (Exception e){
                            Intent localIntent = new Intent(Intent.ACTION_MAIN);
                            localIntent.setComponent(new ComponentName("com.bbk.theme", "com.bbk.theme.Theme"));
                            localIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            localIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(localIntent);
                        }
                    }
                });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;
        }
    }
    public void dev(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://paohdigitalyouth.com")));
    }
}
