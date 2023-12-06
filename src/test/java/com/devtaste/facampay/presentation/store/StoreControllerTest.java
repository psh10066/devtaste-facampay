package com.devtaste.facampay.presentation.store;

import com.devtaste.facampay.application.payment.PaymentService;
import com.devtaste.facampay.application.payment.dto.PaymentDTO;
import com.devtaste.facampay.application.payment.dto.PaymentStoreDTO;
import com.devtaste.facampay.application.user.UserService;
import com.devtaste.facampay.application.user.dto.UserDTO;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.presentation.common.ControllerTest;
import com.devtaste.facampay.presentation.store.request.PostPaymentRequest;
import com.devtaste.facampay.presentation.user.request.UserListRequest;
import com.devtaste.facampay.presentation.user.response.UserPaymentListResponse;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StoreController.class)
public class StoreControllerTest extends ControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private PaymentService paymentService;

    private MockMvc mockMvc;

    @BeforeEach
    void before(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("회원 목록 조회")
    @Test
    void getUserList() throws Exception {
        long storeId = 1;
        UserListRequest request = UserListRequest.of("사용자1", "user@facam.com");

        given(userService.getUserList(storeId, request)).willReturn(List.of(
            UserDTO.of(1L, "사용자1", "user@facam.com"),
            UserDTO.of(2L, "사용자11", "gooduser@facam.com")
        ));

        // when
        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/store/user/list/{storeId}?userName={userName}&userEmail={userEmail}", storeId, request.userName(), request.userEmail())
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(document("store/user/list",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("storeId").description("가맹점 고유번호")
                ),
                queryParameters(
                    parameterWithName("userName").optional().description("사용자 명 (검색)"),
                    parameterWithName("userEmail").optional().description("사용자 이메일 (검색)")
                ),
                responseFields(
                    fieldWithPath("rt").type(JsonFieldType.NUMBER).description("결과 코드"),
                    fieldWithPath("rtMsg").type(JsonFieldType.STRING).description("결과 메세지"),
                    fieldWithPath("response").type(JsonFieldType.OBJECT).description("결과 데이터"),
                    fieldWithPath("response.userList").type(JsonFieldType.ARRAY).description("사용자 목록"),
                    fieldWithPath("response.userList[].userId").type(JsonFieldType.NUMBER).description("사용자 고유번호"),
                    fieldWithPath("response.userList[].userName").type(JsonFieldType.STRING).description("사용자 명"),
                    fieldWithPath("response.userList[].userEmail").type(JsonFieldType.STRING).description("사용자 이메일")
                )
            ));

        then(userService).should().getUserList(storeId, request);
    }

    @DisplayName("회원 결제 정보 조회")
    @Test
    void getPaymentUser() throws Exception {
        long storeId = 1;
        long userId = 1;

        given(userService.getPaymentUser(storeId, userId)).willReturn(UserPaymentListResponse.of(
            UserDTO.of(1L, "사용자1", "user@facam.com"),
            List.of(
                PaymentDTO.of(2L, 10000L, PaymentStatusType.WAITING, LocalDateTime.of(2023, 12, 6, 12, 34, 56)),
                PaymentDTO.of(1L, 20000L, PaymentStatusType.SUCCESS, LocalDateTime.of(2023, 11, 23, 12, 34, 56))
            )
        ));

        // when
        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/store/user/{storeId}/{userId}", storeId, userId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(document("store/user",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("storeId").description("가맹점 고유번호"),
                    parameterWithName("userId").description("사용자 고유번호")
                ),
                responseFields(
                    fieldWithPath("rt").type(JsonFieldType.NUMBER).description("결과 코드"),
                    fieldWithPath("rtMsg").type(JsonFieldType.STRING).description("결과 메세지"),
                    fieldWithPath("response").type(JsonFieldType.OBJECT).description("결과 데이터"),
                    fieldWithPath("response.user").type(JsonFieldType.OBJECT).description("사용자 정보"),
                    fieldWithPath("response.user.userId").type(JsonFieldType.NUMBER).description("사용자 고유번호"),
                    fieldWithPath("response.user.userName").type(JsonFieldType.STRING).description("사용자 명"),
                    fieldWithPath("response.user.userEmail").type(JsonFieldType.STRING).description("사용자 이메일"),
                    fieldWithPath("response.paymentList").type(JsonFieldType.ARRAY).description("결제 목록"),
                    fieldWithPath("response.paymentList[].paymentId").type(JsonFieldType.NUMBER).description("결제 고유번호"),
                    fieldWithPath("response.paymentList[].money").type(JsonFieldType.NUMBER).description("결제 금액"),
                    fieldWithPath("response.paymentList[].paymentStatus").type(JsonFieldType.STRING).attributes(paymentStatusFormat()).description("결제 상태"),
                    fieldWithPath("response.paymentList[].createdAt").type(JsonFieldType.STRING).attributes(dateTimeFormat()).description("결제 생성 시각")
                )
            ));

        then(userService).should().getPaymentUser(storeId, userId);
    }

    @DisplayName("결제 요청")
    @Test
    void postPayment() throws Exception {
        PostPaymentRequest request = new PostPaymentRequest(1L, 2L, 10000L);
        doNothing().when(paymentService).postPayment(request);

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

        then(paymentService).should().postPayment(request);
    }

    @DisplayName("결제 취소")
    @Test
    void cancelPayment() throws Exception {
        long storeId = 1L;
        long paymentId = 2L;
        doNothing().when(paymentService).cancelPayment(storeId, paymentId);

        // when
        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/store/payment/{storeId}/{paymentId}", storeId, paymentId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("rt").value(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(document("store/payment/cancel",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("storeId").description("가맹점 고유번호"),
                    parameterWithName("paymentId").description("결제 고유번호")
                ),
                responseFields(
                    fieldWithPath("rt").type(JsonFieldType.NUMBER).description("결과 코드"),
                    fieldWithPath("rtMsg").type(JsonFieldType.STRING).description("결과 메세지")
                )
            ));

        then(paymentService).should().cancelPayment(storeId, paymentId);
    }
}