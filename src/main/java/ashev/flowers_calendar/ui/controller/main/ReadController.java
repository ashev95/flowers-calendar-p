package ashev.flowers_calendar.ui.controller.main;

import ashev.flowers_calendar.Singleton;
import ashev.flowers_calendar.db.entity.Flower;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadController {

    private MainController parentController;

    @FXML private Label tLblName;
    @FXML private Label tLblDescription;
    @FXML private Label tLblLightType;
    @FXML private Label tLblWaterAmount;
    @FXML private Label tLblComment;
    @FXML private Button btnNew;
    @FXML private Button btnEdit;
    @FXML private Button btnRemove;
    @FXML private Button btnCalendar;

    public void init(MainController mainController) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        this.parentController = mainController;
        btnNew.setOnAction(event -> parentController.createNew());
        btnEdit.setOnAction(event -> parentController.switchToEdit());
        btnRemove.setOnAction(event -> parentController.remove());
        btnCalendar.setOnAction(event -> parentController.showCalendar());
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void loadForm(Flower flower) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tLblName.setText(flower.getName());
        tLblDescription.setText(flower.getDescription());
        tLblLightType.setText(Singleton.getInstance().getLightTypes().stream()
                .filter(lightType -> lightType.getId() == flower.getLightType().getId())
                .findFirst()
                .get().getName());
        tLblWaterAmount.setText(flower.getWaterAmount());
        tLblComment.setText(flower.getComment());
        updateButtonActivation(true);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void resetForm() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tLblName.setText(null);
        tLblDescription.setText(null);
        tLblLightType.setText(null);
        tLblWaterAmount.setText(null);
        tLblComment.setText(null);
        updateButtonActivation(false);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void updateButtonActivation(boolean selected) {
        btnEdit.setDisable(!selected);
        btnRemove.setDisable(!selected);
        btnCalendar.setDisable(!selected);
    }

}
