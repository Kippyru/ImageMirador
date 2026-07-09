package org.dsf.imagemirador.Controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Window;
import org.dsf.imagemirador.Dto.MediaItem;
import org.dsf.imagemirador.Service.FileScannerService;

import java.util.Optional;

public class MainController {

    @FXML
    private ImageView imageWindow ;

    private final FileScannerService fileScannerService = new FileScannerService();

    @FXML
    public void initialize() {
        imageWindow .setPreserveRatio(true);   //esto es para que no se deforme toda la imagen, osea preservar el ratio
        imageWindow .setSmooth(true);       //y esto es para filtrar creo? noc, en la doc de oracle dice algo de eso
        //sii creo abria que sacarlo y no usar filtro ya que podria alterar la imagen original.
        //testing no me mostró ninguna alteración importante hasta ahora (08-07-26). Queda presente el filtro por ahora.


        //esto es para que la imagen se ajuste a su contenedor
        imageWindow .sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                javafx.scene.layout.Region contenedor = (javafx.scene.layout.Region) imageWindow .getParent();
                imageWindow .fitWidthProperty().bind(contenedor.widthProperty());
                imageWindow .fitHeightProperty().bind(contenedor.heightProperty());
            }
        });
    }

    //aca el boton del menu para abrir, tengo que hacer que pueda abrir otras imagenes
    @FXML
    public void openMethod() {
        System.out.println("Snif snif SNIIIF a ver busco tu cuestión...");
        Window window = ventanaImagen.getScene().getWindow();
        Optional<MediaItem> item = fileScannerService.openMethod(window);

        item.ifPresent(mediaItem -> {
            Image image = new Image(mediaItem.path());
            ventanaImagen.setImage(image);
            System.out.println("Con un guau y un miau, lo encontré! Acá está uwu");
        });

    }

    //y este lo cierra dah, tengo que ver si cierra la app, o cierra la imagen, noc
    //bah puse el shortcut ctrl w porq imagino cierra la imagen y no la app
    public void closeMethod() {
        System.out.println("closeada tu wea >:3c");
    }
}
