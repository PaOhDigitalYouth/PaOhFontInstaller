package com.paohdigitalyouth.paohfontinstaller;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.paohdigitalyouth.paohfontinstaller.R;

public class Help extends AppCompatActivity {
    WebView webView;
    private DownloadManager mDownloadManager;
    private long mDownloadedFileID;
    private DownloadManager.Request mRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mDownloadManager = (DownloadManager) getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new myWeb());
        webView.loadUrl("file:///android_asset/help.html");

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                String fileName= URLUtil.guessFileName(s,s2,s3);
                Log.d("FileName",fileName);
                if(checkPermissions()==true){
                    dlFile(s,fileName);
                }
            }
        });
    }

    class myWeb extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("myURL",url);
            if (url.startsWith("mailto:")){
                String myString = url.replace("mailto:","");
                sendEmail(myString);
                return true;
            }else  if (url.startsWith("https://www.facebook.com/419786931807272")){
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("fb://profile/419786931807272"));
                    startActivity(intent);
                }catch (Exception e){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://m.facebook.com/419786931807272"));
                    startActivity(intent);
                }
                return true;
            }else if (url.startsWith("https://play.google.com/store/apps")) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + url.replace("https://play.google.com/store/apps/details?id=",""))));
                }
            }else{
                if (checkInternet()==true){
                }else{
                    return true;
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    public void sendEmail(String email){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PaOh Keyboard");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Enter your feedback here!.");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public boolean checkInternet(){
        boolean what = false;
        CheckInternet checkNet = new CheckInternet(this);
        if (checkNet.isInternetOn()==true){
            what = true;
        }else{
            what = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Attention!");
            builder.setMessage("No internet connection :(");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return what;
    }

    public void dlFile(String url,String fileName){
        try {
            String mBaseFolderPath = android.os.Environment.getExternalStorageDirectory()+ File.separator+ "Download" + File.separator;
            if (!new File(mBaseFolderPath).exists()) {
                new File(mBaseFolderPath).mkdir();
            }
            String mFilePath = "file://" + mBaseFolderPath + "/" + fileName;
            Uri downloadUri = Uri.parse(url);
            mRequest = new DownloadManager.Request(downloadUri);
            mRequest.setDestinationUri(Uri.parse(mFilePath));
            mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            mDownloadedFileID = mDownloadManager.enqueue(mRequest);
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            registerReceiver(downloadReceiver, filter);
            Toast.makeText(this, "Starting Download : "+fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
        }
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //check if the broadcast message is for our enqueued download
            final Uri uri = mDownloadManager.getUriForDownloadedFile(mDownloadedFileID);
            String apk = getRealPathFromURI(uri);

            if (apk.endsWith(".apk")) {
               AlertDialog.Builder builder = new AlertDialog.Builder(Help.this)
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

    private boolean checkPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("If you want to download files,\nplease allow this permission!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(Help.this, listPermissionsNeeded.toArray
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
                    Toast.makeText(this, "Please download again :)", Toast.LENGTH_SHORT).show();
                } else {
                    checkPermissions();
                    Toast.makeText(this, "You need to Allow Write Storage Permission!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.open_in_browser,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(webView.getUrl())));
        }catch (Exception e) {

        }
        return super.onOptionsItemSelected(item);
    }
}
