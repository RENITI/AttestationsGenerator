package fr.reniti.generator.storage.models;


import com.google.gson.annotations.Expose;

import fr.reniti.generator.R;

public enum Reason {

    /* Couvre feu OK */
    CF_TRAVAIL(R.string.reason_cf_travail, AttestationType.COUVRE_FEU, "travail", R.string.reason_cf_travail_desc, R.drawable.ic_baseline_work_16_orange),
    CF_SANTE(R.string.reason_cf_sante, AttestationType.COUVRE_FEU,  "sante", R.string.reason_cf_sante_desc, R.drawable.ic_baseline_local_hospital_16_orange),
    CF_FAMILLE(R.string.reason_cf_famille, AttestationType.COUVRE_FEU,  "famille", R.string.reason_cf_famille_desc, R.drawable.ic_baseline_family_restroom_16_orange),
    CF_CONVOCATION(R.string.reason_cf_convocation_demarches, AttestationType.COUVRE_FEU,  "judiciaire", R.string.reason_cf_convocation_demarches_desc, R.drawable.ic_baseline_assignment_16_orange),
    CF_ANIMAUX(R.string.reason_cf_animaux, AttestationType.COUVRE_FEU,  "animaux", R.string.reason_cf_animaux_desc, R.drawable.ic_baseline_pets_16_orange),

    /* Confinement */
    CONFINEMENT_TRAVAIL(R.string.reason_confinement_travail, AttestationType.CONFINEMENT,  "travail", R.string.reason_confinement_travail_desc, R.drawable.ic_baseline_work_16),
    CONFINEMENT_SANTE(R.string.reason_confinement_sante, AttestationType.CONFINEMENT, "sante", R.string.reason_confinement_sante_desc, R.drawable.ic_baseline_local_hospital_16),
    CONFINEMENT_FAMILLE(R.string.reason_confinement_famille, AttestationType.CONFINEMENT, "famille", R.string.reason_confinement_famille_desc, R.drawable.ic_baseline_family_restroom_16),
    CONFINEMENT_CONVOCATION_DEMARCHE(R.string.reason_confinement_convocation_demarches, AttestationType.CONFINEMENT, "convocation_demarches", R.string.reason_confinement_convocation_demarches_desc, R.drawable.ic_baseline_assignment_16),
    CONFINEMENT_DEMENAGEMENT(R.string.reason_confinement_demenagement, AttestationType.CONFINEMENT,  "demenagement", R.string.reason_confinement_demenagement_desc, R.drawable.ic_baseline_local_shipping_16),
    CONFINEMENT_ACHATS_CULTE_CULTUREL(R.string.reason_confinement_achats_culte_culturel, AttestationType.CONFINEMENT,  "achats_culte_culturel", R.string.reason_confinement_achats_culte_culturel_desc, R.drawable.ic_baseline_shopping_cart_16),
    CONFINEMENT_SPORT(R.string.reason_confinement_sport, AttestationType.CONFINEMENT,  "sport", R.string.reason_confinement_sport_desc, R.drawable.ic_baseline_directions_bike_16);

    @Expose(serialize = false, deserialize = false)
    private final int displayName;

    @Expose(serialize = false, deserialize = false)
    private final AttestationType relatedType;

    //@Expose(serialize = false, deserialize = false)
    //private final int pdfPosY;

    @Expose(serialize = false, deserialize = false)
    private final String id;

    @Expose(serialize = false, deserialize = false)
    private final int longTextId;

    @Expose(serialize = false, deserialize = false)
    private final int iconId;

    //@Expose(serialize = false, deserialize = false)
    //private final int page;

    /**
     * Constructor
     * @param displayName Display name
     * @param id Id
     * @param iconId Icon resource Id
     */
    Reason(int displayName, AttestationType relatedType, String id, int longTextId, int iconId)
    {
        this.displayName = displayName;
        this.relatedType = relatedType;
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
