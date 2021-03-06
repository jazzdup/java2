package com.vodafone.charging.accountservice.domain;

import com.vodafone.charging.accountservice.dto.json.ERIFResponse;
import com.vodafone.charging.accountservice.dto.xml.Response;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.vodafone.charging.accountservice.service.ERDateCalculator.isValidBillingCycleDay;

/**
 * Represents the response to the client after external calls have completed
 */
@Component
public class EnrichedAccountInfo {
    private String accountId;
    private String validationStatus;
    private String ban;
    private List<String> usergroups;
    private Integer billingCycleDay;
    private String serviceProviderId;
    private String childServiceProviderId;
    private String serviceProviderType;
    private String customerType;// is the PRE/POST string for prepay type
    private String errorId;
    private String errorDescription;

    public EnrichedAccountInfo() {
    }

    public EnrichedAccountInfo(@NonNull ERIFResponse erifResponse, String accountId){
        this.accountId = accountId;
        validationStatus = erifResponse.getStatus();
        ban = erifResponse.getBan();
        usergroups = erifResponse.getUserGroups();
        if(isValidBillingCycleDay(erifResponse.getBillingCycleDay())) {
            billingCycleDay = erifResponse.getBillingCycleDay();
        }
        serviceProviderId = erifResponse.getSpId();
        childServiceProviderId = erifResponse.getChildSpId();
        serviceProviderType = erifResponse.getSpType();
        serviceProviderId = erifResponse.getSpId();
        customerType = erifResponse.getIsPrepay();
        errorId = erifResponse.getErrId();
        errorDescription = erifResponse.getErrDescription();
    }

    /**
     * for not-quite-soap version
     * @param response
     */
    public EnrichedAccountInfo(@NonNull Response response, String accountId){
        this.accountId = accountId;
        validationStatus = response.getStatus();
        ban = response.getBan();
        Response.UserGroups userGroups = response.getUserGroups();
        if (userGroups != null && userGroups.getItem() != null) {
            usergroups = new ArrayList<>();
            for (String item : userGroups.getItem()) {
                usergroups.add(item);
            }
        }
        billingCycleDay = response.getBillingCycleDay();
        serviceProviderId = response.getSpId();
        childServiceProviderId = response.getChildSpId();
        serviceProviderType = response.getSpType();
        serviceProviderId = response.getSpId();
        customerType = response.getIsPrepay();
        errorId = response.getErrId();
        errorDescription = response.getErrDescription();
    }

    private EnrichedAccountInfo(final Builder builder) {
        this.accountId = builder.accountId;
        this.validationStatus = builder.validationStatus;
        this.ban = builder.ban;
        this.usergroups = builder.usergroups;
        this.billingCycleDay = builder.billingCycleDay;
        this.serviceProviderId = builder.serviceProviderId;
        this.childServiceProviderId = builder.childServiceProviderId;
        this.serviceProviderType = builder.serviceProviderType;
        this.customerType = builder.customerType;
        this.errorId = builder.errorId;
        this.errorDescription = builder.errorDescription;
    }

    public String getAccountId(){return accountId;}
    public String getValidationStatus() {
        return validationStatus;
    }

    public String getBan() {
        return ban;
    }

    public List<String> getUsergroups() {
        return usergroups;
    }

    public int getBillingCycleDay() {
        return billingCycleDay;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public String getChildServiceProviderId() {
        return childServiceProviderId;
    }

    public String getServiceProviderType() {
        return serviceProviderType;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getErrorId() {
        return errorId;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public static class Builder {
        private String accountId;
        private String validationStatus;
        private String ban;
        private List<String> usergroups;
        private Integer billingCycleDay;
        private String serviceProviderId;
        private String childServiceProviderId;
        private String serviceProviderType;
        private String customerType;
        private String errorId;
        private String errorDescription;

        public Builder(String validationStatus) {
            this.validationStatus = validationStatus;
        }
        public Builder accountId(final String accountId){
            this.accountId = accountId;
            return this;
        }
        public Builder ban(final String ban) {
            this.ban = ban;
            return this;
        }

        public Builder usergroups(final List<String> usergroups) {
            this.usergroups = usergroups;
            return this;
        }

        public Builder billingCycleDay(final int billingCycleDay) {
            this.billingCycleDay = billingCycleDay;
            return this;
        }

        public Builder serviceProviderId(final String serviceProviderId) {
            this.serviceProviderId = serviceProviderId;
            return this;
        }
        public Builder childServiceProviderId(final String childServiceProviderId) {
            this.childServiceProviderId = childServiceProviderId;
            return this;
        }
        public Builder serviceProviderType(final String serviceProviderType) {
            this.serviceProviderType = serviceProviderType;
            return this;
        }
        public Builder customerType(final String customerType) {
            this.customerType = customerType;
            return this;
        }
        public Builder errorId(final String errorId) {
            this.errorId = errorId;
            return this;
        }
        public Builder errorDescription(final String errorDescription) {
            this.errorDescription = errorDescription;
            return this;
        }
        public EnrichedAccountInfo build() {
            return new EnrichedAccountInfo(this);
        }
    }
}
