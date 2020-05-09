package financial.assistant.controllers;

import financial.assistant.entity.MonthlyExpense;
import financial.assistant.entity.MonthlyFinance;
import financial.assistant.entity.UserAccount;
import financial.assistant.repository.UserAccountRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Component
public class ViewAccountInfoComponentController {

    private @Autowired UserAccountRepository userAccountRepository;

    private @FXML Label accountNameLabel;
    private @FXML Label totalIncomeLabel;
    private @FXML Label totalExpensesLabel;
    private @FXML Label remainingLabel;
    private @FXML ListView<String> expensesContainer;
    private String userAccountName;

    @FXML
    public void initialize() {
        Platform.runLater(this::buildContents);
    }

    public void buildContents() {
        UserAccount userAccount = userAccountRepository.findByAccountName(userAccountName);
        MonthlyFinance finances = userAccount.getMonthlyFinances().get(0);

        // add data to labels for monthly expenses/income/etc
        this.accountNameLabel.setText(userAccount.getAccountName());
        this.totalIncomeLabel.setText(finances.getMonthlyIncome().toString());
        this.totalExpensesLabel.setText(finances.getMonthlyExpenses().toString());
        this.remainingLabel.setText(finances.getMonthlyRemaining().toString());

        // sort the list of expenses alphabetically and add them to the list view
        List<MonthlyExpense> expenses = userAccount.getMonthlyExpenses();
        Collections.sort(expenses);
        for(MonthlyExpense expense : expenses) {
            expensesContainer.getItems().add(expense.getExpenseName() + "    ( " + expense.getExpenseCost() + " )");
        }
    }

    public void setUserAccountName(String userAccountName) {
        this.userAccountName = userAccountName;
    }
}
