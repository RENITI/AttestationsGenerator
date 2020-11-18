package fr.reniti.generator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import fr.reniti.generator.activities.AboutActivity;
import fr.reniti.generator.activities.AttestationCreateActivity;
import fr.reniti.generator.activities.ProfileEditActivity;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.PageAdapter;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;
    private final String CHANNEL_ID = "1";
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

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

        if (intent.getAction() == Intent.ACTION_VIEW ) {

        }

        if(intent.hasExtra("snackbar_message"))
        {
            Snackbar.make(viewPager, getResources().getString(intent.getIntExtra("snackbar_message", 0)), Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }

        Intent notifyIntent = new Intent(this, AttestationCreateActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Etes vous sorti ?")
                .setContentText("Cliquez ici pour créer une nouvelle attestation")
                .setAutoCancel(true)
                .setContentIntent(notifyPendingIntent)
                ;


        ArrayList<Reason> reasons = StorageManager.getInstance().getAttestationsManager().getLastReasons();

        for(int i = reasons.size() - 1; i >= 0; i--)
        {

            Reason reason = reasons.get(i);

            builder.addAction(0, reason.getDisplayName(), notifyPendingIntent);
        }

        Notification notification = builder.build();
        NotificationManagerCompat.from(this).notify(1, notification);
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