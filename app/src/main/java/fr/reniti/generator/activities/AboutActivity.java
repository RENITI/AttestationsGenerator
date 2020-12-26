package fr.reniti.generator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.StorageManager;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.activity_about_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_about_title);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);

            ((TextView) findViewById(R.id.activity_about_version)).setText(getString(R.string.activity_about_version, pInfo.versionName, StorageManager.getInstance().calcUsedSpace()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.activity_about_credits)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.activity_about_github_link)).setMovementMethod(LinkMovementMethod.getInstance());

        findViewById(R.id.activity_about_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.reniti.fr"));
                startActivity(intent);
            }
        });

        Switch switchInput = (Switch) findViewById(R.id.activity_about_layout_settings_autodelete);

        switchInput.setChecked(StorageManager.getInstance().getAttestationsManager().isAutoDelete());

        switchInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StorageManager instance = StorageManager.getInstance();
                instance.getAttestationsManager().setAutoDelete(isChecked);
                instance.saveAttestations();
            }
        });

        Switch deleteWarning = (Switch) findViewById(R.id.activity_about_layout_settings_deletewarning);

        deleteWarning.setChecked(StorageManager.getInstance().getAttestationsManager().isDisableDeleteWarning());

        deleteWarning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StorageManager instance = StorageManager.getInstance();
                instance.getAttestationsManager().setDisableDeleteWarning(isChecked);
                instance.saveAttestations();
            }
        });
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