package com.vodafone.charging.accountservice.domain.model;

import com.vodafone.charging.accountservice.domain.ChargingId;
import com.vodafone.charging.accountservice.dto.json.ERIFResponse;
import com.vodafone.charging.accountservice.dto.xml.Response;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.*;

@Entity
@Getter @ToString
@Document(collection = "account")
public class Account {
    @Id
    private String id;
    private ChargingId chargingId;
    private Date lastValidate;
    private String customerType;//TODO: add constraint PRE/POST
    private Integer billingCycleDay;
    private List<Profile> profiles;

    public Account(){}

    private Account(String id, ChargingId chargingId, Date lastValidate, String customerType, Integer billingCycleDay, List<Profile> profiles) {
        this.id = id;
        this.chargingId = chargingId;
        this.lastValidate = lastValidate;
        this.customerType = customerType;
        this.billingCycleDay = billingCycleDay;
        this.profiles = profiles;
    }

    public Account(ChargingId chargingId, Response response, Date lastValidate){
        this.chargingId = chargingId;
        this.lastValidate = lastValidate;
        this.customerType = response.getIsPrepay();
        this.billingCycleDay = response.getBillingCycleDay();
        List<String> usergroups = new ArrayList<>();
        Response.UserGroups userGroups = response.getUserGroups();
        if (userGroups != null && userGroups.getItem() != null) {
            usergroups = new ArrayList<String>();
            for (String item : userGroups.getItem()) {
                usergroups.add(item);
            }
        }
        Profile profile = Profile.builder()
                .userGroups(usergroups)
                .build();
        this.profiles = Arrays.asList(profile);
    }
    public Account(ChargingId chargingId, ERIFResponse response, Date lastValidate){
        this.chargingId = chargingId;
        this.lastValidate = lastValidate;
        this.customerType = response.getIsPrepay();
        this.billingCycleDay = response.getBillingCycleDay();
        Profile profile = Profile.builder()
                .userGroups(response.getUserGroups())
                .build();
        this.profiles = Collections.singletonList(profile);
    }

    public Map<String, Object> asMap() throws IllegalAccessException {
        Map<String, Object> values = new HashMap<>();

        Field[] fieldsArr = this.getClass().getDeclaredFields();

        for (Field field : fieldsArr) {
            values.put(field.getName(), field.get(this));
        }
        return values;
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public static class AccountBuilder {
        private String id;
        private ChargingId chargingId;
        private Date lastValidate;
        private String customerType;
        private Integer billingCycleDay;
        private List<Profile> profiles;

        AccountBuilder() {
        }

        public AccountBuilder id(String id) {
            this.id = id;
            return this;
        }

        public AccountBuilder chargingId(ChargingId chargingId) {
            this.chargingId = chargingId;
            return this;
        }

        public AccountBuilder lastValidate(Date lastValidate) {
            this.lastValidate = lastValidate;
            return this;
        }

        public AccountBuilder customerType(String customerType) {
            this.customerType = customerType;
            return this;
        }

        public AccountBuilder billingCycleDay(Integer billingCycleDay) {
            this.billingCycleDay = billingCycleDay;
            return this;
        }

        public AccountBuilder profiles(List<Profile> profiles) {
            this.profiles = profiles;
            return this;
        }

        public Account build() {
            return new Account(id, chargingId, lastValidate, customerType, billingCycleDay, profiles);
        }

        @Override
        public String toString() {
            return "AccountBuilder{" +
                    "id='" + id + '\'' +
                    ", chargingId=" + chargingId +
                    ", lastValidate=" + lastValidate +
                    ", customerType='" + customerType + '\'' +
                    ", billingCycleDay=" + billingCycleDay +
                    ", profiles=" + profiles +
                    '}';
        }

    }
}
