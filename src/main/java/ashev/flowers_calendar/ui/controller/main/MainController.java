package ashev.flowers_calendar.ui.controller.main;

import ashev.flowers_calendar.Const;
import ashev.flowers_calendar.Singleton;
import ashev.flowers_calendar.Utils;
import ashev.flowers_calendar.db.dao.CalendarDAO;
import ashev.flowers_calendar.db.dao.FlowerDAO;
import ashev.flowers_calendar.db.entity.Flower;
import ashev.flowers_calendar.db.entity.FlowerRow;
import ashev.flowers_calendar.db.util.HibernateUtil;
import ashev.flowers_calendar.ui.controller.calendar.CalendarController;
import ashev.flowers_calendar.ui.controller.calendar.CalendarEditController;
import ashev.flowers_calendar.ui.controller.calendar.CalendarReadController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MainController {

    private FlowerUIState flowerUIState;

    @FXML private GridPane gridPane;

    private EditController editController;
    private ReadController readController;
    private TableController tableController;
    private FormController formController;
    private ImageController imageController;
    private Stage parentStage;

    public void init(Stage parentStage, Pane menubarPane, Pane tablePane, Pane imagePane, Pane formPane,
                     EditController editController, ReadController readController,
                     TableController tableController, FormController formController,
                     MenubarController menubarController, ImageController imageController) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }

        flowerUIState = new FlowerUIState();

        this.parentStage = parentStage;
        this.editController = editController;
        this.readController = readController;
        this.tableController = tableController;
        this.formController = formController;
        this.imageController = imageController;

        resetFormAndSelection();

        parentStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double minWidth = Double.parseDouble(Singleton.getInstance().getAppBundle().getString("main_window.width"));
            minWidth -= (Const.MARGIN_DEFAULT + 5 * Const.BORDER_DEFAULT);
            if (newValue.doubleValue() > minWidth) {
                menubarController.setWidth(newValue.doubleValue() - (2 * Const.MARGIN_DEFAULT - 2 * Const.BORDER_DEFAULT));
            } else {
                menubarController.setWidth(minWidth);
            }
        });

        gridPane.add(menubarPane, 0, 0);
        GridPane.setColumnSpan(menubarPane, 3);
        gridPane.add(tablePane, 0, 1);
        GridPane.setValignment(tablePane, VPos.TOP);
        GridPane.setHalignment(tablePane, HPos.LEFT);
        GridPane.setMargin(tablePane, new Insets(Const.MARGIN_DEFAULT, Const.MARGIN_EMPTY, Const.MARGIN_EMPTY, Const.MARGIN_DEFAULT));
        gridPane.add(imagePane, 1, 1);
        GridPane.setValignment(imagePane, VPos.TOP);
        GridPane.setHalignment(imagePane, HPos.LEFT);
        GridPane.setMargin(imagePane, new Insets(Const.MARGIN_DEFAULT, Const.MARGIN_EMPTY, Const.MARGIN_EMPTY, Const.MARGIN_DEFAULT));
        gridPane.add(formPane, 2, 1);
        GridPane.setValignment(formPane, VPos.TOP);
        GridPane.setHalignment(formPane, HPos.LEFT);
        GridPane.setMargin(formPane, new Insets(Const.MARGIN_DEFAULT, Const.MARGIN_DEFAULT, Const.MARGIN_EMPTY, Const.MARGIN_DEFAULT));

        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void selectFromUI(FlowerRow flowerRow) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        final Flower flower = FlowerDAO.find(flowerRow.getId());
        try {
            formController.setReadMode();
            readController.resetForm();
            imageController.resetImage();
            flowerUIState.selectFlowerRow(flowerRow, flower);
            readController.loadForm(flower);
            imageController.show(flower.getImageBytes());
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            clearSelection();
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void resetFormAndSelection() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        formController.setReadMode();
        readController.resetForm();
        imageController.resetImage();
        clearSelection();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void clearSelection() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tableController.clearSelection();
        flowerUIState.reset();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void remove() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        final Flower flower = flowerUIState.getCurrentFlower();
        if (flower == null) {
            return;
        }
        if (CalendarDAO.hasByFlower(flower)) {
            if (!Utils.showYesNoDialog(Singleton.getInstance().getAppBundle().getString("main_controller.alert.has_children.title"),
                    Singleton.getInstance().getAppBundle().getString("main_controller.alert.has_children.header_text"), "")) {
                return;
            }
        }
        FlowerDAO.remove(flowerUIState.getCurrentFlower());
        resetFormAndSelection();
        tableController.refreshTable();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void refreshTable() {
        final Flower currentFlower = flowerUIState.getCurrentFlower();
        resetFormAndSelection();
        tableController.refreshTable();
        selectFromModel(currentFlower);
    }

    private void selectFromModel(Flower flower) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        try {
            FlowerRow flowerRow = tableController.findFlowerRow(flower.getId());
            tableController.select(flowerRow);
            flowerUIState.selectFlowerRow(flowerRow, flower);
        } catch (Exception e){
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            clearSelection();
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void createNew() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        formController.setEditMode();
        editController.resetForm();
        imageController.resetImage();
        clearSelection();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void switchToEdit() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        formController.setEditMode();
        editController.resetForm();
        resetLightTypeListViewItems();
        imageController.resetImage();
        editController.flowerToFields(flowerUIState.getCurrentFlower());
        try {
            imageController.show(flowerUIState.getCurrentFlower().getImageBytes());
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void resetLightTypeListViewItems() {
        editController.resetItems();
    }

    public void save() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        final Flower flower = editController.getFlowerFromFields();
        try {
            flower.setImage(imageController.getSelectedImageBlob());
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            return;
        }
        if (flowerUIState.isNew()) {
            FlowerDAO.save(flower);
        } else {
            flower.setId(flowerUIState.getCurrentFlower().getId());
            FlowerDAO.update(flower);
        }
        resetFormAndSelection();
        tableController.refreshTable();
        selectFromModel(flower);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void updateReadVision() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        imageController.updateReadVision();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void updateEditVision() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        imageController.updateEditVision();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void selectImage() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        try {
            imageController.chooseImage();
            imageController.display();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private class FlowerUIState {

        private Flower flower;
        private FlowerRow flowerRow;

        public Flower getCurrentFlower() {
            if (log.isDebugEnabled()) {
                log.debug("START");
            }
            if (flower == null && flowerRow != null) {
                flower = FlowerDAO.find(flowerRow.getId());
            }
            if (log.isDebugEnabled()) {
                log.debug("END");
            }
            return flower;
        }

        public FlowerRow getCurrentFlowerRow() {
            if (log.isDebugEnabled()) {
                log.debug("START");
            }
            if (log.isDebugEnabled()) {
                log.debug("END");
            }
            return flowerRow;
        }

        public void reset() {
            if (log.isDebugEnabled()) {
                log.debug("START");
            }
            this.flower = null;
            this.flowerRow = null;
            if (log.isDebugEnabled()) {
                log.debug("END");
            }
        }

        public void selectFlowerRow(FlowerRow flowerRow) {
            if (log.isDebugEnabled()) {
                log.debug("START");
            }
            this.flowerRow = flowerRow;
            if (log.isDebugEnabled()) {
                log.debug("END");
            }
        }

        public void selectFlowerRow(FlowerRow flowerRow, Flower flower) {
            if (log.isDebugEnabled()) {
                log.debug("START");
            }
            this.flowerRow = flowerRow;
            this.flower = flower;
            if (log.isDebugEnabled()) {
                log.debug("END");
            }
        }

        public boolean isNew() {
            if (log.isDebugEnabled()) {
                log.debug("START");
            }
            if (log.isDebugEnabled()) {
                log.debug("END");
            }
            return flowerRow == null && flower == null;
        }

    }

    public void exit() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        try {
            HibernateUtil.shutdown();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        Platform.exit();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void showCalendar() {
        final Flower flower = flowerUIState.getCurrentFlower();
        if (flower == null) {
            return;
        }
        try {
            final FXMLLoader calendarReadDialogLoader = new FXMLLoader(getClass().getResource("/fxml/calendar/read_pane.fxml"), Singleton.getInstance().getFxBundle());
            final Pane calendarReadPane = calendarReadDialogLoader.load();
            final CalendarReadController calendarReadController = calendarReadDialogLoader.getController();

            final FXMLLoader calendarEditDialogLoader = new FXMLLoader(getClass().getResource("/fxml/calendar/edit_pane.fxml"), Singleton.getInstance().getFxBundle());
            final Pane calendarEditPane = calendarEditDialogLoader.load();
            final CalendarEditController calendarEditController = calendarEditDialogLoader.getController();

            final FXMLLoader calendarDialogLoader = new FXMLLoader(getClass().getResource("/fxml/calendar/main.fxml"), Singleton.getInstance().getFxBundle());
            final ScrollPane calendarDialogPane = calendarDialogLoader.load();
            final CalendarController calendarController = calendarDialogLoader.getController();
            calendarController.init(this, calendarReadPane, calendarEditPane, calendarReadController, calendarEditController, flower);
            calendarReadController.init(calendarController);
            calendarEditController.init(calendarController);

            final Stage stage1 = new Stage();
            stage1.getIcons().add(Singleton.getInstance().getIcon());
            stage1.setTitle(Singleton.getInstance().getAppBundle().getString("calendar_dialog.title"));
            stage1.initModality(Modality.WINDOW_MODAL);
            stage1.initOwner(parentStage);
            stage1.setScene(new Scene(calendarDialogPane, Double.parseDouble(Singleton.getInstance().getAppBundle().getString("calendar_dialog.width")), Double.parseDouble(Singleton.getInstance().getAppBundle().getString("calendar_dialog.height"))));
            stage1.show();
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
