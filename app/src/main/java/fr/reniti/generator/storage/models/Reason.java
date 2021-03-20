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
   /* TRAVAIL(R.string.reason_travail, AttestationType.CONFINEMENT, 553, "travail", R.string.activity_attestation_create_reason_travail, R.drawable.ic_baseline_work_16),
    ACHATS(R.string.reason_achats,AttestationType.CONFINEMENT, 482, "achats_culturel_cultuel", R.string.activity_attestation_create_reason_achats,R.drawable.ic_baseline_shopping_cart_16),
    SANTE(R.string.reason_sante,AttestationType.CONFINEMENT, 434, "sante",R.string.activity_attestation_create_reason_sante, R.drawable.ic_baseline_local_hospital_16),
    FAMILLE(R.string.reason_famille,AttestationType.CONFINEMENT, 410, "famille",R.string.activity_attestation_create_reason_famille, R.drawable.ic_baseline_family_restroom_16),
    HANDICAP(R.string.reason_handicap,AttestationType.CONFINEMENT, 373, "handicap", R.string.activity_attestation_create_reason_handicap, R.drawable.ic_baseline_accessible_16),
    SPORT_ANIMAUX(R.string.reason_sport_animaux,AttestationType.CONFINEMENT, 349, "sport_animaux", R.string.activity_attestation_create_reason_sport_animaux, R.drawable.ic_baseline_directions_bike_16),
    CONVOCATION(R.string.reason_convocation,AttestationType.CONFINEMENT, 276, "convocation",R.string.activity_attestation_create_reason_convocation, R.drawable.ic_baseline_assignment_16),
    MISSIONS(R.string.reason_missions,AttestationType.CONFINEMENT, 252, "missions", R.string.activity_attestation_create_reason_missions, R.drawable.ic_baseline_engineering_16),
    ENFANTS(R.string.reason_enfants,AttestationType.CONFINEMENT,228, "enfants",  R.string.activity_attestation_create_reason_missions, R.drawable.ic_baseline_escalator_warning_16),*/

    CF_TRAVAIL(R.string.reason_cf_travail, AttestationType.COUVRE_FEU, 550, "travail", R.string.activity_attestation_create_reason_cf_travail, R.drawable.ic_baseline_work_16_orange),
    CF_SANTE(R.string.reason_cf_sante, AttestationType.COUVRE_FEU, 487, "sante", R.string.activity_attestation_create_reason_cf_sante, R.drawable.ic_baseline_local_hospital_16_orange),
    CF_FAMILLE(R.string.reason_cf_famille, AttestationType.COUVRE_FEU, 412, "famille", R.string.activity_attestation_create_reason_cf_famille, R.drawable.ic_baseline_family_restroom_16_orange),
    CF_HANDICAP(R.string.reason_cf_handicap, AttestationType.COUVRE_FEU, 350, "handicap", R.string.activity_attestation_create_reason_cf_handicap, R.drawable.ic_baseline_accessible_16_orange),
    CF_CONVOCATION(R.string.reason_cf_convocation, AttestationType.COUVRE_FEU, 302, "judiciaire", R.string.activity_attestation_create_reason_cf_convocation, R.drawable.ic_baseline_assignment_16_orange),
    CF_MISSIONS(R.string.reason_cf_missions, AttestationType.COUVRE_FEU, 225, "missions", R.string.activity_attestation_create_reason_cf_missions, R.drawable.ic_baseline_engineering_16_orange),
    CF_TRANSITS(R.string.reason_cf_transits, AttestationType.COUVRE_FEU, 170, "transits", R.string.activity_attestation_create_reason_cf_transits, R.drawable.ic_baseline_train_16_orange),
    CF_ANIMAUX(R.string.reason_cf_animaux, AttestationType.COUVRE_FEU, 114, "animaux", R.string.activity_attestation_create_reason_cf_animaux, R.drawable.ic_baseline_pets_16_orange),


    /* cf WE NA */
    CONF_WE_COURSES(R.string.reason_courses, AttestationType.CONFINEMENT_WEEKEND, 304, "courses", R.string.activity_attestation_create_reason_conf_courses, R.drawable.ic_baseline_shopping_cart_16),
    CONF_WE_SPORT(R.string.reason_sport, AttestationType.CONFINEMENT_WEEKEND, 261, "sport", R.string.activity_attestation_create_reason_conf_sport, R.drawable.ic_baseline_directions_bike_16),
    CONF_WE_RASSEMBLEMENTS(R.string.reason_rassemblements, AttestationType.CONFINEMENT_WEEKEND, 190,"rassemblement",  R.string.activity_attestation_create_reason_conf_rassemblements, R.drawable.ic_baseline_people_16),
    CONF_WE_DEMARCHES(R.string.reason_demarches, AttestationType.CONFINEMENT_WEEKEND,  145, "demarche",R.string.activity_attestation_create_reason_conf_demarches, R.drawable.ic_baseline_account_balance_16),

    /* lockdown 20/03/2021 */
    // raisons CF
    CONFINEMENT_TRAVAIL(R.string.reason_cf_travail, AttestationType.CONFINEMENT, 550, "travail", R.string.activity_attestation_create_reason_cf_travail, R.drawable.ic_baseline_work_16_orange),
    CONFINEMENT_SANTE(R.string.reason_cf_sante, AttestationType.CONFINEMENT, 487, "sante", R.string.activity_attestation_create_reason_cf_sante, R.drawable.ic_baseline_local_hospital_16_orange),
    CONFINEMENT_FAMILLE(R.string.reason_cf_famille, AttestationType.CONFINEMENT, 412, "famille", R.string.activity_attestation_create_reason_cf_famille, R.drawable.ic_baseline_family_restroom_16_orange),
    CONFINEMENT_HANDICAP(R.string.reason_cf_handicap, AttestationType.CONFINEMENT, 350, "handicap", R.string.activity_attestation_create_reason_cf_handicap, R.drawable.ic_baseline_accessible_16_orange),
    CONFINEMENT_CONVOCATION(R.string.reason_cf_convocation, AttestationType.CONFINEMENT, 302, "judiciaire", R.string.activity_attestation_create_reason_cf_convocation, R.drawable.ic_baseline_assignment_16_orange),
    CONFINEMENT_MISSIONS(R.string.reason_cf_missions, AttestationType.CONFINEMENT, 225, "missions", R.string.activity_attestation_create_reason_cf_missions, R.drawable.ic_baseline_engineering_16_orange),
    CONFINEMENT_TRANSITS(R.string.reason_cf_transits, AttestationType.CONFINEMENT, 170, "transit", R.string.activity_attestation_create_reason_cf_transits, R.drawable.ic_baseline_train_16_orange),
    CONFINEMENT_ANIMAUX(R.string.reason_cf_animaux, AttestationType.CONFINEMENT, 114, "animaux", R.string.activity_attestation_create_reason_cf_animaux, R.drawable.ic_baseline_pets_16_orange),

    CONFINEMENT_ACHATS_PRO(R.string.reason_confinement_achats_pro, AttestationType.CONFINEMENT, 582, "achat_pro", R.string.reason_confinement_achats_pro_desc, R.drawable.ic_baseline_work_16),
    CONFINEMENT_ACHATS(R.string.reason_confinement_achats, AttestationType.CONFINEMENT, 528, "courses", R.string.reason_confinement_achats_desc, R.drawable.ic_baseline_shopping_cart_16),
    CONFINEMENT_DEMENAGEMENT(R.string.reason_confinement_demenagement, AttestationType.CONFINEMENT, 480, "demenagement", R.string.reason_confinement_demenagement_desc, R.drawable.ic_baseline_local_shipping_16),
    CONFINEMENT_SPORT(R.string.reason_confinement_sport, AttestationType.CONFINEMENT, 410, "sport", R.string.reason_confinement_sport_desc, R.drawable.ic_baseline_directions_bike_16),
    CONFINEMENT_DEMARCHES(R.string.reason_confinement_demarches, AttestationType.CONFINEMENT, 328, "demarche", R.string.reason_confinement_demarches_desc, R.drawable.ic_baseline_account_balance_16),
    CONFINEMENT_CULTE(R.string.reason_confinement_culte, AttestationType.CONFINEMENT, 272, "culte", R.string.reason_confinement_culte_desc, R.drawable.ic_baseline_foundation_16),
    CONFINEMENT_RASSEMBLEMENTS(R.string.reason_confinement_rassemblements, AttestationType.CONFINEMENT, 238, "rassemblement", R.string.reason_confinement_rassemblements_desc, R.drawable.ic_baseline_people_16);




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
