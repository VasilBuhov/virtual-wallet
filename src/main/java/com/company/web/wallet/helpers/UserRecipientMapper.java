package com.company.web.wallet.helpers;

import com.company.web.wallet.helpers.UserMapper;
import com.company.web.wallet.models.User;
import com.company.web.wallet.models.UserDto;
import com.company.web.wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRecipientMapper {

    private final UserRepository userRepository;

    @Autowired
    public UserRecipientMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
//        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
    public User findUserByEmailOrUsername(String emailOrUsername) {
        return userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername);
    }

//    public User toEntity(UserDto userDto) {
//        User user = userRepository.findByUsernameOrPhoneNumberOrEmail(userDto.getRecipientIdentifier(),
//                        userDto.getRecipientIdentifier(), userDto.getRecipientIdentifier())
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//        return user;
//    }
}
