package financial.assistant.utils.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class AlertProvider {

    private AlertProvider() {
        // hiding default constructor
    }

    public static void openAlert(AlertType alertType, String headerText, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}