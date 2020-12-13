package fr.reniti.generator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.AttestationType;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class ShortcutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent.getAction() == Intent.ACTION_VIEW && intent.getData() != null)
        {
            String reasonId = intent.getData().getQueryParameter("reason");
            String typeId = intent.getData().getQueryParameter("type");



            if(reasonId == null || reasonId.equalsIgnoreCase("other") || typeId == null || AttestationType.getById(typeId) == null ||  Reason.getById(reasonId, AttestationType.getById(typeId)) == null)
            {
                if(typeId == null)
                {
                    Utils.updateShortcuts(this, true);
                }
                Intent i = new Intent(this, AttestationCreateActivity.class);
                i.putExtra("shortcut", true);
                startActivity(i);
                finishAffinity();
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
            test.putExtra("type_id", typeId);
            startActivityForResult(test, 1);

            finish();
        }
    }
}