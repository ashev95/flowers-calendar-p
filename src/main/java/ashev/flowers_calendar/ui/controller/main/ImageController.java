package ashev.flowers_calendar.ui.controller.main;

import ashev.flowers_calendar.Singleton;
import ashev.flowers_calendar.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ImageController {

    private static final Image EMPTY_IMAGE = new Image(ImageController.class.getResourceAsStream("/image/empty.jpg"));

    private double imageWidth;
    private double imageHeight;

    private static final List<String > EXT_LIST_LOWER_CASE = Arrays.asList(Singleton.getInstance().getAppBundle().getString("image_controller.image.ext").split(","));
    private static final List<String > EXT_LIST_FILTER = EXT_LIST_LOWER_CASE.stream().map(s -> "*." + s).collect(Collectors.toList());

    private Stage stage;
    private MainController parentController;
    private FileChooser fChooser;
    private Image selectedImage;
    private byte[] selectedImageBytes;

    @FXML private ImageView imageView;
    @FXML private Button btnSelectImage;

    public void init(Stage stage, MainController mainController) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }

        this.stage = stage;
        this.parentController = mainController;

        imageWidth = Double.parseDouble(Singleton.getInstance().getAppBundle().getString("image_controller.image.width"));
        imageHeight = Double.parseDouble(Singleton.getInstance().getAppBundle().getString("image_controller.image.height"));

        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);

        resetImage();

        fChooser = new FileChooser();
        fChooser.setTitle(Singleton.getInstance().getAppBundle().getString("image_controller.file_chooser.title"));
        fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(Singleton.getInstance().getAppBundle().getString("image_controller.file_chooser.description"), EXT_LIST_FILTER));

        btnSelectImage.setOnAction(event -> parentController.selectImage());

        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void updateReadVision() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        btnSelectImage.setVisible(false);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void updateEditVision() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        btnSelectImage.setVisible(true);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void chooseImage() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        final File file = fChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        final String ext = FilenameUtils.getExtension(file.getName()).trim().toLowerCase();
        if (!EXT_LIST_LOWER_CASE.contains(ext)) {
            Utils.showWarning(Singleton.getInstance().getAppBundle().getString("image_controller.alert.title"),
                    MessageFormat.format(Singleton.getInstance().getAppBundle().getString("image_controller.alert.header_text"), ext),
                    Singleton.getInstance().getAppBundle().getString("image_controller.alert.content_text") +
                            EXT_LIST_LOWER_CASE.stream().map(s -> "\n - " + s).collect(Collectors.joining()));
            return;
        }
        final Image image = new Image(file.toURI().toURL().toString(), imageWidth, imageHeight, false, false);
        selectedImageBytes = imageToBytes(image, ext);
        selectedImage = image;
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    private byte[] imageToBytes(Image image, String formatName) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        byte[] res;
        final BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bImage, formatName, byteArrayOutputStream);
            res  = byteArrayOutputStream.toByteArray();
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    if (log.isErrorEnabled()) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
       return res;
    }

    public void resetImage() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        selectedImageBytes = null;
        selectedImage = null;
        imageView.setImage(EMPTY_IMAGE);
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void display() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        if (selectedImage != null) {
            imageView.setImage(selectedImage);
        } else {
            imageView.setImage(EMPTY_IMAGE);
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public void show(byte[] imageBytes) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        if (imageBytes != null) {
            selectedImageBytes = imageBytes;
            selectedImage = bytesToImage(imageBytes);
            imageView.setImage(selectedImage);
        } else {
            imageView.setImage(EMPTY_IMAGE);
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
    }

    public Blob getSelectedImageBlob() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        SerialBlob serialBlob = null;
        if (selectedImageBytes != null) {
            serialBlob = new SerialBlob(selectedImageBytes);
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return serialBlob;
    }

    private Image bytesToImage(byte[] bytes) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        Image image = null;
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(bytes);
            image = new Image(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    if (log.isErrorEnabled()) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return image;
    }
}
