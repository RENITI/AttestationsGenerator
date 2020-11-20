package fr.reniti.generator.storage.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

import fr.reniti.generator.utils.Utils;
import io.nayuki.qrcodegen.QrCode;

public class Attestation {

    @Expose
    private String uuid;

    @Expose
    private long createdAt;

    @Expose
    private Profile profile;

    @Expose
    private String datesortie;

    @Expose
    private String heuresortie;

    @Expose
    private Reason[] reasons;

    /**
     * Constructor : create new attestation
     * @param profile
     * @param datesortie
     * @param heuresortie
     * @param reasons
     */
    public Attestation(Profile profile, String datesortie, String heuresortie, Reason[] reasons)
    {
        this.uuid = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.profile = profile;
        this.datesortie = datesortie;
        this.heuresortie = heuresortie;
        this.reasons = reasons;
    }

    /**
     * Constructor
     * @param uuid
     * @param createdAt
     * @param profile
     * @param datesortie
     * @param heuresortie
     * @param reasons
     */
    public Attestation(String uuid,
                        long createdAt,
                       Profile profile,
                       String datesortie,
                       String heuresortie,
                       Reason[] reasons)
    {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.profile = profile;
        this.datesortie = datesortie;
        this.heuresortie = heuresortie;
        this.reasons = reasons;
    }

    public String getFileName()
    {
        return uuid + ".pdf";
    }

    public String getReasonsString(boolean human)
    {
        String rawReasons = "";
        for(Reason reason : reasons)
        {
            if(!human)
            {
                rawReasons += ", " + reason.getId();
            } else {
                rawReasons += ", "  + reason.getDisplayName();
            }
        }
        return rawReasons.substring(2);
    }

    public Bitmap getQRCode(int size)
    {
        StringBuilder builder = new StringBuilder();
        Date creationDate = new Date(createdAt);

        builder.append("Cree le: " + Utils.DATE_FORMAT.format(creationDate) + " a " + Utils.HOUR_FORMAT.format(creationDate).replace(':', 'h') + ";\n ");
        builder.append("Nom: " + profile.getLastname() + ";\n ");
        builder.append("Prenom: " + profile.getFirstname() + ";\n ");
        builder.append("Naissance: " + profile.getBirthday() + " a " + profile.getPlaceofbirth() + ";\n ");
        builder.append("Adresse: " + profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity() + ";\n ");
        builder.append("Sortie: " + datesortie + " a " + heuresortie + ";\n ");
        builder.append("Motifs: " + getReasonsString(false));

        Bitmap bitmap = null;

        try {
            QrCode qr0 = QrCode.encodeText(builder.toString(), QrCode.Ecc.HIGH);
            bitmap = qr0.toImage(4, 10);
       } catch (Exception e) {e.printStackTrace();}
       return bitmap;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getDatesortie() {
        return datesortie;
    }

    public String getHeuresortie() {
        return heuresortie;
    }

    public String getUuid() {
        return uuid;
    }

    public Reason[] getReasons() {
        return reasons;
    }

    /**
     * Check if attestation content is valid
     * @return
     */
    public boolean isValid() {
        if(profile == null || reasons == null || reasons.length <= 0 || heuresortie == null || datesortie == null || uuid == null)
        {
            return false;
        }

        for(int i = 0; i < reasons.length; i++)
        {
            if(reasons[i] == null)
            {
                return false;
            }
        }
        return true;
    }
}
