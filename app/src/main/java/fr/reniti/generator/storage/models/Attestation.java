package fr.reniti.generator.storage.models;

import android.graphics.Bitmap;

import com.owlike.genson.annotation.JsonProperty;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import fr.reniti.generator.utils.Utils;
import io.nayuki.qrcodegen.QrCode;

public class Attestation {

    @JsonProperty(serialize = true, deserialize = true)
    private String uuid;

    @JsonProperty(serialize = true, deserialize = true)
    private long createdAt;

    @JsonProperty(serialize = true, deserialize = true)
    private Profile profile;

    @JsonProperty(serialize = true, deserialize = true)
    private String datesortie;

    @JsonProperty(serialize = true, deserialize = true)
    private String heuresortie;

    @JsonProperty(serialize = true, deserialize = true)
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
    public Attestation(@JsonProperty(value = "uuid") String uuid,
                       @JsonProperty(value = "createdAt") long createdAt,
                       @JsonProperty(value = "profile") Profile profile,
                       @JsonProperty(value = "datesortie") String datesortie,
                       @JsonProperty(value = "heuresortie") String heuresortie,
                       @JsonProperty(value = "reasons") Reason[] reasons)
    {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.profile = profile;
        this.datesortie = datesortie;
        this.heuresortie = heuresortie;
        this.reasons = reasons;
    }

    @JsonProperty(serialize = false, deserialize = false)
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

    @JsonProperty(serialize = false, deserialize = false)
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

    @JsonProperty(serialize = false, deserialize = false)
    public long getCreatedAt() {
        return createdAt;
    }

    @JsonProperty(serialize = false, deserialize = false)
    public Profile getProfile() {
        return profile;
    }

    @JsonProperty(serialize = false, deserialize = false)
    public String getDatesortie() {
        return datesortie;
    }

    @JsonProperty(serialize = false, deserialize = false)
    public String getHeuresortie() {
        return heuresortie;
    }

    @JsonProperty(serialize = false, deserialize = false)
    public String getUuid() {
        return uuid;
    }

    @JsonProperty(serialize = false, deserialize = false)
    public Reason[] getReasons() {
        return reasons;
    }
}
