//esto es para las miniaturas
package org.dsf.imagemirador.Service;

import javafx.application.Platform;
import javafx.scene.image.Image;
import org.dsf.imagemirador.Dto.MediaItem;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ThumbnailService {

    // este Executor hace un hilo virtual para CADA tarea
    private final ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public void loadThumbnailAsync(MediaItem item, Consumer<Image> onLoaded) {
        // Esto toma en cuenta que es una imágen nomás, porquee para videos es otro quilombo para más adelante
        if (item == null || item.path().toLowerCase().endsWith(".mp4") || item.path().toLowerCase().endsWith(".mov")) {
            return;
        }

        // asignamos la tarea al hilo virtual que se hizo
        virtualExecutor.submit(() -> {

            // estos parámetros son 100x100 píxeles, requestedHeight, preserveRatio, smooth, backgroundLoading respectivamente,el último es false porque ya estamos en hilo secundario
            Image thumbnail = new Image(item.path(), 100, 100, true, true, false);

            //esto toma el resultado y lo envía de vuelta al hilo de la pantalla, dicen que la interfaz solo se tiene que modificar por el hilo principal
            Platform.runLater(() -> {
                onLoaded.accept(thumbnail);
            });
        });
    }

    // Esto es para apagarr cuando se cierra la app
    public void shutdown() {
        virtualExecutor.shutdown();
    }
}