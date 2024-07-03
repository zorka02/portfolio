module com.zoramarcinova.audiometry_v3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.zoramarcinova.audiometry_v3 to javafx.fxml;
    exports com.zoramarcinova.audiometry_v3;
}