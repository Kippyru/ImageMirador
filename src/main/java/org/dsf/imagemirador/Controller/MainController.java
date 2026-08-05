package org.dsf.imagemirador.Controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.MediaView;
import javafx.stage.Window;
import org.dsf.imagemirador.Dto.MediaItem;
import org.dsf.imagemirador.Service.FileScannerService;
import org.dsf.imagemirador.Viewer.ImageViewer;
import org.dsf.imagemirador.Viewer.MediaViewer;

import java.util.List;

public class MainController {

    @FXML private ImageView imageWindow;
    @FXML private ScrollPane scrollPane;
    @FXML private Group imageGroup;
    @FXML private CheckMenuItem checkMirror;
    @FXML private MediaView mediaWindow;


    private final FileScannerService fileScannerService = new FileScannerService();
    private ImageViewer imageViewer;

    private List<MediaItem> listFiles;
    private int index = 0;

    @FXML
    public void initialize() {
        // le pasamos los elementos al controller imageviewer
        imageViewer = new ImageViewer(imageWindow, scrollPane, imageGroup, checkMirror);
        mediaViewer = new MediaViewer(mediaWindow);
    }

    @FXML
    public void openMethod() {
        System.out.println("Snif snif SNIIIF a ver busco tu cuestión...");
        Window window = imageWindow.getScene().getWindow();
        List<MediaItem> newFiles = fileScannerService.openMethod(window);

        if (newFiles != null && !newFiles.isEmpty()) {
            this.listFiles = newFiles;
            this.index = 0;
            System.out.println("Guau guau! Encontré " + listFiles.size() + " archivos compatibles!");
            showFile();
        } else {
            System.out.println("Snif snif, no encontré ningún archivo compatible en esta carpeta...");
        }
    }

    private void showFile() {
        if (listFiles == null || listFiles.isEmpty()) return;
        //coso que carga el archivo desde imageviewerr
        imageViewer.showImage(listFiles.get(index));
    }

    @FXML
    public void closeMethod() {
        System.out.println("closeada tu wea >:3c");
        imageViewer.clear(); // limpia la vista
        if (listFiles != null) {
            listFiles.clear(); // limpia la memoria
        }
    }


    @FXML
    public void rotateRightMethod() {
        imageViewer.rotateRight();
    }

    @FXML
    public void rotateLeftMethod() {
        imageViewer.rotateLeft();
    }

    @FXML
    public void mirrorMethod() {
        imageViewer.mirror(checkMirror.isSelected());
    }

    @FXML
    public void plusZoomMethod() {
        imageViewer.zoomIn();
    }

    @FXML
    public void minusZoomMethod() {
        imageViewer.zoomOut();
    }

    @FXML
    public void scrollZoomMethod(ScrollEvent event) {
        imageViewer.scrollZoom(event.getDeltaY());
        event.consume();
    }

    @FXML
    public void restoreMethod() {
        imageViewer.restore();
    }

    private MediaViewer mediaViewer;


    //navegacion, puse alt + right, porq right solo a veces no funciona, o si apreto para rotar tambien cuenta y rota y cambia de imagen
    @FXML
    public void rightMethod() {
        if (listFiles == null || listFiles.isEmpty()) return;
        index++;
        if (index >= listFiles.size()) index = 0;
        showFile();
        System.out.println("derecha uwu -> Viendo archivo " + (index + 1) + " de " + listFiles.size());
    }

    @FXML
    public void leftMethod() {
        if (listFiles == null || listFiles.isEmpty()) return;
        index--;
        if (index < 0) index = listFiles.size() - 1;
        showFile();
        System.out.println("izquierda uwu -> Viendo archivo " + (index + 1) + " de " + listFiles.size());
    }
}