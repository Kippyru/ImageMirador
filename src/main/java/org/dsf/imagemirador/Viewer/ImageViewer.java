// entonces aca hay que refactorizar el maincontroller

package org.dsf.imagemirador.Viewer;

import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.dsf.imagemirador.Dto.MediaItem;

public class ImageViewer {

    private final ImageView imageWindow;
    private final ScrollPane scrollPane;
    private final Group imageGroup;
    private final CheckMenuItem checkMirror;

    private static final double ZOOM_SENSITIVITY = 0.1;
    private double zoom = 1.0;

    // acá recibirá cada cuestión de la imagén
    public ImageViewer(ImageView imageWindow, ScrollPane scrollPane, Group imageGroup, CheckMenuItem checkMirror) {
        this.imageWindow = imageWindow;
        this.scrollPane = scrollPane;
        this.imageGroup = imageGroup;
        this.checkMirror = checkMirror;

        //filtros para mantener el ratio
        this.imageWindow.setPreserveRatio(true);
        this.imageWindow.setSmooth(true);
    }

    public void showImage(MediaItem item) {
        if (item == null) return;

        Image image = new Image(item.path());

        imageWindow.fitWidthProperty().unbind();
        imageWindow.fitHeightProperty().unbind();

        imageWindow.setVisible(true);
        imageWindow.setManaged(true);
        imageWindow.setImage(image);

        //un restore para no tener zoom ni rotaciónnn
        restore();

        System.out.println("Con un guau y un miau, lo encontré! Acá está uwu");
    }

    public void clear() {
        imageWindow.setVisible(false); //esto es para ocultar el imageview
        imageWindow.setManaged(false); //esto es para ignorar el imageview
        imageWindow.setImage(null);
    }

    public void rotateRight() {
        imageWindow.setRotate(imageWindow.getRotate() + 90);
    }

    public void rotateLeft() {
        imageWindow.setRotate(imageWindow.getRotate() - 90);
    }

    public void mirror(boolean isSelected) { //hago mirror del grupo, si hago de la image interfieren los scale y por eso se rompia
        imageGroup.setScaleX(isSelected ? -1 : 1);
    }

    public void zoomIn() {
        applyZoom(ZOOM_SENSITIVITY);
    }

    public void zoomOut() {
        applyZoom(-ZOOM_SENSITIVITY);
    }

    public void scrollZoom(double deltaY) {
        if (deltaY > 0) {
            applyZoom(ZOOM_SENSITIVITY);
        } else {
            applyZoom(-ZOOM_SENSITIVITY);
        }
    }

    private void applyZoom(double delta) {
        zoom *= (1 + delta);
        zoom = Math.max(0.05, Math.min(zoom, 20.0));
        imageWindow.setScaleX(zoom);
        imageWindow.setScaleY(zoom);
    }

    public void restore() {
        imageWindow.setRotate(0);
        imageWindow.setScaleX(1);
        imageWindow.setScaleY(1);

        if (checkMirror != null) { //aca hay mucho codigo que se repite con open, hay que separarlo
            checkMirror.setSelected(false);
        }

        zoom = 1.0;
        imageGroup.setScaleX(zoom);
        imageGroup.setScaleY(zoom);

        if (scrollPane != null) { //autoajusta la imagen a la pantalla
            imageWindow.fitWidthProperty().bind(scrollPane.widthProperty());
            imageWindow.fitHeightProperty().bind(scrollPane.heightProperty());
        }
    }
}