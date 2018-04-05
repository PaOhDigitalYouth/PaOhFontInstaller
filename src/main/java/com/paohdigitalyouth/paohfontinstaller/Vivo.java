package com.paohdigitalyouth.paohfontinstaller;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Vivo extends AppCompatActivity implements View.OnClickListener {
    String fontPath;
    LOL fucker;
    String title = "PaOh Zawgyi";
    String font = "PaOh.itz";
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

        fontPath = Environment.getExternalStorageDirectory()+"/Download/i Theme/Font/";
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Vivo.this);
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
                            Toast.makeText(Vivo.this, "Something was wrong :(", Toast.LENGTH_SHORT).show();
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

    public void createDir(){
        storage.createDirectory(fontPath,false);
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
                        error(font);
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
                webView.loadUrl("file:///android_asset/v3.html");
                builder1.setView(view1);
                builder1.setPositiveButton("Change Font", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAD();
                        try {
                            Intent localIntent = new Intent(Intent.ACTION_MAIN);
                            localIntent.setComponent(new ComponentName("com.android.filemanager", "com.android.filemanager"));
                            localIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            localIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(localIntent);
                        } catch (Exception e) {
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.filemanager");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        }
                    }
                });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;
        }
    }


    public void error(String font){
        showAD();
        String name = font.replace(".itz","");
        String dec = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<bbkfont><name>HtetzNaing</name>\n" +
                "<author>Khun Htetz Naing</author>\n" +
                "<file>HtetzNaing.ttf</file>\n" +
                "<id>521</id>\n" +
                "</bbkfont>";
        fucker.Assets2SD(this,font,fontPath,font);
        fucker.unZip(fontPath+font,fontPath+".htetz");
        storage.copy(fontPath+".htetz/fonts/"+font.replace(".itz",".ttf"),fontPath+".htetz/"+font.replace(".itz",".ttf"));
        storage.copy(fontPath+".htetz/preview/preview_fonts_small_0.png",fontPath+".htetz/"+name+"_thumb.jpg");
        storage.copy(fontPath+".htetz/preview/preview_fonts_0.jpg",fontPath+".htetz/"+name+"_preview.jpg");

        File fileo [] = new File(fontPath+".htetz").listFiles();
        for (int i=0;i<fileo.length;i++){
            if (fileo[i].getName().endsWith(".ttf") || fileo[i].getName().endsWith(".jpg")){
            }else{
                if (fileo[i].isDirectory()){
                    storage.deleteDirectory(fileo[i].toString());
                }else{
                    storage.deleteFile(fileo[i].toString());
                }
            }
        }

        dec = dec.replace("HtetzNaing",name);
        storage.createFile(fontPath+".htetz/"+name+".xml",dec);
        File file1 [] = new File(fontPath+".htetz").listFiles();
        ArrayList<String> lll = new ArrayList<>();
        for (int i=0;i<file1.length;i++){
            lll.add(file1[i].toString());
        }
        try {
            fucker.zipper(lll,fontPath+name+".txj");
        } catch (IOException e) {
            e.printStackTrace();
        }
        storage.deleteDirectory(fontPath+".htetz");
        error2(font);
    }

    public void error2(String font){
        String name = font.replace(".itz","");
        fucker.Assets2SD(this,font,fontPath,font);
        fucker.unZip(fontPath+font,fontPath+".htetz");
        storage.deleteFile(fontPath+font);

        storage.move(fontPath+".htetz/fonts/"+font.replace(".itz",".ttf"),fontPath+".htetz/fonts/徘徊.ttf");
        storage.deleteFile(fontPath+".htetz/description.xml");
        fucker.Assets2SD(this,"description.xml",fontPath+".htetz/","description.xml");
        storage.deleteFile(fontPath+".htetz/key");
        fucker.Assets2SD(this,"key",fontPath+".htetz/","key");

        File file1 [] = new File(fontPath+".htetz").listFiles();
        ArrayList<String> lll = new ArrayList<>();
        for (int i=0;i<file1.length;i++){
            lll.add(file1[i].toString());
        }

        try {
            fucker.zipper(lll,fontPath+"z"+name+"z.itz");
        } catch (IOException e) {
            e.printStackTrace();
        }
        storage.deleteDirectory(fontPath+".htetz");
        Toast.makeText(this, "Installation finished\n" + "Now, you can change font!", Toast.LENGTH_LONG).show();
    }

    public void dev(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://paohdigitalyouth.com")));
    }
}
