package fr.reniti.generator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import fr.reniti.generator.MainActivity;
import fr.reniti.generator.R;
import fr.reniti.generator.listeners.DateKeyListener;
import fr.reniti.generator.listeners.TimeKeyListener;
import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class AttestationCreateActivity extends AppCompatActivity {

    private Profile selectedProfile = null;
    private boolean working = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_editor_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attestation_create);

        Toolbar toolbar = findViewById(R.id.activity_attestation_create_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_attestation_create_title);

        Date d = new Date();

        EditText time = ((EditText) findViewById(R.id.activity_attestation_create_input_heuresortie));
        time.setText(Utils.HOUR_FORMAT.format(d));

        /*
        TODO
         */
        //time.setOnKeyListener(new TimeKeyListener());

        EditText date = ((EditText) findViewById(R.id.activity_attestation_create_input_datesortie));
        date.setText(Utils.DATE_FORMAT.format(d));

        /*
        TODO
         */
        //date.setOnKeyListener(new DateKeyListener());

        RadioGroup profileSelect = findViewById(R.id.activity_attestation_create_profil_select);
        Collection<Profile> profiles = StorageManager.getInstance().getProfilesManager().getProfilesList().values();

        if(profiles.size() > 0)
        {
            for(Profile profile : profiles)
            {
                RadioButton radio = new RadioButton(this);

                radio.setText(profile.getFirstname() + " " + profile.getLastname() + "\n" + profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity());
                radio.setPadding(30, 30, 0, 30);

                if(profiles.size() == 1)
                {
                    selectedProfile = profile;
                    radio.setChecked(true);
                } else {
                    radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                selectedProfile = profile;
                            }
                        }
                    });
                }
                profileSelect.addView(radio);
            }
        } else {
            profileSelect.setVisibility(View.INVISIBLE);
        }

        ((Button) findViewById(R.id.activity_attestation_create_confirm_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAttestation();
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
                saveAttestation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setWorking(boolean a)
    {
        working = a;
        View v = findViewById(R.id.attestation_create_loader);
        v.setVisibility(a ? View.VISIBLE : View.INVISIBLE);
        v.invalidate();
    }

    public void saveAttestation() {
        if (working) {
            return;
        }
        setWorking(true);

        if (selectedProfile == null) {
            setWorking(false);
            Toast.makeText(this, R.string.activity_attestation_create_error_profile, Toast.LENGTH_LONG).show();
            return;
        }

        Editable datesortie = ((EditText) findViewById(R.id.activity_attestation_create_input_datesortie)).getText();
        Editable heuresortie = ((EditText) findViewById(R.id.activity_attestation_create_input_heuresortie)).getText();

        if (!Utils.isValidDate(datesortie.toString(), true)) {
            setWorking(false);
            Toast.makeText(this, R.string.activity_attestation_create_error_datesortie, Toast.LENGTH_LONG).show();
            return;
        }

        if (heuresortie.length() < 5) {
            setWorking(false);
            Toast.makeText(this, R.string.activity_attestation_create_error_heuresortie, Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<Reason> reasons = new ArrayList();
        for (Reason reason : Reason.values()) {
            if (((CheckBox) findViewById(reason.getFieldId())).isChecked()) {
                reasons.add(reason);
            }
        }

        if (reasons.size() <= 0) {
            setWorking(false);
            Toast.makeText(this, R.string.activity_attestation_create_error_reasons, Toast.LENGTH_LONG).show();
            return;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Attestation attestation = new Attestation(selectedProfile, datesortie.toString(), heuresortie.toString(), reasons.toArray(new Reason[reasons.size()]));
                StorageManager.getInstance().getAttestationsManager().addAttestationAndSave(attestation);

                PDFBoxResourceLoader.init(getApplicationContext());
                Utils.savePDF(attestation, AttestationCreateActivity.this);

                Intent intent = new Intent(AttestationCreateActivity.this, MainActivity.class);
                intent.putExtra("snackbar_message", R.string.attestation_create_success);

                AttestationCreateActivity.this.startActivity(intent);
            }
        });
        thread.start();
    }
}