package com.devtaste.facampay.presentation.common;

import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import org.springframework.restdocs.snippet.Attributes;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.restdocs.snippet.Attributes.key;

public class DocumentAttributeGenerator {

    public static Attributes.Attribute setFormat(String value) {
        return key("format").value(value);
    }

    public static Attributes.Attribute dateTimeFormat() {
        return setFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    public static Attributes.Attribute paymentStatusFormat() {
        return setFormat(Arrays.stream(PaymentStatusType.values()).map(o -> o + " : " + o.getDescription()).collect(Collectors.joining(", ")));
    }
}