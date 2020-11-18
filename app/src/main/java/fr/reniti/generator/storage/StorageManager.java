package fr.reniti.generator.storage;

import android.app.Activity;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public StorageManager(Activity activity)
    {
        if(instance != null)
        {
            return;
        }

        instance = this;
        this.baseDirectory = activity.getFilesDir();
        this.profilesFile = new File(baseDirectory + "/profiles.json");
        this.attestationsFile = new File(baseDirectory + "/attestations.json");

        reloadProfiles();
        reloadAttestations();

        Logger.getGlobal().info("PATH : " + profilesFile.getAbsolutePath());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                FileInputStream s = new FileInputStream(profilesFile);

                try( BufferedReader br =
                             new BufferedReader( new InputStreamReader(fis, encoding )))
                {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while(( line = br.readLine()) != null ) {
                        sb.append( line );
                        sb.append( '\n' );
                    }
                    return sb.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!profilesManager.checkData())
        {
            saveProfiles();
        }

        if(!attestationsManager.checkData())
        {
            saveAttestations();
        }

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
            this.profilesManager = new GensonBuilder().setThrowExceptionIfNoDebugInfo(true).useRuntimeType(true).useClassMetadata(true).useFields(true, VisibilityFilter.ALL).useIndentation(true).create().deserialize(new FileInputStream(profilesFile), ProfilesManager.class);

           /* if(profilesManager == null)
            {
                profilesManager = new ProfilesManager();
            }*/

        } catch (Exception e) {
            e.printStackTrace();

            profilesFile.delete();
            profilesManager = new ProfilesManager();
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
            this.attestationsManager = new GensonBuilder().useRuntimeType(true).useClassMetadata(true).useFields(true, VisibilityFilter.ALL).useIndentation(true).create().deserialize(new FileInputStream(attestationsFile), AttestationsManager.class);
        } catch (Exception e) {
            e.printStackTrace();

            attestationsFile.delete();
            attestationsManager = new AttestationsManager();
        }
    }
    public void saveAttestations()
    {
        if(attestationsFile.exists())
        {
            attestationsFile.delete();
        }

        try {
            FileOutputStream stream = new FileOutputStream(attestationsFile);

            stream.write(new GensonBuilder().useRuntimeType(true).useClassMetadata(true).useFields(true, VisibilityFilter.ALL).setMethodFilter(VisibilityFilter.NONE).setFieldFilter(VisibilityFilter.ALL).useIndentation(false).create().serializeBytes(attestationsManager));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveProfiles()
    {
        if(profilesFile.exists()) {
            profilesFile.delete();
        }
        try {
            FileOutputStream stream = new FileOutputStream(profilesFile);
            stream.write(new GensonBuilder().useRuntimeType(true).useClassMetadata(true).useFields(true, VisibilityFilter.ALL).setMethodFilter(VisibilityFilter.NONE).setFieldFilter(VisibilityFilter.ALL).useIndentation(false).create().serializeBytes(profilesManager));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public ProfilesManager getProfilesManager() {
        return profilesManager;
    }

    public AttestationsManager getAttestationsManager() {
        return attestationsManager;
    }


}
