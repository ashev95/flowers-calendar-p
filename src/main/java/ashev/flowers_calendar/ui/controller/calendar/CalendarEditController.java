package ashev.flowers_calendar.ui.controller.calendar;

import ashev.flowers_calendar.Utils;
import ashev.flowers_calendar.db.entity.Calendar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Slf4j
public class CalendarEditController {

    @FXML private DatePicker dPkrPourDate;
    @FXML private TextArea tAreaNote;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private CalendarController parentController;

    public void init(CalendarController calendarController) {
        this.parentController = calendarController;
        btnSave.setOnAction(event -> parentController.save());
        btnCancel.setOnAction(event -> parentController.resetFormAndSelection());
    }

    public void resetForm() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        dPkrPourDate.setValue(null);
        tAreaNote.setText(null);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public Calendar getFlowerFromFields() throws ParseException {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        final Calendar calendar = new Calendar();
        calendar.setPourDate(Utils.localDateToDate(dPkrPourDate.getValue()));
        calendar.setNote(tAreaNote.getText());
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return calendar;
    }

    public void flowerToFields(Calendar calendar) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        dPkrPourDate.setValue(Utils.dateToLocalDate(calendar.getPourDate()));
        tAreaNote.setText(calendar.getNote());
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

}
