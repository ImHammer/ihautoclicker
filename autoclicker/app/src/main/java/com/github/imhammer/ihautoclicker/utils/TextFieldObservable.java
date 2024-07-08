package com.github.imhammer.ihautoclicker.utils;

import javafx.beans.Observable;
import javafx.scene.control.TextField;

public class TextFieldObservable
{
    private TextFieldFunctioConsumer consumer;
    private TextField textField;
    private String oldValue;
    
    public TextFieldObservable(TextField textField, TextFieldFunctioConsumer consumer)
    {
        this.consumer = consumer;
        this.textField = textField;
        this.oldValue = textField.getText();

        textField.textProperty().addListener(this::onChange);
    }

    public void onChange(Observable observable)
    {
        if (!this.textField.getText().equals(this.oldValue)) {
            String newValue = this.consumer.apply(this.oldValue, this.textField.getText());
            this.oldValue = newValue;
            this.textField.setText(newValue);
        }
    }

    @FunctionalInterface
    public static interface TextFieldFunctioConsumer
    {
        String apply(String oldValue, String newValue);
    }
}
