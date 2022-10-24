package ashev.flowers_calendar.db.validation;

public class AppValidationException extends RuntimeException {
    public AppValidationException(String errorMessage) {
        super(errorMessage);
    }
}
