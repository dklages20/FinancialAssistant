package financial.assistant;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import financial.assistant.enums.ui.FxmlComponent;
import financial.assistant.exceptions.ComponentLoadException;
import financial.assistant.utils.JavaFXAssistingFunctions;
import financial.assistant.utils.ui.ComponentLoader;

import java.awt.Toolkit;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StageListener.class);

    private final String applicationTitle;
    private final ApplicationContext applicationContext;

    public StageListener(@Value("${spring.application.ui.title}") String applicationTitle,
            ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            Parent root = ComponentLoader.loadComponent(FxmlComponent.MAIN_COMPONENT, applicationContext::getBean);
            buildAndShowStage(event.getStage(), root);
        } catch (ComponentLoadException e) {
            // at this point, if an exception is thrown, then we can't even load the
            // application. We should log the error and then exit the application
            logger.info("An unexpected error occurred while starting the application", e);
            JavaFXAssistingFunctions.shutdownHook(applicationContext);
        }
    }

    private double getScreenWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
    }

    private double getScreenHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;
    }

    private void buildAndShowStage(Stage stage, Parent root) {
        Scene scene = new Scene(root, getScreenWidth(), getScreenHeight());
        scene.getStylesheets().add("/css/theme.css");
        stage.setScene(scene);
        stage.setTitle(this.applicationTitle);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/images/application-icon.png")));
        stage.show();
    }

}
