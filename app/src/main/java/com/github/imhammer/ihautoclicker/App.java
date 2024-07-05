package com.github.imhammer.ihautoclicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import org.json.JSONArray;

import com.github.imhammer.ihautoclicker.controller.MainController;
import com.github.imhammer.ihautoclicker.controller.ProfileController;
import com.github.imhammer.ihautoclicker.controller.ProfileItemControl;
import com.github.imhammer.ihautoclicker.manager.KeysManager;
import com.github.imhammer.ihautoclicker.profile.Profile;
import com.github.imhammer.ihautoclicker.profile.ProfilesManager;
import com.github.imhammer.ihautoclicker.run.ProfileRunnable;
import com.github.imhammer.ihautoclicker.utils.AppUtils;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class App extends Application implements NativeKeyListener
{
    public static final String FXML_MENU_PATH = "/windows/main.fxml";
    public static final String FXML_PROFILE_PATH = "/windows/profile.fxml";
    public static final String FXML_PROFILE_ITEM_PATH = "/windows/profile_item.fxml";
        
    // Window Menus
    protected Stage rootStage;
    protected AnchorPane rootMenuPane;
    protected AnchorPane profileAnchorPane;

    // Window URL
    protected URL profileItemAnchorPaneUrl;

    protected Stage profileDialog;

    private boolean isLinux = System.getProperty("os.name").contains("Linux");
    private boolean isWindows = System.getProperty("os.name").contains("Windows");

    // Preferences
    private Preferences prefs;

    // Controllers
    private MainController mainController;
    private ProfileController profileController;

    // Updater
    private List<ProfileRunnable> profileRunnables = new ArrayList<>();
    private int intervalTicks = 0;
    private Map<String, Integer> nextTimeStep = new HashMap<>();

    // Keys
    private KeysManager keysManager;

    @Override
    public void start(Stage rootStage) throws Exception
    {
        this.keysManager = new KeysManager(this);
        this.rootStage = rootStage;
        this.profileItemAnchorPaneUrl = AppUtils.resource(FXML_PROFILE_ITEM_PATH);
        this.rootMenuPane = FXMLLoader.load(AppUtils.resource(FXML_MENU_PATH));
        this.profileAnchorPane = FXMLLoader.load(AppUtils.resource(FXML_PROFILE_PATH));

        // MENU WINDOW
        Scene scene = new Scene(rootMenuPane);

        this.rootStage.setTitle("IHAutoClicker");
        this.rootStage.setResizable(false);
        this.rootStage.setScene(scene);
        this.rootStage.setOnHidden(this::onHide);
        this.rootStage.setOnCloseRequest(this::onCloseRequest);

        this.rootStage.getIcons().add(new Image(AppUtils.resource("/logo/500x500.png").toExternalForm()));
        this.rootStage.getIcons().add(new Image(AppUtils.resource("/logo/64x64.png").toExternalForm()));
        this.rootStage.getIcons().add(new Image(AppUtils.resource("/logo/32x32.png").toExternalForm()));

        // PROFILE WINDOW (POPUP)
        this.profileDialog = new Stage();
        this.profileDialog.setTitle("Criando perfil");
        this.profileDialog.initStyle(StageStyle.TRANSPARENT);
        this.profileDialog.initModality(Modality.WINDOW_MODAL);
        this.profileDialog.setResizable(false);
        this.profileDialog.setScene(new Scene(this.profileAnchorPane));
        this.profileDialog.getScene().setFill(Color.TRANSPARENT);
        this.profileDialog.initOwner(this.rootStage);

        // Controllers
        this.mainController = new MainController(this, this.rootMenuPane);
        this.profileController = new ProfileController(this, this.profileAnchorPane);

        // JNativeHook
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(this.profileController);
        GlobalScreen.addNativeMouseListener(this.profileController);
        GlobalScreen.addNativeKeyListener(this.keysManager);
        GlobalScreen.addNativeMouseListener(this.keysManager);

        // Load IHAutoClickerLIB
        loadNativeLibrary();

        // Loading proifiles
        loadProfiles();

        // Updater
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run()
            {
                onRun();
            }
        }, 0, 1);

        // Showing Window
        this.rootStage.show();
    }

    public URL getProfileItemAnchorPaneUrl()
    {
        return this.profileItemAnchorPaneUrl;
    }

    public void onCloseRequest(WindowEvent event)
    {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        saveProfiles();
    }

    public void onHide(WindowEvent event)
    {
        
        System.exit(0);
    }

    public void onRun()
    {
        Iterator<ProfileRunnable> profilesRunIterator = this.profileRunnables.iterator();
        while(profilesRunIterator.hasNext()) {
            ProfileRunnable profileRunnable = profilesRunIterator.next();
            profileRunnable.update(this.intervalTicks);

            ProfileItemControl profileItemControl = mainController.getProfileItemControlByProfile(profileRunnable.getProfile());
            if (profileItemControl != null) profileItemControl.getStatusCircle().setFill((profileRunnable.isRunning() ? Color.GREEN : Color.RED));
        }
        
        this.intervalTicks++;
        if (this.intervalTicks > 1000) {
            this.intervalTicks = 0;
        }
    }

    public boolean addNewProfile(Profile profile)
    {
        if (ProfilesManager.exists(profile.getName())) {
            alert("O nome de perfil já está em uso!");
            return false;
        }

        if (ProfilesManager.getByKeyId(profile.getKeyId()) != null) {
            alert("Esta key já esta em uso por outro perfil!");
            return false;
        }

        ProfilesManager.add(profile);
        this.mainController.addProfileList(profile);
        this.profileRunnables.add(new ProfileRunnable(this, profile, this.keysManager));
        return true;
    }

    public void removeProfile(String profileName)
    {
        this.mainController.removeProfileList(profileName);
        this.profileRunnables.removeIf(profileRun -> {
            if (profileRun.getProfile().getName().equals(profileName)) {
                profileRun.removeKeyEvent();
                return true;
            }
            return false;
        });
        ProfilesManager.remove(profileName);
    }

    public void removeProfile(Profile profile)
    {
        removeProfile(profile.getName());
    }

    public void showProfileDialog()
    {
        if (!getProfileDialog().isShowing())
            getProfileDialog().show();
    }

    public void closeProfileDialog()
    {
        if (getProfileDialog().isShowing())
            getProfileDialog().close();
    }

    public Stage getRootStage()
    {
        return this.rootStage;
    }

    public Stage getProfileDialog()
    {
        return this.profileDialog;
    }

    // Configs manager
    public void loadProfiles()
    {
        if (this.prefs == null) {
            this.prefs = Preferences.userNodeForPackage(getClass()).node("json");
        }

        ProfilesManager.load(new JSONArray(this.prefs.get("profiles", "[]")));
        ProfilesManager.getProfiles().forEach(profile -> {
            this.mainController.addProfileList(profile);
            this.profileRunnables.add(new ProfileRunnable(this, profile, this.keysManager));
        });
    }

    public void saveProfiles()
    {
        if (this.prefs == null) {
            this.prefs = Preferences.userNodeForPackage(getClass()).node("json");
        }
        this.prefs.put("profiles", ProfilesManager.save().toString());
    }

    // NATIVE SYSTEM

    private void loadNativeLibrary() throws IOException
    {
        InputStream inputStream = null;
        File tempLibFile = null;

        if (isWindows) {
            tempLibFile = Files.createTempFile("libihacinput", ".dll").toFile();
            inputStream = getClass().getResourceAsStream("/libs/x86_64/libihacinput_win.dll");
        } else if (isLinux) {
            tempLibFile = Files.createTempFile("libihacinput", ".so").toFile();
            inputStream = getClass().getResourceAsStream("/libs/x86_64/libihacinput_lin.so");
        }

        if (tempLibFile == null || inputStream == null) {
            System.exit(1);
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(tempLibFile)) {
            outputStream.write(inputStream.readAllBytes());
            outputStream.flush();
        }

        System.load(tempLibFile.getAbsolutePath());
        tempLibFile.deleteOnExit();
    }

    public native void nativeMouseClick(int mouseButton);

    //// Extra

    public void alert(String message)
    {
        Alert alert = new Alert(AlertType.ERROR, message);
        alert.initOwner(getProfileDialog());
        alert.show();
    }
}
