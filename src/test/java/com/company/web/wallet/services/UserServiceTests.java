//package com.company.web.wallet.services;
//
//import com.company.web.wallet.exceptions.AuthenticationFailureException;
//import com.company.web.wallet.exceptions.EntityDuplicateException;
//import com.company.web.wallet.exceptions.EntityNotFoundException;
//import com.company.web.wallet.exceptions.UnauthorizedOperationException;
//import com.company.web.wallet.models.DTO.UserPasswordDto;
//import com.company.web.wallet.models.User;
//import com.company.web.wallet.repositories.UserRepository;
//import net.bytebuddy.utility.RandomString;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import java.time.LocalDateTime;
//
//import static com.company.web.wallet.Auxiliary.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTests {
//
//    @Mock
//    UserRepository mockRepository;
//
//    @Mock
//    WalletService walletService;
//
//    @Mock
//    JavaMailSender mailSender;
//
//    @InjectMocks
//    UserServiceImpl userService;
//
//
//    @Test
//    public void get_Should_ReturnActiveUser_When_MatchByIdExist() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act
//        User currentUser = userService.getUserById(user.getId());
//
//        //Assert
//        Assertions.assertEquals(user.getId(), currentUser.getId());
//    }
//
//    @Test
//    public void get_Should_ReturnInactiveUser_When_MatchByIdExist() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByIdUnverified(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act
//        User currentUser = userService.getByIdUnverified(user.getId());
//
//        //Assert
//        Assertions.assertEquals(user.getId(), currentUser.getId());
//    }
//
//    @Test
//    public void get_Should_ReturnActiveUser_When_MatchByUsernameExist() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByUsername(Mockito.anyString()))
//                .thenReturn(user);
//
//        //Act
//        User currentUser = userService.getByUsername(user.getUsername());
//
//        //Assert
//        Assertions.assertEquals(user.getId(), currentUser.getId());
//    }
//
//    @Test
//    public void get_Should_ReturnInactiveUser_When_MatchByUsernameExist() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByUsername(Mockito.anyString()))
//                .thenReturn(user);
//
//        //Act
//        User currentUser = userService.getByUsername(user.getUsername());
//
//        //Assert
//        Assertions.assertEquals(user.getId(), currentUser.getId());
//    }
//
//    @Test
//    public void get_Should_ReturnUser_When_MatchByUsernameExist() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(userService.getByUsername(Mockito.anyString()))
//                .thenReturn(user);
//
//        //Act
//        User currentUser = mockRepository.getByUsername(user.getUsername());
//
//        //Assert
//        Assertions.assertEquals(user.getId(), currentUser.getId());
//    }
//
//    @Test
//    public void get_Should_ReturnNonActiveUser_When_MatchByUsernameExist() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(userService.getByUsernameUnverified(Mockito.anyString()))
//                .thenReturn(user);
//
//        //Act
//        User currentUser = mockRepository.getByUsernameUnverified(user.getUsername());
//
//        //Assert
//        Assertions.assertEquals(user.getId(), currentUser.getId());
//    }
//
//    @Test
//    public void get_Should_ReturnActiveUser_When_MatchByEmailExist() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByEmail(Mockito.anyString()))
//                .thenReturn(user);
//
//        //Act
//        User currentUser = userService.getUserByEmail(user.getUsername());
//
//        //Assert
//        Assertions.assertEquals(user.getId(), currentUser.getId());
//    }
//
//    @Test
//    public void get_Should_ReturnActiveUser_When_MatchByPhoneExist() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByPhone(Mockito.anyString()))
//                .thenReturn(user);
//
//        //Act
//        User currentUser = userService.getByPhone(user.getUsername());
//
//        //Assert
//        Assertions.assertEquals(user.getId(), currentUser.getId());
//    }
//
//    @Test
//    public void getAdmins_Should_CallRepository() {
//        //Act
//        userService.getAdmins();
//
//        //Assert
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .getAdmins();
//    }
//
//    @Test
//    public void getBlocked_Should_CallRepository() {
//        //Act
//        userService.getBlocked();
//
//        //Assert
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .getBlocked();
//    }
//
//    @Test
//    public void update_Should_Throw_When_DifferentLoggedUser() {
//        //Arrange
//        User user = createMockUser();
//        User logged = createMockUser();
//        logged.setId(2);
//
//        //Act, Assert
//        Assertions.assertThrows(UnauthorizedOperationException.class,
//                () -> userService.update(user, logged));
//    }
//
//    @Test
//    public void update_Should_Throw_When_DuplicateEmail() {
//        //Arrange
//        User user = createMockUser();
//        User user1 = createMockUser();
//        User logged =createMockUser();
//        user1.setId(10);
//
//        Mockito.when(userService.getUserByEmail(Mockito.anyString()))
//                .thenReturn(user1);
//
//        //Act, Assert
//        Assertions.assertThrows(AuthenticationFailureException.class,
//                () -> userService.update(logged, user));
//    }
//
//    @Test
//    public void update_Should_Throw_When_DuplicatePhone() {
//        //Arrange
//        User user = createMockUser();
//        User user1 = createMockUser();
//        User logged =createMockUser();
//        user1.setId(10);
//
//        Mockito.when(userService.getUserByEmail(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        Mockito.when(userService.getByPhone(Mockito.anyString()))
//                .thenReturn(user1);
//
//        //Act, Assert
//        Assertions.assertThrows(AuthenticationFailureException.class,
//                () -> userService.update(logged, user));
//    }
//
//    @Test
//    public void update_Should_Throw_When_Unauthorized() {
//        //Arrange
//        User logged = createMockUser();
//        User user = createMockUser();
//        logged.setUsername("Updated");
//
//        Mockito.when(userService.getUserByEmail(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        Mockito.when(userService.getByPhone(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        logged.setEmail(user.getEmail());
//
//        //Act, Assert
//        Assertions.assertThrows(UnauthorizedOperationException.class,
//                () -> userService.update(logged, user));
//    }
//
//    @Test
//    public void update_Should_Call_Repository() {
//        //Arrange
//        User logged = createMockUser();
//        User user = createMockUser();
//
//        Mockito.when(userService.getUserByEmail(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        Mockito.when(userService.getByPhone(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        logged.setEmail(user.getEmail());
//        logged.setLastUpdateDate(LocalDateTime.now());
//
//        //Act
//        userService.update(user, logged);
//
//        //Assert
//        Assertions.assertNotEquals(user.getLastUpdateDate(), logged.getLastUpdateDate());
//    }
//
//    @Test
//    public void delete_Should_Throw_When_UserToDeleteIsRoot() {
//        //Arrange
//        User user = createMockUser();
//
//        //Act, Assert
//        Assertions.assertThrows(UnauthorizedOperationException.class,
//                () -> userService.delete(user, 1));
//    }
//
//    @Test
//    public void delete_Should_Throw_When_LoggedUserIsDifferent() {
//        //Arrange
//        User user = createMockUser();
//
//        //Act, Assert
//        Assertions.assertThrows(UnauthorizedOperationException.class,
//                () -> userService.delete(user, 3));
//    }
//
//    @Test
//    public void delete_Should_CallRepository_WhenLoggedUser() {
//        //Arrange
//        User user = createMockUser();
//        user.setId(2);
//
//        //Act
//        userService.delete(user, 2);
//
//        // Assert
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .update(user);
//    }
//
//    @Test
//    public void makeAdmin_Should_Throw_When_UserNotExists() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenThrow(EntityNotFoundException.class);
//
//        //Act, Assert
//        Assertions.assertThrows(EntityNotFoundException.class,
//                () -> userService.makeAdmin(1, user));
//    }
//
//    @Test
//    public void activate_Should_Update_When_LoggedIsAdmin() {
//        //Arrange
//        User user = createDisabledUser();
//
//        Mockito.when(userService.getUserById(Mockito.anyInt()))
//                .thenReturn(user);
//        //Act
//        userService.verify(user);
//
//        // Assert
//        Assertions.assertEquals(userService.getById(user.getId()).getStatus(), statusService.getByType(StatusType.Active));
//    }
//
//    @Test
//    public void makeAdmin_Should_Throw_When_LoggedUserIsNotAdmin() {
//        //Arrange
//        User user = createMockUser();
//        User loggedUser = createMockUser();
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act, Assert
//        Assertions.assertThrows(UnauthorizedOperationException.class,
//                () -> userService.makeAdmin(1, loggedUser));
//    }
//
//    @Test
//    public void makeAdmin_Should_Update_When_LoggedIsAdmin() {
//        //Arrange
//        User user = createMockUser();
//        User loggedUser = createMockAdmin();
//
//        Mockito.when(userService.getById(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act
//        userService.makeAdmin(user.getId(), loggedUser);
//
//        // Assert
//        Assertions.assertEquals(user.getRole(), roleRepository.getByType(RoleType.Admin));
//    }
//
//    @Test
//    public void makeRegularUser_Should_Throw_When_UserNotExists() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenThrow(EntityNotFoundException.class);
//
//        //Act, Assert
//        Assertions.assertThrows(EntityNotFoundException.class,
//                () -> userService.makeRegularUser(1, user));
//    }
//
//    @Test
//    public void makeRegularUser_Should_Throw_When_UserIsAlreadyUser() {
//        //Arrange
//        User logged = createMockAdmin();
//        User user = createMockUser();
//        user.setId(2);
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act, Assert
//        Assertions.assertThrows(UnnecessaryOperationException.class,
//                () -> userService.makeRegularUser(2, logged));
//    }
//
//    @Test
//    public void makeRegularUser_Should_Throw_When_LoggedUserIsNotAdmin() {
//        //Arrange
//        User user = createMockUser();
//        User loggedUser = createMockUser();
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act, Assert
//        Assertions.assertThrows(UnauthorizedOperationException.class,
//                () -> userService.makeRegularUser(1, user));
//    }
//
//    @Test
//    public void makeRegularUser_Should_Update_When_LoggedIsAdmin() {
//        //Arrange
//        User user = createMockAdmin();
//        user.setId(3);
//        User loggedUser = createMockAdmin();
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act
//        userService.makeRegularUser(user.getId(), loggedUser);
//
//        // Assert
//        Assertions.assertEquals(user.getRole(), roleRepository.getByType(RoleType.User));
//    }
//
//    @Test
//    public void block_Should_Throw_When_UserNotExists() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenThrow(EntityNotFoundException.class);
//
//        //Act, Assert
//        Assertions.assertThrows(EntityNotFoundException.class,
//                () -> userService.blockUser(user.getId()));
//    }
//
//    @Test
//    public void block_Should_Update_When_LoggedIsAdmin() {
//        //Arrange
//        User user = createMockUser();
//        user.setId(2);
//        User loggedUser = createMockAdmin();
//        loggedUser.setId(3);
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act
//        userService.blockUser(user.getId());
//
//        // Assert
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .update(user);
//    }
//
//    @Test
//    public void unblock_Should_Throw_When_UserNotExists() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByIdBlocked(Mockito.anyInt()))
//                .thenThrow(EntityNotFoundException.class);
//
//        //Act, Assert
//        Assertions.assertThrows(EntityNotFoundException.class,
//                () -> userService.unblockUser(user.getId()));
//    }
//
//
//
//    @Test
//    public void unblock_Should_Update_When_LoggedIsAdmin() {
//        //Arrange
//        User user = createMockUser();
//        user.setStatus(new Status(StatusType.Blocked));
//        user.setId(3);
//
//        Mockito.when(mockRepository.getByIdBlocked(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act
//        userService.unblockUser(user.getId());
//
//        // Assert
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .update(user);
//    }
//
//    @Test
//    public void create_Should_Throw_When_UserWithSameUsernameExists() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByUsernameFromAll(user.getUsername()))
//                .thenReturn(user);
//
//        Mockito.when(invitationService.getUserByEmail(user.getEmail()))
//                .thenReturn(createMockInvitation());
//
//
//        //Act, Assert
//        Assertions.assertThrows(EntityDuplicateException.class,
//                () -> userService.create(user, "BGN"));
//    }
//
//
//    @Test
//    public void create_Should_Throw_When_UserWithSameEmailExists() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByUsernameFromAll(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        Mockito.when(mockRepository.getUserByEmailFromAll(Mockito.anyString()))
//                .thenReturn(user);
//
//        Mockito.when(invitationService.getUserByEmail(user.getEmail()))
//                .thenReturn(createMockInvitation());
//
//        //Act, Assert
//        Assertions.assertThrows(EntityDuplicateException.class,
//                () -> userService.create(user, "BGN"));
//    }
//
//    @Test
//    public void create_Should_Throw_When_getByEmailNotExists() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(invitationService.getByEmail(user.getEmail()))
//                .thenThrow(EntityNotFoundException.class);
//
//        //Act, Assert
//        Assertions.assertThrows(EntityNotFoundException.class,
//                () -> invitationService.getByEmail(user.getEmail()));
//    }
//
//    @Test
//    public void create_Should_Throw_When_getByUsernameNotExists() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByUsernameFromAll(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        //Act, Assert
//        Assertions.assertThrows(EntityNotFoundException.class,
//                () -> mockRepository.getByUsernameFromAll(user.getUsername()));
//    }
//
//    @Test
//    public void create_Should_Throw_When_getByPhoneNotExists() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getByPhoneFromAll(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        //Act
//        user.setPhoto("test");
//
//
//        //Act, Assert
//        Assertions.assertThrows(EntityNotFoundException.class,
//                () -> mockRepository.getByPhoneFromAll(user.getPhoneNumber()));
//    }
//
//    @Test
//    public void create_Should_Call_Repository() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(invitationService.getByEmail(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        Mockito.when(mockRepository.getByUsernameFromAll(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        Mockito.when(mockRepository.getByEmailFromAll(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        Mockito.when(mockRepository.getByPhoneFromAll(Mockito.anyString()))
//                .thenThrow(EntityNotFoundException.class);
//
//        user.setPhoto("https://res.cloudinary.com/dgp8v40yp/image/upload/v1690354959/Forum/Users%20photos/1.jpg");
//        Role userRole = roleRepository.getByType(RoleType.User);
//        user.setRole(userRole);
//        Status status = statusRepository.getByType(StatusType.Unverified);
//        user.setStatus(status);
//        user.setCreateDate(LocalDateTime.now());
//        user.setLastUpdateDate(LocalDateTime.now());
//
//        String code = RandomString.make(64);
//        VerificationCode verificationCode = new VerificationCode(code, user);
//        user.setVerificationCode(verificationCode);
//
//        userService.create(user, "BGN");
//        walletService.createWallet(user, "BGN");
//
//        // Assert
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .create(user);
//    }
//
//    @Test
//    public void verify_Should_Return_false_When_VerificationCodeIsEmpty() {
//        //Arrange
//        Mockito.when(mockRepository.getByVerificationCode(Mockito.anyString()))
//                .thenReturn(null);
//
//
//        //Act, Assert
////        Assertions.assertFalse(userService.verify(createMockVerificationCode().getCode()));
//    }
//
//    @Test
//    public void verify_Should_Change_Status_When_VerificationCodeIsValid() {
//        //Arrange
//        User user = createMockUser();
//
//        Status status = createMockPendingStatus();
//        VerificationCode code = createMockVerificationCode();
//
//        Mockito.when(mockRepository.getByVerificationCode(Mockito.anyString()))
//                .thenReturn(code);
//
//        Mockito.when(mockRepository.getByIdUnverified(Mockito.anyInt()))
//                .thenReturn(user);
//
//        Mockito.when(statusRepository.getByType(Mockito.any()))
//                .thenReturn(status);
//
//
//        //Act
//        personalDetailsService.create(createMockPersonalDetails());
////        userService.verify(code.getCode());
//
//
//        //Assert
//        Assertions.assertEquals(user.getStatus(), status);
//    }
//
//    @Test
//    public void verifyEmail_Should_Return_false_When_VerificationCodeIsEmpty() {
//        //Arrange
//        Mockito.when(mockRepository.getByVerificationCode(Mockito.anyString()))
//                .thenReturn(null);
//
//
//        //Act, Assert
//        Assertions.assertFalse(userService.verifyEmail(createMockVerificationCode().getCode()));
//    }
//    @Test
//    public void verifyEmail_Should_Change_Status_When_VerificationCodeIsValid() {
//        //Arrange
//        User user = createMockUser();
//
//        Status status = createMockActiveStatus();
//        VerificationCode code = createMockVerificationCode();
//
//        Mockito.when(mockRepository.getByVerificationCode(Mockito.anyString()))
//                .thenReturn(code);
//
//        Mockito.when(mockRepository.getByIdUnverified(Mockito.anyInt()))
//                .thenReturn(user);
//
//        Mockito.when(statusRepository.getByType(Mockito.any()))
//                .thenReturn(status);
//
//        //Act
//        personalDetailsService.create(createMockPersonalDetails());
//        userService.verifyEmail(code.getCode());
//
//        //Assert
//        Assertions.assertEquals(user.getStatus(), status);
//    }
//
////    @Test
////    public void sendVerificationEmail_Should_Send_Email() throws MessagingException, UnsupportedEncodingException {
////        //Arrange
////        User user = createMockUser();
////        VerificationCode verificationCode = createMockVerificationCode();
////
////
////        //Act
////        user.setVerificationCode(verificationCode);
////        MimeMessage message = mailSender.createMimeMessage();
////        String verifyURL = "localhost:8080" + "/users/verify?code=" + user.getVerificationCode();
////        service.sendVerificationEmail(user, verifyURL);
////
////
////        //Assert
////        Mockito.verify(mailSender, Mockito.times(1))
////                .send(message);
////    }
//
//    @Test
//    public void verifyUser_Should_Throw_When_LoggedUserIsNotAdmin() {
//        //Arrange
//        User user = createMockUser();
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenReturn(user);
//
//        //Act, Assert
//        Assertions.assertThrows(UnauthorizedOperationException.class,
//                () -> userService.verifyUser(1, user));
//    }
//
//    @Test
//    public void verifyUser_Should_Verify_When_LoggedUserIsAdmin() {
//        //Arrange
//        User user = createDisabledUser();
//        User admin = createMockAdmin();
//
//        Status activeStatus = createMockActiveStatus();
//
//        Mockito.when(mockRepository.getById(Mockito.anyInt()))
//                .thenReturn(user);
//
//        Mockito.when(statusRepository.getByType(Mockito.any()))
//                .thenReturn(activeStatus);
//        //Act
//        userService.verifyUser(user.getId(), admin);
//
//        // Assert
//        Assertions.assertEquals(user.getStatus(), activeStatus);
//    }
//
//    @Test
//    public void changePassword_Should_Call_Repository() {
//        //Arrange
//        User user = createMockUser();
//        UserPasswordDto userPasswordDto = createMockDPasswordDto();
//
//        //Act
//        userService.changePassword(user, userPasswordDto);
//
//        // Assert
//        Assertions.assertEquals(user.getPassword(), userPasswordDto.getNewPassword());
//    }
//
//}
