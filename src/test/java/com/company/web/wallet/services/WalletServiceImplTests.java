package com.company.web.wallet.services;


import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDeletedException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.exceptions.OperationNotSupportedException;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.Wallet;
import com.company.web.wallet.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class WalletServiceImplTests {


    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private InterestRateService interestRateService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetWalletByIdAndUser() {
        Wallet mockWallet = new Wallet();
        mockWallet.setOwner(new User());

        when(walletRepository.get(anyInt())).thenReturn(mockWallet);

        Wallet returnedWallet = walletService.get(1, mockWallet.getOwner());

        assertEquals(mockWallet, returnedWallet);
    }


    @Test
    public void testGetWalletByIdAndWrongUser() {
        Wallet expectedWallet = mock(Wallet.class);
        User expectedOwner = new User();
        expectedOwner.setId(1);
        User differentUser = new User();
        differentUser.setId(2);

        when(walletRepository.get(1)).thenReturn(expectedWallet);
        when(expectedWallet.getOwner()).thenReturn(expectedOwner);

        assertNotEquals(expectedOwner, differentUser);

        assertThrows(AuthorizationException.class, () -> walletService.get(1, differentUser));
    }


    @Test
    public void testGetDeletedWallet() {

        Wallet mockWallet = new Wallet();
        mockWallet.setStatusDeleted(1);

        User owner = new User();
        owner.setId(1);
        owner.setUsername("Owner");
        mockWallet.setOwner(owner);

        when(walletRepository.get(anyInt())).thenReturn(mockWallet);

        User user = new User();
        user.setId(1);
        user.setUsername("TestUser");

        WalletService walletService = new WalletServiceImpl(walletRepository, null);
        assertThrows(EntityDeletedException.class, () -> {
            walletService.get(1, user);
        });
    }


    @Test
    public void testGetAllWalletsForUser() {
        // Arrange
        User mockUser = new User();
        Wallet wallet1 = new Wallet();
        wallet1.setOwner(mockUser);
        Wallet wallet2 = new Wallet();
        wallet2.setOwner(new User());

        when(walletRepository.getAll()).thenReturn(Arrays.asList(wallet1, wallet2));

        List<Wallet> result = walletService.getAll(mockUser);

        assertEquals(2, result.size());
        assertTrue(result.contains(wallet1));
        assertTrue(result.contains(wallet2));
    }


    @Test
    public void testGetAllNoWalletsForUser() {
        User mockUser = new User();

        when(walletRepository.getAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> {
            walletService.getAll(mockUser);
        });
    }


    @Test
    public void testAddToBalance() {
        User mockUser = new User();
        Wallet mockWallet = new Wallet();
        mockWallet.setOwner(mockUser);
        mockWallet.setBalance(BigDecimal.ZERO);

        when(walletRepository.get(anyInt())).thenReturn(mockWallet);

        walletService.addToBalance(1, mockUser, BigDecimal.TEN);

        verify(walletRepository, times(1)).update(mockWallet);
        assertEquals(BigDecimal.TEN, mockWallet.getBalance());
    }


    @Test
    public void testCreateWallet() {
        double mockInterestRate = 5.5;
        Wallet mockWallet = new Wallet();

        when(interestRateService.getLatestInterestRate()).thenReturn(mockInterestRate);

        walletService.create(mockWallet);

        assertEquals(mockInterestRate, mockWallet.getInterestRate());
        verify(walletRepository, times(1)).create(mockWallet);
    }


    @Test
    public void testUpdateOverdraft() {
        User mockUser = new User();
        Wallet mockWallet = new Wallet();
        mockWallet.setOwner(mockUser);

        when(walletRepository.get(anyInt())).thenReturn(mockWallet);

        walletService.updateOverdraft(1, mockUser, mockWallet);

        verify(walletRepository, times(1)).update(mockWallet);
    }


    @Test
    public void testRemoveFromBalanceWithoutOverdraft() {
        User mockUser = new User();
        Wallet mockWallet = new Wallet();
        mockWallet.setOwner(mockUser);
        mockWallet.setBalance(BigDecimal.valueOf(10));
        mockWallet.setOverdraftEnabled(0);

        when(walletRepository.get(anyInt())).thenReturn(mockWallet);

        walletService.removeFromBalance(1, mockUser, BigDecimal.valueOf(5));

        verify(walletRepository, times(1)).update(mockWallet);
        assertEquals(BigDecimal.valueOf(5), mockWallet.getBalance());
    }


    @Test
    public void testRemoveFromBalanceWithOverdraftNotAllowed() {
        User mockUser = new User();
        Wallet mockWallet = new Wallet();
        mockWallet.setOwner(mockUser);
        mockWallet.setBalance(BigDecimal.valueOf(5));
        mockWallet.setOverdraftEnabled(0);

        when(walletRepository.get(anyInt())).thenReturn(mockWallet);

        assertThrows(OperationNotSupportedException.class, () -> {
            walletService.removeFromBalance(1, mockUser, BigDecimal.valueOf(10));
        });
    }


    @Test
    public void testRemoveFromBalanceWithOverdraftAllowed() {
        User mockUser = new User();
        Wallet mockWallet = new Wallet();
        mockWallet.setOwner(mockUser);
        mockWallet.setBalance(BigDecimal.valueOf(5));
        mockWallet.setOverdraftEnabled(1);

        when(walletRepository.get(anyInt())).thenReturn(mockWallet);

        walletService.removeFromBalance(1, mockUser, BigDecimal.valueOf(10));

        verify(walletRepository, times(1)).update(mockWallet);
        assertEquals(BigDecimal.valueOf(-5), mockWallet.getBalance());
    }


    @Test
    public void testDeleteWallet() {
        User mockUser = new User();
        Wallet mockWallet = new Wallet();
        mockWallet.setOwner(mockUser);

        when(walletRepository.get(anyInt())).thenReturn(mockWallet);

        walletService.delete(1, mockUser);

        verify(walletRepository, times(1)).delete(1);
    }

    @Test
    public void testChargeInterestOnOverdraft() {

        Wallet overdraftWallet = new Wallet();
        overdraftWallet.setId(1);
        overdraftWallet.setOverdraftEnabled(1);
        overdraftWallet.setBalance(BigDecimal.valueOf(1000));
        overdraftWallet.setInterestRate(0.05);


        when(walletRepository.getAll()).thenReturn(Collections.singletonList(overdraftWallet));


        BigDecimal initialBalance = BigDecimal.valueOf(1000);
        BigDecimal expectedDeduction = initialBalance.multiply(BigDecimal.valueOf(overdraftWallet.getInterestRate()));
        BigDecimal expectedBalance = initialBalance.subtract(expectedDeduction);

        walletService.chargeInterestOnOverdraft();

        assertEquals(expectedBalance, overdraftWallet.getBalance());

        verify(walletRepository, times(1)).update(overdraftWallet);
    }

    @Test
    public void testGetWalletIdForUser() {


        User user = new User();
        user.setId(1);

        Integer expectedWalletId = 5;

        when(walletRepository.getWalletIdForUser(user)).thenReturn(expectedWalletId);

        Integer actualWalletId = walletService.getWalletIdForUser(user);

        assertEquals(expectedWalletId, actualWalletId);

        verify(walletRepository, times(1)).getWalletIdForUser(user);
    }
}