package financial.assistant.controllers;

import financial.assistant.controllers.account.EditAccountComponentController;
import financial.assistant.entity.MonthlyExpense;
import financial.assistant.entity.MonthlyFinance;
import financial.assistant.entity.UserAccount;
import financial.assistant.enums.AccountControls;
import financial.assistant.repository.UserAccountRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewAccountComponentController {

    private static final Logger logger = LoggerFactory.getLogger(ViewAccountComponentController.class);

    private @Autowired UserAccountRepository userAccountRepository;
    private @Autowired ApplicationContext applicationContext;

    private @FXML ChoiceBox accountChoices;
    private @FXML Button submitButton;
    private @FXML VBox accountContainer;
    private @FXML Label message;

    public void initialize() {
        logger.info("Initializing component {}", ViewAccountComponentController.class);

        List<UserAccount> userAccounts = userAccountRepository.findAll();
        if(userAccounts != null && userAccounts.size() > 0) {
            List<String> accountNames = userAccounts.stream().map(account -> account.getAccountName()).collect(Collectors.toList());
            this.accountChoices.setItems(FXCollections.observableArrayList(accountNames));
        }

        this.submitButton.setOnAction(this::loadAccountFromChoicebox);
    }

    /**
     * Builds the view containing the account information and options for the selected user account
     */
    public void loadAccountFromChoicebox(ActionEvent event) {
        if (accountChoices.getValue() != null && !accountChoices.getValue().equals("")) {
            buildAccountView();
        } else {
            setErrorMessage("Please select an account");
        }
    }

    /**
     * Builds the view for the account management section of this component
     */
    private void buildAccountView() {

        // clear the success/error message if its set
        if(message.getText() != null && !message.getText().equals("")){
            message.setText("");
        }

        // build view for the account info container
        this.accountContainer.getChildren().addAll(buildAccountOptionsContainer());
        this.accountContainer.getChildren().addAll(loadFinancialInfo());
        this.accountContainer.getChildren().addAll(loadExpenses());
    }

    /**
     * Builds VBox containing the information about the selected account's financial overview (expenses, remaining, income)
     *
     * @return financial overview wrapped inside of a VBox
     */
    private VBox loadFinancialInfo() {
        logger.info("Building financial overview for account = {}", accountChoices.getValue());

        VBox financialInfoContainer = new VBox();
        financialInfoContainer.setSpacing(15.0f);

        // build user account information and add labels/text fields for each value
        UserAccount selectedAccount = userAccountRepository.findByAccountName((String) accountChoices.getValue());
        if(selectedAccount != null && selectedAccount.getMonthlyFinances() != null) {
            List<MonthlyFinance> finances = selectedAccount.getMonthlyFinances();
            Collections.sort(finances);
            MonthlyFinance monthlyFinancialData = finances.get(finances.size() - 1);
            addIncomeData("Monthly Income", monthlyFinancialData.getMonthlyIncome().toString(), financialInfoContainer);
            addIncomeData("Monthly Expenses", monthlyFinancialData.getMonthlyExpenses().toString(), financialInfoContainer);
            addIncomeData("Monthly Remaining", monthlyFinancialData.getMonthlyRemaining().toString(), financialInfoContainer);
        }

        return financialInfoContainer;
    }

    /**
     * Returns a list of objects that represent the selected account's expenses
     *
     * @return objects representing monthly expenses
     */
    private List<Node> loadExpenses() {
        logger.info("Building expenses for account = {}", accountChoices.getValue());
        List<Node> expensesNodes = new ArrayList<>();
        // build expenses information from the user account expenses
        UserAccount userAccount = userAccountRepository.findByAccountName((String) accountChoices.getValue());
        if(userAccount != null) {
            ListView expensesView = new ListView();
            for(MonthlyExpense expense : userAccount.getMonthlyExpenses()) {
                expensesView.getItems().add(String.format("%s  (%s)", expense.getExpenseName(), expense.getExpenseCost().toString()));
            }

            Label expensesLabel = new Label("List of monthly expenses");
            expensesNodes.addAll(Arrays.asList(expensesLabel, expensesView));
        }
        return expensesNodes;
    }

    /**
     * Builds HBox object containing account options for the selected user account
     *
     * @return HBox containing account options
     */
    private HBox buildAccountOptionsContainer() {
        logger.info("Building account options for account management page");
        HBox accountOptionsContainer = new HBox();
        accountOptionsContainer.setSpacing(10.0f);
        accountOptionsContainer.getChildren().addAll(loadAccountOptions());
        return accountOptionsContainer;
    }

    /**
     * Builds a list of buttons containing the different account options available from the AccountControls enum list
     *
     * @return list of account options
     */
    private List<Button> loadAccountOptions() {
        List<Button> options = new ArrayList<>();
        for(AccountControls control : AccountControls.values()) {
            Button controlButton = new Button();
            controlButton.setText(control.getOptions());
            controlButton.setGraphic(new ImageView(new Image(control.getImagePath())));

            switch (control) {
                case DELETE_ACCOUNT:
                    controlButton.setOnAction(this::openDeleteDialog);
                    break;
                case EDIT_ACCOUNT:
                    controlButton.setOnAction(this::openEditAccountDialog);
            }

            options.add(controlButton);
        }
        return options;
    }

    /**
     * Opens a dialog asking the user to confirm the deleting of the selected account
     */
    private void openDeleteDialog(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setContentText("Would you like to delete this account?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                logger.info("Deleting account = {}", accountChoices.getValue());
                UserAccount userAccount = userAccountRepository.findByAccountName((String) accountChoices.getValue());
                userAccountRepository.delete(userAccount);

                // after we delete, we need to remove that account name from the list of accounts, set a success message for the user, and remove the account from the options list
                accountContainer.getChildren().clear();
                setSuccessMessage("Successfully deleted " + (String) accountChoices.getValue() + " account");
                accountChoices.getItems().remove(accountChoices.getItems().indexOf((String) accountChoices.getValue()));
            }
        });
    }

    private void openEditAccountDialog(ActionEvent event) {
        try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/account-management/edit-account.fxml"));
           loader.setControllerFactory(applicationContext::getBean);
           Parent root = loader.load();
           loader.<EditAccountComponentController>getController().setAccountName((String) accountChoices.getValue());

           Stage stage = new Stage();
           stage.setScene(new Scene(root, 600, 600));
           stage.showAndWait();

            // wait for it to close and get the callback

        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets an error message for this component
     *
     * @param message error message
     */
    private void setErrorMessage(String message) {
        this.message.setText(message);
        this.message.setStyle("-fx-text-fill: red");
    }

    /**
     * Sets a success message for this component
     *
     * @param message success message
     */
    private void setSuccessMessage(String message) {
        this.message.setText(message);
        this.message.setStyle("-fx-text-fill: green");
    }

    /**
     * Adds income data to the provided container
     *
     * @param labelText expense name
     * @param fieldText expense cost
     * @param container container to add expense to
     */
    private void addIncomeData(String labelText, String fieldText, VBox container) {
        Label label = new Label(labelText);
        TextField field = new TextField();
        field.setEditable(false);
        field.setText(fieldText);
        container.getChildren().addAll(label, field);
    }
}
