package fr.reniti.generator.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Date;

import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class AttestationGenerationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        finishAffinity();

        Intent intent = getIntent();

        Reason reason = Reason.getById(intent.getStringExtra("reason_id"));
        Date d = new Date();

        AttestationCreateActivity.buildAttestation(this, StorageManager.getInstance().getProfilesManager().getDefaultProfile(), Utils.DATE_FORMAT.format(d), Utils.HOUR_FORMAT.format(d), new Reason[] {reason}, true);
    }
}