package com.company.web.wallet.controllers.RestController;

import com.company.web.wallet.exceptions.AuthorizationException;
import com.company.web.wallet.exceptions.BlockedUserException;
import com.company.web.wallet.exceptions.EntityNotFoundException;
import com.company.web.wallet.helpers.AuthenticationHelper;
import com.company.web.wallet.helpers.InterestRateMapper;
import com.company.web.wallet.models.InterestRate;
import com.company.web.wallet.models.InterestRateDto;
import com.company.web.wallet.models.User;
import com.company.web.wallet.services.InterestRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/interest")
public class InterestRateRestController {
    private final InterestRateMapper interestRateMapper;
    private final InterestRateService interestRateService;
    private final AuthenticationHelper authenticationHelper;
    private final Logger logger = LoggerFactory.getLogger(WalletRestController.class);

    @Autowired
    public InterestRateRestController(InterestRateMapper interestRateMapper, InterestRateService interestRateService, AuthenticationHelper authenticationHelper) {
        this.interestRateMapper = interestRateMapper;
        this.interestRateService = interestRateService;
        this.authenticationHelper = authenticationHelper;
    }

    @PostMapping("/interest")
    public void updateInterestRate(@Valid @RequestBody InterestRateDto interestRateDto, @RequestHeader HttpHeaders httpHeaders) throws ResponseStatusException {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            InterestRate interestRate = interestRateMapper.create(interestRateDto);
            interestRateService.create(interestRate, user);
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
