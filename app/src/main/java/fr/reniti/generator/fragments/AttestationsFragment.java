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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.logging.Logger;

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

                ((TextView) attestationInfosView.findViewById(R.id.attestation_infos_name)).setText("Attestation de " + attestation.getProfile().getFirstname() + " " + attestation.getProfile().getLastname());

                ((TextView) attestationInfosView.findViewById(R.id.attestation_infos_common)).setText("Valable à partir du " + attestation.getDatesortie() + " à " + attestation.getHeuresortie());

                ((TextView) attestationInfosView.findViewById(R.id.attestation_infos_motifs)).setText("Motif" +(attestation.getReasons().length > 1 ? "s" : "")+ ": " + attestation.getReasonsString(true));

                attestationInfosView.setOnClickListener(v -> {

                    Intent intent = new Intent(MainActivity.getInstance().get(), AttestationViewActivity.class);
                    intent.putExtra("attestation_uuid", attestation.getUuid());

                    startActivity(intent);
                });

                attestationInfosView.findViewById(R.id.attestation_infos_delete_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(StorageManager.getInstance().getAttestationsManager().isDisableDeleteWarning())
                        {
                            StorageManager.getInstance().getAttestationsManager().removeAttestation(attestation.getUuid());
                            Toast.makeText(MainActivity.getInstance().get(), R.string.fragment_attestations_delete_success, Toast.LENGTH_SHORT).show();
                            onResume();
                        } else {

                            FragmentActivity mainActivity = getActivity();

                            if(mainActivity == null || mainActivity.isFinishing())
                            {
                                return;
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                            builder.setTitle("ATTENTION");
                            builder.setMessage(R.string.fragment_attestations_delete_title);
                            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

                            });

                            builder.setPositiveButton(R.string.delete, (dialog, which) -> {

                                StorageManager.getInstance().getAttestationsManager().removeAttestation(attestation.getUuid());
                                Toast.makeText(mainActivity, R.string.fragment_attestations_delete_success, Toast.LENGTH_SHORT).show();
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
