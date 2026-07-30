module org.kevin.imagemirador {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.kordamp.ikonli.core;

    opens org.dsf.imagemirador to javafx.fxml;
    exports org.dsf.imagemirador;
    exports org.dsf.imagemirador.Controller;
    opens org.dsf.imagemirador.Controller to javafx.fxml;
}