package com.vodafone.charging.accountservice.domain;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the response to the client after external calls have completed
 */
@Component
public class EnrichedAccountInfo {

    private String validationStatus;
    private String ban;
    private List<String> usergroups;
    private int billingCycleDay;
    private String serviceProviderId;
    private String childServiceProviderId;
    private String serviceProviderType;
    private boolean isPrepay;
    private String errorId;
    private String errorDescription;

    public EnrichedAccountInfo() {
    }

    private EnrichedAccountInfo(EnrichedAccountInfo.Builder builder) {
        this.validationStatus = builder.validationStatus;
        this.ban = builder.ban;
        this.billingCycleDay = builder.billingCycleDay;
        this.serviceProviderId = builder.serviceProviderId;
        this.childServiceProviderId = builder.childServiceProviderId;
        this.serviceProviderType = builder.serviceProviderType;
        this.isPrepay = builder.isPrepay;
        this.errorId = builder.errorId;
        this.errorDescription = builder.errorDescription;
    }

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

    public boolean isPrepay() {
        return isPrepay;
    }

    public String getErrorId() {
        return errorId;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public static class Builder {

        private String validationStatus;
        private String ban;
        private List<String> usergroups;
        private int billingCycleDay;
        private String serviceProviderId;
        private String childServiceProviderId;
        private String serviceProviderType;
        private boolean isPrepay;
        private String errorId;
        private String errorDescription;

        public Builder validationStatus(final String validationStatus) {
            this.validationStatus = validationStatus;
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
        public Builder isPrepay(final boolean isPrepay) {
            this.isPrepay = isPrepay;
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
