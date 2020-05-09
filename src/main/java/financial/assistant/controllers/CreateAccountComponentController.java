package financial.assistant.controllers;

import financial.assistant.entity.MonthlyExpense;
import financial.assistant.entity.MonthlyFinance;
import financial.assistant.entity.UserAccount;
import financial.assistant.enums.ui.FxmlComponent;
import financial.assistant.exceptions.ComponentLoadException;
import financial.assistant.repository.UserAccountRepository;
import financial.assistant.utils.data.NumberUtils;
import financial.assistant.utils.ui.AlertProvider;
import financial.assistant.utils.ui.ComponentLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateAccountComponentController {

    private static final Logger logger = LoggerFactory.getLogger(CreateAccountComponentController.class);

    private @Autowired ApplicationContext applicationContext;
    private @Autowired UserAccountRepository userAccountRepository;

    private @FXML TextField accountNameField;
    private @FXML TextField accountIncomeField;
    private @FXML Button submitButton;
    private @FXML Button addExpenseButton;
    private @FXML VBox expensesContainer;
    private @FXML Label errorLabel;

    public void initialize() {
        logger.info("Initializing component class {}", CreateAccountComponentController.class);
        this.addExpenseButton.setOnAction(this::addExpense);
        this.submitButton.setOnAction(this::onSubmit);
    }

    public void onSubmit(ActionEvent event) {
        if (accountNameField.getText().equals("")) {
            logger.info("Unable to create account because account name is empty");
            createErrorMessage("Please enter an account name");
            return;
        } else if (accountIncomeField.getText().equals("")) {
            logger.info("Unable to create account because account income is empty");
            createErrorMessage("Please enter a monthly income for this account");
            return;
        }

        UserAccount newUserAccount = new UserAccount();
        newUserAccount.setAccountName(accountNameField.getText().trim());

        // build monthly finances
        MonthlyFinance monthlyFinance = new MonthlyFinance();
        monthlyFinance.setMonthlyIncome(NumberUtils.round(Double.parseDouble(accountIncomeField.getText().trim())));
        monthlyFinance.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        monthlyFinance.setUserAccount(newUserAccount);

        // build monthly expenses
        List<MonthlyExpense> monthlyExpenses = new ArrayList<>();
        for (Node node : expensesContainer.getChildren()) {
            HBox singleExpenseContainer = (HBox) node;

            // we know that if there is an HBox available, then we have a textfield,
            // textfield, and datepicker
            TextField expenseName = (TextField) singleExpenseContainer.getChildren().get(0);
            TextField expenseCost = (TextField) singleExpenseContainer.getChildren().get(1);

            if (expenseName.getText().equals("") || expenseCost.getText().equals("")) {
                createErrorMessage("Please ensure all expenses are filled out before submitting the form");
                return;
            }

            MonthlyExpense singleFinancialEntry = new MonthlyExpense();
            singleFinancialEntry.setExpenseCost(NumberUtils.round(Double.parseDouble(expenseCost.getText().trim())));
            singleFinancialEntry.setExpenseName(expenseName.getText().trim());
            singleFinancialEntry.setUserAccount(newUserAccount);
            monthlyExpenses.add(singleFinancialEntry);

        }

        // calculate left over money
        double totalCost = 0;
        for (MonthlyExpense expense : monthlyExpenses) {
            totalCost += expense.getExpenseCost();
        }

        monthlyFinance.setMonthlyExpenses(NumberUtils.round(totalCost));
        monthlyFinance.setMonthlyRemaining(NumberUtils.round(monthlyFinance.getMonthlyIncome() - totalCost));
        newUserAccount.getMonthlyFinances().add(monthlyFinance);
        newUserAccount.getMonthlyExpenses().addAll(monthlyExpenses);
        userAccountRepository.save(newUserAccount);

        createSuccessMessage("Account successfully created");
        logger.info("Saving account with name = {}", newUserAccount.getAccountName());
        clear();
    }

    public void addExpense(ActionEvent event) {
        try {
            Parent root = ComponentLoader.loadComponent(FxmlComponent.ADD_EXPENSE_COMPONENT,
                    applicationContext::getBean);
            this.expensesContainer.getChildren().add(root);
        } catch (ComponentLoadException e) {
            logger.error("An exception occured while loading component = {} from = {}",
                    FxmlComponent.ADD_EXPENSE_COMPONENT, FxmlComponent.ADD_EXPENSE_COMPONENT.getResoucePath());
            AlertProvider.openAlert(AlertType.ERROR, "Error", "Unable to load component",
                    "An problem occurred while trying to load add expense component");
        }
    }

    private void createErrorMessage(String errorMessage) {
        this.errorLabel.setText(errorMessage);
        this.errorLabel.setStyle("-fx-text-fill: red");
    }

    private void createSuccessMessage(String successMessage) {
        this.errorLabel.setText(successMessage);
        this.errorLabel.setStyle("-fx-text-fill: green");
    }

    private void clear() {
        this.accountNameField.clear();
        this.accountIncomeField.clear();
        this.expensesContainer.getChildren().clear();
    }
}
