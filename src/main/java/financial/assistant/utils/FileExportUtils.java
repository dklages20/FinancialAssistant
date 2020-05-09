package financial.assistant.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import financial.assistant.data.UserAccountJsonData;
import financial.assistant.data.UserExpenseJsonData;
import financial.assistant.data.UserFinanceJsonData;
import financial.assistant.entity.MonthlyExpense;
import financial.assistant.entity.MonthlyFinance;
import financial.assistant.entity.UserAccount;
import financial.assistant.enums.ExportFormat;
import financial.assistant.exceptions.ExportAccountException;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Component
public class FileExportUtils {

    public Path exportUserAccount(ExportFormat exportFormat, File exportDirectory, UserAccount userAccount)
           throws ExportAccountException {
        switch (exportFormat) {
            case CSV:
                return exportUserAccountCSV(exportDirectory, userAccount);
            case PDF:
                return exportUserAccountPDF(exportDirectory, userAccount);
            case JSON:
                return exportUserAccountJSON(exportDirectory, userAccount);
        }

        return null;
    }

    public Path exportUserAccountCSV(File exportDirectory, UserAccount userAccount) throws ExportAccountException {

        try {
            Path exportPath = Paths.get(exportDirectory.getPath(),
                    userAccount.getAccountName() + "-" + System.currentTimeMillis() + ".csv");
            Files.createFile(exportPath);

            StringBuilder sb = new StringBuilder();
            sb.append("Account Name, Monthly Income, Monthly Expenses, Monthly Remaining\n");

            // get the most recent monthly finance entry and append it to the CSV
            List<MonthlyFinance> monthlyFinances = userAccount.getMonthlyFinances();
            if (!monthlyFinances.isEmpty()) {
                Collections.sort(monthlyFinances);
                MonthlyFinance monthlyFinance = monthlyFinances.get(0);
                sb.append(String.format("%s,%s,%s,%s%n", userAccount.getAccountName(),
                        monthlyFinance.getMonthlyIncome().toString(), monthlyFinance.getMonthlyExpenses().toString(),
                        monthlyFinance.getMonthlyRemaining().toString()));
            }

            sb.append("\n-------------------------------------------------------------------\n");
            sb.append("Expense Name, Expense Cost\n");

            // print each expense to the CSV on its own line
            List<MonthlyExpense> expenses = userAccount.getMonthlyExpenses();
            if (expenses != null) {
                for (MonthlyExpense expense : expenses) {
                    sb.append(String.format("%s,%s%n", expense.getExpenseName(), expense.getExpenseCost().toString()));
                }
            }

            Files.write(exportPath, sb.toString().getBytes());
            return exportPath;
        } catch (IOException e) {
            throw new ExportAccountException("An exception occurred when trying to export an account", e);
        }

    }

    public Path exportUserAccountPDF(File exportDirectory, UserAccount userAccount) throws ExportAccountException {
        try {
            Path exportPath = Paths.get(exportDirectory.getPath(),
                    userAccount.getAccountName() + "-" + System.currentTimeMillis() + ".pdf");
            Files.createFile(exportPath);

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(exportPath.toFile()));
            document.open();

            Paragraph headerInfo = new Paragraph();
            headerInfo.add("Account summary for your " + userAccount.getAccountName() + " account\n\n\n");
            headerInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(headerInfo);

            Paragraph accountInfo = new Paragraph();
            accountInfo.add("Monthly Income: " + userAccount.getMonthlyFinances().get(0).getMonthlyIncome() + "\n");
            accountInfo.add("Monthly Expenses: " + userAccount.getMonthlyFinances().get(0).getMonthlyExpenses() + "\n");
            accountInfo.add(
                    "Monthly Remaining: " + userAccount.getMonthlyFinances().get(0).getMonthlyRemaining() + "\n\n\n");
            document.add(accountInfo);

            Paragraph expenseInfo = new Paragraph();
            for (MonthlyExpense expense : userAccount.getMonthlyExpenses()) {
                expenseInfo.add(String.format("Expense Name: %s    Expense Cost: %s%n", expense.getExpenseName(),
                        expense.getExpenseCost()));
            }
            document.add(expenseInfo);
            document.close();
            return exportPath;

        } catch (IOException | DocumentException e) {
            throw new ExportAccountException("An error occurred when trying to export an account", e);
        }
    }

    public Path exportUserAccountJSON(File exportDirectory, UserAccount userAccount) throws ExportAccountException {
        try {
            Path exportPath = Paths.get(exportDirectory.getPath(),
                    userAccount.getAccountName() + "-" + System.currentTimeMillis() + ".json");
            Files.createFile(exportPath);

            UserAccountJsonData userAccountJsonData = new UserAccountJsonData();
            userAccountJsonData.setAccountName(userAccount.getAccountName());

            UserFinanceJsonData userFinanceJsonData = new UserFinanceJsonData();
            userFinanceJsonData.setMonthlyIncome(userAccount.getMonthlyFinances().get(0).getMonthlyIncome());
            userFinanceJsonData.setMonthlyExpenses(userAccount.getMonthlyFinances().get(0).getMonthlyExpenses());
            userFinanceJsonData.setMonthlyRemaining(userAccount.getMonthlyFinances().get(0).getMonthlyRemaining());
            userAccountJsonData.setMonthyFinances(userFinanceJsonData);

            UserExpenseJsonData[] expenseData = new UserExpenseJsonData[userAccount.getMonthlyExpenses().size()];
            for (int i = 0; i < expenseData.length; i++) {
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

            return exportPath;
        } catch (IOException e) {
            throw new ExportAccountException("An error occurred while trying to export an account", e);
        }
    }

}
