package com.github.imhammer.ihautoclicker.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.github.imhammer.ihautoclicker.App;
import com.github.imhammer.ihautoclicker.manager.FXPaneManager;
import com.github.imhammer.ihautoclicker.profile.Profile;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MainController
{
    private final App app;
    private final FXPaneManager<AnchorPane> rootAnchorPane;
    private final Collection<ProfileItemControl> profileControls;

    // Buttons
    private Button createNewProfileButton;

    // Panes
    FlowPane profilesListFlowPane;

    public MainController(App app, AnchorPane mainRootPane)
    {
        this.app = app;
        this.rootAnchorPane = new FXPaneManager<>(mainRootPane);
        this.profileControls = new ArrayList<>();
        init();
    }

    private void init()
    {
        this.createNewProfileButton = getFxPaneManager().button("createNewProfileButton");
        this.profilesListFlowPane = (FlowPane) getFxPaneManager()
            .flowPane("profilesListFlowPaneBack")
            .scrollPane("profilesListScrollPane").getContent();

        this.createNewProfileButton.setOnMouseClicked(this::handleCreateNewProfile);
    }

    public void addProfileList(Profile profile)
    {
        try {
            ProfileItemControl profileItemControl = new ProfileItemControl(getApp(), profile.getName());

            this.profileControls.add(profileItemControl);
            this.profilesListFlowPane.getChildren().add(profileItemControl.getRootDisplay());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeProfileList(String profileName)
    {
        this.profileControls.removeIf(profileControl -> (profileControl.getProfile().getName().equals(profileName)));
        this.profilesListFlowPane.getChildren().removeIf(profile -> (profile.getId().equals(profileName)));
    }

    public ProfileItemControl getProfileItemControlByProfile(final Profile profile)
    {
        Optional<ProfileItemControl> optProfileItemControl = this.profileControls.stream().filter(t -> t.getProfile().getName().equals(profile.getName())).findFirst();
        return (optProfileItemControl.isPresent() ? optProfileItemControl.get() : null);
    }

    // Handlers

    public void handleCreateNewProfile(MouseEvent event)
    {
        getApp().showProfileDialog();
    }

    public App getApp()
    {
        return this.app;
    }

    private FXPaneManager<AnchorPane> getFxPaneManager()
    {
        return this.rootAnchorPane;
    }
}
