package fr.reniti.generator.storage;

import com.owlike.genson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class AttestationsManager {

    @JsonProperty(serialize = true, deserialize = true)
    private HashMap<String, Attestation> attestationsList;

    @JsonProperty(serialize = true, deserialize = true)
    private ArrayList<Reason> lastReasons;

    public AttestationsManager()
    {
        this(new HashMap<>(), new ArrayList<>());
    }

    public AttestationsManager(@JsonProperty(value = "attestationsList") HashMap<String, Attestation> attestationsList, @JsonProperty(value= "lastReasons") ArrayList<Reason> lastReasons)
    {
        this.attestationsList = attestationsList;
        this.lastReasons = lastReasons;

    }

    @JsonProperty(serialize = false, deserialize = false)
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

    @JsonProperty(serialize = false, deserialize = false)
    public ArrayList<Reason> getLastReasons() {
        return lastReasons;
    }

    @JsonProperty(serialize = false, deserialize = false)
    public HashMap<String, Attestation> getAttestationsList() {
        return attestationsList;
    }

    @JsonProperty(serialize = false, deserialize = false)
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

    @JsonProperty(serialize = false, deserialize = false)
    public void removeAttestation(String uuid)
    {
        if(attestationsList.containsKey(uuid)) {

            StorageManager.getInstance().removeFile(uuid + ".pdf");
            attestationsList.remove(uuid);
            StorageManager.getInstance().saveAttestations();
        }
    }

    @JsonProperty(serialize = false, deserialize = false)
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
