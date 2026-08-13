package org.dsf.imagemirador.Controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.dsf.imagemirador.Dto.MediaItem;
import org.dsf.imagemirador.Service.FileScannerService;
import org.dsf.imagemirador.Service.ThumbnailService;
import org.dsf.imagemirador.Viewer.ImageViewer;
import org.dsf.imagemirador.Viewer.MediaViewer;
import org.dsf.imagemirador.Viewer.NavigationViewer;

import java.io.File;
import java.util.List;

public class MainController {

    @FXML private ScrollPane thumbnailScrollPane;
    @FXML private TilePane thumbnailGrid;
    @FXML private ImageView imageWindow;
    @FXML private ScrollPane scrollPane;
    @FXML private Group imageGroup;
    @FXML private CheckMenuItem checkMirror;
    @FXML private CheckMenuItem alwaysOnTop;
    @FXML private MediaView mediaWindow;
    @FXML private SplitPane mainSplitPane;

    private final FileScannerService fileScannerService = new FileScannerService();
    private final ThumbnailService thumbnailService = new ThumbnailService();
    private final NavigationViewer navigator = new NavigationViewer();

    private ImageViewer imageViewer;
    private MediaViewer mediaViewer;
    private Stage stage;


    //el controlador recibe el Stage
    public void setStage(Stage stage) {
        this.stage = stage;
        if (imageViewer != null) {
            imageViewer.setStage(stage); //sin esto no anda, la cosa es que es un codigo que se repite lo anterior, no?
        }
    }

    @FXML
    public void initialize() {
        // le pasamos los elementos al controller imageviewer
        imageViewer = new ImageViewer(imageWindow, scrollPane, imageGroup, checkMirror);
        mediaViewer = new MediaViewer(mediaWindow);
        mainSplitPane.getItems().remove(thumbnailScrollPane);
    }

    @FXML
    public void openMethod() {
        System.out.println("Snif snif SNIIIF a ver busco tu cuestión...");
        Window window = imageWindow.getScene().getWindow();


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Elegí un archivo");
        //filtro de extensiones
        fileChooser.getExtensionFilters().add(fileScannerService.getSupportedExtensionsFilter());
        //filtro de todos los archivos
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));

        File lastDir = fileScannerService.getLastDirectory();
        if (lastDir != null && lastDir.exists()) {
            fileChooser.setInitialDirectory(lastDir);
        }

        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile == null) return;


        //se crea el Task para seleccionar archivos
        Task<List<MediaItem>> scanTask = fileScannerService.createScanTask(selectedFile);

        scanTask.setOnSucceeded(event -> {
            List<MediaItem> loadedFiles = scanTask.getValue();
            int startingIndex = fileScannerService.getSelectedFileIndex();

            if (loadedFiles != null && !loadedFiles.isEmpty()) {
                navigator.load(loadedFiles, startingIndex);
                System.out.println("Guau guau! Encontré " + navigator.getTotal() + " archivos compatibles!");

                // Carga interactiva de la cuadrícula
                if (thumbnailGrid != null) thumbnailGrid.getChildren().clear();

                for (MediaItem item : loadedFiles) {
                    thumbnailService.loadThumbnailAsync(item, thumbnail -> {
                        ImageView thumbView = new ImageView(thumbnail);
                        // acá van las mini miniaturasss
                        thumbView.setFitWidth(100);
                        thumbView.setFitHeight(100);
                        thumbView.setPreserveRatio(true);

                        thumbView.setOnMouseClicked(e -> {
                            navigator.setIndex(loadedFiles.indexOf(item));
                            showFile();
                        });

                        // miniatura añadida a la cuadrícula
                        if (thumbnailGrid != null) thumbnailGrid.getChildren().add(thumbView);
                    });
                }

                // Mostrar galería automáticamente si está cerrada
                if (!mainSplitPane.getItems().contains(thumbnailScrollPane)) {
                    mainSplitPane.getItems().add(0, thumbnailScrollPane);
                    mainSplitPane.setDividerPositions(0.25);
                }

                showFile();
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

    private void showFile() {
        MediaItem currentItem = navigator.getCurrent();
        if (currentItem == null) return;

        imageViewer.clear();
        mediaViewer.clear();

        // forzamos toodo a minúsculas para validar
        String path = currentItem.path().toLowerCase();

        // switch para ver si es vid o img
        if (path.endsWith(".mp4") || path.endsWith(".mov")) {
            System.out.println("Encontré tu videooo, agarra croquetas que empieza");
            // si es formato mp4 o mov viene mediaviewer
            mediaViewer.loadMedia(currentItem);
        } else {
            // si no es video, se lo mandamos al ImageViewer
            imageViewer.showImage(currentItem);
        }
    }

    @FXML
    public void toggleGalleryMethod() {
        // si está visible, lo sacamos del SplitPane
        if (mainSplitPane.getItems().contains(thumbnailScrollPane)) {
            mainSplitPane.getItems().remove(thumbnailScrollPane);
            System.out.println("Galería ocultada. Fuera fuera.");
        } else {
            // sino, lo añadimos en la primera posición (índice 0, izquierda)
            mainSplitPane.getItems().add(0, thumbnailScrollPane);
            mainSplitPane.setDividerPositions(0.25); // Le asignamos el 25% del ancho de pantalla
            System.out.println("Cuadrícula de la galería visiblee");
        }
    }
    //navegacion, puse alt + right, porq right solo a veces no funciona, o si apreto para rotar tambien cuenta y rota y cambia de imagen
    @FXML
    public void rightMethod() {
        if (navigator.next() != null) {
            showFile();
            System.out.println("Derecha uwu -> Viendo archivo " + (navigator.getIndex() + 1) + " de " + navigator.getTotal());
        }
    }

    @FXML
    public void leftMethod() {
        if (navigator.previous() != null) {
            showFile();
            System.out.println("Izquierda uwu -> Viendo archivo " + (navigator.getIndex() + 1) + " de " + navigator.getTotal());
        }
    }

    @FXML
    public void closeMethod() {
        System.out.println("closeada tu wea >:3c");
        imageViewer.clear(); // limpia la vista de la imagen
        mediaViewer.clear(); // limpia la vista del video
        navigator.clear(); // limpia  el navegador
    }

    @FXML public void alwaysOnTopMethod() { imageViewer.alwaysOnTop(alwaysOnTop.isSelected()); }
    @FXML public void rotateRightMethod() { imageViewer.rotateRight(); }
    @FXML public void rotateLeftMethod() { imageViewer.rotateLeft(); }
    @FXML public void mirrorMethod() { imageViewer.mirror(checkMirror.isSelected()); }
    @FXML public void plusZoomMethod() { imageViewer.zoomIn(); }
    @FXML public void minusZoomMethod() { imageViewer.zoomOut(); }
    @FXML public void restoreMethod() { imageViewer.restore(); }

    @FXML public void scrollZoomMethod(ScrollEvent event) {
        imageViewer.scrollZoom(event.getDeltaY());
        event.consume();
    }
}