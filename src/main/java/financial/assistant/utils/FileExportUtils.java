package financial.assistant.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import financial.assistant.data.UserAccountJsonData;
import financial.assistant.data.UserExpenseJsonData;
import financial.assistant.data.UserFinanceJsonData;
import financial.assistant.entity.MonthlyExpense;
import financial.assistant.entity.UserAccount;
import financial.assistant.enums.ExportFormat;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileExportUtils {

    public void exportUserAccount(ExportFormat exportFormat, File exportDirectory, UserAccount userAccount) throws IOException {
        switch (exportFormat) {
            case CSV:
                exportUserAccountCSV(exportDirectory, userAccount);
                break;
            case PDF:
                // TODO: implement pdf file export
            case JSON:
                exportUserAccountJSON(exportDirectory, userAccount);
                break;
        }
    }

    public void exportUserAccountCSV(File exportDirectory, UserAccount userAccount) throws IOException {
        Path exportPath = Paths.get(exportDirectory.getPath(), userAccount.getAccountName() + "-" + System.currentTimeMillis() + ".csv");
        Files.createFile(exportPath);

        StringBuilder sb = new StringBuilder();
        sb.append("Account Name, Monthly Income, Monthly Expenses, Monthly Remaining\n");
        sb.append(String.format("%s,%s,%s,%s\n", userAccount.getAccountName(), userAccount.getMonthlyFinances().get(0).getMonthlyIncome().toString(),  userAccount.getMonthlyFinances().get(0).getMonthlyExpenses().toString(),  userAccount.getMonthlyFinances().get(0).getMonthlyRemaining().toString()));
        sb.append("\n-------------------------------------------------------------------\n");

        sb.append("Expense Name, Expense Cost\n");
        for(MonthlyExpense expense : userAccount.getMonthlyExpenses()) {
            sb.append(String.format("%s,%s\n", expense.getExpenseName(), expense.getExpenseCost().toString()));
        }

        Files.write(exportPath, sb.toString().getBytes());
    }

    public void exportUserAccountPDF(File exportDirectory, UserAccount userAccount) {

    }

    public void exportUserAccountJSON(File exportDirectory, UserAccount userAccount) throws IOException {
        Path exportPath = Paths.get(exportDirectory.getPath(), userAccount.getAccountName() + "-" + System.currentTimeMillis() + ".json");
        Files.createFile(exportPath);

        UserAccountJsonData userAccountJsonData = new UserAccountJsonData();
        userAccountJsonData.setAccountName(userAccount.getAccountName());

        UserFinanceJsonData userFinanceJsonData = new UserFinanceJsonData();
        userFinanceJsonData.setMonthlyIncome(userAccount.getMonthlyFinances().get(0).getMonthlyIncome());
        userFinanceJsonData.setMonthlyExpenses(userAccount.getMonthlyFinances().get(0).getMonthlyExpenses());
        userFinanceJsonData.setMonthlyRemaining(userAccount.getMonthlyFinances().get(0).getMonthlyRemaining());
        userAccountJsonData.setMonthyFinances(userFinanceJsonData);

        UserExpenseJsonData [] expenseData = new UserExpenseJsonData[userAccount.getMonthlyExpenses().size()];
        for(int i = 0; i < expenseData.length; i++) {
            UserExpenseJsonData data = new UserExpenseJsonData();
            MonthlyExpense expense = userAccount.getMonthlyExpenses().get(i);
            data.setExpenseName(expense.getExpenseName());
            data.setExpenseCost(expense.getExpenseCost());
            expenseData[i] = data;
        }

        userAccountJsonData.setMonthlyExpenses(expenseData);

        // write the data using jacksons object mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(exportPath.toFile(), userAccountJsonData);
    }

}
