package fr.reniti.generator.utils;

import android.app.Activity;
import android.graphics.Bitmap;

import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;

public class Utils {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");

    public static boolean isValidDate(String rawDate, boolean allowFuture)
    {
        if(rawDate.length() < 8)
        {
            return false;
        }

        try {
            Date d = DATE_FORMAT.parse(rawDate);

            if(allowFuture || d.before(new Date()))
            {
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @from https://github.com/LAB-MI/attestation-deplacement-derogatoire-q4-2020/blob/main/src/js/pdf-util.js
     * @param test
     * @return
     * @throws IOException
     */
    public static int getIdealFontSize(String test) throws IOException {
        int currentSize = 11;

        float textWidth  = PDType1Font.HELVETICA.getStringWidth(test) / 1000 * currentSize;

        while (textWidth  > 83 && currentSize > 7) {
            currentSize--;
            textWidth = PDType1Font.HELVETICA.getStringWidth(test) / 1000 * currentSize;
        }

        return textWidth > 83 ? 7 : currentSize;
    }

    /**
     * Draw text
     * @param content
     * @param tx
     * @param ty
     * @param text
     * @param size
     * @return
     * @throws IOException
     */
    public static PDPageContentStream drawText(PDPageContentStream content, int tx, int ty, String text, int size) throws IOException {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA, size);
        content.newLineAtOffset(tx, ty);
        content.showText(text);
        content.endText();
        return content;
    }

    /**
     * Save PDF related to a attestation
     * @param attestation
     * @param activity
     */
    public static void savePDF(Attestation attestation, Activity activity)
    {
        try {
            InputStream stream = activity.getResources().getAssets().open("certificate.pdf");
            PDDocument document = PDDocument.load(stream);

            PDPage page = document.getPage(0);

            PDPageContentStream content = new PDPageContentStream(document, page, true, true);

            Profile profile = attestation.getProfile();

            content.setNonStrokingColor(0, 0, 0); //black text
            content = Utils.drawText(content, 119, 696, profile.getFirstname() + " " + profile.getLastname(), 11);
            content = Utils.drawText(content, 119, 674, profile.getBirthday(), 11);
            content = Utils.drawText(content, 297, 674, profile.getPlaceofbirth(), 11);
            content = Utils.drawText(content, 133, 652, profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity(), 11);

            for(Reason reason : attestation.getReasons())
            {
                content =Utils.drawText(content, 78, reason.getPdfPosY(), "x", 18);
            }

            content = Utils.drawText(content, 105, 177, profile.getCity(), Utils.getIdealFontSize(profile.getCity()));
            content = Utils.drawText(content, 91, 153, attestation.getDatesortie(), 11);
            content = Utils.drawText(content, 264, 153, attestation.getHeuresortie(), 11);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Bitmap smallQr = attestation.getQRCode(92);
            smallQr.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            PDImageXObject pdImage = new PDImageXObject(document, new ByteArrayInputStream(outputStream.toByteArray()), COSName.DCT_DECODE, smallQr.getWidth(), smallQr.getHeight(), 8, PDDeviceRGB.INSTANCE);
            outputStream.close();

            content.drawImage(pdImage,page.getMediaBox().getWidth() - 156, 100, 92, 92);

            content.close();


            PDPage page2 = new PDPage();
            PDPageContentStream content2 = new PDPageContentStream(document, page2, true , true);

            outputStream = new ByteArrayOutputStream();

            Bitmap qr = attestation.getQRCode(300);
            qr.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            pdImage = new PDImageXObject(document, new ByteArrayInputStream(outputStream.toByteArray()), COSName.DCT_DECODE, qr.getWidth(), qr.getHeight(), 8, PDDeviceRGB.INSTANCE);
            outputStream.close();
            content2.drawImage(pdImage,50, page2.getMediaBox().getHeight() - 350, 300, 300);

            content2.close();
            document.addPage(page2);
            //document.close();

            document.save(activity.getFilesDir() + "/" + attestation.getUuid() + ".pdf");
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
