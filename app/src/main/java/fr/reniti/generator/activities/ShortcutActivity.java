package fr.reniti.generator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;

public class ShortcutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent.getAction() == Intent.ACTION_VIEW && intent.getData() != null)
        {
            String reasonId = intent.getData().getQueryParameter("reason");
            Reason reason = Reason.getById(reasonId);

            if(reasonId.equalsIgnoreCase("other") ||reason == null)
            {
                Intent i = new Intent(this, AttestationCreateActivity.class);
                startActivity(i);
                return;
            }

            if(StorageManager.getInstance() == null)
            {
                new StorageManager(this);
            }

            Profile selectedProfile = StorageManager.getInstance().getProfilesManager().getDefaultProfile();

            if(selectedProfile == null)
            {
                Toast.makeText(this, R.string.activity_attestation_create_error_profile, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, R.string.shortcut_working, Toast.LENGTH_SHORT).show();

            Intent test = new Intent(this, AttestationGenerationActivity.class);
            test.putExtra("reason_id", reasonId);
            startActivityForResult(test, 1);

            finish();
        }
    }
}