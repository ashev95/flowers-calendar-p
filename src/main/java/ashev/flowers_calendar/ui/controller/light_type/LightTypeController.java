package ashev.flowers_calendar.ui.controller.light_type;

import ashev.flowers_calendar.Singleton;
import ashev.flowers_calendar.Utils;
import ashev.flowers_calendar.db.dao.FlowerDAO;
import ashev.flowers_calendar.db.dao.LightTypeDAO;
import ashev.flowers_calendar.db.entity.LightType;
import ashev.flowers_calendar.ui.controller.main.MainController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class LightTypeController {

    @FXML private TextField tFldInput;
    @FXML private ListView listView;
    @FXML private Button btnAdd;
    @FXML private Button btnChange;
    @FXML private Button btnChangeOk;
    @FXML private Button btnChangeCancel;
    @FXML private Button btnRemove;

    private LightType selected;
    private boolean changeMode;

    private MainController mainController;

    public void init(MainController mainController) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        this.mainController = mainController;
        listView.setOnMouseClicked(event -> {
            final String value = (String) listView.getSelectionModel().getSelectedItems().get(0);
            if (value != null) {
                select(value);
                updateButtonActivation(true);
            }
        });
        btnAdd.setOnAction(event -> {
            if (changeMode) {
                return;
            }
            final String inputText = getPreparedTextFieldValue();
            if (inputText.isEmpty()) {
                clearTextField();
                Utils.showWarning(Singleton.getInstance().getAppBundle().getString("light_type_controller.alert.required.title"),
                        Singleton.getInstance().getAppBundle().getString("light_type_controller.alert.required.header_text"), "");
                return;
            }
            final Optional<LightType> first = Singleton.getInstance().getLightTypes().stream()
                    .filter(lightType -> lightType.getName().equals(inputText))
                    .findFirst();
            if (first.isPresent()) {
                Utils.showWarning(Singleton.getInstance().getAppBundle().getString("light_type_controller.alert.exists.title"),
                        Singleton.getInstance().getAppBundle().getString("light_type_controller.alert.exists.header_text"), "");
                return;
            }
                LightTypeDAO.save(new LightType(inputText));
                Singleton.getInstance().setLightTypes(LightTypeDAO.fetchAll());
                refreshListView();
                clearSelection();
                clearTextField();
        });
        btnChange.setOnAction(event -> {
            if (changeMode) {
                return;
            }
            if (selected == null) {
                return;
            }
            setChangeMode(true);
            fillTextField(selected.getName());
        });
        btnChangeOk.setOnAction(event -> {
            if (!changeMode) {
                return;
            }
            final String selectedText = (String) listView.getSelectionModel().getSelectedItems().get(0);
            final String inputText = getPreparedTextFieldValue();
            if (inputText.isEmpty()) {
                clearTextField();
                return;
            }
            if (inputText.equals(selectedText)) {
                setChangeMode(false);
                clearTextField();
                return;
            }
            final String textField = getPreparedTextFieldValue();
            final Optional<LightType> first = Singleton.getInstance().getLightTypes().stream()
                    .filter(lightType -> lightType.getName().equals(textField))
                    .findFirst();
            if (first.isPresent()) {
                Utils.showWarning(Singleton.getInstance().getAppBundle().getString("light_type_controller.alert.exists.title"),
                        Singleton.getInstance().getAppBundle().getString("light_type_controller.alert.exists.header_text"), "");
                return;
            }
            final Optional<LightType> first1 = Singleton.getInstance().getLightTypes().stream()
                    .filter(lightType -> lightType.getName().equals(selectedText))
                    .findFirst();
            if (first1.isPresent()) {
                final LightType lightType = first1.get();
                lightType.setName(inputText);
                LightTypeDAO.update(lightType); // UPDATE ChoiceBox !!!
                Singleton.getInstance().setLightTypes(LightTypeDAO.fetchAll());
                refreshListView();
                clearSelection();
                setChangeMode(false);
                clearTextField();
            }
        });
        btnChangeCancel.setOnAction(event -> {
            if (!changeMode) {
                return;
            }
            setChangeMode(false);
            clearTextField();
        });
        btnRemove.setOnAction(event -> {
            if (changeMode) {
                return;
            }
            final String selectedTest = (String) listView.getSelectionModel().getSelectedItems().get(0);
            if (selectedTest != null && !selectedTest.isEmpty()) {
                final Optional<LightType> first = Singleton.getInstance().getLightTypes().stream()
                        .filter(lightType -> lightType.getName().equals(selectedTest))
                        .findFirst();
                if (first.isPresent()) {
                    final LightType lightType = first.get();
                    if (FlowerDAO.hasByLightType(lightType)) {
                        Utils.showWarning(Singleton.getInstance().getAppBundle().getString("light_type_controller.alert.is_parent.title"),
                                Singleton.getInstance().getAppBundle().getString("light_type_controller.alert.is_parent.header_text"), "");
                        return;
                    }
                    LightTypeDAO.remove(lightType); // UPDATE ChoiceBox !!!
                    Singleton.getInstance().setLightTypes(LightTypeDAO.fetchAll());
                    refreshListView();
                    clearSelection();
                }
            }
        });
        refreshListView();
        clearSelection();
        setChangeMode(false);
        updateButtonActivation(false);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void setChangeMode(boolean value) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        changeMode = value;
        btnAdd.setVisible(!changeMode);
        btnChange.setVisible(!changeMode);
        btnChangeOk.setVisible(changeMode);
        btnChangeCancel.setVisible(changeMode);
        btnRemove.setVisible(!changeMode);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void refreshListView() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        listView.setItems(FXCollections.observableArrayList(Singleton.getInstance().getLightTypes().stream()
                .map(LightType::getName)
                .collect(Collectors.toList())));
        mainController.resetLightTypeListViewItems();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void select(String text) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        selected = Singleton.getInstance().getLightTypes().stream()
                .filter(lightType -> lightType.getName().equals(text))
                .findFirst()
                .get();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void clearSelection() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        listView.getSelectionModel().clearSelection();
        selected = null;
        updateButtonActivation(false);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void fillTextField(String text) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tFldInput.setText(text);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void clearTextField() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tFldInput.setText(null);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private String getPreparedTextFieldValue() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        String res = tFldInput.getText();
        if (res == null) {
            res = "";
        } else {
            res = res.trim();
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return res;
    }

    private void updateButtonActivation(boolean selected) {
        btnChange.setDisable(!selected);
        btnRemove.setDisable(!selected);
    }

}
