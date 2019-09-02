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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.firestore.model.value.ReferenceValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.util.Util;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;

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

        FirebaseMessaging.getInstance().subscribeToTopic("newLesson");
        //Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        Bundle notification = getIntent().getExtras();
        if(notification != null){
            Log.w(DEBUG_TAG,"Notification: "+notification.getString("nameServerBle"));
            Toast.makeText(this,"Lezione disponibile con server: "+notification.getString("nameServerBle"),Toast.LENGTH_LONG);
        }else{
            Log.w(DEBUG_TAG,"Intent null");
        }

        //Initialize Shared preference

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //generateLessonsInDatabase();

        //forzo la disconnessione ad ogni avvio per prova app
        //mAuth.signOut();

        usernameEditText = findViewById(R.id.input_email);
        usernameLayout = findViewById(R.id.input_layout_email);
        passwordEditText = findViewById(R.id.input_password);
        passwordLayout = findViewById(R.id.input_layout_password);

        usernameEditText.setText("prova@prova.it");
        passwordEditText.setText("provaprova");

        signInButon = findViewById(R.id.sign_in_button);
        signInButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressCommand();
                submitForm();
            }
        });

       user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            pref = getSharedPreferences(Util.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
            String tipoUtente = pref.getString("tipo","");
            if(tipoUtente.equals("S"))
                startStudent();
            if(tipoUtente.equals("D"))
                startProf();
        } else {
            // No user is signed in
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
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
//                            progressDialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        }

                        // ...
                    }
                });
    }

    private void startMainActivity(){
        if (user != null) {
            Log.w(DEBUG_TAG,"start main activty");
            String name = user.getDisplayName();
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
                            String tipo = document.getString("tipo");
                            if(tipo.equals("S")) {
                                pref.edit().putString("tipo", "S").apply();
                                startStudent();
                            }
                            if(tipo.equals("D")) {
                                pref.edit().putString("tipo", "D").apply();
                                startProf();
                            }
                        }
                    } else {
                        Log.d(DEBUG_TAG, "Error getting documents: ", task.getException());
                    }

                }
            });



        }
    }

    private void startStudent(){
        Log.d(DEBUG_TAG, "Student");
        Intent mainActivityIntent = new Intent(this,MainActivity.class);
//        progressDialog.dismiss();
//        progressBar.setVisibility(View.GONE);
        startActivity(mainActivityIntent);
    }

    private void startProf(){
        //TODO cambiare activity
        Log.d(DEBUG_TAG, "Prof");
        Intent mainActivityIntent = new Intent(this,MainActivity.class);
//        progressDialog.dismiss();
//        progressBar.setVisibility(View.GONE);
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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    private void generateLessonsInDatabase(){
        //algoritmo per la creazione di 10 lezioni per ogni insegnamento generate in modo casauale
        Log.w(DEBUG_TAG,"Retrieving collection group");

        db.collectionGroup("insegnamenti").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.w(DEBUG_TAG, "Insegamenti");
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

                                    nomeDocente[0] = documentSnapshot.get("cognome")+" "+documentSnapshot.get("nome");
                                    Random random = new Random();
                                    GregorianCalendar calendar;
                                    for(int i=0;i<10;i++) {
                                        calendar = new GregorianCalendar();
                                        calendar.add(Calendar.DATE, random.nextInt(6));
                                        int oraInizioInt = 8+random.nextInt(5);
                                        String oraInizio = String.valueOf(oraInizioInt);
                                        String oraFine = String.valueOf(oraInizioInt + 3);
                                        String nameBle = String.valueOf(random.nextInt(256870));
                                        document.getReference().collection("lezioni")
                                                .add(new Lezione(0,nomeDocente[0],profReference.getPath(), calendar,oraInizio,oraFine,"argomento"+i,nomeInsegnamento,insegnamentoPath, nameBle))
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.w(DEBUG_TAG,"Document in "+insegnamentoPath+" - "+documentReference.getPath()+" created");
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
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View view = inflater.inflate(R.layout.progress_dialog, null);
//        ((ProgressBar) view.findViewById(R.id.progress)).setIndeterminate(true);
//        alertDialogBuilder.setView(view);
//        progressDialog = alertDialogBuilder.create();
//        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        //set the dimension of the dialog
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(progressDialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        progressDialog.show();
//        progressDialog.getWindow().setAttributes(lp);
//        progressDialog.setCancelable(false);
    }
}
