package com.github.imhammer.ihautoclicker.controller.fx;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.Initializable;

public class FXAppController implements Initializable
{
    protected final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        log.info("AppController has been inizialized!");
    }
}
