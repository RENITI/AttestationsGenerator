package fr.reniti.generator.storage.models;

import com.owlike.genson.annotation.JsonProperty;

import fr.reniti.generator.R;

public enum Reason {


    TRAVAIL("Travail", 488, "travail", R.id.activity_attestation_create_reason_travail, R.string.activity_attestation_create_reason_travail),
    ACHATS("Achats", 417, "achats", R.id.activity_attestation_create_reason_achats, R.string.activity_attestation_create_reason_achats),
    SANTE("Santé", 347, "sante", R.id.activity_attestation_create_reason_sante, R.string.activity_attestation_create_reason_sante),
    FAMILLE("Famille", 325, "famille", R.id.activity_attestation_create_reason_famille, R.string.activity_attestation_create_reason_famille),
    HANDICAP("Handicap", 291, "handicap", R.id.activity_attestation_create_reason_handicap, R.string.activity_attestation_create_reason_handicap),
    SPORT_ANIMAUX("Sports et Animaux", 269, "sport_animaux", R.id.activity_attestation_create_reason_sport_animaux, R.string.activity_attestation_create_reason_sport_animaux),
    CONVOCATION("Convocation", 199, "convocation", R.id.activity_attestation_create_reason_convocation, R.string.activity_attestation_create_reason_convocation),
    MISSIONS("Missions", 178, "missions", R.id.activity_attestation_create_reason_missions, R.string.activity_attestation_create_reason_missions),
    ENFANTS("Enfants",157, "enfants", R.id.activity_attestation_create_reason_enfants, R.string.activity_attestation_create_reason_enfants);

    /*TRAVAIL("Activité professionnelle, enseignement et formation", 578, "travail", R.id.activity_attestation_create_reason_travail, R.string.activity_attestation_create_reason_travail),
    ACHATS("Achats", 533, "achats", R.id.activity_attestation_create_reason_achats, R.string.activity_attestation_create_reason_achats),
    SANTE("Consultations et soins", 477, "sante", R.id.activity_attestation_create_reason_sante, R.string.activity_attestation_create_reason_sante),
    FAMILLE("Motif familial impérieux, personnes vulnérables ou précaires ou garde d'enfant", 435, "famille", R.id.activity_attestation_create_reason_famille, R.string.activity_attestation_create_reason_famille),
    HANDICAP("Situation de handicap", 396, "handicap", R.id.activity_attestation_create_reason_handicap, R.string.activity_attestation_create_reason_handicap),
    SPORT_ANIMAUX("Activité individuelle ou animaux de compagnie", 358, "sport_animaux", R.id.activity_attestation_create_reason_sport_animaux, R.string.activity_attestation_create_reason_sport_animaux),
    CONVOCATION("Convocation judiciaire ou administrative", 295, "convocation", R.id.activity_attestation_create_reason_convocation, R.string.activity_attestation_create_reason_convocation),
    MISSIONS("Missions d'intérêt général", 255, "missions", R.id.activity_attestation_create_reason_missions, R.string.activity_attestation_create_reason_missions),
    ENFANTS("Enfants à l'école",211, "enfants", R.id.activity_attestation_create_reason_enfants, R.string.activity_attestation_create_reason_enfants);*/


    @JsonProperty(serialize = false, deserialize = false)
    private String displayName;

    @JsonProperty(serialize = false, deserialize = false)
    private int pdfPosY;

    @JsonProperty(serialize = false, deserialize = false)
    private String id;

    @JsonProperty(serialize = false, deserialize = false)
    private int fieldId;

    @JsonProperty(serialize = false, deserialize = false)
    private int textId;

    /**
     * Constructor
     * @param displayName
     * @param pdfPosY
     * @param id
     * @param fieldId
     * @param textId
     */
    Reason(String displayName, int pdfPosY, String id, int fieldId, int textId)
    {
        this.displayName = displayName;
        this.pdfPosY = pdfPosY;
        this.id = id;
        this.fieldId = fieldId;
        this.textId = textId;
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
     *
     * @return
     */
    public int getTextId() {
        return textId;
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
