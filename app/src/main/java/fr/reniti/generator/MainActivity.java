package fr.reniti.generator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;

import fr.reniti.generator.activities.AboutActivity;
import fr.reniti.generator.activities.AttestationCreateActivity;
import fr.reniti.generator.activities.ProfileEditActivity;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.utils.PageAdapter;
import fr.reniti.generator.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static WeakReference<MainActivity> instance;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = new WeakReference<>(this);

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

        fab.setOnClickListener(view -> {

            if (viewPager.getCurrentItem() == 0) {
                if (StorageManager.getInstance().getProfilesManager().getProfilesList().size() <= 0) {
                    Intent intent = new Intent(MainActivity.getInstance().get(), ProfileEditActivity.class);
                    startActivity(intent);
                    return;
                }

                Intent intent = new Intent(MainActivity.getInstance().get(), AttestationCreateActivity.class);
                startActivity(intent);
            }

            if (viewPager.getCurrentItem() == 1) {
                Intent intent = new Intent(MainActivity.getInstance().get(), ProfileEditActivity.class);
                startActivity(intent);
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
            dialog.setOnShowListener(a -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.textColor)));
            dialog.show();
        }

        Utils.updateShortcuts(this, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if(item.getItemId() == R.id.activity_menu_action_about)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static WeakReference<MainActivity> getInstance() {
        return instance;
    }
}