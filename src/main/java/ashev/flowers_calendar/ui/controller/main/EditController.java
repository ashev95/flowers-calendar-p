package ashev.flowers_calendar.ui.controller.main;

import ashev.flowers_calendar.Singleton;
import ashev.flowers_calendar.db.entity.Flower;
import ashev.flowers_calendar.db.entity.LightType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EditController {

    private MainController parentController;

    @FXML private TextField tFldName;
    @FXML private TextField tFldDescription;
    @FXML private ChoiceBox cBoxLightType;
    @FXML private TextField tFldWaterAmount;
    @FXML private TextArea tAreaComment;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    public void init(MainController mainController) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        this.parentController = mainController;
        cBoxLightType.setConverter(new StringConverter<LightType>() {
            @Override
            public String toString(LightType lightType) {
                return lightType.getName();
            }
            @Override
            public LightType fromString(String string) {
                return Singleton.getInstance().getLightTypes().stream()
                        .filter(lightType -> lightType.getName().equals(string))
                        .findFirst()
                        .get();
            }
        });
        resetItems();
        btnSave.setOnAction(event -> parentController.save());
        btnCancel.setOnAction(event -> parentController.resetFormAndSelection());
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void resetItems() {
        cBoxLightType.setItems(FXCollections.observableArrayList(Singleton.getInstance().getLightTypes()));
    }

    public void resetForm() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tFldName.setText(null);
        tFldDescription.setText(null);
        cBoxLightType.setValue(null);
        tFldWaterAmount.setText(null);
        tAreaComment.setText(null);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public Flower getFlowerFromFields() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        final Flower flower = new Flower();
        flower.setName(tFldName.getText());
        flower.setDescription(tFldDescription.getText());
        flower.setLightType((LightType) cBoxLightType.getValue());
        flower.setWaterAmount(tFldWaterAmount.getText());
        flower.setComment(tAreaComment.getText());
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return flower;
    }

    public void flowerToFields(Flower flower) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tFldName.setText(flower.getName());
        tFldDescription.setText(flower.getDescription());
        cBoxLightType.setValue(Singleton.getInstance().getLightTypes().stream()
                .filter(lightType -> lightType.getName().equals(flower.getLightType().getName()))
                .findFirst()
                .get());
        tFldWaterAmount.setText(flower.getWaterAmount());
        tAreaComment.setText(flower.getComment());
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

}
