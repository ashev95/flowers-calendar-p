package ashev.flowers_calendar;

import ashev.flowers_calendar.db.dao.LightTypeDAO;
import ashev.flowers_calendar.db.util.HibernateUtil;
import ashev.flowers_calendar.ui.controller.main.EditController;
import ashev.flowers_calendar.ui.controller.main.FormController;
import ashev.flowers_calendar.ui.controller.main.ImageController;
import ashev.flowers_calendar.ui.controller.main.MainController;
import ashev.flowers_calendar.ui.controller.main.MenubarController;
import ashev.flowers_calendar.ui.controller.main.ReadController;
import ashev.flowers_calendar.ui.controller.main.TableController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.io.File;
import java.util.ResourceBundle;


@Slf4j
public class Main extends Application {

	@Override
    public void start(Stage stage) throws Exception {
		if (log.isInfoEnabled()) {
			log.info("App is started");
		}

        final ResourceBundle appBundle = ResourceBundle.getBundle("app-bundle" + File.separatorChar + "app", new UTF8Control());
        Singleton.getInstance().setAppBundle(appBundle);

        final ResourceBundle fxBundle = ResourceBundle.getBundle("fx-bundle" + File.separatorChar + "fx", new UTF8Control());
        Singleton.getInstance().setFxBundle(fxBundle);

        final Image icon = new Image(ImageController.class.getResourceAsStream("/image/icon.png"));
        Singleton.getInstance().setIcon(icon);

        //check database connection
        final SessionFactory sessionFactory = HibernateUtil.initSessionFactory();
        if (sessionFactory == null) {
            throw new RuntimeException("Can't initialize database connection");
        }

        //prepare UI
        final FXMLLoader menubarLoader = new FXMLLoader(getClass().getResource("/fxml/main/menubar.fxml"), fxBundle);
        final Pane menubarPane = menubarLoader.load();
        final MenubarController menubarController = menubarLoader.getController();

        final FXMLLoader imageLoader = new FXMLLoader(getClass().getResource("/fxml/main/image.fxml"), fxBundle);
        final Pane imagePane = imageLoader.load();
        final ImageController imageController = imageLoader.getController();

        final FXMLLoader tableLoader = new FXMLLoader(getClass().getResource("/fxml/main/table.fxml"), fxBundle);
        final Pane tablePane = tableLoader.load();
        final TableController tableController = tableLoader.getController();

        final FXMLLoader formLoader = new FXMLLoader(getClass().getResource("/fxml/main/form.fxml"), fxBundle);
        final Pane formPane = formLoader.load();
        final FormController formController = formLoader.getController();

        final FXMLLoader editPaneLoader = new FXMLLoader(getClass().getResource("/fxml/main/edit_pane.fxml"), fxBundle);
        final Pane editPane = editPaneLoader.load();
        final EditController editController = editPaneLoader.getController();

        final FXMLLoader readPaneLoader = new FXMLLoader(getClass().getResource("/fxml/main/read_pane.fxml"), fxBundle);
        final Pane readPane = readPaneLoader.load();
        final ReadController readController = readPaneLoader.getController();

        //build UI
        final 	FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("/fxml/main/main.fxml"), fxBundle);
        final ScrollPane mainPane = mainPaneLoader.load();
        final MainController mainController = mainPaneLoader.getController();

        //initialize UI (order is important)
        Singleton.getInstance().setLightTypes(LightTypeDAO.fetchAll());

        menubarController.init(stage, mainController);
        tableController.init(mainController);
        imageController.init(stage, mainController);
        formController.init(editPane, readPane, mainController);

        mainController.init(stage, menubarPane, tablePane, imagePane, formPane,
                editController, readController, tableController, formController, menubarController, imageController);

        editController.init(mainController);
        readController.init(mainController);

        //show scene
        final Scene scene = new Scene(mainPane, Double.parseDouble(appBundle.getString("main_window.width")) + Const.MARGIN_DEFAULT, Double.parseDouble(appBundle.getString("main_window.height")) + Const.MARGIN_DEFAULT);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setTitle(appBundle.getString("main_window.title"));
        stage.resizableProperty().setValue(false);
        stage.setOnCloseRequest(event -> {
            try {
                HibernateUtil.shutdown();
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error(e.getMessage(), e);
                }
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
