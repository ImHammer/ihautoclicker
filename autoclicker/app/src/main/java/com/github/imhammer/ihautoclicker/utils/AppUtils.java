package com.github.imhammer.ihautoclicker.utils;

import java.net.URL;
import java.util.Random;

import com.github.imhammer.ihautoclicker.App;
import com.github.imhammer.ihautoclicker.profile.Profile;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class AppUtils
{
    public static final int MOUSE_4 = 0x111;
    public static final int MOUSE_5 = 0x222;

    private static Random random = new Random();
    private AppUtils() {}
    
    public static URL resource(String path)
    {
        return App.class.getResource(path);
    }

    public static int toInt(String str, int def)
    {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static String getKeyText(int keyCode)
    {
        if (keyCode == MOUSE_4) return "M4";
        if (keyCode == MOUSE_5) return "M5";
        return NativeKeyEvent.getKeyText(keyCode);
    }

    public static int getNextInterval(Profile profile)
    {
        if (profile.getClicksCount() != -1) {
            return (int) 1000 / profile.getClicksCount();
        }

        int max = Math.max(profile.getInterval1(), profile.getInterval2());
        int min = Math.min(profile.getInterval1(), profile.getInterval2());

        return random.nextInt(max - min - 1) + min;
    }

    public static String getCPSStr(Profile profile)
    {
        if (profile.getClicksCount() == -1) {
            int max = Math.max(profile.getInterval1(), profile.getInterval2());
            int min = Math.min(profile.getInterval1(), profile.getInterval2());

            return "%d ~ %d".formatted(
                1000 / max,
                1000 / min
            );
        }
        return String.valueOf(profile.getClicksCount());
    }

    public static boolean checkOnlyCharacters(String str)
    {
        return str.matches("^[\\w\\s]+$");
    }

    public static boolean checkOnlyNumbers(String str)
    {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static <T> T nullOrDef(T valid, T def)
    {
        return (valid == null ? def : valid);
    }
}
