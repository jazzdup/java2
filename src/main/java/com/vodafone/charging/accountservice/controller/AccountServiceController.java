package com.vodafone.charging.accountservice.controller;

import com.vodafone.charging.accountservice.domain.ChargingId;
import com.vodafone.charging.accountservice.domain.ContextData;
import com.vodafone.charging.accountservice.domain.EnrichedAccountInfo;
import com.vodafone.charging.accountservice.exception.AccountServiceError;
import com.vodafone.charging.accountservice.exception.ApplicationLogicException;
import com.vodafone.charging.accountservice.exception.MethodArgumentValidationException;
import com.vodafone.charging.accountservice.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Spring Rest Controller to glue rest calls to the application logic.
 */
@Api
@RestController
@RequestMapping("/accounts")
@Slf4j
public class AccountServiceController {

    @Autowired
    private AccountService accountService;

    @ApiResponses({@ApiResponse(code = 500, message = "Internal Server Error", response = AccountServiceError.class),
            @ApiResponse(code = 400, message = "Bad Request", response = AccountServiceError.class)})
    @ApiOperation(value = "Obtain enriched charging account information",
            notes = "If you provide some contextual information this operation will process the request and respond with enriched charging account data.  " +
                    "\n In particular properties such as usergroups, customer type, billing account number will be returned ",
            response = EnrichedAccountInfo.class, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            httpMethod = javax.ws.rs.HttpMethod.POST, nickname = "enrichAccountData")

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<EnrichedAccountInfo> enrichAccountData(@RequestHeader HttpHeaders headers,
                                                                 @Valid @RequestBody ContextData contextData) {
        this.checkContextData(contextData);

        EnrichedAccountInfo accountInfo;
        try {
            accountInfo = accountService.enrichAccountData(contextData);
        } catch (Exception e) {
            throw new ApplicationLogicException(e.getMessage(), e);
        }
        return ResponseEntity.ok(accountInfo);
    }

    @RequestMapping(path = "/{chargingType}/{chargingIdValue}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<EnrichedAccountInfo> getAccount(@PathVariable String chargingType, @PathVariable String chargingIdValue) {

        final ChargingId chargingId = new ChargingId.Builder().type(ChargingId.Type.valueOf(chargingType)).value(chargingIdValue).build();
//        final EnrichedAccountInfo accountInfo = accountService.findAccountByChargingId(chargingId);

        throw new UnsupportedOperationException();
    }

    @RequestMapping(path = "/{accountId}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<EnrichedAccountInfo> getAccount(@PathVariable String accountId) {

//        final EnrichedAccountInfo accountInfo = accountService.findAccountByChargingId(accountId);

        throw new UnsupportedOperationException();
    }

    @RequestMapping(path = "/{accountId}/profile/usergroups", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_VALUE})
    public ResponseEntity<List<String>> getUserGroups(@PathVariable long accountId) {
        //Goes to the DB and retrieves a list of usergroups for the customer.  Returned as a list.  AccountId is the key
        throw new UnsupportedOperationException();
    }



    /*
    jsr303 Validation does not appear to work for the ChargingId object within contextInfo.
    Hence this is manually checked here.
     */
    public void checkContextData(final ContextData contextInfo) {
        try {
            checkArgument(isNotEmpty(contextInfo.getChargingId().getValue()), "chargingId.value is compulsory but was empty");
            checkArgument(isNotEmpty(contextInfo.getChargingId().getType()), "chargingId.type is compulsory but was empty");
        } catch (IllegalArgumentException iae) {
            throw new MethodArgumentValidationException(iae.getMessage(), iae);
        }
    }
}
