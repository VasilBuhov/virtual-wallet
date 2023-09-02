package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.AuthenticationFailureException;
import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.GetSiteURLHelper;
import com.company.web.wallet.helpers.UserMapper;
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

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserMvcController(AuthenticationHelper authenticationHelper, UserService userService, UserMapper userMapper) {
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
            }
            else
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
                    System.out.println("Picture h1t on upload");
                    System.out.println("profilePictureFile: " + profilePictureFile);
                } else {
                    user.setProfilePicture(user.getProfilePicture());
                    System.out.println("Picture considered empty");
                    System.out.println("profilePictureFile: " + profilePictureFile);
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
