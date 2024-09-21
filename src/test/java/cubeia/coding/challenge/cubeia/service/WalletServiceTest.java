package cubeia.coding.challenge.cubeia.service;

import cubeia.coding.challenge.cubeia.model.Account;
import cubeia.coding.challenge.cubeia.model.Transaction;
import cubeia.coding.challenge.cubeia.repository.AccountRepository;
import cubeia.coding.challenge.cubeia.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetBalance_ValidAccount() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100.00));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        BigDecimal balance = walletService.getBalance(1L);

        // Assert
        assertEquals(BigDecimal.valueOf(100.00), balance);
    }

    @Test
    public void testGetBalance_AccountNotFound() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> walletService.getBalance(1L));
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void testTransfer_CreditAccount() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100.00));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        walletService.transfer(1L, BigDecimal.valueOf(50.00), "CREDIT");

        // Assert
        assertEquals(BigDecimal.valueOf(150.00), account.getBalance());
        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testTransfer_DebitAccount_SufficientFunds() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100.00));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        walletService.transfer(1L, BigDecimal.valueOf(50.00), "DEBIT");

        // Assert
        assertEquals(BigDecimal.valueOf(50.00), account.getBalance());
        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testTransfer_DebitAccount_InsufficientFunds() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(50.00));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> walletService.transfer(1L, BigDecimal.valueOf(100.00), "DEBIT"));
        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    public void testListTransactions() {
        // Arrange
        Transaction transaction1 = new Transaction(1L, 1L, BigDecimal.valueOf(50.00), "CREDIT", LocalDateTime.now());
        Transaction transaction2 = new Transaction(2L, 1L, BigDecimal.valueOf(30.00), "DEBIT", LocalDateTime.now());
        when(transactionRepository.findByAccountId(1L)).thenReturn(Arrays.asList(transaction1, transaction2));

        // Act
        List<Transaction> transactions = walletService.listTransactions(1L);

        // Assert
        assertEquals(2, transactions.size());
        assertEquals(BigDecimal.valueOf(50.00), transactions.get(0).getAmount());
        assertEquals(BigDecimal.valueOf(30.00), transactions.get(1).getAmount());
    }

    @Test
    public void testCreateAccount() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.ZERO);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        Account createdAccount = walletService.createAccount();

        // Assert
        assertNotNull(createdAccount);
        assertEquals(BigDecimal.ZERO, createdAccount.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}
