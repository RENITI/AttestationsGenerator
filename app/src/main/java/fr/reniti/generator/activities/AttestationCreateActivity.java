package fr.reniti.generator.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.widget.CompoundButtonCompat;

import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import fr.reniti.generator.MainActivity;
import fr.reniti.generator.R;
import fr.reniti.generator.listeners.DateFieldWatcher;
import fr.reniti.generator.listeners.TimeFieldWatcher;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.AttestationType;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class AttestationCreateActivity extends AppCompatActivity {

    public static int NOTIFICATION_ID  = 1459;
    public static final String NOTIFICATION_GROUP = "fr.reniti.generator.CREATE_NOTIFICATION";

    private Profile selectedProfile = null;
    private AttestationType selectedType = null;
    private boolean working = false;

    private HashMap<Reason, CheckBox> boxes = new HashMap<>();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_editor_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new StorageManager(this);
        Date d = new Date();

        setContentView(R.layout.activity_attestation_create);

        Toolbar toolbar = findViewById(R.id.activity_attestation_create_toolbar);

        toolbar.setTitle(R.string.activity_attestation_create_title);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        EditText time = findViewById(R.id.activity_attestation_create_input_heuresortie);
        time.addTextChangedListener(new TimeFieldWatcher(time, Utils.HOUR_FORMAT.format(d)));

        EditText date = ((EditText) findViewById(R.id.activity_attestation_create_input_datesortie));
        date.addTextChangedListener(new DateFieldWatcher(date, true, Utils.DATE_FORMAT.format(d)));

        RadioGroup profileSelect = findViewById(R.id.activity_attestation_create_profil_select);
        Collection<Profile> profiles = StorageManager.getInstance().getProfilesManager().getProfilesList().values();
        String defaultProfileUUID = StorageManager.getInstance().getProfilesManager().getDefaultProfileUUID();

        if(profiles.size() > 0)
        {
            for(Profile profile : profiles)
            {
                RadioButton radio = new RadioButton(this);

                radio.setText(profile.getFirstname() + " " + profile.getLastname() + "\n" + profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity());
                radio.setPadding(30, 30, 0, 30);

                radio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedProfile = profile;
                    }
                 });
                profileSelect.addView(radio);

                if(profile.getUuid().equals(defaultProfileUUID))
                {
                    selectedProfile = profile;
                    profileSelect.check(radio.getId());
                }
            }
        } else {
            Toast.makeText(this, R.string.error_no_profile, Toast.LENGTH_SHORT).show();
            finishAffinity();
        }

        RadioGroup typeSelect = findViewById(R.id.activity_attestation_create_type_select);
        AttestationType defaultType = StorageManager.getInstance().getProfilesManager().getDefaultType();

        if(!defaultType.isAvailable())
        {
            defaultType = AttestationType.getDefault();
        }

        for(AttestationType type : AttestationType.values())
        {
            if(!type.isAvailable())
            {
                continue;
            }

            RadioButton radio = new RadioButton(this);

            radio.setText(getString(type.getName()) + "\n" + getString(type.getExtraText()));
            radio.setPadding(30, 30, 0, 30);

            radio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedType = type;

                    updateReasonsView();
                }
            });
            typeSelect.addView(radio);

            if(defaultType == type)
            {
                selectedType = type;
                typeSelect.check(radio.getId());
            }
        }

        updateReasonsView();
        findViewById(R.id.activity_attestation_create_confirm_btn).setOnClickListener(v -> saveAttestation());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if(item.getItemId() == R.id.activity_editor_confirm_m) {
            saveAttestation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setWorking(boolean a)
    {
        working = a;
        View v = findViewById(R.id.attestation_create_loader);
        v.setVisibility(a ? View.VISIBLE : View.INVISIBLE);
        v.invalidate();
    }

    public void saveAttestation() {
        if (working) {
            return;
        }
        setWorking(true);

        if (selectedProfile == null) {
            setWorking(false);
            Toast.makeText(this, R.string.activity_attestation_create_error_profile, Toast.LENGTH_LONG).show();
            return;
        }

        Editable datesortie = ((EditText) findViewById(R.id.activity_attestation_create_input_datesortie)).getText();
        Editable heuresortie = ((EditText) findViewById(R.id.activity_attestation_create_input_heuresortie)).getText();

        if (!Utils.isValidDate(datesortie.toString(), true)) {
            setWorking(false);
            Toast.makeText(this, R.string.activity_attestation_create_error_datesortie, Toast.LENGTH_LONG).show();
            return;
        }

        if (heuresortie.length() < 5) {
            setWorking(false);
            Toast.makeText(this, R.string.activity_attestation_create_error_heuresortie, Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<Reason> reasons = new ArrayList<>();

        for(Reason reason : boxes.keySet())
        {
            if(boxes.get(reason).isChecked())
            {
                if(reason.getRelatedType() != selectedType)
                {
                    Toast.makeText(this, R.string.error_invalid_reasons, Toast.LENGTH_LONG).show();
                    break;
                }

                reasons.add(reason);
            }
        }

        if (reasons.size() <= 0) {
            setWorking(false);
            Toast.makeText(this, R.string.activity_attestation_create_error_reasons, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra("shortcut") && intent.getBooleanExtra("shortcut", false))
        {
            Intent test = new Intent(this, AttestationGenerationActivity.class);

            StringBuilder reasonList = new StringBuilder();

            for(Reason reason : reasons)
            {
                reasonList.append(";" + reason.getId());
            }

            test.putExtra("reason_id", reasonList.substring(1));
            test.putExtra("type_id", selectedType.getId());
            startActivityForResult(test, 1);
            finishAffinity();
        } else {

            Thread thread = new Thread(() -> {
                buildAttestation(this, selectedProfile, datesortie.toString(), heuresortie.toString(), reasons.toArray(new Reason[0]), false);
            });
            thread.start();
        }
    }

    public void updateReasonsView()
    {
        LinearLayout reasonListView = findViewById(R.id.activity_attestation_create_reason_list);

        boxes.clear();
        reasonListView.removeAllViews();

        for(Reason reason : Reason.values())
        {
            if(reason.getRelatedType() == selectedType)
            {
                CheckBox checkBox = new CheckBox(this);

                LinearLayout.LayoutParams checkBoxLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                checkBoxLayoutParams.setMargins(Utils.dpToPx(16), Utils.dpToPx(6) ,Utils.dpToPx(16), 0);
                checkBox.setLayoutParams(checkBoxLayoutParams);

                checkBox.setText(reason.getLongTextId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.mainColor)));
                } else {
                    CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(getResources().getColor(R.color.mainColor)));
                }

                reasonListView.addView(checkBox);
                boxes.put(reason, checkBox);
            }
        }

    }

    public static void buildAttestation(Activity activity, Profile profile, String dateSortie, String heureSortie, Reason[] reasons, boolean shortcut)
    {

            Attestation attestation = new Attestation(profile, dateSortie,  heureSortie, reasons, reasons[0].getRelatedType());

            PDFBoxResourceLoader.init(activity);
            boolean success = Utils.savePDF(attestation, activity);

            if(success) {
                StorageManager.getInstance().getAttestationsManager().addAttestationAndSave(attestation);
                Utils.updateShortcuts(activity, true);


               if(!StorageManager.getInstance().getAttestationsManager().isDisableNotification())
               {
                   String notificationChannelId = null;

                   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                       NotificationChannel channel = new NotificationChannel("ATTESTATIONS_GENERATOR", "Attestations", NotificationManager.IMPORTANCE_HIGH);
                       NotificationManager notificationManager =
                               (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
                       notificationManager.createNotificationChannel(channel);

                       channel.setGroup(NOTIFICATION_GROUP);

                       notificationChannelId = channel.getId();
                   }

                   String title = activity.getString(R.string.notification_title, profile.getFirstname() + " " + profile.getLastname());

                   NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, notificationChannelId);
                   builder.setSmallIcon(R.mipmap.ic_launcher);
                   builder.setContentTitle(title);
                   builder.setGroup(NOTIFICATION_GROUP);
                   builder.setContentText(activity.getString(R.string.notification_short_desc));
                   builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                   builder.setAutoCancel(true);

                   NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                   bigTextStyle.setBigContentTitle(title);
                   bigTextStyle.setSummaryText("");
                   bigTextStyle.bigText(activity.getString(R.string.notification_long_desc, attestation.getReasons().length > 1 ? "s" : "", attestation.getReasonsString(activity), attestation.getDatesortie(), attestation.getHeuresortie(),  Utils.DATE_FORMAT.format(attestation.getCreatedAt()), Utils.HOUR_FORMAT.format(attestation.getCreatedAt())));

                   builder.setStyle(bigTextStyle);

                   Intent notificationIntent = new Intent(activity, AttestationViewActivity.class);
                   notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   notificationIntent.putExtra("attestation_uuid", attestation.getUuid());
                   PendingIntent pendingNotificationIntent = PendingIntent.getActivity(activity, 0, notificationIntent, 0);

                   builder.setContentIntent(pendingNotificationIntent);

                   Notification summaryNotification =
                           new NotificationCompat.Builder(activity, notificationChannelId)
                                   .setContentTitle(activity.getString(R.string.tab_certificates))
                                   .setSmallIcon(R.mipmap.ic_launcher)
                                   .setGroup(NOTIFICATION_GROUP)
                                   .setGroupSummary(true)
                                   .build();

                   NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
                   notificationManager.notify(NOTIFICATION_ID++, builder.build());
                   notificationManager.notify(1458, summaryNotification);
               }

                if (!shortcut) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("snackbar_message", R.string.attestation_create_success);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(intent);

                    activity.finish();
                } else {

                    if(reasons.length == 1)
                    {
                        Toast.makeText(activity, activity.getString(R.string.activity_attestation_create_success, profile.getFirstname() + " " + profile.getLastname(), activity.getString(reasons[0].getDisplayName()) + " (" + activity.getString(reasons[0].getRelatedType().getShortName()) + ")"), Toast.LENGTH_LONG).show();
                    }else {

                        StringBuilder reasonsStr = new StringBuilder();

                        for(Reason reason : reasons)
                        {
                            reasonsStr.append(", " + activity.getString(reason.getDisplayName()));
                        }
                        Toast.makeText(activity, activity.getString(R.string.activity_attestation_create_success2, profile.getFirstname() + " " + profile.getLastname(), reasonsStr.substring(2) + " (" + activity.getString(reasons[0].getRelatedType().getShortName()) + ")"), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                if (!shortcut) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("error_message", R.string.attestation_create_failed);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(intent);

                    activity.finish();
                } else {
                    Toast.makeText(activity, R.string.attestation_create_failed, Toast.LENGTH_LONG).show();
                }
            }
    }
}