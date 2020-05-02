package financial.assistant.controllers;

import financial.assistant.entity.MonthlyExpense;
import financial.assistant.entity.MonthlyFinance;
import financial.assistant.entity.UserAccount;
import financial.assistant.repository.UserAccountRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateAccountComponentController {

    private @Autowired ApplicationContext applicationContext;
    private @Autowired UserAccountRepository userAccountRepository;

    private @FXML TextField accountNameField;
    private @FXML TextField accountIncomeField;
    private @FXML Button submitButton;
    private @FXML Button addExpenseButton;
    private @FXML VBox expensesContainer;
    private @FXML Label errorLabel;

    public void initialize() {
        this.addExpenseButton.setOnAction(this::addExpense);
        this.submitButton.setOnAction(this::onSubmit);
    }

    public void onSubmit(ActionEvent event) {
        if(accountNameField.getText().equals("")) {
            createErrorMessage("Please enter an account name");
            return;
        } else if(accountIncomeField.getText().equals("")) {
            createErrorMessage("Please enter a monthly income for this account");
            return;
        }

        UserAccount newUserAccount = new UserAccount();
        newUserAccount.setAccountName(accountNameField.getText().trim());

        // build monthly finances
        MonthlyFinance monthlyFinance = new MonthlyFinance();
        monthlyFinance.setMonthlyIncome(Double.parseDouble(accountIncomeField.getText().trim()));
        monthlyFinance.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        monthlyFinance.setUserAccount(newUserAccount);

        // build monthly expenses
        List<MonthlyExpense> monthlyExpenses = new ArrayList<>();
        for(Node node : expensesContainer.getChildren()) {
            HBox singleExpenseContainer = (HBox) node;

            // we know that if there is an HBox available, then we have a textfield, textfield, and datepicker
            TextField expenseName = (TextField) singleExpenseContainer.getChildren().get(0);
            TextField expenseCost = (TextField) singleExpenseContainer.getChildren().get(1);
            DatePicker dueDate = (DatePicker) singleExpenseContainer.getChildren().get(2);

            if(expenseName.getText().equals("") || expenseCost.getText().equals("") || dueDate.getValue() == null) {
                createErrorMessage("Please ensure all expenses are filled out before submitting the form");
                return;
            }

            MonthlyExpense singleFinancialEntry = new MonthlyExpense();
            singleFinancialEntry.setExpenseCost(Double.parseDouble(expenseCost.getText().trim()));
            singleFinancialEntry.setExpenseName(expenseName.getText().trim());
            singleFinancialEntry.setUserAccount(newUserAccount);
            monthlyExpenses.add(singleFinancialEntry);

        }

        // calculate left over money
        double totalCost = 0;
        for(MonthlyExpense expense: monthlyExpenses) {
            totalCost += expense.getExpenseCost();
        }

        monthlyFinance.setMonthlyExpenses(totalCost);
        monthlyFinance.setMonthlyRemaining(monthlyFinance.getMonthlyIncome() - totalCost);
        newUserAccount.getMonthlyFinances().add(monthlyFinance);
        newUserAccount.getMonthlyExpenses().addAll(monthlyExpenses);
        userAccountRepository.save(newUserAccount);

        createSuccessMessage("Account successfully created");
        clear();
    }

    public void addExpense(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add-expense.component.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            this.expensesContainer.getChildren().add(root);
        }catch(IOException e) {
            throw new RuntimeException(e);
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
