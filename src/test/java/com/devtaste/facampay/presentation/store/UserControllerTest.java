package com.devtaste.facampay.presentation.store;

import com.devtaste.facampay.application.payment.PaymentService;
import com.devtaste.facampay.application.payment.dto.PaymentStoreDTO;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.presentation.common.ControllerTest;
import com.devtaste.facampay.presentation.user.UserController;
import com.devtaste.facampay.presentation.user.request.PostPaymentAttemptRequest;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.devtaste.facampay.presentation.common.ApiDocumentUtils.getDocumentRequest;
import static com.devtaste.facampay.presentation.common.ApiDocumentUtils.getDocumentResponse;
import static com.devtaste.facampay.presentation.common.DocumentAttributeGenerator.dateTimeFormat;
import static com.devtaste.facampay.presentation.common.DocumentAttributeGenerator.paymentStatusFormat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    private MockMvc mockMvc;

    @BeforeEach
    void before(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("결제 목록 조회")
    @Test
    void getPaymentList() throws Exception {
        long userId = 1;
        given(paymentService.getPaymentList(any(Long.class))).willReturn(List.of(
            PaymentStoreDTO.of(2L, "가맹점2", 5000L, PaymentStatusType.SUCCESS, LocalDateTime.of(2023, 12, 4, 18, 12, 3, 654321)),
            PaymentStoreDTO.of(1L, "가맹점1", 10000L, PaymentStatusType.WAITING, LocalDateTime.of(2023, 12, 3, 21, 47, 8, 123456))
        ));

        // when
        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/user/payment/list/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(document("user/payment/list",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("userId").description("사용자 고유번호")
                ),
                responseFields(
                    fieldWithPath("rt").type(JsonFieldType.NUMBER).description("결과 코드"),
                    fieldWithPath("rtMsg").type(JsonFieldType.STRING).description("결과 메세지"),
                    fieldWithPath("response").type(JsonFieldType.OBJECT).description("결과 데이터"),
                    fieldWithPath("response.paymentList").type(JsonFieldType.ARRAY).description("결제 목록"),
                    fieldWithPath("response.paymentList[].paymentId").type(JsonFieldType.NUMBER).description("결제 고유번호"),
                    fieldWithPath("response.paymentList[].storeName").type(JsonFieldType.STRING).description("가맹점 명"),
                    fieldWithPath("response.paymentList[].money").type(JsonFieldType.NUMBER).description("결제 금액"),
                    fieldWithPath("response.paymentList[].paymentStatus").type(JsonFieldType.STRING).attributes(paymentStatusFormat()).description("결제 상태"),
                    fieldWithPath("response.paymentList[].createdAt").type(JsonFieldType.STRING).attributes(dateTimeFormat()).description("결제 생성 시각")
                )
            ));

        then(paymentService).should().getPaymentList(userId);
    }

    @DisplayName("결제 시도")
    @Test
    void postPaymentAttempt() throws Exception {
        PostPaymentAttemptRequest request = new PostPaymentAttemptRequest(1L, 2L);

        doNothing().when(paymentService).postPaymentAttempt(request);

        // when
        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/user/payment/attempt")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(document("user/payment/attempt",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 고유번호"),
                    fieldWithPath("paymentId").type(JsonFieldType.NUMBER).description("결제 고유번호")
                ),
                responseFields(
                    fieldWithPath("rt").type(JsonFieldType.NUMBER).description("결과 코드"),
                    fieldWithPath("rtMsg").type(JsonFieldType.STRING).description("결과 메세지")
                )
            ));

        then(paymentService).should().postPaymentAttempt(request);
    }
}