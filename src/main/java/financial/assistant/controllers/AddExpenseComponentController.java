package financial.assistant.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class AddExpenseComponentController {

    private @FXML TextField expenseCost;
    private @FXML TextField expenseName;
    private @FXML DatePicker expenseDueDate;

    public void initialize() {

    }

}
