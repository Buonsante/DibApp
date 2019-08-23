package it.uniba.maw.dibapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;


import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;

public class LoginActivity extends AppCompatActivity {


    Button signInButon;
    EditText usernameEditText, passwordEditText;
    TextInputLayout usernameLayout, passwordLayout;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        //recupera tutte le lezioni appartenenti a qualsiasi insegnamento
        Log.w(DEBUG_TAG,"Retrieving collection group");
        db.collectionGroup("lezioni").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
//                            Log.w(DEBUG_TAG,"Document Collection: "+document.getReference().getParent().getParent().get().getResult().toString());

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //forzo la disconnessione ad ogni avvio per prova app
        mAuth.signOut();

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
                submitForm();
            }
        });

       user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startMainActivity();
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
                            if(tipo.equals("S"))
                                startStudent();
                            if(tipo.equals("D"))
                                startProf();
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
        startActivity(mainActivityIntent);
    }

    private void startProf(){
        //TODO cambiare activity
        Log.d(DEBUG_TAG, "Prof");
        Intent mainActivityIntent = new Intent(this,MainActivity.class);
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
}
