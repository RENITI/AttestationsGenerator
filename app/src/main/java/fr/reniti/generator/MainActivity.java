package fr.reniti.generator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;

import fr.reniti.generator.activities.AboutActivity;
import fr.reniti.generator.activities.AttestationCreateActivity;
import fr.reniti.generator.activities.ProfileEditActivity;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.utils.PageAdapter;

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
               if(viewPager.getCurrentItem() == 0)
               {
                   if(StorageManager.getInstance().getProfilesManager().getProfilesList().size() <= 0)
                   {
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
               if(viewPager.getCurrentItem() == 1)
               {
                   Intent intent = new Intent(MainActivity.getInstance(), ProfileEditActivity.class);
                   startActivity(intent);
                   return;
               }
            }
        });

        Intent intent = getIntent();

        if(intent.hasExtra("snackbar_message"))
        {
            Snackbar.make(viewPager, getResources().getString(intent.getIntExtra("snackbar_message", 0)), Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
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