package financial.assistant.utils;

import javafx.application.Platform;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFXAssistingFunctions {

    public static void shutdownHook(ApplicationContext applicationContext) {
        ((ConfigurableApplicationContext) applicationContext).stop();
        Platform.exit();
        System.exit(0);
    }

}
