-  08072026 Tommy
  - Hecho  
  - Refactorizado: MediaItem DTO y FileScannerService desde MainController.
  - Añadidas nuevos formatos (incluyendo .gif y .bmp).

- 15072026 K
  - Agregue funcionalidad para cerrar la imagen, rotar, zoom, y restaurar
  - Reestructure el fxml para que soporte transformaciones
  - Cree el paquete Viewer, en un futuro se puede poner el videoViewer dentro por ejemplo

  - 15072026 K
    - Agregue funcionalidad para cerrar la imagen, rotar, zoom, y restaurar 
    - Reestructure el fxml para que soporte transformaciones 
    - Cree el paquete Viewer, en un futuro se puede poner el videoViewer dentro por ejemplo

- 16072026 Tommy 
  - Cambiado filechooser -> directoryChooser. Posible expansión a futuro para "ver" subcarpetas, más allá del registro superficial que tiene ahora.
  - Añadidos mensajes de consola mostrando el índice de la imágen (dentro de la carpeta seleccionada).
  - Añadido una transformación a lower case todos los archivos dentro de la carpeta seleccionada porque dog.jpg ≠ Dog.JPG.
  - Añadido un poquito de error handling en FileScannerService.

- 30072026 Tommy
  - Refactorización de MainController. Invoca los methods del nuevo controlador imageViewer.

- 05082026 Tommy
  - Añadido controller MediaViewer para la reproducción de .mp4. 
  - Ajustados formatos aceptables en el FileScannerService.
  - Añadida la base de interacción entre los controllers main y mediaViewer.
  - Ajustado main-view.fxml para mediaWindow.

- 06082026 Tommy
  - Ajustado MainController para trabajar correctamente con formato .mp4. 
  - Visualización de videos primitiva lista!
  - Añadido switch para imágenes/videos, deja visualizar con el mismo botón (file -> open) imágenes y videos desde la misma carpeta en la misma ventana.

- 07082026 K
  - Agregado boton para Pin (always on top)
  - Cambiado directoryChooser -> filechooser otra vez 
  - Añadido persistencia de carpeta al abrir un archivo 
  - Refactorizacion de MainController, se separo la logica de navegacion en NavigatorView librando al controller de ocuparse de distintos estados 
  - Se hizo que el filechooser trabaje de forma asincronica

- 07082026 Tommy
  - Creado ThumbnailService para generar miniaturas rápidas, usando virtual threads para agilizar el consumo de recursos.
  - Modificado el main-view.fxml para implementar cuadrícula con thumbnails y botón de activar/desactivar visibilidad de dicha galería.
  - Ajustado MainController para habilitar las 2 previas implementaciones. Notese que el MainController estaría requiriendo una nueva refactorización dentro de poco.

- 08082026 K 
  - Agregada la view de galería, hice que sea como un overlay transparente

- 13082026 Tommy
  - Refactorización/Integración las 2 ramas developer de cada integrante en 1.
  
- 18082026 Tommy
  - Refactorización de MainController.
  - Restablecido GalleryController y gallery-view.fxml.

- 19082026 Tommy
  - Desarrollado GalleryController y extendido gallery-view.fxml
