package com.github.imhammer.ihautoclicker.run;

import com.github.imhammer.ihautoclicker.App;
import com.github.imhammer.ihautoclicker.manager.KeysManager;
import com.github.imhammer.ihautoclicker.profile.Profile;
import com.github.imhammer.ihautoclicker.utils.AppUtils;

import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;

public class ProfileRunnable
{
    private final App app;
    private final Profile profile;
    private final KeysManager keysManager;
    
    private boolean running = false;
    private int nextClickTick = 0;

    public ProfileRunnable(App app, Profile profile, KeysManager keysManager)
    {
        this.app = app;
        this.profile = profile;
        this.keysManager = keysManager;

        init();
    }

    private void init()
    {
        this.keysManager.addKeyListener(getProfile().getKeyId(), this::onKeyPressed);
    }

    public void removeKeyEvent()
    {
        this.keysManager.removeKeyListener(getProfile().getKeyId());
    }

    public void update(int currentTick)
    {
        if (getProfile().isEnabled()) {
            if (getProfile().isHoldMode()) {
                this.running = this.keysManager.isKeyPressed(getProfile().getKeyId());
            }
            if (this.running) {
                if (currentTick == this.nextClickTick || this.nextClickTick == 0) {
                    this.app.nativeMouseClick(getProfile().getMouseButton());

                    int nextInterval = AppUtils.getNextInterval(getProfile());
    
                    this.nextClickTick = currentTick + nextInterval;
                    if (this.nextClickTick >= 1000) this.nextClickTick -= 1000;    
                }
            } else {
                this.nextClickTick = 0;
            }
        } else {
            this.nextClickTick = 0;
            this.running = false;
        }
    }

    public void onKeyPressed(boolean pressed)
    {
        if (!getProfile().isHoldMode() && pressed) {
            this.running = !this.running;

            if (!this.running) {
                this.nextClickTick = 0;
            }
        }
    }

    public Profile getProfile()
    {
        return this.profile;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public boolean isRunning()
    {
        return this.running;
    }
}
