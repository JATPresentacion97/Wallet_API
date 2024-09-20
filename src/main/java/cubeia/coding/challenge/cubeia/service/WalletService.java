package cubeia.coding.challenge.cubeia.service;

import cubeia.coding.challenge.cubeia.model.Account;
import cubeia.coding.challenge.cubeia.model.Transaction;
import cubeia.coding.challenge.cubeia.repository.AccountRepository;
import cubeia.coding.challenge.cubeia.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getBalance();
    }

    public void transfer(Long accountId, BigDecimal amount, String type) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if ("DEBIT".equals(type)) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance");
            }
            account.setBalance(account.getBalance().subtract(amount));
        } else if ("CREDIT".equals(type)) {
            account.setBalance(account.getBalance().add(amount));
        }

        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public List<Transaction> listTransactions(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Account createAccount() {
        Account account = new Account();
        return accountRepository.save(account);
    }
}