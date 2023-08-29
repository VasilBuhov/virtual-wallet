package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.exceptions.EntityDuplicateException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.GetSiteURLHelper;
import com.company.web.wallet.helpers.UserMapper;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.UserDto;
import com.company.web.wallet.services.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final UserService userService;

    private final UserMapper userMapper;

    public UserMvcController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/process_register")
    public String processRegister(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        userService.createUser(user, GetSiteURLHelper.getSiteURL(request));
        return "register_success";
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model, HttpSession session) {
//        String username = (String) session.getAttribute("currentUser");
//        if (username == null) {
//            return "redirect:/auth/login";
//        }
        try {
            User user = userService.getUserById(id);
            String base64Avatar = "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAIAAABMXPacAAABUUlEQVR4nOzTQQnDQABE0VIWugp6KfRWXfUTNdEVFRGxh0fIfwoGPjP27fW4sjH/esKSpx5wdwXACoAVACsAVgCsAFgBsAJgBcAKgBUAKwBWAKwAWAGwAmAFwAqAFQArAFYArABYAbACYAXACoAVACsAVgCsAFgBsAJgBcAKgBUAKwBWAKwAWAGwAmAFwAqAFQArAFYArABYAbACYAXACoAVACsAVgCsAFgBsAJgBcAKgBUAKwBWAKwA2Pi+p96w5PM79IQlPQArAFYArABYAbACYAXACoAVACsAVgCsAFgBsAJgBcAKgBUAKwBWAKwAWAGwAmAFwAqAFQArAFYArABYAbACYAXACoAVACsAVgCsAFgBsAJgBcAKgBUAKwBWAKwAWAGwAmAFwAqAFQArAFYArABYAbACYAXACoAVACsAVgCsAFgBsAJgBcDOAAAA//9cNgQXxEbgzwAAAABJRU5ErkJggg==";
            byte[] avatarData = user.getAvatar();
            String base64DB = Base64.getEncoder().encodeToString(avatarData);
            model.addAttribute("base64avatar", base64DB);
            model.addAttribute("user", user);
            return "user_details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/new")
    public String showNewUserPage(Model model){
        model.addAttribute("user", new UserDto());
        return "user-new";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult errors, Model model) {
        if(errors.hasErrors()){
            model.addAttribute("errorMessage", "Please fill in all required fields.");
            return "user-new";
        }
        try {
            User newUser = userMapper.fromDto(userDto);
            userService.createUser(newUser, "localhost");
            return "redirect:/" ;
        } catch (EntityDuplicateException e) {
            model.addAttribute("alreadyExists", e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return "AlreadyExistsView";
    }

}
