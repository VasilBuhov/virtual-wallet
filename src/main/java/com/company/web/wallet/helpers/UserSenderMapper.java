package com.company.web.wallet.helpers;

import com.company.web.wallet.models.User;
import com.company.web.wallet.models.DTO.UserSenderDto;
import com.company.web.wallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSenderMapper {
    private final UserRepository userRepository;

    @Autowired
    public UserSenderMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserSenderDto toDto(User user) {
        UserSenderDto userSenderDto = new UserSenderDto();
        userSenderDto.setUsername(user.getUsername());
        userSenderDto.setEmail(user.getEmail());
        userSenderDto.setFirstName(user.getFirstName());
        userSenderDto.setLastName(user.getLastName());

        return userSenderDto;
    }
    public User toEntity(UserSenderDto userSenderDto) {
        User user = new User();
        user.setUsername(userSenderDto.getUsername());
        user.setFirstName(userSenderDto.getFirstName());
        user.setLastName(userSenderDto.getLastName());
        user.setEmail(userSenderDto.getEmail());


        return user;
    }
    public User findUserByEmailOrUsername(String emailOrUsername) {
        return userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername);
    }

}
