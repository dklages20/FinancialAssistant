package financial.assistant.entity;

import javax.persistence.*;

@Entity
@Table(name = "monthly_expense")
public class MonthlyExpense implements Comparable<MonthlyExpense>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "expense_name", nullable = false)
    private String expenseName;

    @Column(name = "expense_cost", nullable = false)
    private Double expenseCost;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private UserAccount userAccount;

    public MonthlyExpense() {
        // needed by hibernate
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if(o instanceof MonthlyExpense) {
            MonthlyExpense that = (MonthlyExpense) o;
            equal = this.id == that.id && this.expenseName.equals(that.expenseName) && this.expenseCost == that.expenseCost && this.userAccount == that.userAccount;
        }
        return equal;
    }

    @Override
    public int compareTo(MonthlyExpense o) {
        return this.getExpenseName().compareTo(o.getExpenseName());
    }
}
