package com.company.web.wallet.helpers;

import com.company.web.wallet.models.User;
import com.company.web.wallet.models.UserSenderDto;
import org.springframework.stereotype.Component;

@Component
public class UserSenderMapper {

    public UserSenderDto toDto(User user) {
        UserSenderDto userSenderDto = new UserSenderDto();
        userSenderDto.setUsername(user.getUsername());
        userSenderDto.setEmail(user.getEmail());
        userSenderDto.setFirstName(user.getFirstName());
        userSenderDto.setLastName(user.getLastName());
        userSenderDto.setAvatar(user.getAvatar());
        return userSenderDto;
    }
    public User toEntity(UserSenderDto userSenderDto) {
        User user = new User();
        user.setUsername(userSenderDto.getUsername());
        user.setFirstName(userSenderDto.getFirstName());
        user.setLastName(userSenderDto.getLastName());
        user.setEmail(userSenderDto.getEmail());
        user.setAvatar(userSenderDto.getAvatar());
        return user;
    }

}
