package fr.reniti.generator.storage;

import com.owlike.genson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.logging.Logger;

import fr.reniti.generator.storage.models.Profile;

public class ProfilesManager {

    @JsonProperty(serialize = true, deserialize = true)
    private HashMap<String, Profile> profilesList;

    @JsonProperty(serialize = true, deserialize = true)
    private String defaultProfile;

    public ProfilesManager()
    {
        this(new HashMap<>(), null);
    }

    public ProfilesManager(@JsonProperty(value = "profilesList") HashMap<String, Profile> profilesList, @JsonProperty(value = "defaultProfile") String defaultProfile)
    {
        this.profilesList = profilesList;
        this.defaultProfile = defaultProfile;
    }

    @JsonProperty(serialize = false, deserialize = false)
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
        return true;
    }

    @JsonProperty(serialize = false, deserialize = false)
    public Profile getDefaultProfile()
    {
        if(defaultProfile == null || profilesList.get(defaultProfile) == null)
        {
            return null;
        }

        return profilesList.get(defaultProfile);
    }

    @JsonProperty(serialize = false, deserialize = false)
    public boolean isDefaultProfile(Profile uuid)
    {
        return defaultProfile != null && defaultProfile.equals(uuid);
    }

    @JsonProperty(serialize = false, deserialize = false)
    public void setDefaultProfile(String uuid)
    {
        this.defaultProfile = uuid;
        StorageManager.getInstance().saveProfiles();
    }

    @JsonProperty(serialize = false, deserialize = false)
    public HashMap<String, Profile> getProfilesList() {
        return profilesList;
    }

    @JsonProperty(serialize = false, deserialize = false)
    public Profile getProfile(String uuid)
    {
        return this.profilesList.get(uuid);
    }

    @JsonProperty(serialize = false, deserialize = false)
    public void removeProfile(String uuid)
    {
        if(profilesList.containsKey(uuid)) {
            profilesList.remove(uuid);

            if(defaultProfile == uuid)
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

    @JsonProperty(serialize = false, deserialize = false)
    public void addProfileAndSave(Profile profile)
    {
        this.profilesList.put(profile.getUuid(), profile);
        this.defaultProfile = profile.getUuid();

        StorageManager.getInstance().saveProfiles();


    }

    @JsonProperty(serialize = false, deserialize = false)
    public String getDefaultProfileUUID() {
        return defaultProfile != null ? defaultProfile : "";
    }
}
