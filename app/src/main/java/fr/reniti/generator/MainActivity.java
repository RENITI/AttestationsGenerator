package fr.reniti.generator;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import fr.reniti.generator.activities.AboutActivity;
import fr.reniti.generator.activities.AttestationCreateActivity;
import fr.reniti.generator.activities.ProfileEditActivity;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.PageAdapter;
import fr.reniti.generator.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        new StorageManager(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.view_pager);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.activity_main_add_btn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Page : Attestations
                 */
                if (viewPager.getCurrentItem() == 0) {
                    if (StorageManager.getInstance().getProfilesManager().getProfilesList().size() <= 0) {
                        Snackbar.make(MainActivity.getInstance().findViewById(R.id.activity_main), getResources().getString(R.string.error_no_profile), Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .show();
                        return;
                    }

                    Intent intent = new Intent(MainActivity.getInstance(), AttestationCreateActivity.class);
                    startActivity(intent);
                    return;
                }

                /**
                 * Page : Profils
                 */
                if (viewPager.getCurrentItem() == 1) {
                    Intent intent = new Intent(MainActivity.getInstance(), ProfileEditActivity.class);
                    startActivity(intent);
                    return;
                }
            }
        });

        Intent intent = getIntent();

        if (intent.hasExtra("snackbar_message")) {
            Snackbar.make(viewPager, getResources().getString(intent.getIntExtra("snackbar_message", 0)), Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }

        if (intent.hasExtra("error_message")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERREUR");
            builder.setMessage(getResources().getString(intent.getIntExtra("error_message", 0)));
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {

            });

            AlertDialog dialog = builder.create();



            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface a) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(instance.getResources().getColor(R.color.textColor));
                }
            });
            dialog.show();
        }

        Utils.updateShortcuts(this, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.activity_menu_action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }
}