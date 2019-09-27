package it.uniba.maw.dibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import static it.uniba.maw.dibapp.util.Util.USER_INFO_PREFERENCE_NAME;

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

        SharedPreferences prefs = getSharedPreferences(USER_INFO_PREFERENCE_NAME, MODE_PRIVATE);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMatricola = findViewById(R.id.editTextMatricola);
        editTextSurname = findViewById(R.id.editTextSurname);

        toolbar = findViewById(R.id.toolbar_user_activity);
        toolbar.setTitle(R.string.account);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.menu_user_activity);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_edit) {
                    enableEdit();
                    return true;
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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

        int id = item.getItemId();

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
            toolbar.getMenu().findItem(R.id.action_edit).setTitle(R.string.edit);
        } else {
            editTextName.setEnabled(true);
            editTextEmail.setEnabled(true);
            editTextMatricola.setEnabled(true);
            editTextSurname.setEnabled(true);
            toolbar.getMenu().findItem(R.id.action_edit).setTitle(R.string.save);
        }
    }

}


