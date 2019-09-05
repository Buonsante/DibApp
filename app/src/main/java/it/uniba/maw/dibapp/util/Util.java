package it.uniba.maw.dibapp.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.util.List;

import it.uniba.maw.dibapp.model.Lezione;

public class Util {

    //Credenziali firebase
    //mail: dibappmaw@gmail.com
    //pass: 12345dib


    public static final String DEBUG_TAG = "dibAppDebug";
    public static final String SHARED_PREFERENCE_NAME = "dibAppPref";
    public static List<Lezione> lezioniList;



    public static void createNotification (Context c) {

        String ANDROID_CHANNEL_ID = "com.sai.ANDROID";
        String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";

        createChannels(c, ANDROID_CHANNEL_ID, ANDROID_CHANNEL_NAME);
        //TODO modificare title e body della notifica
        Notification.Builder nb = getAndroidChannelNotification("Nuova lezione disponibile", "Autore: nomeDocente", ANDROID_CHANNEL_ID, c);

        getManager(c).notify(107, nb.build());
    }

    public static void createChannels(Context c, String channel_id, String channel_name) {

        // create android channel
        NotificationChannel androidChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_DEFAULT);

            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.BLUE);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager(c).createNotificationChannel(androidChannel);
        }
    }

    public static Notification.Builder getAndroidChannelNotification(String title, String body, String channel_id, Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(c, channel_id)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setAutoCancel(true);
        } else {
            return new Notification.Builder(c)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setAutoCancel(true);
        }
    }

    public static NotificationManager getManager(Context c) {
        NotificationManager mManager = null;

        if (mManager == null) {
            mManager = (NotificationManager) c.getSystemService(c.NOTIFICATION_SERVICE);
        }
        return mManager;
    }



}
