package cubeia.coding.challenge.cubeia.repository;


import cubeia.coding.challenge.cubeia.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}