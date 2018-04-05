package com.paohdigitalyouth.paohfontinstaller.noti;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.paohdigitalyouth.paohfontinstaller.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by HtetzNaing on 4/2/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    String title,message,url,image;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        if (remoteMessage.getData().size() > 0) {
            Log.d("Notification Data", "Message data payload: " + remoteMessage.getData());
            image = remoteMessage.getData().get("image");
            message = remoteMessage.getData().get("message");
            url = remoteMessage.getData().get("url");
            title = remoteMessage.getData().get("title");

           Intent intent   = new Intent(this, PopupActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("message",message);
            intent.putExtra("url", url);
            intent.putExtra("image",image);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendNotiwithBitmap(title,message, image, url);
            startActivity(intent);
        } else {
            sendNoti(remoteMessage.getNotification().getBody());
        }
    }

    public void sendNoti(String messageBody) {
        Intent intent = new Intent(this, PopupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("PaOh Keyboard")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    public void sendNotiwithBitmap(String title,String messageBody, String bitmap, String url) {
       Intent intent   = new Intent(this, PopupActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("message",messageBody);
        intent.putExtra("url", url);
        intent.putExtra("image",bitmap);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = null;
        try {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(Picasso.with(getApplicationContext()).load(bitmap).get())
                    //BigPicture Style
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            //This one is same as large icon but it wont show when its expanded that's why we again setting
                            .bigLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon))
                            //This is Big Banner image
                            .bigPicture(Picasso.with(getApplicationContext()).load(bitmap).get())
                            //When Notification expanded title and content text
                            .setBigContentTitle(title)
                            .setSummaryText(messageBody));
        } catch (IOException e) {
            e.printStackTrace();
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
