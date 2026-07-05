package org.dsf.imagemirador.Controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {

    @FXML
    private ImageView ventanaImagen;

    @FXML
    public void initialize() {
        ventanaImagen.setPreserveRatio(true);   //esto es para que no se deforme toda la imagen, osea preservar el ratio
        // ventanaImagen.setSmooth(true);       //y esto es para filtrar creo? noc, en la doc de oracle dice algo de eso
                                                //sii creo abria que sacarlo y no usar filtro ya que podria alterar la imagen original


        //esto es para que la imagen se ajuste a su contenedor
        ventanaImagen.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                javafx.scene.layout.Region contenedor = (javafx.scene.layout.Region) ventanaImagen.getParent();
                ventanaImagen.fitWidthProperty().bind(contenedor.widthProperty());
                ventanaImagen.fitHeightProperty().bind(contenedor.heightProperty());
            }
        });

        //aca buscamos la imagen
        Image image = new Image(getClass().getResourceAsStream("/org/dsf/imagemirador/TestImage/test.jpg"));

        //y aca la muestra
        ventanaImagen.setImage(image);
    }

    //aca el boton del menu para abrir, tengo que hacer que pueda abrir otras imagenes
    public void openMethod() {
        System.out.println("open uwu");
    }
    //y este lo cierra dah, tengo que ver si cierra la app, o cierra la imagen, noc
    //bah puse el shortcut ctrl w porq imagino cierra la imagen y no la app
    public void closeMethod(){
        System.out.println("clsoe uwu");
    }
}
