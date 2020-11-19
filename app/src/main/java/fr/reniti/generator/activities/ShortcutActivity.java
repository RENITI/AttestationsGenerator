package fr.reniti.generator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import java.util.Date;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class ShortcutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        finishAffinity();

        Intent intent = getIntent();
        if(intent.getAction() == Intent.ACTION_VIEW && intent.getData() != null)
        {

            String reasonId = intent.getData().getQueryParameter("reason");

            if(reasonId.equalsIgnoreCase("other"))
            {
                Intent i = new Intent(this, AttestationCreateActivity.class);
                startActivity(i);
                return;
            }


            Reason reason = Reason.getById(reasonId);

            if(StorageManager.getInstance() == null)
            {
                new StorageManager(this);
            }


            Date d = new Date();

            Profile selectedProfile = StorageManager.getInstance().getProfilesManager().getDefaultProfile();

            if(selectedProfile == null)
            {
                Toast.makeText(this, R.string.activity_attestation_create_error_profile, Toast.LENGTH_SHORT).show();
                return;
            }

            AttestationCreateActivity.buildAttestation(this, selectedProfile, Utils.DATE_FORMAT.format(d), Utils.HOUR_FORMAT.format(d), new Reason[] {reason}, true);
            Toast.makeText(this, "Une attestation a été créé pour " + selectedProfile.getFirstname() + " " + selectedProfile.getLastname() + " avec le motif " + reason.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
    }
}