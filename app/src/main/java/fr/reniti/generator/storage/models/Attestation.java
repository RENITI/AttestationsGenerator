package fr.reniti.generator.storage.models;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

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

    @Expose
    private AttestationType type;

    /**
     * Constructor : create new attestation
     * @param profile
     * @param datesortie
     * @param heuresortie
     * @param reasons
     * @param type
     */
    public Attestation(Profile profile, String datesortie, String heuresortie, Reason[] reasons, AttestationType type)
    {
        this(UUID.randomUUID().toString(), System.currentTimeMillis(), profile, datesortie, heuresortie, reasons, type);
    }

    /**
     * Constructor
     * @param uuid
     * @param createdAt
     * @param profile
     * @param datesortie
     * @param heuresortie
     * @param reasons
     * @param type
     */
    public Attestation(String uuid,
                        long createdAt,
                       Profile profile,
                       String datesortie,
                       String heuresortie,
                       Reason[] reasons, AttestationType type)
    {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.profile = profile;
        this.datesortie = datesortie;
        this.heuresortie = heuresortie;
        this.reasons = reasons;
        this.type = type;
    }

    public AttestationType getType() {
        
        return type != null ? type : AttestationType.UNKNOWN;
    }

    public String getFileName()
    {
        return uuid + ".pdf";
    }

    /**
     * Get reasons string
     * @param context in order to have human readable (translation)
     * @return
     */
    public String getReasonsString(@Nullable  Context context)
    {
        String rawReasons = "";
        for(Reason reason : reasons)
        {
            if(context == null)
            {
                rawReasons += ", " + reason.getId();
            } else {
                rawReasons += ", "  + context.getString(reason.getDisplayName());
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
        builder.append("Naissance: " + profile.getBirthday()+ ";\n ");
        builder.append("Adresse: " + profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity() + ";\n ");
        builder.append("Sortie: " + datesortie + " a " + heuresortie + ";\n ");
        builder.append("Motifs: " + getReasonsString(null) + ";");

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

    public boolean hasReason(String rawName)
    {
        for(Reason reason : reasons)
        {
            if(reason.getId().contentEquals(rawName))
            {
                return true;
            }
        }
        return false;
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
                this.type = AttestationType.UNKNOWN;
                this.reasons = new Reason[0];
                break;
               // return false;
            }
        }
        return true;
    }
}
