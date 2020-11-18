package fr.reniti.generator.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import java.util.UUID;

import fr.reniti.generator.MainActivity;
import fr.reniti.generator.R;
import fr.reniti.generator.listeners.DateFieldWatcher;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.utils.Utils;

public class ProfileEditActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_editor_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);



        Toolbar toolbar = findViewById(R.id.activity_profile_edit_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_profile_edit_title);

        EditText birthdate = findViewById(R.id.activity_profile_edit_input_birthdate);
        birthdate.addTextChangedListener(new DateFieldWatcher(birthdate, false));

        ((Button) findViewById(R.id.activity_profile_edit_confirm_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.activity_editor_confirm_m:
                saveProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void saveProfile()
    {
        Editable firstname = ((EditText) findViewById(R.id.activity_profile_edit_input_firstname)).getText();
        Editable lastname =((EditText)  findViewById(R.id.activity_profile_edit_input_lastname)).getText();
        Editable birthdate = ((EditText) findViewById(R.id.activity_profile_edit_input_birthdate)).getText();
        Editable placeofbirth = ((EditText) findViewById(R.id.activity_profile_edit_input_placeofbirth)).getText();
        Editable address = ((EditText) findViewById(R.id.activity_profile_edit_input_address)).getText();
        Editable city = ((EditText) findViewById(R.id.activity_profile_edit_input_city)).getText();
        Editable zipcode = ((EditText) findViewById(R.id.activity_profile_edit_input_zipcode)).getText();

        if(firstname.length() <= 0)
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_firstname, Toast.LENGTH_SHORT).show();
            return;
        }

        if(lastname.length() <= 0)
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_lastname, Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Utils.isValidDate(birthdate.toString(), false))
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_birthday, Toast.LENGTH_SHORT).show();
            return;
        }

        if(placeofbirth.length() <= 0)
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_placeofbirth, Toast.LENGTH_SHORT).show();
            return;
        }

        if(address.length() <= 0)
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_address, Toast.LENGTH_SHORT).show();
            return;
        }

        if(city.length() <= 0)
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_city, Toast.LENGTH_SHORT).show();
            return;
        }

        if(zipcode.length() < 4 || zipcode.length() > 5)
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_zipcode, Toast.LENGTH_SHORT).show();
            return;
        }

        Profile profile = new Profile(UUID.randomUUID().toString(), firstname.toString(), lastname.toString(), birthdate.toString(), placeofbirth.toString(), address.toString(), city.toString(), zipcode.toString());

        StorageManager.getInstance().getProfilesManager().addProfileAndSave(profile);

        Intent intent = new Intent(ProfileEditActivity.this, MainActivity.class);
        intent.putExtra("snackbar_message", R.string.activity_profile_create_success);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        ProfileEditActivity.this.startActivity(intent);
        finish();


    }
}