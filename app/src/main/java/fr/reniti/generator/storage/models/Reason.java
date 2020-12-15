package fr.reniti.generator.storage.models;


import com.google.gson.annotations.Expose;

import fr.reniti.generator.R;

public enum Reason {

    /*TRAVAIL("Travail", 553, "travail", R.id.activity_attestation_create_reason_travail, R.drawable.ic_baseline_work_16),
    ACHATS("Achats, culture et culte", 482, "achats_culturel_cultuel", R.id.activity_attestation_create_reason_achats, R.drawable.ic_baseline_shopping_cart_16),
    SANTE("Santé", 434, "sante", R.id.activity_attestation_create_reason_sante, R.drawable.ic_baseline_local_hospital_16),
    FAMILLE("Famille", 410, "famille", R.id.activity_attestation_create_reason_famille, R.drawable.ic_baseline_family_restroom_16),
    HANDICAP("Handicap", 373, "handicap", R.id.activity_attestation_create_reason_handicap, R.drawable.ic_baseline_accessible_16),
    SPORT_ANIMAUX("Sports et Animaux", 349, "sport_animaux", R.id.activity_attestation_create_reason_sport_animaux, R.drawable.ic_baseline_directions_bike_16),
    CONVOCATION("Convocation", 276, "convocation", R.id.activity_attestation_create_reason_convocation, R.drawable.ic_baseline_assignment_16),
    MISSIONS("Missions", 252, "missions", R.id.activity_attestation_create_reason_missions, R.drawable.ic_baseline_engineering_16),
    ENFANTS("Enfants",228, "enfants", R.id.activity_attestation_create_reason_enfants, R.drawable.ic_baseline_escalator_warning_16);
*/
    TRAVAIL(R.string.reason_travail, AttestationType.CONFINEMENT, 553, "travail", R.string.activity_attestation_create_reason_travail, R.drawable.ic_baseline_work_16),
    ACHATS(R.string.reason_achats,AttestationType.CONFINEMENT, 482, "achats_culturel_cultuel", R.string.activity_attestation_create_reason_achats,R.drawable.ic_baseline_shopping_cart_16),
    SANTE(R.string.reason_sante,AttestationType.CONFINEMENT, 434, "sante",R.string.activity_attestation_create_reason_sante, R.drawable.ic_baseline_local_hospital_16),
    FAMILLE(R.string.reason_famille,AttestationType.CONFINEMENT, 410, "famille",R.string.activity_attestation_create_reason_famille, R.drawable.ic_baseline_family_restroom_16),
    HANDICAP(R.string.reason_handicap,AttestationType.CONFINEMENT, 373, "handicap", R.string.activity_attestation_create_reason_handicap, R.drawable.ic_baseline_accessible_16),
    SPORT_ANIMAUX(R.string.reason_sport_animaux,AttestationType.CONFINEMENT, 349, "sport_animaux", R.string.activity_attestation_create_reason_sport_animaux, R.drawable.ic_baseline_directions_bike_16),
    CONVOCATION(R.string.reason_convocation,AttestationType.CONFINEMENT, 276, "convocation",R.string.activity_attestation_create_reason_convocation, R.drawable.ic_baseline_assignment_16),
    MISSIONS(R.string.reason_missions,AttestationType.CONFINEMENT, 252, "missions", R.string.activity_attestation_create_reason_missions, R.drawable.ic_baseline_engineering_16),
    ENFANTS(R.string.reason_enfants,AttestationType.CONFINEMENT,228, "enfants",  R.string.activity_attestation_create_reason_missions, R.drawable.ic_baseline_escalator_warning_16),

    CF_TRAVAIL(R.string.reason_travail, AttestationType.COUVRE_FEU, 540, "travail", R.string.activity_attestation_create_reason_cf_travail, R.drawable.ic_baseline_work_16_orange),
    CF_SANTE(R.string.reason_sante, AttestationType.COUVRE_FEU, 508, "sante", R.string.activity_attestation_create_reason_cf_sante, R.drawable.ic_baseline_local_hospital_16_orange),
    CF_FAMILLE(R.string.reason_famille, AttestationType.COUVRE_FEU, 474, "famille", R.string.activity_attestation_create_reason_cf_famille, R.drawable.ic_baseline_family_restroom_16_orange),
    CF_HANDICAP(R.string.reason_handicap, AttestationType.COUVRE_FEU, 441, "handicap", R.string.activity_attestation_create_reason_cf_handicap, R.drawable.ic_baseline_accessible_16_orange),
    CF_CONVOCATION(R.string.reason_convocation, AttestationType.COUVRE_FEU, 418, "convocation", R.string.activity_attestation_create_reason_cf_convocation, R.drawable.ic_baseline_assignment_16_orange),
    CF_MISSIONS(R.string.reason_missions, AttestationType.COUVRE_FEU, 397, "missions", R.string.activity_attestation_create_reason_cf_missions, R.drawable.ic_baseline_engineering_16_orange),
    CF_TRANSITS(R.string.reason_transits, AttestationType.COUVRE_FEU, 363, "transits", R.string.activity_attestation_create_reason_cf_transits, R.drawable.ic_baseline_train_16_orange),
    CF_ANIMAUX(R.string.reason_animaux, AttestationType.COUVRE_FEU, 330, "animaux", R.string.activity_attestation_create_reason_cf_animaux, R.drawable.ic_baseline_pets_16_orange);
    @Expose(serialize = false, deserialize = false)
    private final int displayName;

    @Expose(serialize = false, deserialize = false)
    private final AttestationType relatedType;

    @Expose(serialize = false, deserialize = false)
    private final int pdfPosY;

    @Expose(serialize = false, deserialize = false)
    private final String id;

    //@Expose(serialize = false, deserialize = false)
    //private final int fieldId;

    @Expose(serialize = false, deserialize = false)
    private final int longTextId;

    @Expose(serialize = false, deserialize = false)
    private final int iconId;

    /**
     * Constructor
     * @param displayName Display name
     * @param pdfPosY Pdf pos Y
     * @param id Id
     * @param iconId Icon resource Id
     */
    Reason(int displayName, AttestationType relatedType, int pdfPosY, String id, int longTextId, int iconId)
    {
        this.displayName = displayName;
        this.relatedType = relatedType;
        this.pdfPosY = pdfPosY;
        this.id = id;
        this.longTextId = longTextId;
        this.iconId = iconId;
    }

    public int getLongTextId() {
        return longTextId;
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
    public int getDisplayName() {
        return displayName;
    }

    public AttestationType getRelatedType() {
        return relatedType;
    }

    /**
     * Get id
     * @return id
     */
    public String getId() {
        return id;
    }


    public static Reason getById(String id, AttestationType type)
    {
        for(Reason reason : values())
        {
            if(reason.getId().equals(id) && reason.getRelatedType() == type)
            {
                return reason;
            }
        }
        return null;
    }
}
