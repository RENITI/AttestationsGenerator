package fr.reniti.generator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fr.reniti.generator.activities.AttestationViewActivity;
import fr.reniti.generator.MainActivity;
import fr.reniti.generator.R;
import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.StorageManager;

public class AttestationsFragment extends Fragment {

    public static AttestationsFragment newInstance() {
        return (new AttestationsFragment());
    }

    @Override
    public void onResume() {
        super.onResume();

        View fragmentView = getView();

        ArrayList<Attestation> attestations = StorageManager.getInstance().getAttestationsManager().getSortedList();

        LinearLayout profilesContainer = (LinearLayout) fragmentView.findViewById(R.id.fragment_attestations_container);
        profilesContainer.removeAllViews();

        if(attestations.size() > 0)
        {
            RelativeLayout profilesRoot = (RelativeLayout) fragmentView.findViewById(R.id.fragment_attestations_rootview);
            fragmentView.findViewById(R.id.fragment_attestations_default_title).setVisibility(View.INVISIBLE);

            for(Attestation attestation : attestations)
            {
                View attestationInfosView = getLayoutInflater().inflate(R.layout.attestation_infos, (ViewGroup) fragmentView, false);

                TextView nameInfos = attestationInfosView.findViewById(R.id.attestation_infos_name);
                nameInfos.setText("Attestation de " + attestation.getProfile().getFirstname() + " " + attestation.getProfile().getLastname());

                TextView commonInfos = attestationInfosView.findViewById(R.id.attestation_infos_common);
                commonInfos.setText("Valable à partir du " + attestation.getDatesortie() + " à " + attestation.getHeuresortie());

                TextView motifsInfos = attestationInfosView.findViewById(R.id.attestation_infos_motifs);
                motifsInfos.setText("Motif" +(attestation.getReasons().length > 1 ? "s" : "")+ ": " + attestation.getReasonsString(true));

                attestationInfosView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.getInstance(), AttestationViewActivity.class);
                        intent.putExtra("attestation_uuid", attestation.getUuid());

                        startActivity(intent);
                    }
                });

                attestationInfosView.findViewById(R.id.attestation_infos_delete_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StorageManager.getInstance().getAttestationsManager().removeAttestation(attestation.getUuid());
                        onResume();
                    }
                });

                profilesContainer.addView(attestationInfosView);
            }
        } else {
            fragmentView.findViewById(R.id.fragment_attestations_default_title).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attestations, container, false);
    }
}
