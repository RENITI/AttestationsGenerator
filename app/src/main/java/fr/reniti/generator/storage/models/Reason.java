package fr.reniti.generator.storage.models;


import com.google.gson.annotations.Expose;

import fr.reniti.generator.R;

public enum Reason {

    TRAVAIL("Travail", 488, "travail", R.id.activity_attestation_create_reason_travail, R.string.activity_attestation_create_reason_travail, R.drawable.ic_baseline_work_16),
    ACHATS("Achats", 417, "achats", R.id.activity_attestation_create_reason_achats, R.string.activity_attestation_create_reason_achats, R.drawable.ic_baseline_shopping_cart_16),
    SANTE("Santé", 347, "sante", R.id.activity_attestation_create_reason_sante, R.string.activity_attestation_create_reason_sante,R.drawable.ic_baseline_local_hospital_16),
    FAMILLE("Famille", 325, "famille", R.id.activity_attestation_create_reason_famille, R.string.activity_attestation_create_reason_famille, R.drawable.ic_baseline_family_restroom_16),
    HANDICAP("Handicap", 291, "handicap", R.id.activity_attestation_create_reason_handicap, R.string.activity_attestation_create_reason_handicap, R.drawable.ic_baseline_accessible_16),
    SPORT_ANIMAUX("Sports et Animaux", 269, "sport_animaux", R.id.activity_attestation_create_reason_sport_animaux, R.string.activity_attestation_create_reason_sport_animaux, R.drawable.ic_baseline_directions_bike_16),
    CONVOCATION("Convocation", 199, "convocation", R.id.activity_attestation_create_reason_convocation, R.string.activity_attestation_create_reason_convocation, R.drawable.ic_baseline_assignment_16),
    MISSIONS("Missions", 178, "missions", R.id.activity_attestation_create_reason_missions, R.string.activity_attestation_create_reason_missions, R.drawable.ic_baseline_engineering_16),
    ENFANTS("Enfants",157, "enfants", R.id.activity_attestation_create_reason_enfants, R.string.activity_attestation_create_reason_enfants, R.drawable.ic_baseline_escalator_warning_16);

    @Expose(serialize = false, deserialize = false)
    private String displayName;

    @Expose(serialize = false, deserialize = false)
    private int pdfPosY;

    @Expose(serialize = false, deserialize = false)
    private String id;

    @Expose(serialize = false, deserialize = false)
    private int fieldId;

    @Expose(serialize = false, deserialize = false)
    private int textId;

    @Expose(serialize = false, deserialize = false)
    private int iconId;
    /**
     * Constructor
     * @param displayName
     * @param pdfPosY
     * @param id
     * @param fieldId
     * @param textId
     * @param iconId
     */
    Reason(String displayName, int pdfPosY, String id, int fieldId, int textId, int iconId)
    {
        this.displayName = displayName;
        this.pdfPosY = pdfPosY;
        this.id = id;
        this.fieldId = fieldId;
        this.textId = textId;
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    /**
     * Getter
     * @return
     */
    public int getPdfPosY() {
        return pdfPosY;
    }

    /**
     * Getter
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Getter
     * @return
     */
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
    public int getFieldId() {
        return fieldId;
    }

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
