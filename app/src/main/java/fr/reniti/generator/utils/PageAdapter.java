package fr.reniti.generator.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import fr.reniti.generator.fragments.AttestationsFragment;
import fr.reniti.generator.fragments.ProfilesFragment;

public class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager mgr) {
            super(mgr);
        }

        @Override
        public int getCount() {
            return(2);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return AttestationsFragment.newInstance();
                case 1:
                    return ProfilesFragment.newInstance();
                default:
                    return null;
            }
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Attestations";
                case 1:
                    return "Profils";
                default:
                    return null;
            }
        }
}
