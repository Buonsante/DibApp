package it.uniba.maw.dibapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.util.Util;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;
import static it.uniba.maw.dibapp.util.Util.SHARED_PREFERENCE_NAME;

public class LoginActivity extends AppCompatActivity {


    Button signInButon;
    EditText usernameEditText, passwordEditText;
    TextInputLayout usernameLayout, passwordLayout;

//    AlertDialog progressDialog;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    FirebaseFirestore db;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //generateLessonsInDatabase();

        //forzo la disconnessione ad ogni avvio per prova app
        //mAuth.signOut();

        usernameEditText = findViewById(R.id.input_email);
        usernameLayout = findViewById(R.id.input_layout_email);
        passwordEditText = findViewById(R.id.input_password);
        passwordLayout = findViewById(R.id.input_layout_password);

        signInButon = findViewById(R.id.sign_in_button);
        signInButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        pref = getSharedPreferences(Util.SHARED_PREFERENCE_NAME, MODE_PRIVATE);


    }


    private void signInUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(DEBUG_TAG, "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            startMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(DEBUG_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            usernameLayout.setError(getString(R.string.authentication_failed));
                            passwordLayout.setError(getString(R.string.authentication_failed));
                            requestFocus(passwordEditText);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        }
                    }
                });
    }

    /**
     *     Esempio utente in database
     *
     *     annoImmatricolazione "2018/2019"
     *     cdl "ITPS"
     *     cognome "Rossi"
     *     corso "A"
     *     dataNascita "12101997"
     *     mail "prova@prova.it"
     *     matricola "667080"
     *     nome "Mario"
     *     sendNotification true
     *     tipo "S"
     *     token "dRwIX8KNU8g:APA91bGOXO_Yuke3gfflf6eKqKJqT6QHEwDch0izAuxJioCmGqfOnatwZhQEjxDxNeEf-pcEyAICo0n3ZdoEK9eaLxIf5t0GPnRZiSiMm59I7gVAhrovw2zZnulVBsMrgXWye5iX--85"
     */
    private void startMainActivity(){
            Log.w(DEBUG_TAG,"start main activty");

            String mail = user.getEmail();
            CollectionReference utenti = db.collection("utenti");
            // Create a query against the collection.
            Query query = utenti.whereEqualTo("mail", mail);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(DEBUG_TAG, "User: "+document.toString() );
                            SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCE_NAME,MODE_PRIVATE);
                            pref.edit().putBoolean("firstRun",false).commit();
                            String token = pref.getString("token", "");
                            document.getReference().update("token", token);

                            //save values profile in shered preference
                            saveProfileData(document);

                            String tipo = document.getString("tipo");
                            if(tipo.equals("S")) {
                                pref.edit().putString("tipo", "S").apply();
                                startMain();
                            }
                            if(tipo.equals("D")) {
                                pref.edit().putString("tipo", "D").apply();
                                startMain();
                            }
                        }
                    } else {
                        Log.d(DEBUG_TAG, "Error getting documents: ", task.getException());
                    }

                }
            });
    }

    private void saveProfileData(QueryDocumentSnapshot document) {
        String nome = document.getString("nome");
        String cognome = document.getString("cognome");
        String mail = document.getString("mail");
        String matricola = document.getString("matricola");

        SharedPreferences.Editor editor = getSharedPreferences("profile data", MODE_PRIVATE).edit();
        editor.putString("nome", nome);
        editor.putString("cognome", cognome);
        editor.putString("mail", mail);
        editor.putString("matricola", matricola);
        editor.apply();
    }

    private void startMain(){
        Intent mainActivityIntent = new Intent(this,MainActivity.class);
        finishAffinity();
        startActivity(mainActivityIntent);
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        progressCommand();
        signInUser(usernameEditText.getText().toString(),passwordEditText.getText().toString());
    }

    private boolean validateEmail() {
        String email = usernameEditText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            usernameLayout.setError(getString(R.string.err_msg_email));
            requestFocus(usernameEditText);
            return false;
        } else {
            usernameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (passwordEditText.getText().toString().trim().isEmpty()) {
            passwordLayout.setError(getString(R.string.err_msg_password));
            requestFocus(passwordEditText);
            return false;
        } else {
            passwordLayout.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void generateLessonsInDatabase(){
        //algoritmo per la creazione di 10 lezioni per ogni insegnamento generate in modo casauale
        Log.w(DEBUG_TAG,"Retrieving collection group");

        db.collectionGroup("insegnamenti").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.w(DEBUG_TAG, "Insegnamenti");
                        for (final DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
//                            Log.w(DEBUG_TAG,"Document Collection: "+document.getReference().getParent().getParent().get().getResult().toString());
                            final String insegnamentoPath = document.getReference().getPath();
                            final String nomeInsegnamento = document.getString("nome");
                            final String[] nomeDocente = new String[1];
                            final String cdl = document.getString("cdl");
                            final DocumentReference profReference = (DocumentReference) document.get("docente");
                            profReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Log.i(DEBUG_TAG,documentSnapshot.toString());
                                    nomeDocente[0] = documentSnapshot.get("cognome")+" "+documentSnapshot.get("nome");
                                    Random random = new Random();
                                    GregorianCalendar calendar;
                                    for(int i=0;i<4;i++) {
                                        calendar = new GregorianCalendar();
                                        calendar.add(Calendar.DATE, random.nextInt(10));
                                        int oraInizioInt = 8+random.nextInt(5);
                                        String oraInizio = String.valueOf(oraInizioInt);
                                        String oraFine = String.valueOf(oraInizioInt + 3);
                                        document.getReference().collection("lezioni")
                                                .add(new Lezione(0,nomeDocente[0],profReference.getPath(), calendar,oraInizio,oraFine,"",nomeInsegnamento,insegnamentoPath, null))
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.w(DEBUG_TAG,"Document in "+insegnamentoPath+" - "+documentReference.getPath()+" created");
//                                                        documentReference.update("hadCommented", "",
//                                                                "utentiRegistrati","");
                                                    }
                                                });
                                    }
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void progressCommand() {
        progressBar = findViewById(R.id.login_progress);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }
}
