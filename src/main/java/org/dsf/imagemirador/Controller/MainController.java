package org.dsf.imagemirador.Controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Window;
import org.dsf.imagemirador.Dto.MediaItem;
import org.dsf.imagemirador.Service.FileScannerService;

import java.util.Optional;

public class MainController {

    @FXML
    private ImageView imageWindow ;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Group imageGroup;
    @FXML
    private CheckMenuItem checkMirror;

    private static final double zoomSensitivity = 0.1;
    private double zoom = 1.0;

    private final FileScannerService fileScannerService = new FileScannerService();

    @FXML
    public void initialize() {
        imageWindow .setPreserveRatio(true);    //esto es para que no se deforme toda la imagen, preservar el ratio
            imageWindow .setSmooth(true);       //testing no me mostró ninguna alteración importante hasta ahora (08-07-26)
                                                //queda presente el filtro
    }

    //aca el boton del menu para abrir, tengo que hacer que pueda abrir otras imagenes
    @FXML
    public void openMethod() {
        System.out.println("Snif snif SNIIIF a ver busco tu cuestión...");
        Window window = imageWindow.getScene().getWindow();
        Optional<MediaItem> item = fileScannerService.openMethod(window);

        item.ifPresent(mediaItem -> {
            Image image = new Image(mediaItem.path());
            //soltamos bindings anteriores por si quedaron
            imageWindow.fitWidthProperty().unbind();
            imageWindow.fitHeightProperty().unbind();

            //hacemos visible imageview y carga de la imagen
            imageWindow.setVisible(true);
            imageWindow.setManaged(true);
            imageWindow.setImage(image);

            //reinicio de transformaciones
            imageWindow.setRotate(0);
            imageWindow.setScaleX(1);
            imageWindow.setScaleY(1);
            //reinicia el zoom, tambien lo hace el de restaurar, hay que separarlo en una funcion aparte
            zoom = 1.0;
            imageGroup.setScaleX(zoom);
            imageGroup.setScaleY(zoom);

            if (checkMirror != null) {
                checkMirror.setSelected(false);
            }
            //esto bindea la imagen a los bordes, para que se ajuste automaticamente
            if (scrollPane != null) {
                imageWindow.fitWidthProperty().bind(scrollPane.widthProperty());
                imageWindow.fitHeightProperty().bind(scrollPane.heightProperty());
            }
            System.out.println("Con un guau y un miau, lo encontré! Acá está uwu");
        });
    }

    //este cierra la imagen. ctrl + w
    public void closeMethod() {
        System.out.println("closeada tu wea >:3c");
        imageWindow.setVisible(false);  //esto es para ocultar el imageview
        imageWindow.setManaged(false);  //esto es para ignorar el imageview
        imageWindow.setImage(null);
    }

    @FXML
    public void rotateRightMethod() {
        imageWindow.setRotate(imageWindow.getRotate() + 90);
    }

    @FXML
    public void rotateLeftMethod() {
        imageWindow.setRotate(imageWindow.getRotate() - 90);
    }

    @FXML
    public void mirrorMethod() {    //hago mirror del grupo, si hago de la image interfieren los scale y por eso se rompia
        imageGroup.setScaleX(checkMirror.isSelected() ? -1 : 1);
    }

    @FXML
    public void plusZoomMethod() {
        applyZoom(zoomSensitivity);
    }

    @FXML
    public void minusZoomMethod() {
        applyZoom(-zoomSensitivity);
    }

    @FXML
    public void scrollZoomMethod(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            applyZoom(zoomSensitivity);
        } else {
            applyZoom(-zoomSensitivity);
        }
        event.consume();
    }

    private void applyZoom(double delta) {
        zoom *= (1 + delta);
        zoom = Math.max(0.05, Math.min(zoom, 20.0));

        imageWindow.setScaleX(zoom);
        imageWindow.setScaleY(zoom);
    }

    @FXML
    public void restoreMethod() { //aca hay mucho codigo que se repite con open, hay que separarlo
        imageWindow.setRotate(0);
        imageWindow.setScaleX(1);
        imageWindow.setScaleY(1);
        if (checkMirror != null) {
            checkMirror.setSelected(false);
        }

        zoom = 1.0;
        imageGroup.setScaleX(zoom);
        imageGroup.setScaleY(zoom);

        //autoajusta la imagen a la pantalla
        if (scrollPane != null) {
            imageWindow.fitWidthProperty().bind(scrollPane.widthProperty());
            imageWindow.fitHeightProperty().bind(scrollPane.heightProperty());
        }
    }

    //navegacion, puse alt + right, porq right solo a veces no funciona, o si apreto para rotar tambien cuenta y rota y cambia de imagen
    @FXML
    public void rightMethod() {
        System.out.println("derecha uwu");
    }

    @FXML
    public void leftMethod() {
        System.out.println("izquierda uwu");
    }
}