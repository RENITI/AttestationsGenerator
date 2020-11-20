package fr.reniti.generator.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocumentCatalog;
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDAcroForm;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDCheckbox;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDField;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.Profile;
import fr.reniti.generator.storage.models.Reason;

public class Utils {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
    public static final Reason[] DEFAULT_REASONS = new Reason[] {Reason.ACHATS, Reason.TRAVAIL, Reason.SPORT_ANIMAUX};

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
     * @param attestation Attestation to convert
     * @param activity Instance
     */
    public static void savePDF(Attestation attestation, Activity activity)
    {
        try {
            InputStream stream = activity.getResources().getAssets().open("certificate.pdf");
            PDDocument document = PDDocument.load(stream);

            PDDocumentInformation information = document.getDocumentInformation();
            information.setAuthor("Ministère de l'intérieur");
            information.setCreator("");
            information.setProducer("DNUM/SDIT");
            information.setTitle("COVID-19 - Déclaration de déplacement");
            information.setSubject("Attestation de déplacement dérogatoire");
            information.setKeywords("covid19,covid-19,attestation,déclaration,déplacement,officielle,gouvernement");

            PDPage page = document.getPage(0);

            PDDocumentCatalog docCatalog = document.getDocumentCatalog();

            PDAcroForm acroForm = docCatalog.getAcroForm();

            for(PDField field : acroForm.getFields())
            {
                field.setReadOnly(true);
                field.setNoExport(true);
            }
            acroForm.setNeedAppearances(true);
            docCatalog.setAcroForm(acroForm);

            PDPageContentStream content = new PDPageContentStream(document, page, true, true);

            Profile profile = attestation.getProfile();

            content.setNonStrokingColor(0, 0, 0); //black text

            for(Reason r : Reason.values())
            {
                content.setNonStrokingColor(0, 0, 0);
                content.addRect(56, r.getPdfPosY()-2, 14, 14);
                content.fill();

                content.setNonStrokingColor(255, 255, 255);
                content.addRect(57, r.getPdfPosY()-1, 12, 12);
                content.fill();
            }

            content.setNonStrokingColor(0, 0, 0);

            content = Utils.drawText(content, 107, 657, profile.getFirstname() + " " + profile.getLastname(), 11);
            content = Utils.drawText(content, 107, 627, profile.getBirthday(), 11);
            content = Utils.drawText(content, 240, 627, profile.getPlaceofbirth(), 11);
            content = Utils.drawText(content, 124, 596, profile.getAddress() + " " + profile.getZipcode() + " " + profile.getCity(), 11);

            for(Reason reason : attestation.getReasons())
            {
                content =Utils.drawText(content, 59, reason.getPdfPosY(), "x", 17);
            }

            content = Utils.drawText(content, 93, 122, profile.getCity(), Utils.getIdealFontSize(profile.getCity()));
            content = Utils.drawText(content, 76, 92, attestation.getDatesortie(), 11);
            content = Utils.drawText(content, 246, 92, attestation.getHeuresortie(), 11);

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

        } catch (IOException e) {
            e.printStackTrace();
        }
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

                for (Reason reason : StorageManager.getInstance().getAttestationsManager().getLastReasons()) {
                    rank--;

                    builder = new ShortcutInfo.Builder(context, reason.getId()).setShortLabel(reason.getDisplayName()).setIcon(Icon.createWithResource(context, reason.getIconId())).setRank(rank).setLongLabel(reason.getDisplayName());
                    builder.setIntent(new Intent(Intent.ACTION_VIEW, new Uri.Builder().scheme("renitiattgen").authority("shortcut").appendQueryParameter("reason", reason.getId()).build()));

                    shortcutInfoList.add(builder.build());
                }

                shortcutManager.addDynamicShortcuts(shortcutInfoList);
            }
        }
    }
}
