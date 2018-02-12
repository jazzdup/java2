package com.vodafone.charging.integrationtest.http;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.vodafone.charging.accountservice.AccountServiceApplication;
import com.vodafone.charging.accountservice.domain.ChargingId;
import com.vodafone.charging.accountservice.domain.ContextData;
import com.vodafone.charging.accountservice.domain.ERIFResponse;
import com.vodafone.charging.accountservice.domain.EnrichedAccountInfo;
import com.vodafone.charging.accountservice.domain.xml.Response;
import com.vodafone.charging.accountservice.properties.PropertiesAccessor;
import com.vodafone.charging.data.builder.ContextDataDataBuilder;
import com.vodafone.charging.data.message.JsonConverter;
import com.vodafone.charging.mock.WiremockPreparer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static com.google.common.collect.Lists.newArrayList;
import static com.vodafone.charging.data.ApplicationPortsEnum.DEFAULT_ER_IF_PORT;
import static com.vodafone.charging.data.builder.ChargingIdDataBuilder.aChargingId;
import static com.vodafone.charging.data.builder.HttpHeadersDataBuilder.aHttpHeaders;
import static com.vodafone.charging.data.builder.IFResponseData.aERIFResponse;
import static com.vodafone.charging.data.builder.IFResponseData.anXmlResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;


//TODO This test needs to be reviewed.  Wiremock may not be the best approach.
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountServiceApplication.class, webEnvironment = DEFINED_PORT)
public class VfAccountServiceHttpTest {

    private static final Logger log = LoggerFactory.getLogger(VfAccountServiceHttpTest.class);

    private String url = "http://localhost:8080/accounts";
    private String erifUrl = "http://localhost:8458/broker/router.jsp";

    @Autowired
    private JsonConverter jsonConverter;

    @MockBean
    PropertiesAccessor propertiesAccessor;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(DEFAULT_ER_IF_PORT.value());

    @Test
    public void shouldValidateAccountAndReturnOKAgainstMockedERIFJson() throws Exception {
        //given
        given(propertiesAccessor.getProperty(eq("erif.url"), anyString())).willReturn(erifUrl);
        final ERIFResponse erifResponse = aERIFResponse();
        //set expectedInfo to be what we're setting in the mock
        final EnrichedAccountInfo expectedInfo = new EnrichedAccountInfo(erifResponse);
        ChargingId chargingId = aChargingId();
        final ContextData contextData = ContextDataDataBuilder.aContextData(chargingId);
        HttpHeaders headers = aHttpHeaders(contextData.getClientId(),
                contextData.getLocale(),
                contextData.getChargingId());

        WiremockPreparer.prepareForValidateJson(chargingId);

        //when
        ResponseEntity<EnrichedAccountInfo> responseEntity = testRestTemplate.exchange(url, POST, new HttpEntity<>(contextData, headers), EnrichedAccountInfo.class);
        EnrichedAccountInfo enrichedAccountInfo = responseEntity.getBody();

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(enrichedAccountInfo).isEqualToIgnoringGivenFields(expectedInfo, "usergroups");
        //ERIF reverses the usergroups order
        assertThat(enrichedAccountInfo.getUsergroups().get(0)).isEqualTo(expectedInfo.getUsergroups().get(1));
        assertThat(enrichedAccountInfo.getUsergroups().get(1)).isEqualTo(expectedInfo.getUsergroups().get(0));
    }
    @Test
    public void shouldValidateAccountAndReturnOKAgainstMockedERIFSoap() throws Exception {
        //given
        given(propertiesAccessor.getPropertyForOpco(eq("erif.communication.protocol"), anyString(), anyString())).willReturn("soap");
        given(propertiesAccessor.getProperty(eq("erif.url"), anyString())).willReturn(erifUrl);

        final Response erifResponse = anXmlResponse();
        //set expectedInfo to be what we're setting in the mock
        final EnrichedAccountInfo expectedInfo = new EnrichedAccountInfo(erifResponse);
        ChargingId chargingId = aChargingId();
        final ContextData contextData = ContextDataDataBuilder.aContextData(chargingId);
        HttpHeaders headers = aHttpHeaders(contextData.getClientId(),
                contextData.getLocale(),
                contextData.getChargingId());

        WiremockPreparer.prepareForValidateSoap(erifResponse);

        //when
        ResponseEntity<EnrichedAccountInfo> responseEntity = testRestTemplate.exchange(url, POST, new HttpEntity<>(contextData, headers), EnrichedAccountInfo.class);
        EnrichedAccountInfo enrichedAccountInfo = responseEntity.getBody();

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expectedInfo).isEqualToComparingFieldByFieldRecursively(enrichedAccountInfo);
    }
    @Test
    public void shouldAcceptJsonString() throws Exception {

        //given
        given(propertiesAccessor.getProperty(eq("erif.url"), anyString())).willReturn(erifUrl);
        final ERIFResponse expectedResponse = aERIFResponse();
        ChargingId chargingId = aChargingId();
        final ContextData contextData = ContextDataDataBuilder.aContextData(chargingId);
        WiremockPreparer.prepareForValidateJson(chargingId);

        HttpHeaders headers = aHttpHeaders(contextData.getClientId(),
                contextData.getLocale(),
                contextData.getChargingId());
        headers.setAccept(newArrayList(APPLICATION_JSON_UTF8, APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON_UTF8);

        log.info(jsonConverter.toJson(contextData.asMap()) + "\n\n");
        RequestEntity<String> requestEntity = new RequestEntity<>(jsonConverter.toJson(contextData.asMap()), headers, POST, URI.create(url));


        //when
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(url, POST, requestEntity, Object.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //TODO Deserialize the String response and check
    }

}