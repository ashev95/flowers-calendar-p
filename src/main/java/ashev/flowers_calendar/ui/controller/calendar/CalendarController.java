package ashev.flowers_calendar.ui.controller.calendar;

import ashev.flowers_calendar.Singleton;
import ashev.flowers_calendar.Utils;
import ashev.flowers_calendar.db.dao.CalendarDAO;
import ashev.flowers_calendar.db.entity.Calendar;
import ashev.flowers_calendar.db.entity.Flower;
import ashev.flowers_calendar.ui.controller.main.MainController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class CalendarController {

    @FXML private TableView tableview;
    @FXML private Pane formPane;

    private Pane readPane;
    private Pane editPane;
    private CalendarEditController editController;
    private CalendarReadController readController;
    private Flower flower;

    private Calendar selected;

    private MainController mainController;

    public void init(MainController mainController, Pane readPane, Pane editPane,
                     CalendarReadController readController, CalendarEditController editController, Flower flower) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }

        this.mainController = mainController;
        this.readPane = readPane;
        this.editPane = editPane;
        this.readController = readController;
        this.editController = editController;
        this.flower = flower;

        resetFormAndSelection();

        final TableColumn colDate = new TableColumn(Singleton.getInstance().getAppBundle().getString("calendar_controller.table_column.pour_date.title"));
        colDate.prefWidthProperty().bind(new SimpleDoubleProperty(Double.parseDouble(Singleton.getInstance().getAppBundle().getString("calendar_controller.table_column.pour_date.pref_width"))));
        colDate.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Calendar, String>, ObservableValue<String>>) calendar -> {
            final SimpleStringProperty property = new SimpleStringProperty();
            final Date pourDate = calendar.getValue().getPourDate();
            if (pourDate != null) {
                property.setValue(Utils.format(calendar.getValue().getPourDate()));
            }
            return property;
        });
        final TableColumn colNote = new TableColumn(Singleton.getInstance().getAppBundle().getString("calendar_controller.table_column.note.title"));
        colNote.prefWidthProperty().bind(new SimpleDoubleProperty(Double.parseDouble(Singleton.getInstance().getAppBundle().getString("calendar_controller.table_column.note.pref_width"))));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        tableview.getColumns().addAll(colDate, colNote);
        tableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final Calendar calendar = (Calendar) newValue;
                selectFromUI(calendar);
                if (log.isDebugEnabled()) {
                    log.debug("selected Calendar's id: {}", calendar.getId());
                }
            }
        });

        refreshTable();

        setReadMode();

        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void setReadMode() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        if (!formPane.getChildren().contains(readPane)) {
            formPane.getChildren().clear();
            formPane.getChildren().add(readPane);
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void setEditMode() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        if (!formPane.getChildren().contains(editPane)) {
            formPane.getChildren().clear();
            formPane.getChildren().add(editPane);
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void refreshTable() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tableview.setItems(FXCollections.observableArrayList(CalendarDAO.fetchByFlower(flower)));
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void selectFromUI(Calendar value) {
        try {
            setReadMode();
            readController.resetForm();
            this.selected = value;
            readController.loadForm(this.selected);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            clearSelection();
        }
    }

    public void resetFormAndSelection() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        setReadMode();
        readController.resetForm();
        clearSelection();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void clearSelection() {
        tableview.getSelectionModel().clearSelection();
        selected = null;
    }

    public void remove() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        CalendarDAO.remove(selected);
        resetFormAndSelection();
        refreshTable();
        mainController.refreshTable();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private void selectFromModel(Calendar calendar) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        try {
            final Calendar calendar1 = findCalendarRow(calendar.getId());
            this.selected = calendar1;
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

    public Calendar findCalendarRow(long id) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        final ObservableList<Calendar> items = tableview.getItems();
        final Calendar calendar = items.stream()
                .filter(calendar1 -> calendar1.getId() == id)
                .findFirst()
                .get();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return calendar;
    }

    public void createNew() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        setEditMode();
        editController.resetForm();
        clearSelection();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void switchToEdit() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        setEditMode();
        editController.resetForm();
        editController.flowerToFields(selected);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void save() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        try {
            final Calendar calendar = editController.getFlowerFromFields();
            calendar.setFlower(flower);
            if (selected == null) {
                CalendarDAO.save(calendar);
            } else {
                calendar.setId(selected.getId());
                CalendarDAO.update(calendar);
            }
            resetFormAndSelection();
            refreshTable();
            selectFromModel(calendar);
            mainController.refreshTable();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

}
