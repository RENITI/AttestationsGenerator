package fr.reniti.generator.storage.models;


import com.google.gson.annotations.Expose;

import fr.reniti.generator.R;

public enum Reason {

    /* Couvre feu OK */
    CF_TRAVAIL(R.string.reason_cf_travail, AttestationType.COUVRE_FEU, "travail", R.string.reason_cf_travail_desc, R.drawable.ic_baseline_work_16_orange),
    CF_SANTE(R.string.reason_cf_sante, AttestationType.COUVRE_FEU,  "sante", R.string.reason_cf_sante_desc, R.drawable.ic_baseline_local_hospital_16_orange),
    CF_FAMILLE(R.string.reason_cf_famille, AttestationType.COUVRE_FEU,  "famille", R.string.reason_cf_famille_desc, R.drawable.ic_baseline_family_restroom_16_orange),
    CF_CONVOCATION(R.string.reason_cf_convocation_demarches, AttestationType.COUVRE_FEU,  "convocation_demarches", R.string.reason_cf_convocation_demarches_desc, R.drawable.ic_baseline_assignment_16_orange),
    CF_TRANSIT(R.string.reason_cf_transit, AttestationType.COUVRE_FEU, "transit", R.string.reason_cf_transit, R.drawable.ic_baseline_train_16),
    CF_ANIMAUX(R.string.reason_cf_animaux, AttestationType.COUVRE_FEU,  "animaux", R.string.reason_cf_animaux_desc, R.drawable.ic_baseline_pets_16_orange);

    @Expose(serialize = false, deserialize = false)
    private final int displayName;

    @Expose(serialize = false, deserialize = false)
    private final AttestationType relatedType;

    @Expose(serialize = false, deserialize = false)
    private final String id;

    @Expose(serialize = false, deserialize = false)
    private final int longTextId;

    @Expose(serialize = false, deserialize = false)
    private final int iconId;

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
