package com.vodafone.charging.accountservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ApiModel(description = "Unique Identifier for Charging Customers")
public class ChargingId {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Type {
        VODAFONE_ID("vodafoneid"),
        MSISDN("msisdn"),
        PSTN("pstn"),
        STB("stb");

        private final String chargingType;

        Type(String type) {
            this.chargingType = type;
        }

        public String type() {
            return chargingType;
        }
    }

    @ApiModelProperty(value = "Vodafone Charging Account Type", required = true, allowableValues = "msisdn,vodafoneid,pstn,stb")
    @NotBlank
    private String type;

    @ApiModelProperty(value = "Vodafone Charging Account Identifier", required = true)
    @NotBlank
    private String value;

    public ChargingId() {
    }

    public ChargingId(final Type type, final String value) {
        this.type = type.type();
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public static class Builder {
        private Type type;
        private String value;

        public ChargingId.Builder type(final Type type) {
            this.type = type;
            return this;
        }

        public ChargingId.Builder value(final String value) {
            this.value = value;
            return this;
        }

        public ChargingId build() {
            return new ChargingId(type, value);
        }
    }

    @Override
    public String toString() {
        return "ChargingId{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }

    public static Optional<ChargingId> fromString(String string, String value) {
        for(Type type : Type.values()) {
            if (type.type().equalsIgnoreCase(string) ) {
                return Optional.of(new ChargingId.Builder().type(type).value(value).build());
            }
        }
        return Optional.empty();
    }

    public String toIfString() {
        return type + ": " + value;
    }
}
