package com.paohdigitalyouth.paohfontinstaller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
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

public class MIUI extends AppCompatActivity implements View.OnClickListener {
    Button btnInstall,btnChange;
    TextView textView;
    String myPath;
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miui);

        myPath = Environment.getExternalStorageDirectory()+"/PaOhFontInstaller/";
        btnInstall = findViewById(R.id.btnInstall);
        btnChange = findViewById(R.id.btnChange);
        textView = findViewById(R.id.tvDec_MI);
        textView.setText("Install ကိုႏွိပ္ပါ။\n" +
                "ထည့္သြင္းၿပီးပါၿပီလို႔ ျပလာရင္ Change Font ကိုႏွိပ္ပါ\n" +
                "Theme ထဲကိုေရာက္သြားပါလိမ့္မယ္။\n" +
                "PaOh Zawgyi ဆိုတဲ့ Theme ကိုေ႐ြးၿပီး\n" +
                "Apply လုပ္ေပးလိုက္ပါ ;)\n" +
                "အထက္ပါအဆင့္ေတြေအာင္ျမင္သြားရင္ေတာ့\n" +
                "သင့္ဖုန္းမွာပအိုဝ္းစာဖတ္႐ူ႕ႏိုင္ပါၿပီ။\n" +
                "စာ႐ိုက္ရန္အတြက္ PaOh Keyboard ကို\n" +
                "ထည့္သြင္းၿပီး ပအိုဝ္းစာပါေရးႏိုင္ပါၿပီ။");

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInstall:
                install();
                break;
            case R.id.btnChange:
                change();
                break;
        }
    }

    public void install(){
        showAD();
        if (checkPermissions()==true){
            makeDir();
            LOL lol = new LOL();
            if (lol.Assets2SD(this,"miui",myPath,"miui.zip")){
                if (lol.ABUnzip(myPath+"miui.zip",Environment.getExternalStorageDirectory()+"/MIUI/theme/")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle("အသိေပးခ်က္")
                            .setMessage("သင့္ဖုန္းတြင္ ပအိုဝ္းေဖာင့္ထည့္သြင္းၿပီးသြားပါၿပီ။\n" +
                                    "Change Font ကိုဆက္ႏွိပ္ပါ\n" +
                                    "PaOh Zawgyi ကိုေ႐ြးၿပီး Apply လုပ္ေပးလိုက္ပါ။")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(this, "Please try again :)", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please try again :)", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void change(){
        showAD();
        try {
            Toast.makeText(this, "Choose PaOh Zawgyi Theme" + " > Apply", Toast.LENGTH_LONG).show();
            Intent localIntent = new Intent(Intent.ACTION_MAIN);
            localIntent.setComponent(new ComponentName("com.android.thememanager", "com.android.thememanager.activity.ThemeSettingsActivity"));
            startActivity(localIntent);

        } catch (Exception e) {
            Toast.makeText(this, "Choose PaOh Zawgyi Theme" + " > Apply", Toast.LENGTH_LONG).show();
            Intent localIntent = new Intent(Intent.ACTION_MAIN);
            localIntent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$FontSettingsActivity"));
            startActivity(localIntent);
        }
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
                    ActivityCompat.requestPermissions(MIUI.this, listPermissionsNeeded.toArray
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
