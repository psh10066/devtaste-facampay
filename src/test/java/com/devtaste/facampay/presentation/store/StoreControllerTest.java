package com.devtaste.facampay.presentation.store;

import com.devtaste.facampay.application.payment.PaymentService;
import com.devtaste.facampay.presentation.common.ControllerTest;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.devtaste.facampay.presentation.common.ApiDocumentUtils.getDocumentRequest;
import static com.devtaste.facampay.presentation.common.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StoreController.class)
public class StoreControllerTest extends ControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentApplicationService;

    private MockMvc mockMvc;

    @BeforeEach
    void before(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("결제 요청")
    @Test
    void postPayment() throws Exception {
        doNothing().when(paymentApplicationService).postPayment(any(PostPaymentRequest.class));

        PostPaymentRequest request = new PostPaymentRequest(1L, 2L, 10000L);

        // when
        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/store/payment")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(document("store/payment",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                    fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("가맹점 고유번호"),
                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 고유번호"),
                    fieldWithPath("money").type(JsonFieldType.NUMBER).description("금액")
                ),
                responseFields(
                    fieldWithPath("rt").type(JsonFieldType.NUMBER).description("결과 코드"),
                    fieldWithPath("rtMsg").type(JsonFieldType.STRING).description("결과 메세지")
                )
            ));
    }
}