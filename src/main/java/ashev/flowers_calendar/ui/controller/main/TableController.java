package ashev.flowers_calendar.ui.controller.main;

import ashev.flowers_calendar.Singleton;
import ashev.flowers_calendar.Utils;
import ashev.flowers_calendar.db.dao.FlowerRowDAO;
import ashev.flowers_calendar.db.entity.FlowerRow;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class TableController {

    private MainController parentController;

    @FXML private TableView tableview;

    public void init(MainController mainController) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        this.parentController = mainController;
        final TableColumn colName = new TableColumn(Singleton.getInstance().getAppBundle().getString("table_controller.table_column.name.title"));
        colName.prefWidthProperty().bind(new SimpleDoubleProperty(Double.parseDouble(Singleton.getInstance().getAppBundle().getString("table_controller.table_column.name.pref_width"))));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        final TableColumn colDate = new TableColumn(Singleton.getInstance().getAppBundle().getString("table_controller.table_column.pourDate.title"));
        colDate.prefWidthProperty().bind(new SimpleDoubleProperty(Double.parseDouble(Singleton.getInstance().getAppBundle().getString("table_controller.table_column.pourDate.pref_width"))));
        colDate.setCellValueFactory((Callback<TableColumn.CellDataFeatures<FlowerRow, String>, ObservableValue<String>>) flowerRow -> {
            final SimpleStringProperty property = new SimpleStringProperty();
            final Date pourDate = flowerRow.getValue().getPourDate();
            if (pourDate != null) {
                property.setValue(Utils.format(pourDate));
            }
            return property;
        });
        tableview.getColumns().addAll(colName, colDate);
        tableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final FlowerRow flowerRow = (FlowerRow) newValue;
                parentController.selectFromUI(flowerRow);
                if (log.isDebugEnabled()) {
                    log.debug("selected FlowerRow's id: {}", flowerRow.getId());
                }
            }
        });
        refreshTable();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void refreshTable() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tableview.setItems(FXCollections.observableArrayList(FlowerRowDAO.fetchAll()));
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void clearSelection() {
        tableview.getSelectionModel().clearSelection();
    }

    public FlowerRow findFlowerRow(long id) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        final ObservableList<FlowerRow> items = tableview.getItems();
        final FlowerRow flowerRow = items.stream()
                .filter(flowerRow1 -> flowerRow1.getId() == id)
                .findFirst()
                .get();
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return flowerRow;
    }

    public void select(FlowerRow flowerRow) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        tableview.getSelectionModel().select(flowerRow);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }
    
}
