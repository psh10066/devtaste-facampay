package com.devtaste.facampay.presentation.common;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class ApiDocumentUtils {

    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
            modifyUris()
                .host("localhost")
                .port(8081),
            prettyPrint());
    }

    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }

}