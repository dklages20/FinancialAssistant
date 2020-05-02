package financial.assistant.repository;

import financial.assistant.entity.MonthlyFinance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyFinanceRepository extends JpaRepository<MonthlyFinance, Integer> {
}
