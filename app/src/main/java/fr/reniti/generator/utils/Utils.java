package fr.reniti.generator.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;

import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocumentCatalog;
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType0Font;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDAcroForm;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDField;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.AttestationType;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;

public class Utils {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
    //public static final Reason[] DEFAULT_REASONS = new Reason[] {Reason.ACHATS, Reason.TRAVAIL, Reason.SPORT_ANIMAUX};


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

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
     * @param test String which we need to calc ideal font size
     * @return Ideal font size
     * @throws IOException If text contains invalid chars
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
     * @param content PDPageContentStream instance
     * @param text Text to draw
     * @param size Text size
     * @return Updated PDPageContentStream
     * @throws IOException If text contains invalid chars
     */
    public static PDPageContentStream drawText(PDPageContentStream content, PDFPos pdfPos, String text, int size, PDFont pdFont) throws Exception {
        content.beginText();
        content.setFont(pdFont, size);
        content.newLineAtOffset(pdfPos.getX(), pdfPos.getY());
        content.showText(text);
        content.endText();
        return content;
    }

    /**
     * Save PDF related to a attestation
     * @param attestation Attestation to convert
     * @param activity Instance
     */
    public static boolean savePDF(Attestation attestation, Activity activity)
    {
        try {

            AttestationType type = attestation.getType();

            InputStream stream = activity.getResources().getAssets().open(type.getAssetName());
            PDDocument document = PDDocument.load(stream);

            PDFont pdFont = PDType0Font.load(document, activity.getResources().getAssets().open("Roboto-Regular.ttf"));

            PDDocumentInformation information = document.getDocumentInformation();
            information.setAuthor("Ministère de l'intérieur");
            information.setCreator("");
            information.setProducer("DNUM/SDIT");
            information.setTitle("COVID-19 - Déclaration de déplacement");
            information.setSubject("Attestation de déplacement dérogatoire");
            information.setKeywords("covid19,covid-19,attestation,déclaration,déplacement,officielle,gouvernement");

            PDPage page = document.getPage(0);
            PDPageContentStream content = new PDPageContentStream(document, page, true, true);

            Profile profile = attestation.getProfile();

            content.setNonStrokingColor(0, 0, 0);

            content = Utils.drawText(content, type.getIdentityPos(), profile.getFirstname() + " " + profile.getLastname(), 11, pdFont);
            content = Utils.drawText(content, type.getBirthDayPos(), profile.getBirthday(), 11, pdFont);
            content = Utils.drawText(content, type.getBirthPlacePos(), profile.getPlaceofbirth(), 11, pdFont);
            content = Utils.drawText(content, type.getCompleteAdressPos(), profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity(), 11, pdFont);

            for(Reason reason : attestation.getReasons())
            {
                content =Utils.drawText(content, new PDFPos(type.getReasonsBaseX(), reason.getPdfPosY()), "x", 12, pdFont);
            }

            content = Utils.drawText(content, type.getBottomCityPos(), profile.getCity(), Utils.getIdealFontSize(profile.getCity()), pdFont);

            content = Utils.drawText(content, type.getDatePos(), attestation.getDatesortie(), 11, pdFont);
            content = Utils.drawText(content, type.getTimePos(), attestation.getHeuresortie(), 11, pdFont);


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Bitmap smallQr = attestation.getQRCode(92);
            smallQr.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            PDImageXObject pdImage = new PDImageXObject(document, new ByteArrayInputStream(outputStream.toByteArray()), COSName.DCT_DECODE, smallQr.getWidth(), smallQr.getHeight(), 8, PDDeviceRGB.INSTANCE);
            outputStream.close();

            content.drawImage(pdImage,page.getMediaBox().getWidth() - 156, 25, 92, 92);

            content.close();


            PDPage page2 = new PDPage();
            PDPageContentStream content2 = new PDPageContentStream(document, page2, true , true);

            outputStream = new ByteArrayOutputStream();

            Bitmap qr = attestation.getQRCode(300);
            qr.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            pdImage = new PDImageXObject(document, new ByteArrayInputStream(outputStream.toByteArray()), COSName.DCT_DECODE, qr.getWidth(), qr.getHeight(), 8, PDDeviceRGB.INSTANCE);
            outputStream.close();
            content2.drawImage(pdImage,50, page2.getMediaBox().getHeight() - 390, 300, 300);

            content2.close();
            document.addPage(page2);

            document.save(activity.getFilesDir() + "/" + attestation.getUuid() + ".pdf");
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Update app shortcuts
     * @param context Instance
     * @param force Force the update
     */
    public static void updateShortcuts(Context context, boolean force) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {

            ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);

            if(shortcutManager.getDynamicShortcuts().size() < 4 || force)
            {
                shortcutManager.removeAllDynamicShortcuts();

                List<ShortcutInfo> shortcutInfoList = new ArrayList<>();

                int rank = 4;ShortcutInfo.Builder builder = new ShortcutInfo.Builder(context, "other").setShortLabel(context.getResources().getString(R.string.activity_attestation_create_title)).setIcon(Icon.createWithResource(context, R.drawable.ic_baseline_create_24)).setRank(rank);
                builder.setIntent(new Intent(Intent.ACTION_VIEW, new Uri.Builder().scheme("renitiattgen").authority("shortcut").appendQueryParameter("reason", "other").build()));

                shortcutInfoList.add(builder.build());

                if(StorageManager.getInstance() == null)
                {
                    new StorageManager(context);
                }

                for (Reason reason : StorageManager.getInstance().getAttestationsManager().getLastReasons()) {
                    rank--;

                    String displayName = context.getString(reason.getDisplayName());
                    builder = new ShortcutInfo.Builder(context, reason.getId()).setShortLabel(displayName).setIcon(Icon.createWithResource(context, reason.getIconId())).setRank(rank).setLongLabel(displayName + " (" + context.getString(reason.getRelatedType().getShortName()) + ")");
                    builder.setIntent(new Intent(Intent.ACTION_VIEW, new Uri.Builder().scheme("renitiattgen").authority("shortcut").appendQueryParameter("type", "" + reason.getRelatedType().getId()).appendQueryParameter("reason", reason.getId()).build()));

                    shortcutInfoList.add(builder.build());
                }

                shortcutManager.addDynamicShortcuts(shortcutInfoList);
            }
        }
    }
}
