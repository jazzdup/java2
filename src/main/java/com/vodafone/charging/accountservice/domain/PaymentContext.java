package com.vodafone.charging.accountservice.domain;

import com.vodafone.charging.accountservice.dto.client.CatalogInfo;
import com.vodafone.charging.accountservice.dto.client.TransactionInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Context information about a payment (e.g. transaction details, any account attributes required, catalogInfo)
 */
@Builder @Getter
public class PaymentContext {

    @NonNull
    private Locale locale;

    @NonNull
    @Autowired
    private ChargingId chargingId;

    @NonNull
    @Autowired
    private TransactionInfo transactionInfo;

    @Autowired
    private CatalogInfo catalogInfo;

    @Nullable
//    @Autowired
    private ApprovalCriteria approvalCriteria;


}
