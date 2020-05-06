package financial.assistant;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StageListener.class);

    private final String applicationTitle;
    private final Resource fxml;
    private final ApplicationContext applicationContext;

    public StageListener(@Value("${spring.application.ui.title}") String applicationTitle, @Value("classpath:/fxml/main.component.fxml") Resource resource, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.fxml = resource;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        double height = getScreenHeight();
        double width = getScreenWidth();
        Stage stage = event.getStage();
        try {
            logger.info("Loading fxml from file://{}", this.fxml.getURL().getPath());
            logger.info("Starting application with width = {} and height = {}", width, height);
            URL url = this.fxml.getURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.setTitle(this.applicationTitle);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/images/application-icon.png")));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private double getScreenWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
    }

    private double getScreenHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;
    }

}
