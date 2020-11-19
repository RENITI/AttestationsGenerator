package fr.reniti.generator.storage;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class AttestationsManager {

    @Expose
    private HashMap<String, Attestation> attestationsList;

    @Expose
    private ArrayList<Reason> lastReasons;

    public AttestationsManager()
    {
        this(new HashMap<>(), new ArrayList<>());
    }

    public AttestationsManager(HashMap<String, Attestation> attestationsList, ArrayList<Reason> lastReasons)
    {
        this.attestationsList = attestationsList;
        this.lastReasons = lastReasons;
    }

    public boolean checkData()
    {
        if(lastReasons == null) {
            lastReasons = new ArrayList<>();
        }

        if(lastReasons.size() < 3)
        {
            for(Reason reason : Utils.DEFAULT_REASONS)
            {
                if(lastReasons.size() >= 3)
                {
                    break;
                }

                if(!lastReasons.contains(reason))
                {
                    lastReasons.add(reason);
                }
            }
            return false;
        }
        return true;
    }

    public ArrayList<Reason> getLastReasons() {
        return lastReasons;
    }

    public HashMap<String, Attestation> getAttestationsList() {
        return attestationsList;
    }

    public Attestation getAttestation(String uuid)
    {
        return attestationsList.get(uuid);
    }

    public ArrayList<Attestation> getSortedList()
    {
        ArrayList<Attestation> collection = new ArrayList<>(attestationsList.values());
        Collections.sort(collection, (Comparator<Attestation>) (o1, o2) -> (int) (o2.getCreatedAt() - o1.getCreatedAt()));
        return collection;
    }

    public void removeAttestation(String uuid)
    {
        if(attestationsList.containsKey(uuid)) {

            StorageManager.getInstance().removeFile(uuid + ".pdf");
            attestationsList.remove(uuid);
            StorageManager.getInstance().saveAttestations();
        }
    }

    public void addAttestationAndSave(Attestation attestation)
    {
        this.attestationsList.put(attestation.getUuid(), attestation);

        if(lastReasons == null)
            lastReasons = new ArrayList<>();

        for(Reason reason : attestation.getReasons())
        {

            if(lastReasons.contains(reason)) {
                lastReasons.remove(reason);
            }

            if(lastReasons.size() >= 3)
                lastReasons.remove(0);

            lastReasons.add(reason);
        }
        StorageManager.getInstance().saveAttestations();
    }
}
