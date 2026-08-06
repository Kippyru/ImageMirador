package org.dsf.imagemirador.Viewer;

import javafx.scene.control.ScrollPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.dsf.imagemirador.Dto.MediaItem;

public class MediaViewer {
    private final MediaView mediaView;

    // motor de reproducción
    private MediaPlayer mediaPlayer;
    private ScrollPane scrollPane;

    public MediaViewer(MediaView mediaView) {
        this.mediaView = mediaView;


        //el video se ajusta a la ventana
        this.mediaView.setPreserveRatio(true);

    }

    public void loadMedia(MediaItem item) {
        if (item == null) return;

        //limpiar img o vid que se estaba viendo
        clear();


        try {
            // agarra el path del file
            Media media = new Media(item.path());

            // hacemos el nuevo reproductor
            mediaPlayer = new MediaPlayer(media);

            if (scrollPane != null) {
                mediaView.fitWidthProperty().bind(scrollPane.widthProperty());
                mediaView.fitHeightProperty().bind(scrollPane.heightProperty());
            }

            // enchufaaa a la pantalla MediaView, lo hace mediaplayer al... si, al mediaplayer(nuestro)
            mediaView.setMediaPlayer(mediaPlayer);

            // hacemos visible
            mediaView.setVisible(true);
            mediaView.setManaged(true);

            // esto de acá es para activar el autoplay
            mediaPlayer.setAutoPlay(true);

            System.out.println("Video cargado! Ahora reproduciendo...  " + item.name());

        } catch (Exception e) {
            System.out.println("Error al cargar el video: " + e.getMessage());
        }
    }

    public void playMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pauseMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resetMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO); // esto es para resetear la duración, necesita ajustes porque creo que se entorpece con el restore de imageviewer
                                            // yo sugiero que veamos esto más adelante, cuando refactoricemos más el main
        }
    }

    // clearrr, estoy pensando en tal vez añadir esto a un eventual controller de (valga la redundancia) controles (next, prev, + - volumen, etc)
    public void clear() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // libera recursos del motor
            mediaPlayer = null;
            System.out.println("Limpiado el motooor mediaPlayer!");
        }
        mediaView.setMediaPlayer(null); // limpiamos la vista/pantalla
        mediaView.setVisible(false);
        mediaView.setManaged(false);
        System.out.println("Limpiada la pantalla mediaView!");
    }
}