package fr.reniti.generator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import java.util.UUID;

import fr.reniti.generator.MainActivity;
import fr.reniti.generator.R;
import fr.reniti.generator.listeners.DateFieldWatcher;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Profile;
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

        EditText birthDate = findViewById(R.id.activity_profile_edit_input_birthdate);
        birthDate.addTextChangedListener(new DateFieldWatcher(birthDate, false));

        ((Button) findViewById(R.id.activity_profile_edit_confirm_btn)).setOnClickListener((View.OnClickListener) v -> saveProfile());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if(item.getItemId() == R.id.activity_editor_confirm_m)
        {
            saveProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void saveProfile()
    {
        Editable firstName = ((EditText) findViewById(R.id.activity_profile_edit_input_firstname)).getText();
        Editable lastName =((EditText)  findViewById(R.id.activity_profile_edit_input_lastname)).getText();
        Editable birthDate = ((EditText) findViewById(R.id.activity_profile_edit_input_birthdate)).getText();
        Editable placeOfBirth = ((EditText) findViewById(R.id.activity_profile_edit_input_placeofbirth)).getText();
        Editable address = ((EditText) findViewById(R.id.activity_profile_edit_input_address)).getText();
        Editable city = ((EditText) findViewById(R.id.activity_profile_edit_input_city)).getText();
        Editable zipCode = ((EditText) findViewById(R.id.activity_profile_edit_input_zipcode)).getText();

        if(firstName.length() <= 0)
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_firstname, Toast.LENGTH_SHORT).show();
            return;
        }

        if(lastName.length() <= 0)
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_lastname, Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Utils.isValidDate(birthDate.toString(), false))
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_birthday, Toast.LENGTH_SHORT).show();
            return;
        }

        if(placeOfBirth.length() <= 0)
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

        if(zipCode.length() < 4 || zipCode.length() > 5)
        {
            Toast.makeText(this, R.string.activity_profile_edit_error_zipcode, Toast.LENGTH_SHORT).show();
            return;
        }

        Profile profile = new Profile(UUID.randomUUID().toString(), firstName.toString(), lastName.toString(), birthDate.toString(), placeOfBirth.toString(), address.toString(), city.toString(), zipCode.toString());

        StorageManager.getInstance().getProfilesManager().addProfileAndSave(profile);

        Intent intent = new Intent(ProfileEditActivity.this, MainActivity.class);
        intent.putExtra("snackbar_message", R.string.activity_profile_create_success);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        ProfileEditActivity.this.startActivity(intent);
        finish();
    }
}