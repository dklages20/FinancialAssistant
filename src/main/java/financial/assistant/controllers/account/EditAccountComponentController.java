package financial.assistant.controllers.account;

import financial.assistant.controllers.AddExpenseComponentController;
import financial.assistant.data.MonthlyExpenseData;
import financial.assistant.entity.MonthlyExpense;
import financial.assistant.entity.MonthlyFinance;
import financial.assistant.entity.UserAccount;
import financial.assistant.repository.MonthlyExpenseRepository;
import financial.assistant.repository.UserAccountRepository;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EditAccountComponentController {

    private static final Logger logger = LoggerFactory.getLogger(EditAccountComponentController.class);

    private @FXML TextField accountNameField;
    private @FXML TextField monthlyIncomeField;
    private @FXML VBox expensesContainer;
    private @FXML Button addExpenseButton;
    private @FXML Button submitButton;
    private @FXML Label message;
    private @Autowired UserAccountRepository userAccountRepository;
    private @Autowired ApplicationContext applicationContext;
    private @Autowired MonthlyExpenseRepository monthlyExpenseRepository;

    private String accountName;

    public void initialize() {
        Platform.runLater(this::buildComponent);
    }

    public void buildComponent() {

        // find the user account from the provided account name
        UserAccount userAccount = userAccountRepository.findByAccountName(accountName);
        if(userAccount != null) {
            accountNameField.setText(userAccount.getAccountName());
            MonthlyFinance financeOverview = userAccount.getMonthlyFinances().get(0);
            monthlyIncomeField.setText(financeOverview.getMonthlyIncome().toString());

            // iterate through all the expenses and add them to the expenses container
            for(MonthlyExpense expense : userAccount.getMonthlyExpenses()) {
                expensesContainer.getChildren().add(buildExpenseContainer(expense));
            }
        }

        addExpenseButton.setOnAction(this::addExpense);
        submitButton.setOnAction(this::saveAndClose);
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    private Parent buildExpenseContainer(MonthlyExpense expense) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add-expense.component.fxml"));
            Parent root = loader.load();
            if(expense != null) {
                logger.debug("expense is available with name = {} and cost = {}", expense.getExpenseName(), expense.getExpenseCost());
                loader.setControllerFactory(applicationContext::getBean);
                AddExpenseComponentController controller = loader.getController();
                controller.setExpenseName(expense.getExpenseName());
                controller.setExpenseCost(expense.getExpenseCost().toString());
                loader.setController(controller);
            }
            return root;
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAndClose(ActionEvent event) {

        // first, we should do validation on all the data to avoid doing all the calculations and stuff needlessly
        if(accountNameField.getText().equals("")) {
            createError("Please enter a name for the account");
            return;
        } else if(monthlyIncomeField.getText().equals("")) {
            createError("Please enter a monthly income for the account");
            return;
        }

        // we need to iterate through ALL the expenses and make sure the name and cost are available to us
        List<MonthlyExpenseData> monthlyExpenses = new ArrayList<>();
        for(Node node : expensesContainer.getChildren()) {

            // the add account component is wrapped in an hbox, so we can cast it here
            HBox expense = (HBox) node;

            // we know that the first child is the expense name, and the second is the expense cost. They are both wrapped in a text field
            if(expense.getChildren().size() < 2) {
                throw new RuntimeException("Unexpected error occurred when saving editing account. Expense container child list was of size = " + expense.getChildren().size());
            }

            TextField expenseName = (TextField) expense.getChildren().get(0);
            TextField expenseCost = (TextField) expense.getChildren().get(1);

            if(expenseName.getText().equals("") || expenseCost.getText().equals("")) {
                createError("Please ensure all fields are filled out for added expenses");
                return;
            }

            try {
                monthlyExpenses.add(new MonthlyExpenseData(expenseName.getText(), Double.parseDouble(expenseCost.getText())));
            }catch(NumberFormatException e) {
                logger.error("An error occurred while parsing expenses", e);
                createModalError("An error occurred while reading expenses", "Please make sure the cost of all expenses contain only numbers without any special characters except for \".\"" );
                return;
            }

        }

        // at this point, we know the data is good, so we can start saving the info
        UserAccount userAccount = userAccountRepository.findByAccountName(accountName);
        if(userAccount != null) {

            // we need to make sure the account name wasn't changed
            if(!accountNameField.getText().equals(accountName)) {
                userAccount.setAccountName(accountNameField.getText());
            }

            MonthlyFinance monthlyFinance = new MonthlyFinance();
            monthlyFinance.setCreatedOn(new Timestamp(System.currentTimeMillis()));

            try {
                monthlyFinance.setMonthlyIncome(Double.parseDouble(monthlyIncomeField.getText()));
            }catch(NumberFormatException e){
                logger.error("An error occurred while parsing monthly income field", e);
                createModalError("An error occurred while reading the accounts monthly income", "Please make sure the monthly income field contains only numbers without any special characters except for \".\"");
                return;
            }

            // go through the accounts expenses and update any new data, remove old data, and create new
            List<MonthlyExpense> expenses = userAccount.getMonthlyExpenses();
            monthlyExpenses.forEach(e -> {

                // find the expense with the provided name if it exists
                Optional<MonthlyExpense> optExpense = expenses.stream().filter(ex -> ex.getExpenseName().equals(e.getExpenseName())).findFirst();
                if(optExpense.isPresent()) {
                    logger.info("Expense already exists, updating cost = {}", e.getExpenseCost());
                    optExpense.get().setExpenseCost(e.getExpenseCost());
                } else {
                    logger.info("New expense = {} found", e.getExpenseName());
                    MonthlyExpense expense = new MonthlyExpense();
                    expense.setExpenseName(e.getExpenseName());
                    expense.setExpenseCost(e.getExpenseCost());
                    expense.setUserAccount(userAccount);
                    expenses.add(expense);
                }
            });

            // after adding/updating the expenses, we need to remove any expenses that weren't present in the list
            List<String> remainingExpenseNames = monthlyExpenses.stream().map(expense -> expense.getExpenseName()).collect(Collectors.toList());
            List<MonthlyExpense> expensesToRemove = new ArrayList<>();
            for(MonthlyExpense expense : expenses) {
                if(!remainingExpenseNames.contains(expense.getExpenseName())) {
                    logger.info("Removing expense with name = {} from account = {} expense list", expense.getExpenseName(), userAccount.getAccountName());
                    expensesToRemove.add(expense);
                }
            }
            expenses.removeAll(expensesToRemove);

            // set the expense list to the new filtered data
            userAccount.setMonthlyExpenses(expenses);

            // now we need to go through and calculate the metrics for the monthly finance object
            double totalExpenses = 0;
            for(MonthlyExpenseData expenseData : monthlyExpenses) {
                totalExpenses += expenseData.getExpenseCost();
            }

            monthlyFinance.setMonthlyExpenses(totalExpenses);
            monthlyFinance.setMonthlyRemaining(monthlyFinance.getMonthlyIncome() - totalExpenses);
            monthlyFinance.setUserAccount(userAccount);
            userAccount.getMonthlyFinances().add(monthlyFinance);

            // at this point, everything should be filled out, attempt to save and if any exceptions occur, pop the error modal letting them know an unexpected error occurred and they should contact
            try {
                userAccountRepository.save(userAccount);
                expensesToRemove.forEach(e -> monthlyExpenseRepository.delete(e));
                ((Stage) expensesContainer.getScene().getWindow()).close();
            }catch(Exception e) {
                logger.error("An unexpected error occurred while saving user account", e);
                createModalError("Unexpected Error", "An unexpected error occurred while saving your account. Please create an issue for this at https://github.com/dklages20/FinancialAssistant");
                throw e;
            }
        }
    }

    private void addExpense(ActionEvent event) {
        expensesContainer.getChildren().add(buildExpenseContainer(null));
    }

    private void createError(String errorMessage) {
        message.setText(errorMessage);
        message.setStyle("-fx-text-fill: red");
    }

    private void createModalError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }
}
