package financial.assistant.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_account")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MonthlyExpense> monthlyExpenses = new ArrayList<>();

    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MonthlyFinance> monthlyFinances = new ArrayList<>();

    public UserAccount () {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<MonthlyExpense> getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(List<MonthlyExpense> monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public List<MonthlyFinance> getMonthlyFinances() {
        return monthlyFinances;
    }

    public void setMonthlyFinances(List<MonthlyFinance> monthlyFinances) {
        this.monthlyFinances = monthlyFinances;
    }
}
