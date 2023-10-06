package com.wizard.rewards720;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFbMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        String clickAction = message.getNotification().getClickAction();


        Log.d("MyFbMessageService", "onMessageReceived: message ID: " + message.getMessageId() +
                " \n mess Type: " + message.getMessageType()
                + " \n mess Title: " + message.getNotification().getTitle()
                + " \n mess click action: " + clickAction);

//            Toast.makeText(this, "Message is received right now", Toast.LENGTH_SHORT).show();
                /*if (clickAction=="test"){
                    Log.d("MyFbMessageService", "onMessageReceived: test is clicked");

                }*/
        pushNotification(message, clickAction);

    }

    public void pushNotification(RemoteMessage message, String clickAction) {
        String channelId ="fcm_fallback_notification_channel";

        Intent intent = new Intent(clickAction);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent,
                PendingIntent.FLAG_IMMUTABLE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {100, 1000,100, 1000};

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);




        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setSound(uri)
                .setTicker("lastWarning")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(pattern)
                .setVibrate(new long[] {1000,1000})
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        builder.setOngoing(true);
//        Notification note = builder.build();
//        note.defaults |= Notification.DEFAULT_VIBRATE;
//        note.defaults |= Notification.DEFAULT_SOUND;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "Main", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(pattern);


            int visibility = NotificationCompat.VISIBILITY_PUBLIC;
            notificationChannel.setLockscreenVisibility(visibility);

            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId(channelId);
        }
        manager.notify(1, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("MyFbMessageService", "onNewToken: New Token generated..");
    }
}
