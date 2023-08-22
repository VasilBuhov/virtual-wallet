package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.GetSiteURLHelper;
import com.company.web.wallet.helpers.UserMapper;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.UserDto;
import com.company.web.wallet.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;
    private byte[] avatarBytes;
    private final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper,
                              AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {

            authenticationHelper.tryGetUser(httpHeaders);
            List<User> users = userService.getAllUsers();
            return userMapper.toDtoList(users);
        } catch (BlockedUserException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) throws ResponseStatusException {
        try {
            User user = userService.getUserById(id);
            return userMapper.toDtoInfo(user);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public UserDto createUser(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserDto userDto, HttpServletRequest request) throws ResponseStatusException {
        try {
            authenticationHelper.tryGetUser(headers);
            User user = userMapper.fromDto(userDto);
            userService.createUser(user, GetSiteURLHelper.getSiteURL(request));
            return userMapper.toDto(user);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestHeader HttpHeaders headers, @PathVariable int id,
                                             @Valid @RequestBody UserDto userDto) throws ResponseStatusException {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            User user = userMapper.fromDto(userDto);
            user.setId(id);
            userService.update(authenticatedUser, user);
            return ResponseEntity.ok("User with ID " + id + " has been successfully updated.");
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<String> blockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) throws ResponseStatusException {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            if (authenticatedUser.getUserLevel() != 1) {
                throw new AuthorizationException("Only admin can block/unblock users.");
            }

            userService.blockOrUnblock(id, true); // Block the user

            return ResponseEntity.ok("User with ID " + id + " has been blocked.");
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/createAdmin")
    public ResponseEntity<String> makeRegularUserAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) throws ResponseStatusException {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            if (authenticatedUser.getId() != 4) {
                throw new AuthorizationException("Only admin can make other users admins");
            }
            userService.makeRegularUserAdmin(id);
            return ResponseEntity.ok("User with ID " + id + " is now admin");
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<String> unblockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) throws ResponseStatusException {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            if (authenticatedUser.getUserLevel() != 1) {
                throw new AuthorizationException("Only admin can block/unblock users.");
            }

            userService.blockOrUnblock(id, false); // Unblock the user

            return ResponseEntity.ok("User with ID " + id + " has been unblocked.");
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@RequestHeader HttpHeaders headers, @PathVariable int id) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.delete(user, id);
            return ResponseEntity.ok("User with ID " + id + " has been successfully deleted.");
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/upload/{id}")
    public String uploadAvatar(@RequestHeader HttpHeaders headers, @RequestParam("avatarFile") MultipartFile file, @PathVariable int id,
                               @Valid @RequestBody UserDto userDto) throws IOException {
        byte[] avatarBytes = file.getBytes();
        User authenticatedUser = authenticationHelper.tryGetUser(headers);
        User user = userMapper.fromDto(userDto);
        user.setId(id);
        user.setAvatar(avatarBytes);
        userService.update(authenticatedUser, user);
        return "redirect:/";
    }

    @GetMapping("/avatar/{id}")
    public String showUploadForm(Model model, @PathVariable int id) {
        String base64Avatar = Base64.getEncoder().encodeToString(userService.getUserById(id).getAvatar());
        model.addAttribute("avatarBase64", base64Avatar);
        return "user_details";
    }
}
