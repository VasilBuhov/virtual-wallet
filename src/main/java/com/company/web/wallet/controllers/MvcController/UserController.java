package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.*;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.GetSiteURLHelper;
import com.company.web.wallet.helpers.UserMapper;
import com.company.web.wallet.models.DTO.UserPasswordDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.DTO.UserDto;
import com.company.web.wallet.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(AuthenticationHelper authenticationHelper, UserService userService, UserMapper userMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/process_register")
    public String processRegister(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        userService.create(user, GetSiteURLHelper.getSiteURL(request));
        return "user_register_success";
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "user_verify_success";
        } else {
            return "user_verify_fail";
        }
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) {
            return "redirect:/auth/login";
        }
        try {
            User user = userService.getUserById(id);
            byte[] avatarData = user.getProfilePicture();
            if (avatarData != null) {
                String base64DB = Base64.getEncoder().encodeToString(avatarData);
                model.addAttribute("base64avatar", base64DB);
            } else
                model.addAttribute("base64avatar", "");
            model.addAttribute("user", user);
            return "user_details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/new")
    public String showNewUserPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "user_register";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("errorMessage", "Please fill in all required fields.");
            return "user_register";
        }
        try {
            User newUser = userMapper.fromDto(userDto);
            userService.create(newUser, "localhost");
            return "redirect:/";
        } catch (EntityDuplicateException e) {
            model.addAttribute("alreadyExists", e.getMessage());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return "errors/782";
    }

    @GetMapping("/profile")
    public String showUserProfile(Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            model.addAttribute("base64avatar", null);
            try {
                User user = userService.getByUsername(username);
                if (user.getProfilePicture() != null) {
                    byte[] avatarData = user.getProfilePicture();
                    String base64DB = Base64.getEncoder().encodeToString(avatarData);
                    model.addAttribute("base64avatar", base64DB);
                }
                model.addAttribute("user", userMapper.toDto(user));
                return "user_edit";
            } catch (EntityNotFoundException e) {
                model.addAttribute("error", "User not found");
                return "errors/404";
            }
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/idVerification")
    public String idVerification(Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) return "redirect:/auth/login";
        User authenticatedUser = userService.getByUsername(username);
        //get idCard by Service
        if (userService.getIdCard(authenticatedUser.getId()) != null) {
            byte[] idCardData = userService.getIdCard(authenticatedUser.getId());
            String idCardData64DB = Base64.getEncoder().encodeToString(idCardData);
            model.addAttribute("idCardData64DB", idCardData64DB);
        } else model.addAttribute("idCardData64DB", null);
        //get selfie by Service
        if (userService.getSelfie(authenticatedUser.getId()) != null) {
            byte[] selfieData = userService.getSelfie(authenticatedUser.getId());
            String selfieData64DB = Base64.getEncoder().encodeToString(selfieData);
            model.addAttribute("selfieData64DB", selfieData64DB);
        } else model.addAttribute("selfieData64DB", null);

        try {
            model.addAttribute("user", userMapper.toDto(authenticatedUser));
            return "user_id_verification_upload";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "User not found");
            return "errors/404";
        }
    }


    @GetMapping("/password")
    public String showChangePasswordPage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/401";
        }

        try {
            String username = (String) session.getAttribute("currentUser");
            User user = userService.getByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("passwordDto", new UserPasswordDto());
            return "user_password_change";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
    }

    @PostMapping("/password")
    public String changePassword(@Valid @ModelAttribute("passwordDto") UserPasswordDto userPasswordDto,
                                 BindingResult errors,
                                 Model model, HttpSession session) {

        if (errors.hasErrors()) {
            return "user_password_change";
        }

        if (!userPasswordDto.getNewPassword().equals(userPasswordDto.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "password_error",
                    "Password confirmation should match password.");
            return "user_password_change";
        }

        String username = (String) session.getAttribute("currentUser");
        if (!userPasswordDto.getCurrentPassword().equals(userService.getByUsername(username).getPassword())) {
            errors.rejectValue("currentPassword", "password_error",
                    "Wrong current password.");
            return "user_password_change";
        }

        try {
            userService.changePassword(userService.getByUsername(username), userPasswordDto);
            return "user_pass_change_success";//change success?
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/pokes")
    public String showUserPokes(Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            try {
                return null;//TODO support this operation
            } catch (EntityNotFoundException e) {
                model.addAttribute("error", "User not found");
                return "errors/404";
            }
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/request")
    public String requestFunds(Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) {
            return "redirect:/auth/login";
        }
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "user_request_funds";
    }

    @GetMapping("/contacts")
    public String showContacts(Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) return "redirect:/auth/login";
        User authenticatedUser = userService.getByUsername(username);
        List<User> contacts = userService.getAllContacts(authenticatedUser.getId());
        if (contacts.isEmpty()) return "user_no_contacts";
        model.addAttribute("contacts", contacts);
        return "user_contacts";
    }

    @GetMapping("/contacts/add/{id}")
    public String addUserContact(@PathVariable int id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) return "redirect:/auth/login";
        User authenticatedUser = userService.getByUsername(username);
        try {
            userService.addContact(authenticatedUser.getId(), id);
            User targetUser = userService.getUserById(id);
            model.addAttribute("targetUser", targetUser);
            return "user_contact_add_success";
        } catch (EntityDuplicateException e) {
            model.addAttribute("error", e.getMessage());
            return "user_contact_already_added";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/contacts/remove/{id}")
    public String removeUserContact(@PathVariable int id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) return "redirect:/auth/login";
        User authenticatedUser = userService.getByUsername(username);
        try {
            userService.removeContact(authenticatedUser.getId(), id);
            User targetUser = userService.getUserById(id);
            model.addAttribute("targetUser", targetUser);
            return "user_contact_remove_success";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "user_contact_does_not_exist";
        }
    }

    @PostMapping("/profile")
    public String updateUserProfile(@Valid @ModelAttribute("user") UserDto userDto, @RequestParam(value = "profilePictureFile", required = false) MultipartFile profilePictureFile, BindingResult errors, Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            if (errors.hasErrors()) return "user_edit";
            try {
                User authenticatedUser = userService.getByUsername(username);
                User user = userMapper.fromDto(userDto);
                user.setId(authenticatedUser.getId());
                if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
                    user.setProfilePicture(profilePictureFile.getBytes());
                } else {
                    user.setProfilePicture(authenticatedUser.getProfilePicture());
                }
                user.setUsername(authenticatedUser.getUsername());
                user.setPassword(authenticatedUser.getPassword());
                user.setVerified(authenticatedUser.getVerified());
                user.setUserLevel(authenticatedUser.getUserLevel());
                user.setEnabled(authenticatedUser.isEnabled());
                userService.update(authenticatedUser, user);
                return "user_update_success";
            } catch (EntityNotFoundException e) {
                model.addAttribute("error", "User not found");
                return "errors/404";
            } catch (AuthorizationException e) {
                model.addAttribute("error", "Unauthorized access");
                return "errors/401";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/idVerification")
    public String handleIdVerification(@RequestParam("idCard") MultipartFile idCardFile,
                                       @RequestParam("selfie") MultipartFile selfieFile,
                                       HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username == null) return "redirect:/auth/login";
        User authenticatedUser = userService.getByUsername(username);
        boolean nothingToUpload = true;
        if ((idCardFile != null && !idCardFile.isEmpty()) || (selfieFile != null && !selfieFile.isEmpty())) {
            userService.uploadIdCardAndSelfie(authenticatedUser.getId(), idCardFile, selfieFile);
            nothingToUpload = false;
        }
        if (nothingToUpload) return "user_nothing_to_upload";
        return "user_upload_id_success"; // Redirect to a success page
    }

    @GetMapping("/cpanel")
    public String showAdminPanel(@RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                 Model model, HttpSession session) {
        if (authenticationHelper.isAdmin(session)) {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> usersPage = userService.getAllUsersPage(pageable);
            model.addAttribute("usersPage", usersPage);
            int totalPages = usersPage.getTotalPages();
            model.addAttribute("totalPages", totalPages);

            Page<User> usersUnverifiedPage = userService.getAllUnverifiedUsersPage(pageable);
            model.addAttribute("usersUnverifiedPage", usersUnverifiedPage);
            int totalUnverifiedPages = usersUnverifiedPage.getTotalPages();
            model.addAttribute("totalUnverifiedPages", totalUnverifiedPages);

            Page<User> usersBlockedPage = userService.getAllBlockedUsersPage(pageable);
            model.addAttribute("usersBlockedPage", usersBlockedPage);
            int usersBlockedPages = usersBlockedPage.getTotalPages();
            model.addAttribute("usersBlockedPages", usersBlockedPages);

            Page<User> usersAdminPage = userService.getAllAdminUsersPage(pageable);
            model.addAttribute("usersAdminPage", usersAdminPage);
            int usersAdminPages = usersAdminPage.getTotalPages();
            model.addAttribute("usersAdminPages", usersAdminPages);

            Page<User> usersDeletedPage = userService.getAllDeletedUsersPage(pageable);
            model.addAttribute("usersDeletedPage", usersDeletedPage);
            int usersDeletedPages = usersDeletedPage.getTotalPages();
            model.addAttribute("usersDeletedPages", usersDeletedPages);
            return "admin_panel";
        } else {
            return "errors/401";
        }
    }

    @GetMapping("/delete")
    public String showDeleteUserPage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            User user = userService.getByUsername(username);
            if (user != null) {
                model.addAttribute("user", user);
                return "user_deleted";
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("password") String password, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            try {
                User authenticatedUser = userService.getByUsername(username);
                authenticationHelper.verifyAuthentication(authenticatedUser.getUsername(), password);

                userService.deleteUser(authenticatedUser, authenticatedUser.getId());
                if (userService.getByUsername(username) == null) {
                    session.invalidate();
                }
                return "redirect:/";
            } catch (AuthenticationFailureException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            } catch (EntityNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
    }

}
