package com.vodafone.charging.accountservice.service;

import com.vodafone.charging.accountservice.domain.AccountSummary;
import com.vodafone.charging.accountservice.domain.EnrichedAccountData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountValidationService {

    private static final Logger log = LoggerFactory.getLogger(AccountValidationService.class);

    public EnrichedAccountData validateChargingId(AccountSummary accountSummary) {

        //TODO call IF api to call ER IF
        throw new UnsupportedOperationException();
    }
}
