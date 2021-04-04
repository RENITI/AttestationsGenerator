package fr.reniti.generator.storage.models;

import android.icu.util.Calendar;

import com.google.gson.annotations.Expose;

import java.util.Date;

import fr.reniti.generator.R;
import fr.reniti.generator.utils.PDFPos;

public enum AttestationType {

    UNKNOWN("unknown", R.string.attestation_type_unknown, R.string.attestation_type_unknown, R.string.attestation_type_unknown, "", null, null, null, null, null, null, null, 0),



    CONFINEMENT("confinement", R.string.attestation_type_confinement, R.string.attestation_type_confinement_shortname, R.string.attestation_type_confinement_extra, "certificate-04-04-2021.json",
            /* nom */
            new PDFPos(111, 516),
            /* date naissance */
            new PDFPos(111, 501),
            /* lieu naissance */
            new PDFPos(228, 501),
            /* address */
            new PDFPos(126, 487),
            /* bottomcity */
            new PDFPos(72, 99),
            /* date sortie */
            new PDFPos(72, 83),
            /* heure sortie */
            new PDFPos(310, 83),
            60),


    // Updated 04-04-2021
    COUVRE_FEU("couvre_feu", R.string.attestation_type_couvrefeu, R.string.attestation_type_couvrefeu_shortname, R.string.attestation_type_couvrefeu_extra, "certificate-04-04-2021-CF.json",
            /* nom */
            new PDFPos(144, 705),
            /* date naissance */
            new PDFPos(144, 684),
            /* lieu naissance */
            new PDFPos(310, 684),
            /* address */
            new PDFPos(148, 665),
            /* bottomcity */
            new PDFPos(72, 99),
            /* date sortie */
            new PDFPos(72, 83),
            /* heure sortie */
            new PDFPos(310, 83),
            72);

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

    @Expose(serialize = false, deserialize = false)
    private final PDFPos identityPos;

    @Expose(serialize = false, deserialize = false)
    private final PDFPos birthDayPos;

    @Expose(serialize = false, deserialize = false)
    private final PDFPos birthPlacePos;

    @Expose(serialize = false, deserialize = false)
    private final PDFPos completeAdressPos;

    @Expose(serialize = false, deserialize = false)
    private final PDFPos bottomCityPos;

    @Expose(serialize = false, deserialize = false)
    private final PDFPos datePos;

    @Expose(serialize = false, deserialize = false)
    private final PDFPos timePos;

    private int reasonsBaseX;

    AttestationType(String id, int name, int shortName, int extraText, String assetName, PDFPos identityPos, PDFPos birthDayPos, PDFPos birthPlacePos, PDFPos completeAdressPos, PDFPos bottomCityPos, PDFPos datePos, PDFPos timePos, int reasonsBaseX)
    {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.extraText = extraText;
        this.assetName = assetName;
        this.identityPos = identityPos;
        this.birthDayPos = birthDayPos;
        this.birthPlacePos = birthPlacePos;
        this.completeAdressPos = completeAdressPos;
        this.bottomCityPos = bottomCityPos;
        this.datePos = datePos;
        this.timePos = timePos;
        this.reasonsBaseX = reasonsBaseX;
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

    public int getReasonsBaseX() {
        return reasonsBaseX;
    }

    public PDFPos getBirthDayPos() {
        return birthDayPos;
    }

    public PDFPos getBirthPlacePos() {
        return birthPlacePos;
    }

    public PDFPos getBottomCityPos() {
        return bottomCityPos;
    }

    public PDFPos getCompleteAdressPos() {
        return completeAdressPos;
    }

    public PDFPos getDatePos() {
        return datePos;
    }

    public PDFPos getIdentityPos() {
        return identityPos;
    }

    public PDFPos getTimePos() {
        return timePos;
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
