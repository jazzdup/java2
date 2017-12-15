package com.vodafone.charging.accountservice.controller;

import com.vodafone.charging.accountservice.domain.EnrichedAccountInfo;
import com.vodafone.charging.accountservice.exception.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static com.vodafone.charging.data.builder.ContextDataDataBuilder.aContextData;
import static com.vodafone.charging.data.builder.ContextDataDataBuilder.aContextDataWithNullContextName;
import static com.vodafone.charging.data.builder.EnrichedAccountInfoDataBuilder.aEnrichedAccountInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceControllerTest {

    @InjectMocks
    private AccountServiceController accountServiceController;

    @Before()
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldReturnOk() {
        //given
        final EnrichedAccountInfo expectedAccountInfo = aEnrichedAccountInfo();

        //when
        final ResponseEntity<EnrichedAccountInfo> enrichedAccountInfoResponse =
                accountServiceController.enrichAccountData(aContextData());

        //then
        assertThat(ResponseEntity.ok(expectedAccountInfo))
                .isEqualToIgnoringGivenFields(enrichedAccountInfoResponse, "body");
        assertThat(expectedAccountInfo).isEqualToComparingFieldByField(enrichedAccountInfoResponse.getBody());
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleNullBody() {
        accountServiceController.enrichAccountData(null);
    }

    @Test
    public void shouldHandleEmptyMandatoryValue() {
        assertThatThrownBy(() -> accountServiceController.enrichAccountData(aContextDataWithNullContextName()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Mandatory Content is not provided in request body")
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}
