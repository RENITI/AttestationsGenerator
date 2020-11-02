package fr.reniti.generator.storage.models;

import com.owlike.genson.annotation.JsonProperty;

import fr.reniti.generator.R;

public enum Reason {

    TRAVAIL("Travail", 578, "travail", R.id.activity_attestation_create_reason_travail),
    ACHATS("Achats", 533, "achats", R.id.activity_attestation_create_reason_achats),
    SANTE("Santé", 477, "sante", R.id.activity_attestation_create_reason_sante),
    FAMILLE("Famille", 435, "famille", R.id.activity_attestation_create_reason_famille),
    HANDICAP("Handicap", 396, "handicap", R.id.activity_attestation_create_reason_handicap),
    SPORT_ANIMAUX("Sport et animaux", 358, "sport_animaux", R.id.activity_attestation_create_reason_sport_animaux),
    CONVOCATION("Convocation", 295, "convocation", R.id.activity_attestation_create_reason_convocation),
    MISSIONS("Missions", 255, "missions", R.id.activity_attestation_create_reason_missions),
    ENFANTS("Enfants",211, "enfants", R.id.activity_attestation_create_reason_enfants);


    @JsonProperty(serialize = false, deserialize = false)
    private String displayName;

    @JsonProperty(serialize = false, deserialize = false)
    private int pdfPosY;

    @JsonProperty(serialize = false, deserialize = false)
    private String id;

    @JsonProperty(serialize = false, deserialize = false)
    private int fieldId;

    /**
     * Constructor
     * @param displayName
     * @param pdfPosY
     * @param id
     */
    Reason(String displayName, int pdfPosY, String id, int fieldId)
    {
        this.displayName = displayName;
        this.pdfPosY = pdfPosY;
        this.id = id;
        this.fieldId = fieldId;
    }

    /**
     * Getter
     * @return
     */
    @JsonProperty(serialize = false, deserialize = false)
    public int getPdfPosY() {
        return pdfPosY;
    }

    /**
     * Getter
     * @return
     */
    @JsonProperty(serialize = false, deserialize = false)
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Getter
     * @return
     */
    @JsonProperty(serialize = false, deserialize = false)
    public String getId() {
        return id;
    }

    /**
     * Getter
     * @return
     */
    @JsonProperty(serialize = false, deserialize = false)
    public int getFieldId() {
        return fieldId;
    }

    @JsonProperty(serialize = false, deserialize = false)
    public static Reason getById(String id)
    {
        for(Reason reason : values())
        {
            if(reason.getId().equals(id))
            {
                return reason;
            }
        }
        return null;
    }
}
