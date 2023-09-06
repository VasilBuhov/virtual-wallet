package com.company.web.wallet.services;

import com.company.web.wallet.models.Invitation;
import com.company.web.wallet.models.User;
import com.company.web.wallet.repositories.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public InvitationServiceImpl(InvitationRepository invitationRepository, JavaMailSender mailSender) {
        this.invitationRepository = invitationRepository;
        this.mailSender = mailSender;
    }

    @Override
    public Invitation getByEmail(String email) {
        return invitationRepository.getByEmail(email);
    }

    @Override
    public void create(Invitation invitation) {
        invitationRepository.create(invitation);
    }

    @Override
    public void sendInvitation(User inviter, String targetUserEmail) throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "wallet.project.a48@badmin.org";
        String senderName = "The Wallet App";
        String subject = "You`ve been invited to register ot the Wallet App!";
        String content = "Dear User,<br>"
                + "You have been invited by [[Inviter]] to join the Wallet App - a virtual wallet application.<br>"
                + "Please click the link below to register in the next 7 days and get a bonus of 0.05USD on your wallet:<br>"
                + "<h2>[[registrationLink]]</h2>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(targetUserEmail);
        helper.setSubject(subject);

        content = content.replace("[[Inviter]]", inviter.getUsername());
        content = content.replace("[[registrationLink]]", "http://localhost:8080/users/new/invited");
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public int getInvitationsCount(User user) {
        return invitationRepository.getInvitationsCount(user);
    }
}
