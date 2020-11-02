package fr.reniti.generator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.StorageManager;

public class AttestationViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attestation_viewer);

        Toolbar toolbar = findViewById(R.id.activity_attestation_viewer_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_attestation_view_title);

        Intent intent = getIntent();

        Attestation attestation = StorageManager.getInstance().getAttestationsManager().getAttestation(intent.getStringExtra("attestation_uuid"));

        PDFView view = findViewById(R.id.activity_attestation_viewer_pdf);
        File file = new File(getFilesDir() + "/" + attestation.getUuid() + ".pdf");

        if(!file.exists())
        {
            return;
        }
        view.fromFile(file).load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}