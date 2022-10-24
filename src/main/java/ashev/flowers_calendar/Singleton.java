package ashev.flowers_calendar;

import ashev.flowers_calendar.db.entity.LightType;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ResourceBundle;

public class Singleton {

    private static Singleton instance;

    @Getter
    @Setter
    private List<LightType> lightTypes;

    @Getter
    @Setter
    private ResourceBundle appBundle;

    @Getter
    @Setter
    private ResourceBundle fxBundle;

    @Getter
    @Setter
    private Image icon;

    public synchronized static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

}
