package it.uniba.maw.dibapp.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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


        Log.d(DEBUG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(DEBUG_TAG, "Message data payload: " + remoteMessage.getData());

            scheduleJob();

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(DEBUG_TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    private void scheduleJob() {
        Intent intent = new Intent();
        intent.setAction("NEW_LESSON");
        sendBroadcast(intent);
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(final String token) {
        Log.d(DEBUG_TAG, "Refreshed token: " + token);
        SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCE_NAME,MODE_PRIVATE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //modifica il token dell'user nel database
        if(!pref.getBoolean("firstRun",true)) {
            db.collection("utenti").whereEqualTo("mail", user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    queryDocumentSnapshots.getDocuments().get(0).getReference().update("token", token);
                }
            });
        }
        //aggiunge il nuovo token alle preference
        pref.edit().putString("token",token).commit();

        //TODO aggingere salvataggio token in database in login activity

    }


}
