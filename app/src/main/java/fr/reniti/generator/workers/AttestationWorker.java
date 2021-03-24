package fr.reniti.generator.workers;

import android.content.Context;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;

import fr.reniti.generator.activities.AttestationCreateActivity;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.AttestationType;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class AttestationWorker extends Worker {
    public AttestationWorker(
             Context context,
             WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        // Do the work here--in this case, upload the images.

        Date d = new Date();


        AttestationType type = AttestationType.COUVRE_FEU;

        Reason[] list = new Reason[] {
                Reason.CF_ANIMAUX
        };



        AttestationCreateActivity.buildAttestation(this.getApplicationContext(), StorageManager.getInstance().getProfilesManager().getDefaultProfile(), Utils.DATE_FORMAT.format(d), Utils.HOUR_FORMAT.format(d), list, true);


        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}
