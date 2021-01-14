package fr.reniti.generator.storage;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Logger;

import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;
import fr.reniti.generator.utils.Utils;

public class AttestationsManager {

    @Expose
    private HashMap<String, Attestation> attestationsList;

    @Expose
    private ArrayList<Reason> lastReasons;

    @Expose
    private boolean autoDelete;

    @Expose
    private boolean disableDeleteWarning;

    public AttestationsManager()
    {
        this(new HashMap<>(), new ArrayList<>(), false, false);
    }

    public AttestationsManager(HashMap<String, Attestation> attestationsList, ArrayList<Reason> lastReasons, boolean autoDelete, boolean disableDeleteWarning)
    {
        this.attestationsList = attestationsList;
        this.lastReasons = lastReasons;
        this.autoDelete = autoDelete;
        this.disableDeleteWarning = disableDeleteWarning;
    }

    public boolean isDisableDeleteWarning() {
        return disableDeleteWarning;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setDisableDeleteWarning(boolean disableDeleteWarning)
    {
        this.disableDeleteWarning = disableDeleteWarning;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    /**
     * Check data
     * @return false if update required
     */
    public boolean checkData()
    {
        boolean isValid = true;

        if(lastReasons == null) {
            lastReasons = new ArrayList<>();
        }

        for(Attestation attestation : new ArrayList<>(attestationsList.values()))
        {
            if(!attestation.isValid() || (autoDelete && (System.currentTimeMillis() - attestation.getCreatedAt()) >= 86400000))
            {
                attestationsList.remove(attestation.getUuid());
                isValid = false;
            }
        }

        for(Reason reason : (ArrayList<Reason>) lastReasons.clone())
        {
            if(!reason.getRelatedType().isAvailable())
            {
                lastReasons.remove(reason);
                isValid = false;
            }
        }

        return isValid;
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

        if(lastReasons == null) {
            lastReasons = new ArrayList<>();
        }

        for(Reason reason : attestation.getReasons())
        {
            if(lastReasons.contains(reason)) {
                lastReasons.remove(reason);
            }

            if(lastReasons.size() >= 3) {
                lastReasons.remove(0);
            }

            lastReasons.add(reason);
        }

        StorageManager.getInstance().getProfilesManager().setDefaultTypeAndSave(attestation.getType());

        StorageManager.getInstance().saveAttestations();
    }
}
