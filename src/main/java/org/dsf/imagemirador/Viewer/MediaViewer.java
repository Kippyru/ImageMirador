package org.dsf.imagemirador.Viewer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.dsf.imagemirador.Dto.MediaItem;

public class MediaViewer {

    // El componente visual que recibimos desde el FXML
    private final MediaView mediaView;

    // El motor de reproducción
    private MediaPlayer mediaPlayer;

    public MediaViewer(MediaView mediaView) {
        this.mediaView = mediaView;

        // Configuraciones iniciales (para que el video se ajuste a la ventana, igual que las imágenes)
        this.mediaView.setPreserveRatio(true);
    }

    public void loadMedia(MediaItem item) {
        if (item == null) return;

        // 1. Limpiamos cualquier video que se estuviera reproduciendo antes
        clear();

        try {
            // 2. Cargamos el archivo físico
            Media media = new Media(item.path());

            // 3. Creamos el motor de reproducción
            mediaPlayer = new MediaPlayer(media);

            // 4. Conectamos el motor a la "pantalla" (MediaView)
            mediaView.setMediaPlayer(mediaPlayer);

            // 5. Hacemos visible el MediaView
            mediaView.setVisible(true);
            mediaView.setManaged(true);

            // Opcional: Auto-reproducir cuando carga
            // mediaPlayer.setAutoPlay(true);

            System.out.println("Video cargado: " + item.name());

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
            mediaPlayer.seek(Duration.ZERO); // Forma abreviada de Duration.ofSeconds(0.0)
        }
    }

    // Métdo vital para evitar fugas de memoria (memory leaks) o audios superpuestos
    public void clear() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // Libera los recursos del motor
            mediaPlayer = null;
        }
        //mediaView.setImage(null); // Limpiamos la vista
        mediaView.setVisible(false);
        mediaView.setManaged(false);
    }
}