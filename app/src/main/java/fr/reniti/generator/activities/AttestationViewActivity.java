package fr.reniti.generator.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.List;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Attestation;

public class AttestationViewActivity extends AppCompatActivity {

    private Attestation attestation;
    private int state;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_viewer_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attestation_viewer);

        Toolbar toolbar = findViewById(R.id.activity_attestation_viewer_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_attestation_view_title);

        Intent intent = getIntent();

        attestation = StorageManager.getInstance().getAttestationsManager().getAttestation(intent.getStringExtra("attestation_uuid"));

        PDFView view = findViewById(R.id.activity_attestation_viewer_pdf);

        File file = new File(getFilesDir() + "/" + attestation.getUuid() + ".pdf");

        state = 0;

        if(!file.exists())
        {
            return;
        }

        view.fromFile(file).load();
    }


    /**
     * Toggle between PDF View and QR Code View
     */
    public void toggleQRCode()
    {
        ImageView qr = findViewById(R.id.activity_attestation_viewer_qr_image);

        if(state == 0)
        {
            qr.setImageBitmap(attestation.getQRCode(256));
        }

        state = state == 2 ? 1 : 2;

        if(state == 2)
        {
            findViewById(R.id.activity_attestation_viewer_qr_image).setVisibility(View.VISIBLE);
            findViewById(R.id.activity_attestation_viewer_pdf).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.activity_attestation_viewer_qr_image).setVisibility(View.INVISIBLE);
            findViewById(R.id.activity_attestation_viewer_pdf).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if(item.getItemId() == R.id.activity_attestation_view_qr)
        {
            toggleQRCode();
            return true;
        }

        if(item.getItemId() == R.id.activity_attestation_view_pdf)
        {
            exportPDF();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Used to export PDF
     */
    public void exportPDF() {
        File sourceFile = new File(getFilesDir() + "/" + attestation.getFileName());
        Uri uri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", sourceFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");

        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resInfoList.size() > 0) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }

            intent = Intent.createChooser(intent, "Ouvrir le PDF");
            startActivity(intent);

        } else {
            Toast.makeText(this, R.string.error_no_pdf_reader, Toast.LENGTH_SHORT).show();
        }
    }
}