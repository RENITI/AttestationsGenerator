package fr.reniti.generator.storage;

import com.owlike.genson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import fr.reniti.generator.storage.models.Attestation;

public class AttestationsManager {

    @JsonProperty(serialize = true, deserialize = true)
    private HashMap<String, Attestation> attestationsList;

    public AttestationsManager()
    {
        this(new HashMap<>());
    }

    public AttestationsManager(@JsonProperty(value = "attestationsList") HashMap<String, Attestation> attestationsList)
    {
        this.attestationsList = attestationsList;
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
        StorageManager.getInstance().saveAttestations();
    }
}
