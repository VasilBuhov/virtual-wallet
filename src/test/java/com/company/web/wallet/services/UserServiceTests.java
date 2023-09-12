package com.company.web.wallet.services;

import com.company.web.wallet.Auxiliary;
import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.company.web.wallet.Auxiliary.createMockUser;
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
    @Mock
    MimeMessageHelper mimeMessageHelper;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void testGetUserById() {
        // Arrange
        int userId = 1;
        User mockUser = createMockUser(); // Create a mock user

        // Define the behavior of your UserRepository mock
        when(mockRepository.getById(userId)).thenReturn(mockUser);

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertEquals(mockUser, result);
        verify(mockRepository, times(1)).getById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        // Arrange
        String userEmail = "test@mock.com";
        User mockUser = createMockUser(); // Use the Auxiliary method to create a mock user

        // Define the behavior of your UserRepository mock
        when(mockRepository.getByEmail(userEmail)).thenReturn(mockUser);

        // Act
        User result = userService.getUserByEmail(userEmail);

        // Assert
        assertEquals(mockUser, result);
        verify(mockRepository, times(1)).getByEmail(userEmail);
    }

    @Test
    public void testGetUserByPhone() {
        // Arrange
        String userPhone = "0888888888";
        User mockUser = Auxiliary.createMockUser(); // Use the Auxiliary method to create a mock user

        // Define the behavior of your UserRepository mock
        when(mockRepository.getByPhone(userPhone)).thenReturn(mockUser);

        // Act
        User result = userService.getByPhone(userPhone);

        // Assert
        assertEquals(mockUser, result);
        verify(mockRepository, times(1)).getByPhone(userPhone);
    }

    @Test
    public void testGetByIdUnverified() {
        // Arrange
        int userId = 1;
        User mockUser = Auxiliary.createMockUser(); // Use the updated Auxiliary method to create a mock user

        // Define the behavior of your UserRepository mock
        when(mockRepository.getByIdUnverified(userId)).thenReturn(mockUser);

        // Act
        User result = userService.getByIdUnverified(userId);

        // Assert
        assertEquals(mockUser, result);
        verify(mockRepository, times(1)).getByIdUnverified(userId);
    }

    @Test
    public void testGetByUsernameUnverified() {
        // Arrange
        String username = "Root";
        User mockUser = Auxiliary.createMockUser(); // Use the updated Auxiliary method to create a mock user

        // Define the behavior of your UserRepository mock
        when(mockRepository.getByUsernameUnverified(username)).thenReturn(mockUser);

        // Act
        User result = userService.getByUsernameUnverified(username);

        // Assert
        assertEquals(mockUser, result);
        verify(mockRepository, times(1)).getByUsernameUnverified(username);
    }

    @Test
    public void testGetAdmins() {
        // Arrange
        User mockAdmin = Auxiliary.createMockAdmin(); // Use the Auxiliary method to create a mock admin
        List<User> mockAdminsList = List.of(mockAdmin);

        // Define the behavior of your UserRepository mock
        when(mockRepository.getAdmins()).thenReturn(mockAdminsList);

        // Act
        List<User> result = userService.getAdmins();

        // Assert
        assertEquals(mockAdminsList, result);
        verify(mockRepository, times(1)).getAdmins();
    }

    @Test
    public void testGetBlockedUsers() {
        // Arrange
        User blockedUser = Auxiliary.createDisabledUser(); // Create a blocked user
        User nonBlockedUser = Auxiliary.createMockUser(); // Create a normal unblocked user
        User adminUser = Auxiliary.createMockAdmin(); // Create an admin user
        List<User> mockUsersList = Arrays.asList(blockedUser, nonBlockedUser, adminUser);

        // Define the behavior of your UserRepository mock
        when(mockRepository.getBlocked()).thenReturn(mockUsersList);

        // Act
        List<User> result = userService.getBlocked();

        // Assert
        assertEquals(mockUsersList, result);
        verify(mockRepository, times(1)).getBlocked();
    }

    /**
     * Pagination tests insert
     **/

    @Test
    public void testFindByUsernameContaining() {
        // Arrange
        String username = "Root";
        Pageable pageable = PageRequest.of(0, 10); // Pageable configuration

        User user1 = Auxiliary.createMockUser();
        User user2 = Auxiliary.createMockUser();
        List<User> mockUsersList = Arrays.asList(user1, user2);

        // Create a Page object
        Page<User> mockPage = new PageImpl<>(mockUsersList, pageable, mockUsersList.size());

        // Update the stubbing using ArgumentMatchers to match any string containing "Root"
        when(mockRepository.findByUsernameContaining(contains(username), eq(pageable))).thenReturn(mockPage);

        // Act
        Page<User> result = userService.findByUsernameContaining(username, pageable);

        // Assert
        assertEquals(mockPage, result);
        verify(mockRepository, times(1)).findByUsernameContaining(contains(username), eq(pageable));
    }

    @Test
    public void testGetAllUsersPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10); // Pageable configuration

        User user1 = Auxiliary.createMockUser();
        User user2 = Auxiliary.createMockUser();
        List<User> mockUsersList = Arrays.asList(user1, user2);

        // Create a Page object
        Page<User> mockPage = new PageImpl<>(mockUsersList, pageable, mockUsersList.size());

        // Define the behavior of your UserRepository mock
        when(mockRepository.findAllUsers(pageable)).thenReturn(mockPage);

        // Act
        Page<User> result = userService.getAllUsersPage(pageable);

        // Assert
        assertEquals(mockPage, result);
        verify(mockRepository, times(1)).findAllUsers(pageable);
    }

    @Test
    public void testGetAllUnverifiedUsersPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10); // Pageable configuration

        User user1 = Auxiliary.createMockUser();
        User user2 = Auxiliary.createMockUser();
        List<User> mockUsersList = Arrays.asList(user1, user2);

        // Create a Page object
        Page<User> mockPage = new PageImpl<>(mockUsersList, pageable, mockUsersList.size());

        // Define the behavior of your UserRepository mock
        when(mockRepository.findAllUnverifiedUsers(pageable)).thenReturn(mockPage);

        // Act
        Page<User> result = userService.getAllUnverifiedUsersPage(pageable);

        // Assert
        assertEquals(mockPage, result);
        verify(mockRepository, times(1)).findAllUnverifiedUsers(pageable);
    }


    @Test
    public void testGetAllBlockedUsersPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10); // Pageable configuration

        User blockedUser1 = Auxiliary.createDisabledUser(); // Create a blocked user
        User blockedUser2 = Auxiliary.createDisabledUser(); // Create another blocked user
        List<User> mockBlockedUsersList = Arrays.asList(blockedUser1, blockedUser2);

        // Create a Page object for blocked users
        Page<User> mockBlockedPage = new PageImpl<>(mockBlockedUsersList, pageable, mockBlockedUsersList.size());

        // Define the behavior of your UserRepository mock
        when(mockRepository.findAllBlockedUsers(pageable)).thenReturn(mockBlockedPage);

        // Act
        Page<User> result = userService.getAllBlockedUsersPage(pageable);

        // Assert
        assertEquals(mockBlockedPage, result);
        verify(mockRepository, times(1)).findAllBlockedUsers(pageable);
    }

    @Test
    public void testGetAllAdminUsersPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10); // Pageable configuration

        User adminUser1 = Auxiliary.createMockAdmin(); // Create an admin user
        User adminUser2 = Auxiliary.createMockAdmin(); // Create another admin user
        List<User> mockAdminUsersList = Arrays.asList(adminUser1, adminUser2);

        // Create a Page object for admin users
        Page<User> mockAdminPage = new PageImpl<>(mockAdminUsersList, pageable, mockAdminUsersList.size());

        // Define the behavior of your UserRepository mock
        when(mockRepository.findAllAdminUsers(pageable)).thenReturn(mockAdminPage);

        // Act
        Page<User> result = userService.getAllAdminUsersPage(pageable);

        // Assert
        assertEquals(mockAdminPage, result);
        verify(mockRepository, times(1)).findAllAdminUsers(pageable);
    }

    @Test
    public void testGetAllDeletedUsersPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10); // Pageable configuration

        User deletedUser1 = Auxiliary.createDisabledUser(); // Create a deleted user
        User deletedUser2 = Auxiliary.createDisabledUser(); // Create another deleted user
        List<User> mockDeletedUsersList = Arrays.asList(deletedUser1, deletedUser2);

        // Create a Page object for deleted users
        Page<User> mockDeletedPage = new PageImpl<>(mockDeletedUsersList, pageable, mockDeletedUsersList.size());

        // Define the behavior of your UserRepository mock
        when(mockRepository.findAllDeletedUsers(pageable)).thenReturn(mockDeletedPage);

        // Act
        Page<User> result = userService.getAllDeletedUsersPage(pageable);

        // Assert
        assertEquals(mockDeletedPage, result);
        verify(mockRepository, times(1)).findAllDeletedUsers(pageable);
    }

    @Test
    public void testGetAllContacts() {
        // Arrange
        int userId = 1;
        User contact1 = Auxiliary.createMockUser();
        User contact2 = Auxiliary.createMockUser();
        List<User> mockContactsList = Arrays.asList(contact1, contact2);

        // Define the behavior of your UserRepository mock
        when(mockRepository.getAllContacts(userId)).thenReturn(mockContactsList);

        // Act
        List<User> result = userService.getAllContacts(userId);

        // Assert
        assertEquals(mockContactsList, result);
        verify(mockRepository, times(1)).getAllContacts(userId);
    }

    @Test
    public void testCreateUser_DuplicateEmail() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("test@mock.com");
        newUser.setUsername("newUser");
        newUser.setPassword("password");
        String siteURL = "https://example.com";

        // Mock behavior of UserRepository to return an existing user with the same email
        when(mockRepository.getByEmail(newUser.getEmail())).thenReturn(newUser);

        // Act and Assert
        assertThrows(EntityDuplicateException.class, () -> userService.create(newUser, siteURL));

        // Verify that UserRepository methods were called
        verify(mockRepository, times(1)).getByEmail(newUser.getEmail());
        verify(mockRepository, never()).getByUsername(newUser.getUsername());
        verify(mockRepository, never()).create(newUser);
        verify(mailSender, never()).send((MimeMessage) Mockito.any()); // Verify that no email was sent
    }

    @Test
    public void testCreateUser_DuplicateUsername() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("test@mock.com");
        newUser.setUsername("newUser");
        newUser.setPassword("password");
        String siteURL = "https://example.com";

        // Mock behavior of UserRepository to return an existing user with the same username
        when(mockRepository.getByEmail(newUser.getEmail())).thenReturn(null);
        when(mockRepository.getByUsername(newUser.getUsername())).thenReturn(newUser);

        // Act and Assert
        assertThrows(EntityDuplicateException.class, () -> userService.create(newUser, siteURL));

        // Verify that UserRepository methods were called
        verify(mockRepository, times(1)).getByEmail(newUser.getEmail());
        verify(mockRepository, times(1)).getByUsername(newUser.getUsername());
        verify(mockRepository, never()).create(newUser);
        verify(mailSender, never()).send((MimeMessage) Mockito.any()); // Verify that no email was sent
    }

    @Test
    public void testCreateUser_NullUsernameAndPassword() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("test@mock.com");
        newUser.setUsername(null);
        newUser.setPassword(null);
        String siteURL = "https://example.com";

        // Act and Assert
        assertThrows(NullPointerException.class, () -> userService.create(newUser, siteURL));
    }

}
