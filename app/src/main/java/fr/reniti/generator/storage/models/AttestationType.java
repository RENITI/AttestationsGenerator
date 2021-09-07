package fr.reniti.generator.storage.models;

import com.google.gson.annotations.Expose;

import fr.reniti.generator.R;

public enum AttestationType {

    UNKNOWN("unknown", R.string.attestation_type_unknown, R.string.attestation_type_unknown, R.string.attestation_type_unknown, ""),
    COUVRE_FEU("couvre_feu", R.string.attestation_type_couvrefeu, R.string.attestation_type_couvrefeu_shortname, R.string.attestation_type_couvrefeu_extra, "certificate-01-08-2021-CF.json"),
    CONFINEMENT("confinement", R.string.attestation_type_confinement, R.string.attestation_type_confinement_shortname, R.string.attestation_type_confinement_extra, "certificate-01-08-2021.json");

    @Expose(serialize = false, deserialize = false)
    private final String id;

    @Expose(serialize = false, deserialize = false)
    private final int name;

    @Expose(serialize = false, deserialize = false)
    private final int shortName;

    @Expose(serialize = false, deserialize = false)
    private final int extraText;

    @Expose(serialize = false, deserialize = false)
    private final String assetName;


    AttestationType(String id, int name, int shortName, int extraText, String assetName)
    {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.extraText = extraText;
        this.assetName = assetName;
    }

    public static AttestationType getDefault()
    {
        return COUVRE_FEU;
    }

    public boolean isAvailable()
    {

        if(this == UNKNOWN)
        {
            return false;
        }
        return true;
    }

    public int getShortName() {
        return shortName;
    }

    public String getAssetName() {
        return assetName;
    }

    public int getExtraText() {
        return extraText;
    }

    public String getId() {
        return id;
    }

    public int getName() {
        return name;
    }

    public static AttestationType getById(String id)
    {
        for(AttestationType type : values())
        {
            if(type.getId().equals(id))
            {
                return type;
            }
        }
        return null;
    }
}
