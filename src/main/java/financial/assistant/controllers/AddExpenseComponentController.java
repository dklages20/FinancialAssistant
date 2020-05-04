package financial.assistant.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AddExpenseComponentController {

    private @FXML TextField expenseCostField;
    private @FXML TextField expenseNameField;
    private @Autowired ApplicationContext applicationContext;

    private String expenseCost;
    private String expenseName;

    public void initialize() {
        Platform.runLater(() -> {
            if(expenseName != null && expenseCost != null) {
                expenseCostField.setText(expenseCost);
                expenseNameField.setText(expenseName);
            }
        });
    }

    public void setExpenseCost(String expenseCost) {
        this.expenseCost = expenseCost;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

}
