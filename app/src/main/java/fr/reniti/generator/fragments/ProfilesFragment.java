package fr.reniti.generator.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.StorageManager;

public class ProfilesFragment extends Fragment {


    @Override
    public void onResume() {
        super.onResume();

        View fragmentView = getView();
        Collection<Profile> profiles = StorageManager.getInstance().getProfilesManager().getProfilesList().values();

        LinearLayout profilesContainer = (LinearLayout) fragmentView.findViewById(R.id.fragment_profiles_container);
        profilesContainer.removeAllViews();

        if(profiles.size() > 0)
        {

            RelativeLayout profilesRoot = (RelativeLayout) fragmentView.findViewById(R.id.fragment_profiles_rootview);
            fragmentView.findViewById(R.id.fragment_profiles_default_title).setVisibility(View.INVISIBLE);

            for(Profile profile : profiles)
            {
                View profileInfosView = getLayoutInflater().inflate(R.layout.profile_infos, (ViewGroup) fragmentView, false);
                TextView nameInfos = profileInfosView.findViewById(R.id.profile_infos_name);
                nameInfos.setText(profile.getFirstname() + " " + profile.getLastname());

                TextView birthInfos = profileInfosView.findViewById(R.id.profile_infos_birth);
                birthInfos.setText("Né(e) le " + profile.getBirthday() + " à " + profile.getPlaceofbirth());

                TextView locationInfos = profileInfosView.findViewById(R.id.profile_infos_location);
                locationInfos.setText(profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity());

                ImageButton deleteBtn = profileInfosView.findViewById(R.id.profile_infos_delete_btn);

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StorageManager.getInstance().getProfilesManager().removeProfile(profile.getUuid());
                        onResume();
                    }
                });


                profilesContainer.addView(profileInfosView);
                Logger.getGlobal().log(Level.INFO, "Profile : " + profile.getFirstname() + " " + profile.getLastname() + " / " + profiles.size());
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
