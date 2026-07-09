package org.dsf.imagemirador.Service;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.dsf.imagemirador.Dto.MediaItem;

import java.io.File;
import java.util.Optional;

public class FileScannerService {

    public Optional<MediaItem> abrirImagen(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Snif snif ¿qué archivo querés ver?");

        // Esto es para filtrar, deja esas extensiones nomásh, sino te muestra extensiones no disponibles todavía guau guau grrr
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.bmp", "*.gif") //el * es para aceptar cualquier nombre, mientras termine con .jpg/png/.gif
                // te va a mostrar el archivo
                // Taaambién le añadí el formato bmp, de onda nomás jaja
        );

        // Abroo el coso de Windows para elegir la wea a mostrar
        File file = fileChooser.showOpenDialog(window);

        if (file != null) {

            String path = file.toURI().toString(); //JavaFX Image quiere si o si el formato ese URL (file://..)
            String name = file.getName();
            String type = "IMAGE";

            return Optional.of(new MediaItem(path, name, type));
        }

        // No elegiste nada bueno no retorna nada c:
        return Optional.empty();
    }
}