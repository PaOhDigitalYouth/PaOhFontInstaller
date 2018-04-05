package com.paohdigitalyouth.paohfontinstaller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;

public class Other extends AppCompatActivity implements View.OnClickListener {
    Button btnInstall,btnAll;
    ImageButton btnRestore;
    String font,name,path;
    LOL my;
    ProgressDialog progressDialog;
    AdView banner;
    AdRequest adRequest;
    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        getSupportActionBar().setElevation(0);

        font = "paoh.ttf";
        name = "PaOh Zawgyi";
        path = Environment.getExternalStorageDirectory()+"/Android/data/"+getPackageName()+"/";

        btnInstall = findViewById(R.id.btnInstall);
        btnAll = findViewById(R.id.btnAll);
        btnRestore = findViewById(R.id.btnRestore);
        btnInstall.setOnClickListener(this);
        btnAll.setOnClickListener(this);
        btnRestore.setOnClickListener(this);
        my = new LOL();
        creDir();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("ATTENTION!");
        progressDialog.setMessage("WORKING...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInstall:
                install(font);
                break;
            case R.id.btnAll:
                installAll(font);
                break;
            case R.id.btnRestore:
                break;
        }
    }

    public boolean creDir(){
        boolean b =false;
        File file = new File(path);
        if (file.exists()){
            File n []= new File(path).listFiles();
            for (int i=0;i<n.length;i++){
                n[i].delete();
            }
            b=true;
        }else{
            if (file.mkdirs()){
                b=true;
            }
        }
        return b;
    }

    public void install(String font){
        try {
            my.GetRoot ();
            if (my.HaveRoot==true){
                if (creDir()==true) {
                    if (new File(path+font).exists()==true){
                        goWork(font);
                    }else{
                        if (my.Assets2SD(this,"huawei/paoh.ttf",path,"paoh.ttf")){
                                goWork(font);
                        }
                    }
                }
            }else{
                showAD();
                Toast.makeText(this, "Please Root First :(", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            showAD();
            Toast.makeText(this, "Please Root First :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void goWork(String font){
        new install().execute(font);
    }

    public void installAll(String font){
        try {
            my.GetRoot ();
            if (my.HaveRoot==true){
                if (creDir()==true) {
                    if (new File(path+font).exists()==true){
                        goWorkAll(font);
                    }else{
                        if (my.Assets2SD(this,"huawei/paoh.ttf",path,"paoh.ttf")){
                                goWorkAll(font);
                        }
                    }
                }
            }else{
                showAD();
                Toast.makeText(this, "Please Root First :(", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            showAD();
            e.printStackTrace();
            Toast.makeText(this, "Please Root First :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void goWorkAll(String font){
        new installAll().execute(font);
    }

    public void dev(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://paohdigitalyouth.com")));
    }

    class install extends AsyncTask<String,Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String font = strings[0];
            boolean b = false;
            if (all(font)==true){
                b=true;
            }
            return b;
        }

        public boolean all(String font){
            Log.d("FontPath",path+font);
            my.RootCmd("mount -o remount ,rw /system", "", null, null, false);
            my.RootCmd("mount -o rw,remount -t auto /system", "", null, null, false);
            my.RootCmd("mount -o rw,remount /system", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmar-Bold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmar-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmarUI-Bold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmarUI-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/DroidSansMyanmar.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SamsungMyanmar.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SamsungMyanmarUI-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SamsungMyanmarZawgyiUI-Bold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SamsungMyanmarZawgyiUI-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SmartZawgyi.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/Padauk-book.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/Padauk-bookbold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/Padauk.ttf", "", null, null, false);
            my.RootCmd("chmod 644 /system/fonts/*.ttf", "", null, null, false);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            if (aBoolean==true){
                Toast.makeText(Other.this, "Install Finished!\n Rebooting...", Toast.LENGTH_SHORT).show();
                my.RootCmd("reboot","",null,null,false);
            }else{
                Toast.makeText(Other.this, "Install Failed :(", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class installAll extends AsyncTask<String,Integer,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String font = strings[0];
            boolean b = false;
            if (all(font)==true){
                b=true;
            }
            return b;
        }

        public boolean all(String font){
            Log.d("FontPath",path+font);
            my.RootCmd("mount -o remount ,rw /system", "", null, null, false);
            my.RootCmd("mount -o rw,remount -t auto /system", "", null, null, false);
            my.RootCmd("mount -o rw,remount /system", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmar-Bold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmar-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmarUI-Bold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmarUI-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/DroidSansMyanmar.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SamsungMyanmar.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SamsungMyanmarUI-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SamsungMyanmarZawgyiUI-Bold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SamsungMyanmarZawgyiUI-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/SmartZawgyi.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/Padauk-book.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/Padauk-bookbold.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/DroidSans.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/DroidSans-Regular.ttf", "", null, null, false);
            my.RootCmd("dd if=" + path + font + " of=/system/fonts/Roboto-Regular.ttf", "", null, null, false);
            my.RootCmd("chmod 644 /system/fonts/*.ttf", "", null, null, false);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            if (aBoolean==true){
                Toast.makeText(Other.this, "Install Finished!\n Rebooting...", Toast.LENGTH_SHORT).show();
                my.RootCmd("reboot","",null,null,false);
            }else{
                Toast.makeText(Other.this, "Install Failed :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
