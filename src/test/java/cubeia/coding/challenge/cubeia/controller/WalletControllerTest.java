package cubeia.coding.challenge.cubeia.controller;

import cubeia.coding.challenge.cubeia.model.Account;
import cubeia.coding.challenge.cubeia.model.Transaction;
import cubeia.coding.challenge.cubeia.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Updated to openMocks
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    public void testGetBalance() throws Exception {
        // Mocking the WalletService response
        when(walletService.getBalance(1L)).thenReturn(BigDecimal.valueOf(100.00));

        mockMvc.perform(get("/api/wallet/balance/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00")); // Ensure the expected string matches two decimal places
    }


    @Test
    public void testTransferFunds() throws Exception {
        doNothing().when(walletService).transfer(eq(1L), eq(BigDecimal.valueOf(50.00)), eq("credit"));

        mockMvc.perform(post("/api/wallet/transfer")
                        .param("accountId", "1")
                        .param("amount", "50.00")
                        .param("type", "credit")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));
    }

    @Test
    public void testListTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccountId(1L);
        transaction.setAmount(BigDecimal.valueOf(50.00));
        transaction.setType("credit");
        transaction.setTimestamp(LocalDateTime.parse("2023-09-20T10:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        transactions.add(transaction);

        when(walletService.listTransactions(1L)).thenReturn(transactions);

        mockMvc.perform(get("/api/wallet/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100.00));

        when(walletService.createAccount()).thenReturn(account);

        mockMvc.perform(post("/api/wallet/create-account"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
