package cubeia.coding.challenge.cubeia.controller;

import cubeia.coding.challenge.cubeia.model.Account;
import cubeia.coding.challenge.cubeia.model.Transaction;
import cubeia.coding.challenge.cubeia.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    public void testGetBalance() throws Exception {
        // Mocking the WalletService response
        when(walletService.getBalance(1L)).thenReturn(BigDecimal.valueOf(100.00));

        mockMvc.perform(get("/api/wallet/balance/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

    @Test
    public void testTransferFunds() throws Exception {
        // Mocking the WalletService behavior
        when(walletService.transfer(eq(1L), eq(BigDecimal.valueOf(50.00)), eq("credit"))).thenReturn(null);

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
        // Mocking the WalletService response
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1L, 1L, BigDecimal.valueOf(50.00), "credit", "2023-09-20T10:00:00"));
        when(walletService.listTransactions(1L)).thenReturn(transactions);

        mockMvc.perform(get("/api/wallet/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateAccount() throws Exception {
        // Mocking the WalletService response
        Account account = new Account(1L, BigDecimal.valueOf(100.00));
        when(walletService.createAccount()).thenReturn(account);

        mockMvc.perform(post("/api/wallet/create-account"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
