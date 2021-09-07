package fr.reniti.generator.storage;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import fr.reniti.generator.MainActivity;

public class StorageManager {

    private static StorageManager instance;

    public static StorageManager getInstance()
    {
        return instance;
    }

    private File baseDirectory;
    private File profilesFile;
    private File attestationsFile;

    private ProfilesManager profilesManager;
    private AttestationsManager attestationsManager;

    public StorageManager(Context activity)
    {
        if(instance != null)
        {
            return;
        }

        instance = this;
        this.baseDirectory = activity.getFilesDir();
        this.profilesFile = new File(baseDirectory + "/profiles.json");
        this.attestationsFile = new File(baseDirectory + "/attestations.json");

        try {
            reloadProfiles();
            reloadAttestations();

            if (!profilesManager.checkData()) {
                saveProfiles();
            }

            if (!attestationsManager.checkData()) {
                saveAttestations();
            }
        } catch (Exception e)
        {
            e.printStackTrace();

        }
    }

    public String calcUsedSpace()
    {
        double size = 0;

        for(File f : baseDirectory.listFiles())
        {
            if(f.isFile())
                size += f.length();
        }

        return (size < 100000 ? (String.format("%.2g", size /1000) + " ko") : (String.format("%.2g", size / 1000000) + " mo")).replace(',', '.');
    }

    public void removeFile(String name)
    {
        File f = new File(baseDirectory + "/" + name);

        if(f.exists())
        {
            f.delete();
        }
    }

    public void reloadProfiles()
    {
        if(!profilesFile.exists())
        {
            profilesManager = new ProfilesManager();
            return;
        }

        try {
            this.profilesManager = new GsonBuilder().create().fromJson(new FileReader(profilesFile), ProfilesManager.class);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(MainActivity.getInstance().get(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            profilesFile.delete();
            profilesManager = new ProfilesManager();
        }

        if(this.profilesManager == null)
        {
            this.profilesManager = new ProfilesManager();
        }
    }

    public void reloadAttestations()
    {
        if(!attestationsFile.exists())
        {
            attestationsManager = new AttestationsManager();
            return;
        }

        try {
           this.attestationsManager = new GsonBuilder().create().fromJson(new FileReader(attestationsFile), AttestationsManager.class);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(MainActivity.getInstance().get(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            attestationsFile.delete();
            attestationsManager = new AttestationsManager();
        }

        if(this.attestationsManager == null)
        {
            this.attestationsManager = new AttestationsManager();
        }
    }
    public void saveAttestations()
    {
        if(attestationsFile.exists())
        {
            attestationsFile.delete();
        }

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(attestationsFile));
            outputStreamWriter.write(new GsonBuilder().create().toJson(attestationsManager));
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProfiles()
    {
        if(profilesFile.exists())
        {
            profilesFile.delete();
        }

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(profilesFile));
            outputStreamWriter.write(new GsonBuilder().create().toJson(profilesManager));
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProfilesManager getProfilesManager() { return profilesManager; }

    public AttestationsManager getAttestationsManager() {
        return attestationsManager;
    }
}
