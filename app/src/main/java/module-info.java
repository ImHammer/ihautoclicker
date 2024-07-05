module com.github.imhammer.ihautoclicker
{
    requires java.base;
    requires java.prefs;
    requires org.json;
    requires javafx.base;
    requires javafx.fxml;

    requires transitive com.github.kwhat.jnativehook;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    opens com.github.imhammer.ihautoclicker;

    exports com.github.imhammer.ihautoclicker;
    exports com.github.imhammer.ihautoclicker.run;
    exports com.github.imhammer.ihautoclicker.utils;
    exports com.github.imhammer.ihautoclicker.manager;
    exports com.github.imhammer.ihautoclicker.profile;
    exports com.github.imhammer.ihautoclicker.controller;
    exports com.github.imhammer.ihautoclicker.controller.fx;
}
