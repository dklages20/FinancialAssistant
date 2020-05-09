package financial.assistant.utils;

import javafx.application.Platform;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public final class JavaFXAssistingFunctions {

    private JavaFXAssistingFunctions() {
        // hiding default constructor
    }

    public static void shutdownHook(ApplicationContext applicationContext) {
        ((ConfigurableApplicationContext) applicationContext).stop();
        Platform.exit();
        System.exit(0);
    }

}
