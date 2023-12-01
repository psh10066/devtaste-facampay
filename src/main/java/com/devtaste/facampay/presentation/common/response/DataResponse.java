package com.devtaste.facampay.presentation.common.response;

public class DataResponse<T> extends SuccessResponse {

    public T response;

    public DataResponse(T response) {
        this.response = response;
    }
}
