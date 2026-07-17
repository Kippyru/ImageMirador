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

import java.util.List;

public class MainController {

    @FXML
    private ImageView imageWindow;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Group imageGroup;
    @FXML
    private CheckMenuItem checkMirror;

    private static final double zoomSensitivity = 0.1;
    private double zoom = 1.0;

    private final FileScannerService fileScannerService = new FileScannerService();

    // la lista y el index para el array de archivos dentro de la carpeta que seleccionemos
    private List<MediaItem> listFiles;
    private int index = 0;

    @FXML
    public void initialize() {
        imageWindow.setPreserveRatio(true);    //esto es para que no se deforme toda la imagen, preservar el ratio
        imageWindow.setSmooth(true);           //testing no me mostró ninguna alteración importante hasta ahora (08-07-26)
                                               //queda presente el filtro
    }

    //aca el boton del menu para abrir, tengo que hacer que pueda abrir otras imagenes
    @FXML
    public void openMethod() {
        System.out.println("Snif snif SNIIIF a ver busco tu cuestión...");
        Window window = imageWindow.getScene().getWindow();
        //bueno acá el arraay, chequeos y reiniciado de index
        List<MediaItem> newFiles = fileScannerService.openMethod(window);

        if (newFiles != null && !newFiles.isEmpty()) {
            this.listFiles = newFiles;
            this.index = 0; // Reiniciamos el índice a la primera imagen

            System.out.println("Guau guau! Encontré " + listFiles.size() + " archivos compatibles!");
            showFile();
        } else {
            System.out.println("Snif snif, no encontré ningún archivo compatible en esta carpeta...");
        }
    }

    private void showFile() {
        if (listFiles == null || listFiles.isEmpty()) return;

        // Obtenemos la imagen actual de la lista usando el índice
        MediaItem currentItem = listFiles.get(index);
        Image image = new Image(currentItem.path());

        // soltamos bindings anteriores por si quedaron
        imageWindow.fitWidthProperty().unbind();
        imageWindow.fitHeightProperty().unbind();

        // hacemos visible imageview y carga de la imagen
        imageWindow.setVisible(true);
        imageWindow.setManaged(true);
        imageWindow.setImage(image);

        // reinicio de transformaciones
        imageWindow.setRotate(0);
        imageWindow.setScaleX(1);
        imageWindow.setScaleY(1);

        // reinicia el zoom
        zoom = 1.0;
        imageGroup.setScaleX(zoom);
        imageGroup.setScaleY(zoom);

        if (checkMirror != null) {
            checkMirror.setSelected(false);
        }

        // esto bindea la imagen a los bordes, para que se ajuste automaticamente
        if (scrollPane != null) {
            imageWindow.fitWidthProperty().bind(scrollPane.widthProperty());
            imageWindow.fitHeightProperty().bind(scrollPane.heightProperty());
        }

        System.out.println("Con un guau y un miau, lo encontré! Acá está uwu");
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
//navegacion, puse alt + right, porq right solo a veces no funciona, o si apreto para rotar tambien cuenta y rota y cambia de imagen
    @FXML
    public void rightMethod() {
        // no encuentro nada = hacer nada
        if (listFiles == null || listFiles.isEmpty()) return;
        //+1 al index para mostrar lo siguiente en el array (que tiene cargados los archivos)
        index++;

        // Si nos pasamos de la cantidad de imágenes, volvemos a la primera (índice 0)
        if (index >= listFiles.size()) {
            index = 0;
        }

        showFile();
        System.out.println("derecha uwu -> Viendo archivo " + (index + 1) + " de " + listFiles.size());
    }

    @FXML
    public void leftMethod() {
        // no encuentro nada = hacer nada (otra vez)
        if (listFiles == null || listFiles.isEmpty()) return;
        // -1 al index para mostrar lo siguiente en el array (que tiene cargados los archivos)
        index--; // Restamos 1 al índice

        // esto para ir a la última imágen nomás, miro el tamaño del array y le resto 1 para que el index sea justamente la última
        if (index < 0) {
            index = listFiles.size() - 1;
        }

        showFile();
        System.out.println("izquierda uwu -> Viendo archivo " + (index + 1) + " de " + listFiles.size());
    }
}