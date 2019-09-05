package it.uniba.maw.dibapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;
import static it.uniba.maw.dibapp.util.Util.SHARED_PREFERENCE_NAME;

public class firebaseNotificationService extends FirebaseMessagingService {
    public firebaseNotificationService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(DEBUG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(DEBUG_TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(DEBUG_TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(DEBUG_TAG, "Refreshed token: " + token);
        SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCE_NAME,MODE_PRIVATE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //aggiunge il nuovo token all'array nel database
        db.collection("token").document("token").update("token",FieldValue.arrayUnion(token));

        //recupera il vecchio token dalle preference
        String old_token = pref.getString("token",null);

        //se il vecchio token non è nullo lo elimina dal database
        if(old_token != null)
            db.document("token/token").update("token",FieldValue.arrayRemove(old_token));

        //aggiunge il nuovo token alle preference
        pref.edit().putString("token",token);

    }


}
