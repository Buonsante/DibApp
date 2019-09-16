package it.uniba.maw.dibapp;

import androidx.appcompat.app.AppCompatActivity;

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
    private EditText editTextUsername;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);

        toolbar = findViewById(R.id.toolbar_user_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.account);
        toolbar.setTitleTextColor(Color.WHITE);

        editTextName.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextUsername.setEnabled(false);
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
            editTextUsername.setEnabled(false);
            menu.findItem(R.id.action_edit).setTitle(R.string.edit);
        } else {
            editTextName.setEnabled(true);
            editTextEmail.setEnabled(true);
            editTextUsername.setEnabled(true);
            menu.findItem(R.id.action_edit).setTitle(R.string.save);
        }
    }

}


