package ashev.flowers_calendar.ui.controller.main;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FormController {

    @FXML private Pane formPane;

    private MainController parentController;
    private Pane editPane;
    private Pane readPane;

    public void init(Pane editPane, Pane readPane, MainController mainController) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        this.editPane = editPane;
        this.readPane = readPane;
        this.parentController = mainController;
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void setReadMode() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        if (!formPane.getChildren().contains(readPane)) {
            formPane.getChildren().clear();
            formPane.getChildren().add(readPane);
        }
        parentController.updateReadVision();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void setEditMode() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        if (!formPane.getChildren().contains(editPane)) {
            formPane.getChildren().clear();
            formPane.getChildren().add(editPane);
        }
        parentController.updateEditVision();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

}
