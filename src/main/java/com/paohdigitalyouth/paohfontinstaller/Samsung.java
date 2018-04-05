package com.paohdigitalyouth.paohfontinstaller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Samsung extends AppCompatActivity implements View.OnClickListener {
    Button btnInstall,btnChange;
    TextView tvDec;
    LOL lol;
    String myPath;
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samsung);

        lol = new LOL();
        myPath = Environment.getExternalStorageDirectory()+"/PaOhFontInstaller/";
        btnInstall = findViewById(R.id.btnInstall);
        btnChange = findViewById(R.id.btnChange);
        tvDec = findViewById(R.id.tvDec);

        btnInstall.setOnClickListener(this);
        btnChange.setOnClickListener(this);

        if (beforeLollipop()==true){
            tvDec.setText("Install ကိုႏွိပ္ၿပီး PaOh Zawgyi(iFont) ကို\n" +
                    "Install လုပ္ပါ။\n" +
                    "ၿပီးလွ်င္ Change Font ကိုႏွိပ္ၿပီး\n" +
                    "PaOh Zawgyi(iFont) ကိုေ႐ြးေပးလိုက္ပါ။\n" +
                    "အထက္ပါအဆင့္ေတြေအာင္ျမင္သြားရင္ေတာ့\n" +
                    "သင့္ဖုန္းမွာပအိုဝ္းစာဖတ္႐ူ႕ႏိုင္ပါၿပီ။\n" +
                    "စာ႐ိုက္ရန္အတြက္ PaOh Keyboard ကို\n" +
                    "ထည့္သြင္းၿပီး ပအိုဝ္းစာပါေရးႏိုင္ပါၿပီ။");
        }else{
            tvDec.setText("Install ကိုႏွိပ္ၿပီး PaOhZawgyi(zFont) ကို\n" +
                    "Install လုပ္ပါ။\n" +
                    "ၿပီးလွ်င္ Change Font ကိုႏွိပ္ၿပီး Font ကိုဆက္ႏွိပ္ပါ။\n" +
                    "PaOhZawgyi(zFont) ကိုေ႐ြးေပးလိုက္ပါ။\n" +
                    "အထက္ပါအဆင့္ေတြေအာင္ျမင္သြားရင္ေတာ့\n" +
                    "သင့္ဖုန္းမွာပအိုဝ္းစာဖတ္႐ူ႕ႏိုင္ပါၿပီ။\n" +
                    "စာ႐ိုက္ရန္အတြက္ PaOh Keyboard ကို\n" +
                    "ထည့္သြင္းၿပီး ပအိုဝ္းစာပါေရးႏိုင္ပါၿပီ။");
        }

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

    public boolean beforeLollipop(){
        boolean f = false;
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){
            f = true;
        } else{
            f = false;
        }
        return f;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInstall:
                if (beforeLollipop()==true){
                    beforeLolliInstall();
                }else{
                    afterLolliInstall();
                }
                break;
            case R.id.btnChange:
                if (beforeLollipop()==true){
                    beforeLolliChange();
                }else{
                    afterLolliChange();
                }
                break;
        }
    }

    public void beforeLolliInstall(){
        if (checkPermissions()==true){
            makeDir();
            if (lol.Assets2SD(this, "paoh",myPath, "paoh.apk")==true){
                install(Uri.fromFile(new File(myPath+ "paoh.apk")));
            }else{
                Toast.makeText(this, "Please Try Again!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void afterLolliInstall(){
        if (checkPermissions()==true){
            makeDir();
            boolean check = appInstalled("com.monotype.android.font.samsungsans");

            if(check==true) {
                if (lol.Assets2SD(this, "paoh2", myPath, "paoh2.apk") == true) {
                    install(Uri.fromFile(new File(myPath + "paoh2.apk")));
                } else {
                    Toast.makeText(this, "Please Try Again!!", Toast.LENGTH_SHORT).show();
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("အသိေပးခ်က္")
                        .setMessage("သင့္ဖုန္းတြင္ Samsung Sans ေဖာင့္အား\n" +
                                "ပထမဦးစြာ Install လုပ္ရမည္။\n" +
                                "Samsung Sans ေဖာင့္ Install လုပ္ၿပီးမွသာ\n" +
                                "PaOh Zawgyi ေဖာင့္အား Install လုပ္လို႔ရမည္ျဖစ္သည္။\n" +
                                "ေအာက္ပါ OK ခလုတ္ကိုႏွိပ္ၿပီး\n" +
                                "Samsung Sans ကို Install လုပ္လိုက္ပါ။\n" +
                                "ၿပီးလွ်င္ Install ခလုတ္ကိုထပ္လာႏွိပ္ၿပီး\n" +
                                "PaOh Zawgyi ေဖာင့္အား Install လုပ္ပါ။")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (lol.Assets2SD(Samsung.this,"sans",myPath,"sans.apk")) {
                                    install(Uri.fromFile(new File(myPath + "sans.apk")));
                                } else {
                                    Toast.makeText(Samsung.this, "Please Try Again!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
    }

    public void beforeLolliChange(){
        showAD();
        Intent mainIntent;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.flipfont.FontListProgressActivity");
        intent.setComponent(componentName);

        Intent intent1 = new Intent(Intent.ACTION_VIEW);
        ComponentName componentName1 = new ComponentName("com.android.settings", "com.android.settings.flipfont.FontListActivity");
        intent1.setComponent(componentName1);

        if (isCallable(intent)){
            mainIntent = intent;
        }else if (isCallable(intent1)){
            mainIntent = intent1;
        }else{
            mainIntent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        }

        try {
            startActivity(mainIntent);
        }catch (Exception e){

        }
    }

    public void afterLolliChange(){
        showAD();
        Intent mainIntent;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.flipfont.FontListProgressActivity");
        intent.setComponent(componentName);

        Intent intent1 = new Intent(Intent.ACTION_VIEW);
        ComponentName componentName1 = new ComponentName("com.android.settings", "com.android.settings.flipfont.FontListActivity");
        intent1.setComponent(componentName1);

        if (isCallable(intent)){
            mainIntent = intent;
        }else if (isCallable(intent1)){
            mainIntent = intent1;
        }else{
            mainIntent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        }

        try {
            startActivity(mainIntent);
        }catch (Exception e){

        }
    }

    private boolean appInstalled(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public void makeDir(){
        File file = new File(myPath);
        if (!file.exists()){
            file.mkdirs();
        }else{
            File f [] = new File(myPath).listFiles();
            for (int i=0;i<f.length;i++){
                f[i].delete();
            }
        }
    }

    public void install(Uri uri){
        showAD();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Intent intent1 = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent1.setData(uri);
                intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent1);
            } else {
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setDataAndType(uri, "application/vnd.android.package-archive");
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please Allow this Storage Write Permission!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(Samsung.this, listPermissionsNeeded.toArray
                            (new String[listPermissionsNeeded.size()]), 5217);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            Log.d("TAG","Permission"+"\n"+String.valueOf(false));
            return false;
        }
        Log.d("Permission","Permission"+"\n"+String.valueOf(true));
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5217: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please try again :)", Toast.LENGTH_SHORT).show();
                } else {
                    checkPermissions();
                    Toast.makeText(this, "You need to Allow Write Storage Permission!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void dev(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://paohdigitalyouth.com")));
    }
}
