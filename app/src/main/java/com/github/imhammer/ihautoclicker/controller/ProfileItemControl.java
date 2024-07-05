package com.github.imhammer.ihautoclicker.controller;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.github.imhammer.ihautoclicker.App;
import com.github.imhammer.ihautoclicker.manager.FXPaneManager;
import com.github.imhammer.ihautoclicker.profile.Profile;
import com.github.imhammer.ihautoclicker.profile.ProfilesManager;
import com.github.imhammer.ihautoclicker.utils.AppUtils;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class ProfileItemControl
{
    private final App app;
    private final Profile profile;
    private final FXPaneManager<AnchorPane> rootAnchorPane;
    
    private Image activedToggleImage;
    private Image desactivedToggleImage;

    private Circle statusCircle;
    
    public ProfileItemControl(App app, String profileName) throws IOException, NoSuchElementException
    {
        this.app = app;
        this.profile = ProfilesManager.get(profileName).get();
        this.rootAnchorPane = new FXPaneManager<>(FXMLLoader.load(getApp().getProfileItemAnchorPaneUrl()));

        this.rootAnchorPane.getRoot().setId(profileName);

        load();
    }
    
    // Handlers

    private void load()
    {
        getProfileNameLabel().setText(getProfile().getName());
        getActiveKeyLabel().setText(AppUtils.getKeyText(getProfile().getKeyId()));

        getProfileDeleteButton().setOnMouseClicked(event -> getApp().removeProfile(getProfile()));

        this.desactivedToggleImage = getProfileEnabled().getImage();
        this.activedToggleImage = new Image(AppUtils.resource("/icons/ActivedToggle.png").toExternalForm());

        getProfileEnabled().setOnMouseClicked(event -> {
            getProfile().setEnabled(!getProfile().isEnabled());
            updateImageToggle();
        });
        updateImageToggle();

        getCpsCountLabel().setText(AppUtils.getCPSStr(getProfile()));

        this.statusCircle = (Circle) getFxPaneManager().node("statusCircle", Circle.class);
    }

    public void updateImageToggle()
    {
        if (this.profile.isEnabled()) {
            getProfileEnabled().setImage(this.activedToggleImage);
        } else {
            getProfileEnabled().setImage(this.desactivedToggleImage);
        }
        getBlackUpPane().setVisible(!getProfile().isEnabled());
    }

    // Nodes

    public Circle getStatusCircle()
    {
        return this.statusCircle;
    }

    public AnchorPane getBlackUpPane()
    {
        return getFxPaneManager().anchorPane("blackUpPane").getRoot();
    }

    public ImageView getProfileEnabled()
    {
        return getFxPaneManager().image("profileEnabled");
    }

    public ImageView getProfileDeleteButton()
    {
        return getFxPaneManager().image("profileDeleteButton");
    }

    public Label getProfileNameLabel()
    {
        return getFxPaneManager().label("profileNameLabel");
    }

    public Label getActiveKeyLabel()
    {
        return getFxPaneManager().label("activeKeyLabel");
    }

    public Label getCpsCountLabel()
    {
        return getFxPaneManager().label("cpsCountLabel");
    }

    // Generic

    public App getApp()
    {
        return this.app;
    }

    public Profile getProfile()
    {
        return this.profile;
    }

    public AnchorPane getRootDisplay()
    {
        return this.rootAnchorPane.getRoot();
    }

    private FXPaneManager<AnchorPane> getFxPaneManager()
    {
        return this.rootAnchorPane;
    }
}
