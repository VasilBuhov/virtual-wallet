package com.company.web.wallet.services;

import com.company.web.wallet.models.Invitation;
import com.company.web.wallet.models.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface InvitationService {
    boolean checkByEmail(String email);

    Invitation getByEmail(String email);

    void create(Invitation invitation);

    void sendInvitation(User inviter, String targetUserEmail) throws MessagingException, UnsupportedEncodingException;

    int getInvitationsCount(User user);
}
