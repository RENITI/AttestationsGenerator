package fr.reniti.generator.storage.models;

import android.icu.util.Calendar;

import com.google.gson.annotations.Expose;

import java.util.Date;

import fr.reniti.generator.utils.PDFPos;

public enum AttestationType {

    CONFINEMENT("confinement", "Déplacement dérogatoire Confinement", "Confinement", "28/11/2020", "certificate.pdf", new PDFPos(92, 702), new PDFPos(92, 684), new PDFPos(214, 684), new PDFPos(104, 665), new PDFPos(78, 76), new PDFPos(63, 58), new PDFPos(227, 58), 47),
    COUVRE_FEU("couvre_feu", "Déplacement dérogatoire Couvre-feu", "Couvre-feu", "15/12/2020", "cf_certificate.pdf", new PDFPos(119, 669), new PDFPos(119, 646), new PDFPos(312, 646), new PDFPos(133, 622), new PDFPos(105, 168), new PDFPos(91, 146), new PDFPos(312, 146), 73);

    @Expose(serialize = false, deserialize = false)
    private final String id;

    @Expose(serialize = false, deserialize = false)
    private final String name;

    @Expose(serialize = false, deserialize = false)
    private final String shortName;

    @Expose(serialize = false, deserialize = false)
    private final String documentDate;

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

    AttestationType(String id, String name, String shortName, String documentDate, String assetName, PDFPos identityPos, PDFPos birthDayPos, PDFPos birthPlacePos, PDFPos completeAdressPos, PDFPos bottomCityPos, PDFPos datePos, PDFPos timePos, int reasonsBaseX)
    {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.documentDate = documentDate;
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
        if(CONFINEMENT.isAvailable())
        {
            return CONFINEMENT;
        }
        return COUVRE_FEU;
    }

    public boolean isAvailable()
    {
        if(this == CONFINEMENT)
        {
            // 16 dec 2020
            if(System.currentTimeMillis() >= 1608073200000L)
            {
                return false;
            }
        }
        return true;
    }

    public String getShortName() {
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

    public String getDocumentDate() {
        return documentDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
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
