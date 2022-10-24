package ashev.flowers_calendar;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class Utils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static String format(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static LocalDate dateToLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date localDateToDate(LocalDate localDate) {
        Date result = null;
        if (localDate != null) {
            final Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            result = Date.from(instant);
        }
        return result;
    }

    public static void showWarning(String title, String header, String content) {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public static boolean showYesNoDialog(String title, String header, String content) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        final Optional<ButtonType> buttonType = alert.showAndWait();
        return  (buttonType.isPresent() && ButtonType.OK.getButtonData().getTypeCode().equals(buttonType.get().getButtonData().getTypeCode()));
    }

}
