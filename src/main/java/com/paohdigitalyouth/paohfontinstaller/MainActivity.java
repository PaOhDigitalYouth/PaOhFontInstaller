package com.paohdigitalyouth.paohfontinstaller;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Button btnSamsung,btnMi,btnVivo,btnOppo,btnHuawei,btnASUS,btnOther;
    ActivityManager am;
    List<ActivityManager.RunningAppProcessInfo> processes;
    AdRequest adRequest;
    AdView banner;
    InterstitialAd interstitialAd;
    private DownloadManager mDownloadManager;
    private long mDownloadedFileID;
    private DownloadManager.Request mRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDownloadManager = (DownloadManager) getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
        am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        btnSamsung = findViewById(R.id.btnSamsung);
        btnMi = findViewById(R.id.btnMIUI);
        btnVivo = findViewById(R.id.btnVivo);
        btnOppo = findViewById(R.id.btnOppo);
        btnHuawei = findViewById(R.id.btnHuawei);
        btnASUS = findViewById(R.id.btnASUS);
        btnOther = findViewById(R.id.btnOther);

        btnSamsung.setOnClickListener(this);
        btnMi.setOnClickListener(this);
        btnVivo.setOnClickListener(this);
        btnOppo.setOnClickListener(this);
        btnHuawei.setOnClickListener(this);
        btnASUS.setOnClickListener(this);
        btnOther.setOnClickListener(this);

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

        new FontChanger(this).setFont(this,"paoh.ttf",true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showAD();
            startActivity(new Intent(this,Help.class));
            return true;
        }else if (id==R.id.about){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("About App")
                    .setMessage("App Version : 1.0\nDeveloper : Khun Htetz Naing\nPowered By PaOh Digital Youth")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showAD();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_samsung) {
            samsung();
        } else if (id == R.id.nav_miui) {
            miui();
        } else if (id == R.id.nav_vivo) {
            vivo();
        } else if (id == R.id.nav_oppo) {
            oppo();
        } else if (id == R.id.nav_huawei) {
            huawi();
        } else if (id == R.id.nav_asus) {
            asus();
        }else if (id == R.id.nav_help) {
            showAD();
            startActivity(new Intent(this,Help.class));
        } else if (id == R.id.nav_rate) {
            rate();
        }else if (id == R.id.nav_share) {
            share();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSamsung:
                samsung();
                break;
            case R.id.btnMIUI:
                miui();
                break;
            case R.id.btnVivo:
                vivo();
                break;
            case R.id.btnOppo:
                oppo();
                break;
            case R.id.btnHuawei:
                huawi();
                break;
            case R.id.btnASUS:
                asus();
                break;
            case R.id.btnOther:
                other();
                break;
        }
    }


    public void samsung(){
        if (checkPermissions()==true) {
            showAD();
            startActivity(new Intent(this, Samsung.class));
        }
    }

    public void vivo(){
        if (checkPermissions()==true) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(R.drawable.vivo_choose_icon);
            imageView.setPadding(5, 0, 5, 0);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setView(imageView)
                    .setPositiveButton("Auto", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showAD();
                            if (getOSVersion() >= 30) {
                                startActivity(new Intent(MainActivity.this, VivoPlus.class));
                            } else if (getOSVersion() == 0) {
                                Toast.makeText(MainActivity.this, "You phone is not vivo :)", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(MainActivity.this, Vivo.class));
                            }
                        }
                    })
                    .setNegativeButton("Install 1", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (iThemeUpdate() == true) {
                                showAD();
                                Intent intent = new Intent(MainActivity.this, VivoPlus.class);
                                startActivity(intent);
                            } else {
                            }
                        }
                    })
                    .setNeutralButton("Install 2", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showAD();
                            startActivity(new Intent(MainActivity.this, Vivo.class));
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

public int getOSVersion(){
        Properties prop = new Properties();
        try {
        prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        String lol = prop.getProperty("ro.vivo.os.version",null);
        if (lol==null){
        lol = prop.getProperty("ro.vivo.os.build.display.id",null);
        }

        int i = 0;
        if (lol==null){
        i = 0;
        }else{
        lol = lol.replaceAll("\\D+","");
        i = Integer.parseInt(lol);
        }
        return i;

        } catch (IOException e) {
        e.printStackTrace();
        return 0;
        }
        }

    public void miui(){
        if (checkPermissions()==true) {
            showAD();
            startActivity(new Intent(this, MIUI.class));
        }
    }

    public void oppo(){
        if (checkPermissions()==true) {
            showAD();
            startActivity(new Intent(this, Oppo.class));
        }
    }

    public void huawi(){
        if (checkPermissions()==true) {
            showAD();
            startActivity(new Intent(this, Huawei.class));
        }
    }

    public void asus(){
        if (checkPermissions()==true) {
            showAD();
            startActivity(new Intent(this, ASUS.class));
        }
    }

    public void other(){
        showAD();
        if (checkPermissions()==true) {
            startActivity(new Intent(this, Other.class));
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
                    ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray
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

    public void rate(){
        ImageView imageView = new ImageView(this);
        imageView.setPadding(5,0,5,0);
        imageView.setImageResource(R.drawable.rateme);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                }
            }
        });
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                .setTitle("ံHelp Us")
                .setView(imageView)
                .setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAD();
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        }
                    }
                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"PaOh Font Installer for Android Free at Google Play Store : play.google.com/store/apps/details?id="+getPackageName()+"\n\nDirect Download : http://bit.ly/2FLfqG8\n#mmFacebookToolBox");
        startActivity(Intent.createChooser(intent,"Share App Via..."));
    }

    public void dev(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://paohdigitalyouth.com")));
    }

    public boolean iThemeUpdate() {
        boolean b = false;
        String version = null;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.bbk.theme", 0);
            version = packageInfo.versionName;
            version = version.replace(".", "");
            int current = Integer.parseInt(version);
            final int need = 4001;
            if (current == need) {
                Log.d("iThemeVersion", "Ogay");
                b = true;
            }

            if (current<need){
                b = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Requires!");
                builder.setMessage("သင့္ဖုန္းတြင္ i Theme 4.0.0.1 ကို \nUpgrade လုပ္ေပးဖို႔လိုအပ္ပါတယ္။\n" +
                        "ေအာက္ပါ Download Now ခလုတ္ကိုႏွိပ္ၿပီးေဒါင္းယူလိုက္ပါ။\n" +
                        "ေဒါင္းလုဒ္လုပ္ျခင္းၿပီးဆုံးပါက Install လုပ္ေပးလိုက္ပါ။\n" +
                        "ၿပီးမွေဖာင့္ျပန္လာထည့္ပါ။");
                builder.setPositiveButton("Download now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAD();
                        dliTheme("https://github.com/KhunHtetzNaing/Files/releases/download/5/iTheme_Latest.apk");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            if (current>need){
                b = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Requires!");
                builder.setMessage("You need to downgrade i Theme 4.0.0.1.\nClick Download now for download i Theme v4.0.0.1 version. After downloaded please install!");
                builder.setPositiveButton("Download now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAD();
                        dliTheme("https://github.com/KhunHtetzNaing/Files/releases/download/5/iTheme_Latest.apk");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            showAD();
            Toast.makeText(this, "Your phone is not Vivo", Toast.LENGTH_SHORT).show();
        }
        return b;
    }

    public void dliTheme(String url){
        if (checkPermissions()==true) {
            try {
                String mBaseFolderPath = android.os.Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator;
                if (!new File(mBaseFolderPath).exists()) {
                    new File(mBaseFolderPath).mkdir();
                }

                String mFilePath = "file://" + mBaseFolderPath + "/" + "iTheme_Latestz.apk";
                Uri downloadUri = Uri.parse(url);
                mRequest = new DownloadManager.Request(downloadUri);
                mRequest.setDestinationUri(Uri.parse(mFilePath));
                mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                mDownloadedFileID = mDownloadManager.enqueue(mRequest);
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                registerReceiver(downloadReceiver, filter);
                Toast.makeText(this, "Starting Download", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Download Failed: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //check if the broadcast message is for our enqueued download
            final Uri uri = mDownloadManager.getUriForDownloadedFile(mDownloadedFileID);
            String apk = getRealPathFromURI(uri);
            if (apk.endsWith(".apk")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Download Completed!")
                        .setMessage("Do you want to install "+new File(apk).getName()+"?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
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

                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                Toast.makeText(context, "Downloaded : "+new File(apk).getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}
