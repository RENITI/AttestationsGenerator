package fr.reniti.generator.storage;

import com.owlike.genson.annotation.JsonProperty;

import java.util.HashMap;

import fr.reniti.generator.storage.models.Profile;

public class ProfilesManager {

    @JsonProperty(serialize = true, deserialize = true)
    private HashMap<String, Profile> profilesList;

    public ProfilesManager()
    {
        this(new HashMap<>());
    }

    public ProfilesManager(@JsonProperty(value = "profilesList") HashMap<String, Profile> profilesList)
    {
        this.profilesList = profilesList;
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
            StorageManager.getInstance().saveProfiles();
        }
    }

    @JsonProperty(serialize = false, deserialize = false)
    public void addProfileAndSave(Profile profile)
    {
        this.profilesList.put(profile.getUuid(), profile);
        StorageManager.getInstance().saveProfiles();
    }
}
