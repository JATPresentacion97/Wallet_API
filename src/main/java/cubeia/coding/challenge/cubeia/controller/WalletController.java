package cubeia.coding.challenge.cubeia.controller;

import cubeia.coding.challenge.cubeia.model.Account;
import cubeia.coding.challenge.cubeia.model.Transaction;
import cubeia.coding.challenge.cubeia.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long accountId) {
        BigDecimal balance = walletService.getBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestParam Long accountId, @RequestParam BigDecimal amount, @RequestParam String type) {
        walletService.transfer(accountId, amount, type);
        return ResponseEntity.ok("Transfer successful");
    }

    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<Transaction>> listTransactions(@PathVariable Long accountId) {
        List<Transaction> transactions = walletService.listTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/create-account")
    public ResponseEntity<Account> createAccount() {
        Account account = walletService.createAccount();
        return ResponseEntity.ok(account);
    }
}