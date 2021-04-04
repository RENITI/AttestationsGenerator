package fr.reniti.generator.fragments;

import android.content.DialogInterface;
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
import java.util.Date;
import java.util.logging.Logger;

import fr.reniti.generator.activities.AttestationGenerationActivity;
import fr.reniti.generator.activities.AttestationViewActivity;
import fr.reniti.generator.MainActivity;
import fr.reniti.generator.R;
import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.AttestationType;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.utils.Utils;

public class AttestationsFragment extends Fragment {

    public static AttestationsFragment newInstance() {
        return (new AttestationsFragment());
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() == null)
        {
            return;
        }

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



                ((TextView) attestationInfosView.findViewById(R.id.attestation_infos_name)).setText(getString(R.string.fragment_attestation_name, attestation.getProfile().getFirstname() + " " + attestation.getProfile().getLastname()));

                ((TextView) attestationInfosView.findViewById(R.id.attestation_infos_date)).setText(attestation.getDatesortie());
                ((TextView) attestationInfosView.findViewById(R.id.attestation_infos_time)).setText(attestation.getHeuresortie());

                if(attestation.getType() != AttestationType.UNKNOWN) {
                    ((TextView) attestationInfosView.findViewById(R.id.attestation_infos_motifs_text)).setText(attestation.getReasonsString(null) + " (" + getString(attestation.getType().getShortName()) + ")");
                }
                attestationInfosView.setOnClickListener(v -> {

                    Intent intent = new Intent(getActivity(), AttestationViewActivity.class);
                    intent.putExtra("attestation_uuid", attestation.getUuid());

                    startActivity(intent);
                });

                attestationInfosView.findViewById(R.id.attestation_infos_delete_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(StorageManager.getInstance().getAttestationsManager().isDisableDeleteWarning())
                        {
                            StorageManager.getInstance().getAttestationsManager().removeAttestation(attestation.getUuid());
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
                            builder.setMessage(R.string.fragment_attestations_delete_title);
                            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

                            });

                            builder.setPositiveButton(R.string.delete, (dialog, which) -> {

                                StorageManager.getInstance().getAttestationsManager().removeAttestation(attestation.getUuid());
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

                attestationInfosView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("DETAILS");

                        StringBuilder message = new StringBuilder();
                        Date creationDate = new Date(attestation.getCreatedAt());

                        Profile profile = attestation.getProfile();

                        builder.setMessage(getString(R.string.fragment_attestation_details, getString(attestation.getType().getShortName()), Utils.DATE_FORMAT.format(creationDate), Utils.HOUR_FORMAT.format(creationDate), profile.getLastname(), profile.getFirstname(), profile.getBirthday(), profile.getAddress(), attestation.getDatesortie(), attestation.getHeuresortie(), attestation.getType() != AttestationType.UNKNOWN ? attestation.getReasonsString(getContext()) : ""));
                        builder.setPositiveButton(R.string.ok, (dialog, which) -> {

                        });

                        if(attestation.getType() != AttestationType.UNKNOWN) {
                            builder.setNeutralButton(R.string.fragment_attestation_details_clone, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent test = new Intent(getActivity(), AttestationGenerationActivity.class);
                                    test.putExtra("attestation_uuid", attestation.getUuid());
                                    startActivityForResult(test, 2);
                                }
                            });

                        }
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(a -> {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.textColor));
                        });
                        dialog.show();
                        return false;
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
