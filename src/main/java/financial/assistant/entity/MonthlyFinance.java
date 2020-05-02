package financial.assistant.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "monthly_finance")
public class MonthlyFinance implements Comparable<MonthlyFinance>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "monthly_income", nullable = false)
    private Double monthlyIncome;

    @Column(name = "monthly_expenses", nullable = false)
    private Double monthlyExpenses;

    @Column(name = "monthly_remaining", nullable = false)
    private Double monthlyRemaining;

    @Column(name = "created_on", nullable = false)
    private Timestamp createdOn;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn
    private UserAccount userAccount;

    public MonthlyFinance() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public int compareTo(MonthlyFinance o) {
        return this.getCreatedOn().compareTo(o.getCreatedOn());
    }
}
