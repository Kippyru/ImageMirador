package org.dsf.imagemirador.Controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.dsf.imagemirador.Dto.MediaItem;
import org.dsf.imagemirador.Service.FileScannerService;
import org.dsf.imagemirador.Viewer.ImageViewer;
import org.dsf.imagemirador.Viewer.NavigationViewer;

import java.io.File;
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
    @FXML
    private CheckMenuItem alwaysOnTop;
    @FXML
    private CheckMenuItem checkGallery;
    @FXML
    private javafx.scene.Node galleryView;

    private final FileScannerService fileScannerService = new FileScannerService();
    private final NavigationViewer navigator = new NavigationViewer();
    private ImageViewer imageViewer;
    private Stage stage;

    //el controlador recibe el Stage
    public void setStage(Stage stage) {
        this.stage = stage;
        imageViewer.setStage(stage); //sin esto no anda, la cosa es que es un codigo que se repite lo anterior, no?
    }

    @FXML
    public void initialize() {
        imageViewer = new ImageViewer(imageWindow, scrollPane, imageGroup, checkMirror);
        //por default, la galeria inicia oculta
        galleryView.setVisible(false);
        galleryView.setManaged(false);
    }

    @FXML
    public void openMethod() {
        System.out.println("Snif snif SNIIIF a ver busco tu cuestión...");
        Window window = imageWindow.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Elegí un archivo");

        //filtro de extenciones
        fileChooser.getExtensionFilters().add(fileScannerService.getSupportedExtensionsFilter());

        //filtro de todos los archivos
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File lastDir = fileScannerService.getLastDirectory();
        if (lastDir != null && lastDir.exists()) {
            fileChooser.setInitialDirectory(lastDir);
        }

        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile == null) {
            System.out.println("Selección cance-helada");
            return;
        }

        //se crea el Task para seleccionar archivos
        Task<List<MediaItem>> scanTask = fileScannerService.createScanTask(selectedFile);

        scanTask.setOnSucceeded(event -> {
            List<MediaItem> loadedFiles = scanTask.getValue();
            int startingIndex = fileScannerService.getSelectedFileIndex();

            if (loadedFiles != null && !loadedFiles.isEmpty()) {
                navigator.load(loadedFiles, startingIndex);
                System.out.println("Guau guau! Encontré " + navigator.getTotal() + " archivos compatibles!");
                imageViewer.showImage(navigator.getCurrent());
            } else {
                System.out.println("Snif snif, no encontré ningún archivo compatible...");
            }
        });

        scanTask.setOnFailed(event -> {
            System.err.println("Error fatal... CHOVER");
            scanTask.getException().printStackTrace();
        });

        //ejecuta el hilo
        Thread thread = new Thread(scanTask);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void rightMethod() {
        MediaItem nextItem = navigator.next();
        if (nextItem != null) {
            imageViewer.showImage(nextItem);
            System.out.println("Derecha uwu -> Viendo archivo " + (navigator.getIndex() + 1) + " de " + navigator.getTotal());
        }
    }

    @FXML
    public void leftMethod() {
        MediaItem prevItem = navigator.previous();
        if (prevItem != null) {
            imageViewer.showImage(prevItem);
            System.out.println("Izquierda uwu -> Viendo archivo " + (navigator.getIndex() + 1) + " de " + navigator.getTotal());
        }
    }

    @FXML
    public void closeMethod() {
        imageViewer.clear();
        navigator.clear();
        System.out.println("Vista limpiada >:3c");
    }

    @FXML public void alwaysOnTopMethod() { imageViewer.alwaysOnTop(alwaysOnTop.isSelected()); } //LITERALMENTE YO
    @FXML public void rotateRightMethod() { imageViewer.rotateRight(); }
    @FXML public void rotateLeftMethod() { imageViewer.rotateLeft(); }
    @FXML public void mirrorMethod() { imageViewer.mirror(checkMirror.isSelected()); }
    @FXML public void plusZoomMethod() { imageViewer.zoomIn(); }
    @FXML public void minusZoomMethod() { imageViewer.zoomOut(); }
    @FXML public void restoreMethod() { imageViewer.restore(); }

    @FXML
    public void scrollZoomMethod(ScrollEvent event) {
        imageViewer.scrollZoom(event.getDeltaY());
        event.consume();
    }

    @FXML
    public void toggleGalleryMethod() {
        boolean isVisible = checkGallery.isSelected();
        galleryView.setVisible(isVisible);
        galleryView.setManaged(isVisible);

        if (isVisible) {
            System.out.println("Galeria abierta OwO");
        } else {
            System.out.println("Galeria cerrada UnU");
        }
    }
}