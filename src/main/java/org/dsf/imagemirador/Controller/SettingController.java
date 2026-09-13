package org.dsf.imagemirador.Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.dsf.imagemirador.Dto.AppConfig;
import org.dsf.imagemirador.Service.ConfigService;
import org.dsf.imagemirador.Service.ThemeManager;

public class SettingController {

    @FXML public ComboBox<ThemeManager.Theme> themeCombo;
    @FXML public Label themeLabel;

    @FXML public CheckBox loopCheckBox;
    @FXML public TextField secondsField;

    private ThemeManager themeManager;
    private Stage settingsStage;
    private ConfigService configService;

    public void setThemeManager(ThemeManager themeManager) {
        this.themeManager = themeManager;
        setupUI();
    }

    public void setSettingsStage(Stage stage) {
        this.settingsStage = stage;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
        setupPlaybackUI(); // a configurar la UI de video en cuanto recibe el servicio
    }

    private void setupUI() {
        if (themeManager == null) return;

        themeCombo.getItems().addAll(ThemeManager.Theme.values());
        themeCombo.valueProperty().bindBidirectional(themeManager.themeProperty());
        updateThemeLabel();

        themeManager.themeProperty().addListener((obs, old, newVal) -> {
            updateThemeLabel();
            applyThemeToSettings(newVal);
        });
    }

    private void setupPlaybackUI() {
        if (configService == null) return;

        AppConfig config = configService.getConfig();

        // cargar valores actuales en interfaz
        loopCheckBox.setSelected(config.isAutoplayShortVideos());
        secondsField.setText(String.valueOf(config.getMaxShortVideoSeconds()));

        // guardar automáticamente cuando se hace clic en el CheckBox
        loopCheckBox.selectedProperty().addListener((obs, old, newVal) -> {
            config.setAutoplayShortVideos(newVal);
            configService.saveConfig();
        });

        // guardar cuando se cambia el número
        secondsField.textProperty().addListener((obs, old, newVal) -> {
            if (!newVal.matches("\\d*")) {
                // forzando solo números
                secondsField.setText(newVal.replaceAll("[^\\d]", ""));
            } else if (!newVal.isEmpty()) {
                // guardamos en la config
                int seconds = Integer.parseInt(newVal);
                config.setMaxShortVideoSeconds(seconds);
                configService.saveConfig();
            }
        });
    }


    public void applyThemeToSettings(ThemeManager.Theme theme) {
        if (settingsStage == null) {
            System.out.println("settingsstage es null oh nuuu");
            return;
        }

        Scene scene = settingsStage.getScene();
        if (scene == null) {
            System.out.println("Scene es null");
            return;
        }

        try {
            System.out.println("Aplicando tema a Settings: " + theme.getDisplayName());

            scene.getStylesheets().clear();

            String baseCSS = getClass().getResource("/css/base.css").toExternalForm();
            scene.getStylesheets().add(baseCSS);
            System.out.println("CSS agregado");

            String themeCSS = getClass().getResource(
                    "/css/" + theme.getCssName() + ".css"
            ).toExternalForm();
            scene.getStylesheets().add(themeCSS);
            System.out.println("Tema aplicado: " + themeCSS);

        } catch (Exception e) {
            System.err.println("Error en tema: " + e.getMessage());
            e.printStackTrace();
        }
    }




    private void updateThemeLabel() {
        themeLabel.setText("Current theme: " + themeManager.getCurrentTheme().getDisplayName());
    }
}
