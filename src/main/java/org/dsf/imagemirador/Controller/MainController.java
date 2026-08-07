package org.dsf.imagemirador.Controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.media.MediaView;
import javafx.stage.Window;
import org.dsf.imagemirador.Dto.MediaItem;
import org.dsf.imagemirador.Service.FileScannerService;
import org.dsf.imagemirador.Service.ThumbnailService;
import org.dsf.imagemirador.Viewer.ImageViewer;
import org.dsf.imagemirador.Viewer.MediaViewer;

import java.util.List;

public class MainController {
    @FXML private ScrollPane thumbnailScrollPane;
    @FXML private TilePane thumbnailGrid;
    @FXML private ImageView imageWindow;
    @FXML private ScrollPane scrollPane;
    @FXML private Group imageGroup;
    @FXML private CheckMenuItem checkMirror;
    @FXML private MediaView mediaWindow;
    @FXML private SplitPane mainSplitPane;

    private final FileScannerService fileScannerService = new FileScannerService();
    private final ThumbnailService thumbnailService = new ThumbnailService();
    private ImageViewer imageViewer;

    private List<MediaItem> listFiles;
    private int index = 0;
    private MediaViewer mediaViewer;
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
        List<MediaItem> newFiles = fileScannerService.openMethod(window);

        if (newFiles != null && !newFiles.isEmpty()) {
            this.listFiles = newFiles;
            this.index = 0;

            // 1. Limpiamos la cuadrícula anterior por si abrimos una carpeta nueva
            if (thumbnailGrid != null) {
                thumbnailGrid.getChildren().clear();
            }

            for (MediaItem item : listFiles) {
                thumbnailService.loadThumbnailAsync(item, thumbnail -> {
                    // acá van las miniaturasss
                    ImageView thumbView = new ImageView(thumbnail);
                    thumbView.setFitWidth(100);
                    thumbView.setFitHeight(100);
                    thumbView.setPreserveRatio(true);

                    //cuando clickean a la miniatura, busca la posición y la pasa al visor grande
                    thumbView.setOnMouseClicked(event -> {
                        this.index = listFiles.indexOf(item);
                        showFile();
                    });

                    // miniatura añadida a la cuadrícula
                    if (thumbnailGrid != null) {
                        thumbnailGrid.getChildren().add(thumbView);
                    }
                });}
            showFile();
        } else {
            System.out.println("Snif snif, no encontré ningún archivo compatible en esta carpeta...");
        }
    }
    @FXML
    public void toggleGalleryMethod() {
        if (mainSplitPane.getItems().contains(thumbnailScrollPane)) {
            // si está visible, lo sacamos del SplitPane
            mainSplitPane.getItems().remove(thumbnailScrollPane);
            System.out.println("Galería ocultada. Fuera fuera.");
        } else {
            // sino, lo añadimos en la primera posición (índice 0, izquierda)
            mainSplitPane.getItems().add(0, thumbnailScrollPane);
            mainSplitPane.setDividerPositions(0.25); // Le asignamos el 25% del ancho de pantalla
            System.out.println("Cuadrícula de la galería visiblee");
        }
    }

    private void showFile() {
        if (listFiles == null || listFiles.isEmpty()) return;

        MediaItem currentItem = listFiles.get(index);

        // limpiamos los 2 visores por las dudas
        imageViewer.clear();
        mediaViewer.clear();

        // forzamos toodo a minúsculas para validar
        String path = currentItem.path().toLowerCase();

        // switch para ver si es vid o img
        if (path.endsWith(".mp4") || path.endsWith(".mov")) {
            // si es formato mp4 o mov viene mediaviewer
            System.out.println("Encontré tu videooo, agarra croquetas que empieza");
            mediaViewer.loadMedia(currentItem);
        } else {
            // si no es video, se lo mandamos al ImageViewer
            imageViewer.showImage(currentItem);
        }
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