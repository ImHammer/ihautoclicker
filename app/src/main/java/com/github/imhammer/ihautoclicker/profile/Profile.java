package com.github.imhammer.ihautoclicker.profile;

import org.json.JSONObject;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

public class Profile
{
    public static Profile fromJsonObject(JSONObject object)
    {
        return new Profile(
            object.getBoolean("enabled"),
            object.getString("name"),
            object.getInt("clicks_count"),
            object.getInt("interval_1"),
            object.getInt("interval_2"),
            object.getInt("key_id"),
            object.getInt("mouse_button"),
            object.getBoolean("is_hold")
        );
    }

    private boolean enabled = true;

    private String name = "Novo perfil!";
    private int clicksCount = 10;
    private int interval1 = 1000;
    private int interval2 = 1000;

    private int keyId = NativeKeyEvent.VC_F;
    private int mouseButton = NativeMouseEvent.BUTTON1;
    
    private boolean holdMode = false;

    public Profile(boolean enabled, String name, int clicksCount, int interval1, int interval2, int keyId, int mouseButton,
            boolean holdMode)
    {
        this.enabled = enabled;
        this.name = name;
        this.clicksCount = clicksCount;
        this.interval1 = interval1;
        this.interval2 = interval2;
        this.keyId = keyId;
        this.mouseButton = mouseButton;
        this.holdMode = holdMode;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getClicksCount()
    {
        return clicksCount;
    }

    public void setClicksCount(int clicksCount)
    {
        this.clicksCount = clicksCount;
    }

    public int getInterval1()
    {
        return interval1;
    }

    public void setInterval1(int interval1)
    {
        this.interval1 = interval1;
    }

    public int getInterval2()
    {
        return interval2;
    }

    public void setInterval2(int interval2)
    {
        this.interval2 = interval2;
    }

    public int getKeyId()
    {
        return keyId;
    }

    public void setKeyId(int keyId)
    {
        this.keyId = keyId;
    }

    public int getMouseButton()
    {
        return mouseButton;
    }

    public void setMouseButton(int mouseButton)
    {
        this.mouseButton = mouseButton;
    }

    public boolean isHoldMode()
    {
        return holdMode;
    }

    public void setHoldMode(boolean holdMode)
    {
        this.holdMode = holdMode;
    }

    public JSONObject toJsonObject()
    {
        return (new JSONObject())
            .put("enabled", isEnabled())
            .put("name", getName())
            .put("clicks_count", getClicksCount())
            .put("interval_1", getInterval1())
            .put("interval_2", getInterval2())
            .put("key_id", getKeyId())
            .put("mouse_button", getMouseButton())
            .put("is_hold", isHoldMode());
    }
}
