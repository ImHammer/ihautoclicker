package com.github.imhammer.ihautoclicker.controller.fx;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.Initializable;

public class FXProfileController implements Initializable
{
    protected final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        log.info("ProfileController has been inizialized!");
    }
}
