package fr.reniti.generator.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Collection;

import fr.reniti.generator.MainActivity;
import fr.reniti.generator.R;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Profile;

public class ProfilesFragment extends Fragment {


    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() == null)
        {
            return;
        }

        View fragmentView = getView();
        Collection<Profile> profiles = StorageManager.getInstance().getProfilesManager().getProfilesList().values();
        String defaultProfileUuid = StorageManager.getInstance().getProfilesManager().getDefaultProfileUUID();

        LinearLayout profilesContainer = (LinearLayout) fragmentView.findViewById(R.id.fragment_profiles_container);
        profilesContainer.removeAllViews();

        if(profiles.size() > 0)
        {
            fragmentView.findViewById(R.id.fragment_profiles_default_title).setVisibility(View.INVISIBLE);

            for(Profile profile : profiles)
            {
                View profileInfosView = getLayoutInflater().inflate(R.layout.profile_infos, (ViewGroup) fragmentView, false);
                TextView nameInfos = profileInfosView.findViewById(R.id.profile_infos_name);

                if(defaultProfileUuid.equals(profile.getUuid()))
                {
                    nameInfos.setText(getString(R.string.fragment_profile_name_default, profile.getFirstname() + " " + profile.getLastname()));

                } else {
                    nameInfos.setText(profile.getFirstname() + " " + profile.getLastname());

                }
                
                TextView birthInfos = profileInfosView.findViewById(R.id.profile_infos_birth_text);
                birthInfos.setText(getString(R.string.fragment_profile_birth, profile.getBirthday(), profile.getPlaceofbirth()));

                TextView locationInfos = profileInfosView.findViewById(R.id.profile_infos_address_text);
                locationInfos.setText(profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity());

                ImageButton deleteBtn = profileInfosView.findViewById(R.id.profile_infos_delete_btn);

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(StorageManager.getInstance().getAttestationsManager().isDisableDeleteWarning()) {
                            StorageManager.getInstance().getProfilesManager().removeProfile(profile.getUuid());
                            Toast.makeText(getActivity(), R.string.fragment_delete_success, Toast.LENGTH_SHORT).show();
                            onResume();
                        } else {

                            FragmentActivity mainActivity = getActivity();

                            if(mainActivity == null || mainActivity.isFinishing())
                            {
                                return;
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                            builder.setTitle("ATTENTION");
                            builder.setMessage(R.string.fragment_profiles_delete_title);
                            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

                            });

                            builder.setPositiveButton(R.string.delete, (dialog, which) -> {

                                StorageManager.getInstance().getProfilesManager().removeProfile(profile.getUuid());
                                Toast.makeText(mainActivity, R.string.fragment_delete_success, Toast.LENGTH_SHORT).show();
                                onResume();
                            });

                            AlertDialog dialog = builder.create();
                            dialog.setOnShowListener(a -> {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mainActivity.getResources().getColor(R.color.design_default_color_error));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mainActivity.getResources().getColor(R.color.textColor));
                            });
                            dialog.show();

                        }
                    }
                });

                if(!defaultProfileUuid.equals(profile.getUuid()))
                {
                    profileInfosView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StorageManager.getInstance().getProfilesManager().setDefaultProfile(profile.getUuid());
                            Toast.makeText(getActivity(), R.string.fragment_profiles_default, Toast.LENGTH_SHORT).show();
                            onResume();
                        }
                    });
                }

                profilesContainer.addView(profileInfosView);
            }
        } else {
            fragmentView.findViewById(R.id.fragment_profiles_default_title).setVisibility(View.VISIBLE);
        }
    }

    public static Fragment newInstance() {
        return (new ProfilesFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profiles, container, false);
    }
}
