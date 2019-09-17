package it.uniba.maw.dibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

public class UserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextMatricola;
    private EditText editTextSurname;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        SharedPreferences prefs = getSharedPreferences("profile data", MODE_PRIVATE);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMatricola = findViewById(R.id.editTextMatricola);
        editTextSurname = findViewById(R.id.editTextSurname);

        toolbar = findViewById(R.id.toolbar_user_activity);
        toolbar.setTitle(R.string.account);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        editTextName.setText(prefs.getString("nome", ""));
        editTextName.setEnabled(false);
        editTextSurname.setText(prefs.getString("cognome", ""));
        editTextSurname.setEnabled(false);
        editTextEmail.setText(prefs.getString("mail", ""));
        editTextEmail.setEnabled(false);
        editTextMatricola.setText(prefs.getString("matricola", ""));
        editTextMatricola.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_activity, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            enableEdit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enableEdit() {
        if(editTextName.isEnabled()){
            editTextName.setEnabled(false);
            editTextEmail.setEnabled(false);
            editTextMatricola.setEnabled(false);
            editTextSurname.setEnabled(false);
            menu.findItem(R.id.action_edit).setTitle(R.string.edit);
        } else {
            editTextName.setEnabled(true);
            editTextEmail.setEnabled(true);
            editTextMatricola.setEnabled(true);
            editTextSurname.setEnabled(true);
            menu.findItem(R.id.action_edit).setTitle(R.string.save);
        }
    }

}


