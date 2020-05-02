package financial.assistant.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import financial.assistant.data.UserAccountJsonData;
import financial.assistant.data.UserExpenseJsonData;
import financial.assistant.data.UserFinanceJsonData;
import financial.assistant.entity.MonthlyExpense;
import financial.assistant.entity.UserAccount;
import financial.assistant.enums.ExportFormat;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileExportUtils {

    public Path exportUserAccount(ExportFormat exportFormat, File exportDirectory, UserAccount userAccount)
            throws IOException {
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

    public Path exportUserAccountCSV(File exportDirectory, UserAccount userAccount) throws IOException {
        Path exportPath = Paths.get(exportDirectory.getPath(),
                userAccount.getAccountName() + "-" + System.currentTimeMillis() + ".csv");
        Files.createFile(exportPath);

        StringBuilder sb = new StringBuilder();
        sb.append("Account Name, Monthly Income, Monthly Expenses, Monthly Remaining\n");
        sb.append(String.format("%s,%s,%s,%s\n", userAccount.getAccountName(),
                userAccount.getMonthlyFinances().get(0).getMonthlyIncome().toString(),
                userAccount.getMonthlyFinances().get(0).getMonthlyExpenses().toString(),
                userAccount.getMonthlyFinances().get(0).getMonthlyRemaining().toString()));
        sb.append("\n-------------------------------------------------------------------\n");

        sb.append("Expense Name, Expense Cost\n");
        for (MonthlyExpense expense : userAccount.getMonthlyExpenses()) {
            sb.append(String.format("%s,%s\n", expense.getExpenseName(), expense.getExpenseCost().toString()));
        }

        Files.write(exportPath, sb.toString().getBytes());
        return exportPath;

    }

    public Path exportUserAccountPDF(File exportDirectory, UserAccount userAccount) throws IOException {
        Path exportPath = Paths.get(exportDirectory.getPath(),
                userAccount.getAccountName() + "-" + System.currentTimeMillis() + ".pdf");
        Files.createFile(exportPath);

        Document document = new Document();

        try {
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
                expenseInfo.add(String.format("Expense Name: %s    Expense Cost: %s\n", expense.getExpenseName(),
                        expense.getExpenseCost()));
            }
            document.add(expenseInfo);

            document.close();

        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        }

        return exportPath;
    }

    public Path exportUserAccountJSON(File exportDirectory, UserAccount userAccount) throws IOException {
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
    }

}
