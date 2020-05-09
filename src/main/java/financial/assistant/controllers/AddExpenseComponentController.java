package financial.assistant.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class AddExpenseComponentController {

    private @FXML TextField expenseCostField;
    private @FXML TextField expenseNameField;
    private @FXML Button deleteExpenseButton;

    private String expenseCost;
    private String expenseName;

    public void initialize() {
        Platform.runLater(() -> {
            if(expenseName != null && expenseCost != null) {
                expenseCostField.setText(expenseCost);
                expenseNameField.setText(expenseName);
            }

            deleteExpenseButton.setOnAction(e -> {
                HBox parent = (HBox) expenseCostField.getParent();
                VBox wrappingContainer = (VBox) expenseCostField.getParent().getParent();
                wrappingContainer.getChildren().remove(parent);
            });
        });
    }

    public void setExpenseCost(String expenseCost) {
        this.expenseCost = expenseCost;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

}
