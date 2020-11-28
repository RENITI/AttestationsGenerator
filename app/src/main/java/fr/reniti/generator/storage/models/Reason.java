package fr.reniti.generator.storage.models;


import com.google.gson.annotations.Expose;

import fr.reniti.generator.R;

public enum Reason {

    TRAVAIL("Travail", 553, "travail", R.id.activity_attestation_create_reason_travail, R.drawable.ic_baseline_work_16),
    ACHATS("Achats, culture et culte", 482, "achats_culturel_cultuel", R.id.activity_attestation_create_reason_achats, R.drawable.ic_baseline_shopping_cart_16),
    SANTE("Santé", 434, "sante", R.id.activity_attestation_create_reason_sante, R.drawable.ic_baseline_local_hospital_16),
    FAMILLE("Famille", 410, "famille", R.id.activity_attestation_create_reason_famille, R.drawable.ic_baseline_family_restroom_16),
    HANDICAP("Handicap", 373, "handicap", R.id.activity_attestation_create_reason_handicap, R.drawable.ic_baseline_accessible_16),
    SPORT_ANIMAUX("Sports et Animaux", 349, "sport_animaux", R.id.activity_attestation_create_reason_sport_animaux, R.drawable.ic_baseline_directions_bike_16),
    CONVOCATION("Convocation", 276, "convocation", R.id.activity_attestation_create_reason_convocation, R.drawable.ic_baseline_assignment_16),
    MISSIONS("Missions", 252, "missions", R.id.activity_attestation_create_reason_missions, R.drawable.ic_baseline_engineering_16),
    ENFANTS("Enfants",228, "enfants", R.id.activity_attestation_create_reason_enfants, R.drawable.ic_baseline_escalator_warning_16);

    @Expose(serialize = false, deserialize = false)
    private final String displayName;

    @Expose(serialize = false, deserialize = false)
    private final int pdfPosY;

    @Expose(serialize = false, deserialize = false)
    private final String id;

    @Expose(serialize = false, deserialize = false)
    private final int fieldId;

    @Expose(serialize = false, deserialize = false)
    private final int iconId;

    /**
     * Constructor
     * @param displayName Display name
     * @param pdfPosY Pdf pos Y
     * @param id Id
     * @param fieldId Field resource Id
     * @param iconId Icon resource Id
     */
    Reason(String displayName, int pdfPosY, String id, int fieldId, int iconId)
    {
        this.displayName = displayName;
        this.pdfPosY = pdfPosY;
        this.id = id;
        this.fieldId = fieldId;
        this.iconId = iconId;
    }

    /**
     * Get Icon resource Id
     * @return iconId
     */
    public int getIconId() {
        return iconId;
    }

    /**
     * Get pdf pos y
     * @return pdfPosY
     */
    public int getPdfPosY() {
        return pdfPosY;
    }

    /**
     * Get display name
     * @return displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Get field resource Id
     * @return fieldId
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
