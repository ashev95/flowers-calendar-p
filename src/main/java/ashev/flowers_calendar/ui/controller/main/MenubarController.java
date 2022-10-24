package ashev.flowers_calendar.ui.controller.main;

import ashev.flowers_calendar.Singleton;
import ashev.flowers_calendar.ui.controller.about.AboutController;
import ashev.flowers_calendar.ui.controller.light_type.LightTypeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MenubarController {

    private Stage parentStage;
    private MainController parentController;

    @FXML private MenuBar menubar;
    @FXML private MenuItem menuItemExit;
    @FXML private MenuItem menuItemAbout;
    @FXML private MenuItem menuItemLightType;

    public void init(Stage stage, MainController mainController) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        this.parentStage = stage;
        this.parentController = mainController;
        menuItemLightType.setOnAction(event -> {
            try {
                parentController.resetFormAndSelection();
                final FXMLLoader lightTypeDialogLoader = new FXMLLoader(getClass().getResource("/fxml/light_type/main.fxml"), Singleton.getInstance().getFxBundle());
                final ScrollPane lightTypeDialogPane = lightTypeDialogLoader.load();
                final LightTypeController lightTypeController = lightTypeDialogLoader.getController();
                lightTypeController.init(mainController);
                final Stage stage1 = new Stage();
                stage1.getIcons().add(Singleton.getInstance().getIcon());
                stage1.setTitle(Singleton.getInstance().getAppBundle().getString("light_type_dialog.title"));
                stage1.initModality(Modality.WINDOW_MODAL);
                stage1.initOwner(parentStage);
                stage1.setScene(new Scene(lightTypeDialogPane, Double.parseDouble(Singleton.getInstance().getAppBundle().getString("light_type_dialog.width")), Double.parseDouble(Singleton.getInstance().getAppBundle().getString("light_type_dialog.height"))));
                stage1.show();
            } catch (IOException e) {
                if (log.isErrorEnabled()) {
                    log.error(e.getMessage(), e);
                }
            }
        });
        menuItemAbout.setOnAction(event -> {
            try {
                final FXMLLoader aboutDialogLoader = new FXMLLoader(getClass().getResource("/fxml/about/main.fxml"), Singleton.getInstance().getFxBundle());
                final ScrollPane aboutDialogPane = aboutDialogLoader.load();
                final AboutController aboutController = aboutDialogLoader.getController();
                aboutController.init();
                final Stage stage1 = new Stage();
                stage1.getIcons().add(Singleton.getInstance().getIcon());
                stage1.setTitle(Singleton.getInstance().getAppBundle().getString("about_dialog.title"));
                stage1.initModality(Modality.WINDOW_MODAL);
                stage1.initOwner(parentStage);
                stage1.setScene(new Scene(aboutDialogPane, Double.parseDouble(Singleton.getInstance().getAppBundle().getString("about_dialog.width")), Double.parseDouble(Singleton.getInstance().getAppBundle().getString("about_dialog.height"))));
                stage1.show();
            } catch (IOException e) {
                if (log.isErrorEnabled()) {
                    log.error(e.getMessage(), e);
                }
            }
        });
        menuItemExit.setOnAction(event -> parentController.exit());
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void setWidth(double width) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        menubar.setPrefWidth(width);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

}
