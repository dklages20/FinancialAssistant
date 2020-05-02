package financial.assistant.data;

public class UserAccountJsonData {

    private String accountName;
    private UserFinanceJsonData monthyFinances;
    private UserExpenseJsonData[] monthlyExpenses;

    public UserAccountJsonData() {

    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public UserFinanceJsonData getMonthyFinances() {
        return monthyFinances;
    }

    public void setMonthyFinances(UserFinanceJsonData monthyFinances) {
        this.monthyFinances = monthyFinances;
    }

    public UserExpenseJsonData[] getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(UserExpenseJsonData[] monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }
}
