package fr.reniti.generator.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Date;

import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.AttestationType;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class AttestationGenerationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        AttestationType type = AttestationType.getById(intent.getStringExtra("type_id"));
        String[] reasonList = intent.getStringExtra("reason_id").split(";");

        Reason[] list = new Reason[reasonList.length];

        for(int i = 0; i < list.length; i++)
        {
            list[i] = Reason.getById(reasonList[i], type);
        }

        Date d = new Date();

        AttestationCreateActivity.buildAttestation(this, StorageManager.getInstance().getProfilesManager().getDefaultProfile(), Utils.DATE_FORMAT.format(d), Utils.HOUR_FORMAT.format(d), list, true);

        finishAffinity();
    }
}