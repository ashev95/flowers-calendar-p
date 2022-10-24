package ashev.flowers_calendar.ui.controller.about;

import ashev.flowers_calendar.Singleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AboutController {

    @FXML private Label lAppName;
    @FXML private Label lAppJDK;

    public void init() {
        lAppName.setText(Singleton.getInstance().getAppBundle().getString("app.name"));
        lAppJDK.setText(Singleton.getInstance().getAppBundle().getString("app.jdk"));
    }

}
