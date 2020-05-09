package financial.assistant.utils.ui;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import financial.assistant.enums.ui.FxmlComponent;
import financial.assistant.exceptions.ComponentLoadException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;

public final class ComponentLoader {

    private static final Logger logger = LoggerFactory.getLogger(ComponentLoader.class);

    private ComponentLoader() {
        // hiding default constructor
    }

    public static Parent loadComponent(FxmlComponent component, Callback<Class<?>, Object> controllerFactory)
            throws ComponentLoadException {

        logger.info("Loading component = {} from location = {}", component, component.getResoucePath());

        try {
            FXMLLoader loader = new FXMLLoader(ComponentLoader.class.getResource(component.getResoucePath()));
            if (controllerFactory != null) {
                loader.setControllerFactory(controllerFactory);
            }
            return (Parent) loader.load();
        } catch (IOException e) {
            throw new ComponentLoadException("An error occurred while loading component = " + component, e);
        }
    }

}