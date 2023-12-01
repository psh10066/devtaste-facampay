package com.devtaste.facampay.presentation.common.response;

import com.devtaste.facampay.infrastructure.exception.message.ResponseMessage;

public class SuccessResponse {

    public int rt;

    public String rtMsg;

    public SuccessResponse() {
        this.rt = 200;
        this.rtMsg = ResponseMessage.SUCCESS_MSG;
    }

    public SuccessResponse(String rtMsg) {
        this.rt = 200;
        this.rtMsg = rtMsg;
    }
}
