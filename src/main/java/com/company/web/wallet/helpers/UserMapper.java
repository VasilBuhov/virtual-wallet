package com.company.web.wallet.helpers;

import com.company.web.wallet.models.User;
import com.company.web.wallet.models.DTO.UserDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPassword(user.getPassword());
        userDto.setPhone(user.getPhone());
        userDto.setPokes(user.getPokes());
        userDto.setTFA(user.getTFA());
        userDto.setCards(user.getCards());
        userDto.setWallets(user.getWallets());
        userDto.setSavingsWallets(user.getSavingsWallets());
        return userDto;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());
        user.setPokes(userDto.getPokes());
        user.setProfilePicture(userDto.getAvatar());
        user.setTFA(userDto.getTFA());
        user.setWallets(userDto.getWallets());
        user.setCards(userDto.getCards());
        user.setSavingsWallets(userDto.getSavingsWallets());
        return user;
    }

    public UserDto toDtoInfo(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setAvatar(user.getProfilePicture());
        userDto.setUserLevel(user.getUserLevel());
        userDto.setPokes(user.getPokes());
        userDto.setPhone(user.getPhone());
        userDto.setWallets(user.getWallets());
        userDto.setCards(user.getCards());
        userDto.setSavingsWallets(user.getSavingsWallets());
        return userDto;
    }

    public List<UserDto> toDtoList(List<User> users) {
        List<UserDto> list = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = toDtoInfo(user);
            list.add(userDto);
        }
        return list;
    }
}
