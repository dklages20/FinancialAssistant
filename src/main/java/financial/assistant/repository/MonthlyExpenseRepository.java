package financial.assistant.repository;

import financial.assistant.entity.MonthlyExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyExpenseRepository extends JpaRepository<MonthlyExpense, Integer> {

}
