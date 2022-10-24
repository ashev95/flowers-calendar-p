package ashev.flowers_calendar.ui.controller.calendar;

import ashev.flowers_calendar.Utils;
import ashev.flowers_calendar.db.entity.Calendar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalendarReadController {

    @FXML private Label tLblPourDate;
    @FXML private Label tLblNote;
    @FXML private Button btnNew;
    @FXML private Button btnEdit;
    @FXML private Button btnRemove;
    
    private CalendarController parentController;

    public void init(CalendarController calendarController) {
        this.parentController = calendarController;
        btnNew.setOnAction(event -> parentController.createNew());
        btnEdit.setOnAction(event -> parentController.switchToEdit());
        btnRemove.setOnAction(event -> parentController.remove());
    }
    
    public void loadForm(Calendar calendar) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tLblPourDate.setText(Utils.format(calendar.getPourDate()));
        tLblNote.setText(calendar.getNote());
        updateButtonActivation(true);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void resetForm() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tLblPourDate.setText(null);
        tLblNote.setText(null);
        updateButtonActivation(false);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void updateButtonActivation(boolean selected) {
        btnEdit.setDisable(!selected);
        btnRemove.setDisable(!selected);
    }

}
