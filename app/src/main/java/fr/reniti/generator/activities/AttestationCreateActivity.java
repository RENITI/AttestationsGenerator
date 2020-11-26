package fr.reniti.generator.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.app.Activity;
import android.content.Context;
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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import fr.reniti.generator.MainActivity;
import fr.reniti.generator.R;
import fr.reniti.generator.listeners.DateFieldWatcher;
import fr.reniti.generator.listeners.TimeFieldWatcher;
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


        new StorageManager(this);

        Date d = new Date();

        setContentView(R.layout.activity_attestation_create);


        Toolbar toolbar = findViewById(R.id.activity_attestation_create_toolbar);


        toolbar.setTitle(R.string.activity_attestation_create_title);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        EditText time = findViewById(R.id.activity_attestation_create_input_heuresortie);
        time.addTextChangedListener(new TimeFieldWatcher(time, Utils.HOUR_FORMAT.format(d)));

        EditText date = ((EditText) findViewById(R.id.activity_attestation_create_input_datesortie));
        date.addTextChangedListener(new DateFieldWatcher(date, true, Utils.DATE_FORMAT.format(d)));

        RadioGroup profileSelect = findViewById(R.id.activity_attestation_create_profil_select);
        Collection<Profile> profiles = StorageManager.getInstance().getProfilesManager().getProfilesList().values();
        String defaultProfileUUID = StorageManager.getInstance().getProfilesManager().getDefaultProfileUUID();

        if(profiles.size() > 0)
        {
            for(Profile profile : profiles)
            {
                RadioButton radio = new RadioButton(this);

                radio.setText(profile.getFirstname() + " " + profile.getLastname() + "\n" + profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity());
                radio.setPadding(30, 30, 0, 30);

                radio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedProfile = profile;
                    }
                 });
                profileSelect.addView(radio);


                if(profile.getUuid().equals(defaultProfileUUID))
                {
                    selectedProfile = profile;
                    profileSelect.check(radio.getId());
                }
            }
        } else {
            Toast.makeText(this, R.string.error_no_profile, Toast.LENGTH_SHORT).show();
            finishAffinity();
        }

        findViewById(R.id.activity_attestation_create_confirm_btn).setOnClickListener(v -> saveAttestation());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if(item.getItemId() == R.id.activity_editor_confirm_m) {
            saveAttestation();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        ArrayList<Reason> reasons = new ArrayList<>();
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

        Thread thread = new Thread(() -> {
            buildAttestation(this, selectedProfile, datesortie.toString(), heuresortie.toString(), reasons.toArray(new Reason[0]), false);
        });
        thread.start();
    }

    public static void buildAttestation(Activity activity, Profile profile, String dateSortie, String heureSortie, Reason[] reasons, boolean shortcut)
    {

            Attestation attestation = new Attestation(profile, dateSortie,  heureSortie, reasons);

            PDFBoxResourceLoader.init(activity);
            boolean success = Utils.savePDF(attestation, activity);

            if(success) {

                StorageManager.getInstance().getAttestationsManager().addAttestationAndSave(attestation);
                Utils.updateShortcuts(activity, true);

                if (!shortcut) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("snackbar_message", R.string.attestation_create_success);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(intent);

                    activity.finish();
                } else {
                    Toast.makeText(activity, "Une attestation a été créé pour " + profile.getFirstname() + " " + profile.getLastname() + " avec le motif " + reasons[0].getDisplayName(), Toast.LENGTH_SHORT).show();

                }
            } else {


                if (!shortcut) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("error_message", R.string.attestation_create_failed);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(intent);

                    activity.finish();
                } else {
                    Toast.makeText(activity, R.string.attestation_create_failed, Toast.LENGTH_LONG).show();



                }
            }

    }
}