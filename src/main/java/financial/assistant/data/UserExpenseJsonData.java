package financial.assistant.data;

public class UserExpenseJsonData {

    private String expenseName;
    private Double expenseCost;

    public UserExpenseJsonData() {

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
