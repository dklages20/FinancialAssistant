package financial.assistant.entity;

import javax.persistence.*;

@Entity
@Table(name = "monthly_expense")
public class MonthlyExpense {

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
}
