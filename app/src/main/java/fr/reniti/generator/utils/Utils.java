package fr.reniti.generator.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType0Font;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import fr.reniti.generator.R;
import fr.reniti.generator.storage.StorageManager;
import fr.reniti.generator.storage.models.Attestation;
import fr.reniti.generator.storage.models.AttestationType;
import fr.reniti.generator.storage.models.Reason;

public class Utils {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");

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
     * Save PDF related to a attestation
     * @param attestation Attestation to convert
     * @param activity Instance
     */
    public static boolean savePDF(Attestation attestation, Context activity)
    {
        try {

            AttestationType type = attestation.getType();

            PDDocument document = new PDDocument();

            PDFont fontLucioleRegular = PDType0Font.load(document, activity.getResources().getAssets().open("Luciole-Regular.ttf"));
            PDFont fontLucioleBold = PDType0Font.load(document, activity.getResources().getAssets().open("Luciole-Bold.ttf"));
            PDFont fontLucioleRegularItalic = PDType0Font.load(document, activity.getResources().getAssets().open("Luciole-Regular-Italic.ttf"));
            PDFont fontLucioleBoldItalic = PDType0Font.load(document, activity.getResources().getAssets().open("Luciole-Bold-Italic.ttf"));

            PDDocumentInformation information = document.getDocumentInformation();
            information.setAuthor("Ministère de l'intérieur");
            information.setCreator("");
            information.setProducer("DNUM/SDIT");
            information.setTitle("COVID-19 - Déclaration de déplacement");
            information.setSubject("Attestation de déplacement dérogatoire");
            information.setKeywords("covid19,covid-19,attestation,déclaration,déplacement,officielle,gouvernement");

            PDPage page = new PDPage(PDRectangle.A4);

            PDPageContentStream pageContentStream = new PDPageContentStream(document, page, true, true, true);

            JsonReader reader = new JsonReader(new InputStreamReader(activity.getResources().getAssets().open(type.getAssetName())));
            JsonArray data = new Gson().fromJson(reader, JsonArray.class);

            for(int i = 0; i < data.size(); i++)
            {
                JsonObject object = data.get(i).getAsJsonObject();

                if(object.has("label"))
                {
                    float top = (float) (object.get("top").getAsInt() * 0.6702782);
                    float left = (float) (object.get("left").getAsInt() * 0.6702782);
                    float size = (object.get("size").getAsInt() / 1.5151F);

                    pageContentStream.beginText();

                    switch (object.get("font").getAsString())
                    {
                        case "Luciole":
                            pageContentStream.setFont(fontLucioleRegular, size);
                            break;
                        case "LucioleItalic":
                            pageContentStream.setFont(fontLucioleRegularItalic, size);
                            break;
                        case "LucioleBold":
                            pageContentStream.setFont(fontLucioleBold, size);
                            break;
                        case "LucioleBoldItalic":
                            pageContentStream.setFont(fontLucioleBoldItalic, size);
                            break;
                        default: break;
                    }

                    pageContentStream.newLineAtOffset(left, page.getCropBox().getHeight() - top);

                    String ltype = object.get("type").getAsString();

                    if(ltype.contentEquals("checkbox"))
                    {
                        pageContentStream.showText( (attestation.hasReason(object.get("reason").getAsString()) ? "[x] " : "[ ] ") + object.get("label").getAsString());
                    } else if(ltype.contentEquals("input")) {

                        //String
                        JsonArray inputs = object.get("inputs").getAsJsonArray();

                        StringBuilder builder = new StringBuilder(object.get("label").getAsString());

                        for(int j = 0; j < inputs.size(); j++)
                        {
                            switch (inputs.get(j).getAsString())
                            {
                                case "firstname":
                                    builder.append(" " + attestation.getProfile().getFirstname());
                                    break;
                                case "lastname":
                                    builder.append(" " + attestation.getProfile().getLastname());
                                    break;
                                case "birthday":
                                    builder.append(" " + attestation.getProfile().getBirthday());
                                    break;
                                case "address":
                                    builder.append(" " + attestation.getProfile().getAddress());
                                    break;
                                case "zipcode":
                                    builder.append(" " + attestation.getProfile().getZipcode());
                                    break;
                                case "city":
                                    builder.append(" " + attestation.getProfile().getCity());
                                    break;
                                case "datesortie":
                                    builder.append(" " + attestation.getDatesortie());
                                    break;
                                case "heuresortie":
                                    builder.append(" " + attestation.getHeuresortie());
                                    break;
                                default: break;
                            }
                        }

                        pageContentStream.showText(builder.toString());
                    } else {
                        pageContentStream.showText(object.get("label").getAsString());
                    }

                    pageContentStream.endText();
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();;
            PDImageXObject pdImage;


            pdImage = JPEGFactory.createFromStream(document, activity.getAssets().open("RF.jpg"));

            pageContentStream.drawImage(pdImage, 30, page.getMediaBox().getHeight() - 80, 56, 50);

            pdImage = JPEGFactory.createFromStream(document, activity.getAssets().open("TAC.jpg"));

            pageContentStream.drawImage(pdImage, page.getMediaBox().getWidth() - 66, page.getMediaBox().getHeight() - 80, 33, 50);

            // QR Code 1
            Bitmap smallQr = attestation.getQRCode(82);
            smallQr.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            pdImage = new PDImageXObject(document, new ByteArrayInputStream(outputStream.toByteArray()), COSName.DCT_DECODE, smallQr.getWidth(), smallQr.getHeight(), 8, PDDeviceRGB.INSTANCE);
            outputStream.close();

            pageContentStream.drawImage(pdImage,page.getMediaBox().getWidth() - 107, 21, 82, 82);

            pageContentStream.close();
            document.addPage(page);

            PDPage page2 = new PDPage();
            PDPageContentStream content2 = new PDPageContentStream(document, page2, true , true);

            outputStream = new ByteArrayOutputStream();

            // QR Code 2
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

            Logger.getGlobal().warning("ERROR =  " + e.getMessage());
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

                    if(reason == null || reason.getDisplayName() == 0)
                    {
                        continue;
                    }

                    rank--;

                    String displayName;

                    // Skipping invalid reason
                    try {
                        displayName = context.getString(reason.getDisplayName());
                    } catch (NullPointerException e)
                    {
                        continue;
                    }

                    builder = new ShortcutInfo.Builder(context, reason.getId()).setShortLabel(displayName).setIcon(Icon.createWithResource(context, reason.getIconId())).setRank(rank).setLongLabel(displayName + " (" + context.getString(reason.getRelatedType().getShortName()) + ")");
                    builder.setIntent(new Intent(Intent.ACTION_VIEW, new Uri.Builder().scheme("renitiattgen").authority("shortcut").appendQueryParameter("type", "" + reason.getRelatedType().getId()).appendQueryParameter("reason", reason.getId()).build()));

                    shortcutInfoList.add(builder.build());
                }

                shortcutManager.addDynamicShortcuts(shortcutInfoList);
            }
        }
    }
}
