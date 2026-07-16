package org.dsf.imagemirador.Service;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import org.dsf.imagemirador.Dto.MediaItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileScannerService {

    private static final List<String> SUPPORTED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif", ".bmp");

    public List<MediaItem> openMethod(Window window) {
        //cambiado filechooser por directorychooser, por ahora está bien
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecciona tu carpeta con archivos uwu");

        // Abre el diálogo para elegir la carpeta, aviso que testeando veo que NO se actualiza en tiempo real; no muestra imágenes nuevas y las que se quiten dan blanco,
        // pero siguen ocupando el espacio en el array, capaaaz con un "refresh" cada tantito se resuelva? Más adelante veré
        File selectedDirectory = directoryChooser.showDialog(window);

        // carpeta vacía, devuelve array vacío
        if (selectedDirectory == null) {
            return new ArrayList<>();
        }

        return scanDirectory(selectedDirectory.toPath());
    }

    private List<MediaItem> scanDirectory(Path directoryPath) {
        // esto de acá es para ver solamente archivos superficiales sin entrar a subcarpetitas, por ahora está bien pero más adelante podríamos cambiarlo
        // a Files.walk para explorar subdirectorios
        try (Stream<Path> paths = Files.list(directoryPath)) {
            return paths
                    .filter(Files::isRegularFile) // mira si el archivo está ahi en la superficie nomás
                    .filter(this::isSupportedFile) // y acá filtra dentro del directorio
                    .map(path -> new MediaItem(
                            path.toUri().toString(),
                            path.getFileName().toString(),
                            "IMAGE"
                    ))
                    .collect(Collectors.toList()); // Lo convierte a List<MediaItem>
        } catch (IOException e) {
            System.err.println("Ehmm... rrror al leer la carpeta: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private boolean isSupportedFile(Path path) {
        // testeando aprendí que las extensiones pueden ser mayúsculas, y eso hace que dejen de ser aceptadas.
        // Esto lo arreglamos haciendo que todito se vuelva minúscula y listoo.
        String fileName = path.getFileName().toString().toLowerCase();
        return SUPPORTED_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }
}