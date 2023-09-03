package com.company.web.wallet;

import com.company.web.wallet.models.DTO.UserPasswordDto;
import com.company.web.wallet.models.User;

import java.time.LocalDateTime;

public class Auxiliary {

    public Auxiliary() {
    }

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("Root");
        mockUser.setPhone("0888888888");
        mockUser.setPassword("Root");
        mockUser.setEmail("test@mock.com");
        mockUser.setCreateDate(LocalDateTime.now());
        mockUser.setLastUpdateDate(LocalDateTime.now());
        mockUser.setEnabled(true);
        mockUser.setUserLevel(0);

        return mockUser;
    }

    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setUserLevel(0);
        return mockUser;
    }

    public static User createDisabledUser() {
        User mockUser = createMockUser();
        mockUser.setUserLevel(-1);
        return mockUser;
    }

    public static UserPasswordDto createMockDPasswordDto() {
        var mockPasswordDto = new UserPasswordDto();
        mockPasswordDto.setCurrentPassword("Kote2016");
        mockPasswordDto.setNewPassword("KolioChuvashLiMe");
        mockPasswordDto.setPasswordConfirm("KolioChuvashLiMe");
        return mockPasswordDto;
    }
}
