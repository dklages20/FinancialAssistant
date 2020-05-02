package financial.assistant.data;

public class UserFinanceJsonData {

    private Double monthlyIncome;
    private Double monthlyExpenses;
    private Double monthlyRemaining;

    public UserFinanceJsonData() {

    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Double getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(Double monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public Double getMonthlyRemaining() {
        return monthlyRemaining;
    }

    public void setMonthlyRemaining(Double monthlyRemaining) {
        this.monthlyRemaining = monthlyRemaining;
    }
}
