package org.dsf.imagemirador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dsf.imagemirador.Controller.MainController;

import java.io.IOException;

public class VisualHotApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VisualHotApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        //inyecta el stage al main controller
        MainController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("VisualHot!");
        stage.setScene(scene);
        stage.show();
    }
}