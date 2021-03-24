package fr.reniti.generator.storage.models;


import com.google.gson.annotations.Expose;

import fr.reniti.generator.R;

public enum Reason {

    /* Couvre feu OK */
    CF_TRAVAIL(R.string.reason_cf_travail, AttestationType.COUVRE_FEU, 579, "travail", R.string.activity_attestation_create_reason_cf_travail, R.drawable.ic_baseline_work_16_orange, 0),
    CF_SANTE(R.string.reason_cf_sante, AttestationType.COUVRE_FEU, 546, "sante", R.string.activity_attestation_create_reason_cf_sante, R.drawable.ic_baseline_local_hospital_16_orange, 0),
    CF_FAMILLE(R.string.reason_cf_famille, AttestationType.COUVRE_FEU, 512, "famille", R.string.activity_attestation_create_reason_cf_famille, R.drawable.ic_baseline_family_restroom_16_orange, 0),
    CF_HANDICAP(R.string.reason_cf_handicap, AttestationType.COUVRE_FEU, 478, "handicap", R.string.activity_attestation_create_reason_cf_handicap, R.drawable.ic_baseline_accessible_16_orange, 0),
    CF_CONVOCATION(R.string.reason_cf_convocation, AttestationType.COUVRE_FEU, 460, "judiciaire", R.string.activity_attestation_create_reason_cf_convocation, R.drawable.ic_baseline_assignment_16_orange, 0),
    CF_MISSIONS(R.string.reason_cf_missions, AttestationType.COUVRE_FEU, 410, "missions", R.string.activity_attestation_create_reason_cf_missions, R.drawable.ic_baseline_engineering_16_orange, 0),
    CF_TRANSITS(R.string.reason_cf_transits, AttestationType.COUVRE_FEU, 378, "transit", R.string.activity_attestation_create_reason_cf_transits, R.drawable.ic_baseline_train_16_orange, 0),
    CF_ANIMAUX(R.string.reason_cf_animaux, AttestationType.COUVRE_FEU, 343, "animaux", R.string.activity_attestation_create_reason_cf_animaux, R.drawable.ic_baseline_pets_16_orange, 0),

    /* Confinement loc */
    CONFINEMENT_LOC_SPORT(R.string.reason_confinement_sport, AttestationType.CONFINEMENT_LOC, 367, "sport", R.string.reason_confinement_sport_desc, R.drawable.ic_baseline_directions_bike_16, 0),

    /* Confinement dep */
    CONFINEMENT_DEP_ACHATS(R.string.reason_confinement_dep_achats, AttestationType.CONFINEMENT_DEP, 244, "achats", R.string.reason_confinement_dep_achats_desc, R.drawable.ic_baseline_shopping_cart_16, 0),
    CONFINEMENT_DEP_ENFANTS(R.string.reason_confinement_dep_enfants, AttestationType.CONFINEMENT_DEP, 161, "enfants", R.string.reason_confinement_dep_enfants_desc, R.drawable.ic_baseline_family_restroom_16, 0),
    CONFINEMENT_DEP_CULTE(R.string.reason_confinement_dep_culte, AttestationType.CONFINEMENT_DEP, 781, "culte_culturel", R.string.reason_confinement_dep_culte_desc, R.drawable.ic_baseline_foundation_16, 1),
    CONFINEMENT_DEP_DEMARCHES(R.string.reason_confinement_dep_demarches, AttestationType.CONFINEMENT_DEP, 726, "demarche", R.string.reason_confinement_dep_demarches_desc, R.drawable.ic_baseline_account_balance_16, 1),

    /* Confinement */
    CONFINEMENT_TRAVAIL(R.string.reason_confinement_travail, AttestationType.CONFINEMENT_OTHER, 629, "travail", R.string.reason_confinement_travail_desc, R.drawable.ic_baseline_work_16, 1),
    CONFINEMENT_SANTE(R.string.reason_confinement_sante, AttestationType.CONFINEMENT_OTHER, 533, "sante", R.string.reason_confinement_sante_desc, R.drawable.ic_baseline_local_hospital_16, 1),
    CONFINEMENT_FAMILLE(R.string.reason_confinement_famille, AttestationType.CONFINEMENT_OTHER, 477, "famille", R.string.reason_confinement_famille_desc, R.drawable.ic_baseline_family_restroom_16, 1),
    CONFINEMENT_HANDICAP(R.string.reason_confinement_handicap, AttestationType.CONFINEMENT_OTHER, 422, "handicap", R.string.reason_confinement_handicap_desc, R.drawable.ic_baseline_accessible_16, 1),
    CONFINEMENT_CONVOCATION(R.string.reason_confinement_convocation, AttestationType.CONFINEMENT_OTHER, 380, "judiciaire", R.string.reason_confinement_convocation_desc, R.drawable.ic_baseline_assignment_16, 1),
    CONFINEMENT_DEMENAGEMENT(R.string.reason_confinement_demenagement, AttestationType.CONFINEMENT_OTHER, 311, "demenagement", R.string.reason_confinement_demenagement_desc, R.drawable.ic_baseline_local_shipping_16, 1),
    CONFINEMENT_TRANSITS(R.string.reason_confinement_transit, AttestationType.CONFINEMENT_OTHER, 243, "transit", R.string.reason_confinement_transit, R.drawable.ic_baseline_train_16, 1);

    @Expose(serialize = false, deserialize = false)
    private final int displayName;

    @Expose(serialize = false, deserialize = false)
    private final AttestationType relatedType;

    @Expose(serialize = false, deserialize = false)
    private final int pdfPosY;

    @Expose(serialize = false, deserialize = false)
    private final String id;

    @Expose(serialize = false, deserialize = false)
    private final int longTextId;

    @Expose(serialize = false, deserialize = false)
    private final int iconId;

    @Expose(serialize = false, deserialize = false)
    private final int page;

    /**
     * Constructor
     * @param displayName Display name
     * @param pdfPosY Pdf pos Y
     * @param id Id
     * @param iconId Icon resource Id
     * @param page Page number starting from 0
     */
    Reason(int displayName, AttestationType relatedType, int pdfPosY, String id, int longTextId, int iconId, int page)
    {
        this.displayName = displayName;
        this.relatedType = relatedType;
        this.pdfPosY = pdfPosY;
        this.id = id;
        this.longTextId = longTextId;
        this.iconId = iconId;
        this.page = page;
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

    public int getPage() {
        return page;
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
