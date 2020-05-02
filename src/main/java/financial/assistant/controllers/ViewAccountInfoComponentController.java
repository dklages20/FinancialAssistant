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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;


@Component
public class ViewAccountInfoComponentController {

    private @Autowired UserAccountRepository userAccountRepository;
    private @Autowired ApplicationContext applicationContext;

    private @FXML Label accountNameLabel;
    private @FXML Label totalIncomeLabel;
    private @FXML Label totalExpensesLabel;
    private @FXML Label remainingLabel;
    private @FXML ListView expensesContainer;
    private String userAccountName;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
           buildContents();
        });
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
        expenses.sort(new Comparator<MonthlyExpense>() {
            @Override
            public int compare(MonthlyExpense o1, MonthlyExpense o2) {
                if(o1.getExpenseName().compareTo(o2.getExpenseName()) > 0) {
                    return 1;
                } else if (o1.getExpenseName().compareTo(o2.getExpenseName()) < 0) {
                    return -1;
                } else return 1;
            }
        });

        for(MonthlyExpense expense : expenses) {
            expensesContainer.getItems().add(expense.getExpenseName() + "    ( " + expense.getExpenseCost() + " )");
        }
    }

    public void setUserAccountName(String userAccountName) {
        this.userAccountName = userAccountName;
    }
}
