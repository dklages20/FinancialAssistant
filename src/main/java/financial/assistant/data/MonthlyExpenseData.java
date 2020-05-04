package financial.assistant.data;

public class MonthlyExpenseData {

    private String expenseName;
    private Double expenseCost;

    public MonthlyExpenseData(String expenseName, Double expenseCost) {
        this.expenseCost = expenseCost;
        this.expenseName = expenseName;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public Double getExpenseCost() {
        return expenseCost;
    }

    public void setExpenseCost(Double expenseCost) {
        this.expenseCost = expenseCost;
    }
}
