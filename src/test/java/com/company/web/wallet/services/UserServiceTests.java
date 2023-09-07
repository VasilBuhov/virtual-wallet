package com.company.web.wallet.services;

import com.company.web.wallet.Auxiliary;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockRepository;

    @Mock
    WalletService walletService;

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void testMarkUserApproved() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Arrange
//        UserService userService = new UserService(mockRepository);
        User authenticatedUser = Auxiliary.createMockAdmin(); // Using your helper method
        User userToApprove = Auxiliary.createMockUser(); // Using your helper method
        LocalDateTime currentDateTime = LocalDateTime.now();

        when(mockRepository.getById(userToApprove.getId())).thenReturn(userToApprove);

        // Act
        userService.markUserApproved(authenticatedUser, userToApprove);

        // Assert
        verify(mockRepository).update(userToApprove);
        assertTrue(userToApprove.isPhotoVerified());
        assertTrue(userToApprove.getLastUpdateDate().isAfter(currentDateTime));
    }

    @Test
    public void testMailSender() throws MessagingException, UnsupportedEncodingException {

    }

    @Test
    public void testChangePassword() {

    }
    @Test
    public void testVerifyValidVerificationCode() {
        String verificationCode = "validCode";

        // Create a mock User with a verification code and isEnabled set to false
        User mockUser = new User();
        mockUser.setVerificationCode(verificationCode);
        mockUser.setEnabled(false);

        // Mock the behavior of userRepository.getByVerificationCode to return the mockUser
        when(mockRepository.getByVerificationCode(verificationCode)).thenReturn(mockUser);

        // Call the service method
        boolean result = userService.verify(verificationCode);

        // Verify that user's verification code is set to null and isEnabled is set to true
        assertTrue(result);
        assertNull(mockUser.getVerificationCode());
        assertTrue(mockUser.isEnabled());

        // Verify that userRepository.update was called with the mockUser
        verify(mockRepository).update(mockUser);
    }

    @Test
    public void testVerifyInvalidVerificationCode() {
        String verificationCode = "invalidCode";

        // Mock the behavior of userRepository.getByVerificationCode to return null
        when(mockRepository.getByVerificationCode(verificationCode)).thenReturn(null);

        // Call the service method
        boolean result = userService.verify(verificationCode);

        // Verify that the result is false
        assertFalse(result);

        // Verify that userRepository.update was not called
        verify(mockRepository, never()).update(any(User.class));
    }

    @Test
    public void testVerifyAlreadyEnabledUser() {
        String verificationCode = "validCode";

        // Create a mock User with a verification code and isEnabled set to true
        User mockUser = new User();
        mockUser.setVerificationCode(verificationCode);
        mockUser.setEnabled(true);

        // Mock the behavior of userRepository.getByVerificationCode to return the mockUser
        when(mockRepository.getByVerificationCode(verificationCode)).thenReturn(mockUser);

        // Call the service method
        boolean result = userService.verify(verificationCode);

        // Verify that the result is false
        assertFalse(result);

        // Verify that userRepository.update was not called
        verify(mockRepository, never()).update(any(User.class));
    }

    @Test
    public void testGenerateRandom2FA() {

    }


    @Test
    public void testGet2FA() {
        int userId = 1;
        String expected2FA = "123456"; // Replace with your expected 2FA value

        // Mock the behavior of mockRepository.get2FA
        when(mockRepository.get2FA(userId)).thenReturn(expected2FA);

        // Call the service method
        String actual2FA = userService.get2FA(userId);

        // Verify that mockRepository.get2FA was called with the correct argument
        Mockito.verify(mockRepository).get2FA(userId);

        // Assert that the returned 2FA matches the expected value
        assert(expected2FA.equals(actual2FA));
    }

    @Test
    public void testCheck2FA() {
        String provided2FA = "123456"; // Replace with your provided 2FA value
        User candidateUser = Auxiliary.createMockUser();

        // Mock the behavior of userService.get2FA (since it's used in check2FA)
        when(mockRepository.get2FA(candidateUser.getId())).thenReturn(provided2FA);

        // Call the service method
        boolean result = userService.check2FA(provided2FA, candidateUser);

        // Verify that mockRepository.get2FA was called with the correct argument
        Mockito.verify(mockRepository).get2FA(candidateUser.getId());

        // Assert that the result is true, indicating a successful 2FA check
        assert(result);
    }

    @Test
    public void testGetAllPhotoUnverified() {
        // Create mock data for the list of users
        List<User> mockUsers = Arrays.asList(
                Auxiliary.createMockUser(),
                Auxiliary.createMockUser(),
                Auxiliary.createMockUser()
        );

        // Mock the behavior of mockRepository.getAllPhotoUnverified
        when(mockRepository.getAllPhotoUnverified()).thenReturn(mockUsers);

        // Call the service method
        List<User> actualUsers = userService.getAllPhotoUnverified();

        // Verify that mockRepository.getAllPhotoUnverified was called
        Mockito.verify(mockRepository).getAllPhotoUnverified();

        // Assert that the returned list of users matches the mock data
        assert(actualUsers.equals(mockUsers));
    }

}
