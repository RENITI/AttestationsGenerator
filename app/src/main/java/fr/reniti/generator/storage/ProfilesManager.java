package fr.reniti.generator.storage;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

import fr.reniti.generator.storage.models.AttestationType;
import fr.reniti.generator.storage.models.Profile;

public class ProfilesManager {

    @Expose
    private HashMap<String, Profile> profilesList;

    @Expose
    private String defaultProfile;

    @Expose
    private AttestationType defaultType;

    public ProfilesManager()
    {
        this(new HashMap<>(), null, AttestationType.getDefault());
    }

    public ProfilesManager( HashMap<String, Profile> profilesList,  String defaultProfile, AttestationType defaultType)
    {
        this.profilesList = profilesList;
        this.defaultProfile = defaultProfile;
        this.defaultProfile = defaultProfile;
    }

    public boolean checkData()
    {
        if(defaultProfile == null)
        {
            for(String d : profilesList.keySet())
            {
                defaultProfile = d;
                break;
            }
            return false;
        }

        if(defaultType == null)
        {
            defaultType = AttestationType.getDefault();
            return false;
        }

        return true;
    }

    public Profile getDefaultProfile()
    {
        if(defaultProfile == null || profilesList.get(defaultProfile) == null)
        {
            return null;
        }

        return profilesList.get(defaultProfile);
    }

    public void setDefaultProfile(String uuid)
    {
        this.defaultProfile = uuid;
        StorageManager.getInstance().saveProfiles();
    }

    public HashMap<String, Profile> getProfilesList() {
        return profilesList;
    }

    public void removeProfile(String uuid)
    {
        if(profilesList.containsKey(uuid)) {
            profilesList.remove(uuid);

            if(defaultProfile.equals(uuid))
            {
                defaultProfile = "";

                for(String d : profilesList.keySet())
                {
                    defaultProfile = d;
                    break;
                }
            }
            StorageManager.getInstance().saveProfiles();
        }
    }

    public void addProfileAndSave(Profile profile)
    {
        this.profilesList.put(profile.getUuid(), profile);
        this.defaultProfile = profile.getUuid();

        StorageManager.getInstance().saveProfiles();
    }

    public String getDefaultProfileUUID() {
        return defaultProfile != null ? defaultProfile : "";
    }

    public AttestationType getDefaultType() {
        return defaultType != null ? defaultType : AttestationType.getDefault();
    }

    public void setDefaultTypeAndSave(AttestationType type)
    {
        this.defaultType = type;
        StorageManager.getInstance().saveProfiles();
    }
}
