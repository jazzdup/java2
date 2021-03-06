package com.vodafone.charging.accountservice.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
public class MessageControl{
    private Locale locale;

    public MessageControl(Locale locale) {
        this.locale = locale;
    }
}
