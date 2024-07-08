package com.github.imhammer.ihautoclicker.controller;

import com.github.imhammer.ihautoclicker.App;
import com.github.imhammer.ihautoclicker.manager.FXPaneManager;
import com.github.imhammer.ihautoclicker.profile.Profile;
import com.github.imhammer.ihautoclicker.utils.AppUtils;
import com.github.imhammer.ihautoclicker.utils.TextFieldObservable;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ProfileController implements NativeKeyListener, NativeMouseListener
{
    private final App app;
    private final FXPaneManager<AnchorPane> rootAnchorPane;
    
    // Fields
    private TextField profileNameField;
    private TextField clicksCountField;
    private TextField intervalField1;
    private TextField intervalField2;

    // CheckBox's
    private CheckBox clicksCountCheck;
    private CheckBox holdModeCheck;

    // Buttons
    private Button changeKeyButton;
    private Button saveProfileButton;
    private Button cancelProfileButton;

    // Radio
    private RadioButton leftMouseRadio;
    private RadioButton middleMouseRadio;
    private RadioButton rightMouseRadio;

    // Labels
    private Label selectedKeyLabel;
    private Label clicksCountLabel;
    private Label intervalLabel1;
    private Label intervalLabel2;

    //

    private int selectedKey = NativeKeyEvent.VC_F;
    private boolean settingKey = false;

    public ProfileController(App app, AnchorPane mainRootPane)
    {
        this.app = app;
        this.rootAnchorPane = new FXPaneManager<>(mainRootPane);
        init();
    }

    private void init()
    {
        // Find Components
        this.profileNameField = getRootAnchorPane().textField("profileNameField");
        this.clicksCountField = getRootAnchorPane().textField("clicksCountField");
        this.intervalField1 = getRootAnchorPane().textField("intervalField1");
        this.intervalField2 = getRootAnchorPane().textField("intervalField2");
        
        this.clicksCountCheck = getRootAnchorPane().checkBox("clicksCountCheck");
        this.holdModeCheck = getRootAnchorPane().checkBox("holdModeCheck");
        
        this.changeKeyButton = getRootAnchorPane().button("changeKeyButton");
        this.saveProfileButton = getRootAnchorPane().button("saveProfileButton");
        this.cancelProfileButton = getRootAnchorPane().button("cancelProfileButton");
        
        this.leftMouseRadio = getRootAnchorPane().radioButton("leftMouseRadio");
        this.middleMouseRadio = getRootAnchorPane().radioButton("middleMouseRadio");
        this.rightMouseRadio = getRootAnchorPane().radioButton("rightMouseRadio");
        
        this.selectedKeyLabel = getRootAnchorPane().label("selectedKeyLabel");
        this.clicksCountLabel = getRootAnchorPane().label("clicksCountLabel");
        this.intervalLabel1 = getRootAnchorPane().label("intervalLabel1");
        this.intervalLabel2 = getRootAnchorPane().label("intervalLabel2");
        
        // Config components
        getClicksCountCheck().selectedProperty().addListener(this::handleToggleClicksCount);
        getChangeKeyButton().setOnMouseClicked(this::handleChangeKey);

        getSaveProfileButton().setOnMouseClicked(this::handleSaveProfile);
        getCancelProfileButton().setOnMouseClicked(this::handleCloseProfile);

        new TextFieldObservable(this.profileNameField, (oldValue, newValue) -> AppUtils.checkOnlyCharacters(newValue) ? newValue : oldValue);
        new TextFieldObservable(this.clicksCountField, (oldValue, newValue) -> AppUtils.checkOnlyNumbers(newValue) ? newValue : oldValue);
        new TextFieldObservable(this.intervalField1, (oldValue, newValue) -> AppUtils.checkOnlyNumbers(newValue) ? newValue : oldValue);
        new TextFieldObservable(this.intervalField2, (oldValue, newValue) -> AppUtils.checkOnlyNumbers(newValue) ? newValue : oldValue);
    }

    public void resetAll()
    {
        // TODO: Reset to default components
        getProfileNameField().setText("Novo Perfil");
        getClicksCountField().setText("10");
        getIntervalField1().setText("1000");
        getIntervalField2().setText("1000");

        getClicksCountCheck().setSelected(true);
        getHoldModeCheck().setSelected(false);

        getLeftMouseRadio().setSelected(true);

        setSelectedKey(NativeKeyEvent.VC_F);
        setSettingSelectedKey(false);
    }
    
    // Handlers
    public void handleToggleClicksCount(Observable observable)
    {
        boolean clicksCountCondition = getClicksCountCheck().isSelected();

        getClicksCountLabel().setDisable(!clicksCountCondition);
        getClicksCountField().setDisable(!clicksCountCondition);
        getIntervalLabel1().setDisable(clicksCountCondition);
        getIntervalLabel2().setDisable(clicksCountCondition);
        getIntervalField1().setDisable(clicksCountCondition);
        getIntervalField2().setDisable(clicksCountCondition);
    }

    public void handleChangeKey(MouseEvent event)
    {
        setSettingSelectedKey(true);
    }

    public void setSelectedKey(int nativeKeyCode)
    {
        this.selectedKey = nativeKeyCode;
        getSelectedKeyLabel().setText(AppUtils.getKeyText(nativeKeyCode));
    }

    public void setSettingSelectedKey(boolean settingKey)
    {
        this.settingKey = settingKey;
        getChangeKeyButton().setDisable(settingKey);
    }

    public boolean isInSettingKey()
    {
        return this.settingKey;
    }

    public void handleSaveProfile(MouseEvent event)
    {
        int selectedMouseButton = NativeMouseEvent.BUTTON1;
        if (getMiddleMouseRadio().isSelected()) {
            selectedMouseButton = NativeMouseEvent.BUTTON3;
        } else if (getRightMouseRadio().isSelected()) {
            selectedMouseButton = NativeMouseEvent.BUTTON2;
        }

        Profile profile = new Profile(
            false,
            getProfileNameField().getText(),
            AppUtils.toInt((getClicksCountCheck().isSelected() ? getClicksCountField().getText() : "-1"), 10),
            AppUtils.toInt(getIntervalField1().getText(), 1000),
            AppUtils.toInt(getIntervalField2().getText(), 1000),
            this.selectedKey,
            selectedMouseButton,
            getHoldModeCheck().isSelected()
        );

        if (getApp().addNewProfile(profile)) {
            resetAll();
            getApp().closeProfileDialog();
        }
    }

    public void handleCloseProfile(MouseEvent event)
    {
        resetAll();
        getApp().closeProfileDialog();
    }

    // JNativeHook events
    @Override
    public void nativeKeyPressed(NativeKeyEvent event)
    {
        Platform.runLater(() -> {
            if (isInSettingKey()) {
                setSettingSelectedKey(false);
                setSelectedKey(event.getKeyCode());
            }
        });
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent)
    {
        int mouseButtonId = -1;
        if (nativeMouseEvent.getButton() == NativeMouseEvent.BUTTON4) mouseButtonId = AppUtils.MOUSE_4;
        if (nativeMouseEvent.getButton() == NativeMouseEvent.BUTTON5) mouseButtonId = AppUtils.MOUSE_5;

        if (mouseButtonId != -1) {
            final int _mouseButtonId = mouseButtonId;
            Platform.runLater(() -> {
                if (isInSettingKey()) {
                    setSettingSelectedKey(false);
                    setSelectedKey(_mouseButtonId);
                }
            });
        }
    }

    // Getters
    public App getApp()
    {
        return this.app;
    }

    private FXPaneManager<AnchorPane> getRootAnchorPane()
    {
        return rootAnchorPane;
    }

    public TextField getProfileNameField()
    {
        return profileNameField;
    }

    public TextField getClicksCountField()
    {
        return clicksCountField;
    }

    public TextField getIntervalField1()
    {
        return intervalField1;
    }

    public TextField getIntervalField2()
    {
        return intervalField2;
    }

    public CheckBox getClicksCountCheck()
    {
        return clicksCountCheck;
    }

    public CheckBox getHoldModeCheck()
    {
        return holdModeCheck;
    }

    public Button getChangeKeyButton()
    {
        return changeKeyButton;
    }

    public Button getSaveProfileButton()
    {
        return saveProfileButton;
    }

    public Button getCancelProfileButton()
    {
        return cancelProfileButton;
    }

    public RadioButton getLeftMouseRadio()
    {
        return leftMouseRadio;
    }

    public RadioButton getMiddleMouseRadio()
    {
        return middleMouseRadio;
    }

    public RadioButton getRightMouseRadio()
    {
        return rightMouseRadio;
    }

    public Label getSelectedKeyLabel()
    {
        return selectedKeyLabel;
    }

    public Label getClicksCountLabel()
    {
        return clicksCountLabel;
    }

    public Label getIntervalLabel1()
    {
        return intervalLabel1;
    }

    public Label getIntervalLabel2()
    {
        return intervalLabel2;
    }
}
