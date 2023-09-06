package com.company.web.wallet.repositories;

import com.company.web.wallet.models.Invitation;
import com.company.web.wallet.models.User;

public interface InvitationRepository {
    void create(Invitation invitation);

    Invitation getByEmail(String email);

    int getInvitationsCount(User user);
}
