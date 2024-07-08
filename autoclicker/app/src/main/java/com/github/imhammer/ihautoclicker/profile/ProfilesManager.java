package com.github.imhammer.ihautoclicker.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfilesManager
{
    private ProfilesManager() {}

    private static final List<Profile> profiles = new ArrayList<>();

    // Loading profiles
    public static void load(JSONArray jsProfilesArr)
    {
        jsProfilesArr.forEach(t -> {
            if (t instanceof JSONObject) {
                profiles.add(Profile.fromJsonObject((JSONObject)t));
            }
        });
        System.out.println("Loaded: %d profiles!".formatted(profiles.size()));
    }

    // Saving profiles
    public static JSONArray save()
    {
        JSONArray arr = new JSONArray();
        for (Profile profile : profiles) {
            arr.put(profile.toJsonObject());
        }

        System.out.println("Saved: %d profiles!".formatted(profiles.size()));
        return arr;
    }

    public static Profile getByKeyId(int keyId)
    {
        Optional<Profile> pf = profiles.stream().filter(profile -> (profile.getKeyId() == keyId)).findFirst();
        if (pf.isPresent()) return pf.get();

        return null;
    }

    public static void add(Profile profile)
    {
        profiles.add(profile);
    }

    public static boolean exists(String profileName)
    {
        return get(profileName).isPresent();
    }

    public static Optional<Profile> get(String profileName)
    {
        return profiles.stream().filter(profile -> (profile.getName().equals(profileName))).findFirst();
    }

    public static void remove(String profileName)
    {
        profiles.removeIf(t -> t.getName().equals(profileName));
    }

    public static List<Profile> getProfiles()
    {
        return profiles;
    }
}
