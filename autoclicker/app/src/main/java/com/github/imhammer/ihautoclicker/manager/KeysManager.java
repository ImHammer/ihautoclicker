package com.github.imhammer.ihautoclicker.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.imhammer.ihautoclicker.App;
import com.github.imhammer.ihautoclicker.utils.AppUtils;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

public class KeysManager implements NativeKeyListener, NativeMouseListener
{
    private final App app;

    // Keyboard
    private Map<Integer, KeyRunListener> keysListeners = new HashMap<>();
    private List<Integer> pressedKeys = new ArrayList<>();
    private int currentPressedKey = -1;
    
    public KeysManager(App app)
    {
        this.app = app;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event)
    {
        if (!this.pressedKeys.contains(Integer.valueOf(event.getKeyCode()))) {
            this.pressedKeys.add(Integer.valueOf(event.getKeyCode()));

            if (this.keysListeners.containsKey(event.getKeyCode())) {
                this.keysListeners.get(event.getKeyCode()).run(true);
            }
        }
        this.currentPressedKey = event.getKeyCode();
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event)
    {
        if (this.pressedKeys.contains(Integer.valueOf(event.getKeyCode()))) {
            this.pressedKeys.removeIf(intg -> intg.intValue() == event.getKeyCode());

            if (this.keysListeners.containsKey(event.getKeyCode())) {
                this.keysListeners.get(event.getKeyCode()).run(false);
            }
        }
        if (this.currentPressedKey == event.getKeyCode()) {
            this.currentPressedKey = -1;
        }
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent)
    {
        int mouseButtonId = -1;
        if (nativeMouseEvent.getButton() == NativeMouseEvent.BUTTON4) mouseButtonId = AppUtils.MOUSE_4;
        if (nativeMouseEvent.getButton() == NativeMouseEvent.BUTTON5) mouseButtonId = AppUtils.MOUSE_5;

        if (mouseButtonId != -1) {
            if (!this.pressedKeys.contains(Integer.valueOf(mouseButtonId))) {
                this.pressedKeys.add(Integer.valueOf(mouseButtonId));
    
                if (this.keysListeners.containsKey(mouseButtonId)) {
                    this.keysListeners.get(mouseButtonId).run(true);
                }
            }
            this.currentPressedKey = mouseButtonId;
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent)
    {
        int mouseButtonId = -1;
        if (nativeMouseEvent.getButton() == NativeMouseEvent.BUTTON4) mouseButtonId = AppUtils.MOUSE_4;
        if (nativeMouseEvent.getButton() == NativeMouseEvent.BUTTON5) mouseButtonId = AppUtils.MOUSE_5;

        if (mouseButtonId != -1) {
            final int _mouseButtonId = mouseButtonId;
            if (this.pressedKeys.contains(Integer.valueOf(mouseButtonId))) {
                this.pressedKeys.removeIf(intg -> intg.intValue() == _mouseButtonId);
    
                if (this.keysListeners.containsKey(mouseButtonId)) {
                    this.keysListeners.get(mouseButtonId).run(false);
                }
            }
            if (this.currentPressedKey == mouseButtonId) {
                this.currentPressedKey = -1;
            }
        }
    }

    // Handlers

    public void addKeyListener(int keyId, KeyRunListener listener)
    {
        this.keysListeners.put(Integer.valueOf(keyId), listener);
    }

    public void removeKeyListener(int keyId)
    {
        this.keysListeners.remove(keyId);
    }

    // Verification

    public boolean isKeyPressed(int key)
    {
        return this.pressedKeys.contains(key);
    }

    public int getCurrentPressed()
    {
        return this.currentPressedKey;
    }

    @FunctionalInterface
    public static interface KeyRunListener 
    {
        void run(boolean pressed);
    }
}
