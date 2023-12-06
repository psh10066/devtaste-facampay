package com.devtaste.facampay.application.user;

import com.devtaste.facampay.application.user.dto.UserDTO;
import com.devtaste.facampay.domain.model.payment.Payment;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUser;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUserRepository;
import com.devtaste.facampay.domain.model.user.User;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.presentation.user.request.UserListRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private StoreToUserRepository storeToUserRepository;

    @DisplayName("회원 목록 조회")
    @Test
    public void getUserList() {
        long storeId = 1;
        UserListRequest request = UserListRequest.of("사용자1", "user@facam.com");

        given(userRepository.searchStoreToUserList(storeId, request)).willReturn(List.of(
            UserDTO.of(1L, "사용자1", "user@facam.com"),
            UserDTO.of(2L, "사용자11", "gooduser@facam.com")
        ));

        userService.getUserList(storeId, request);

        then(userRepository).should().searchStoreToUserList(storeId, request);
    }

    @DisplayName("회원 결제 정보 조회")
    @Test
    public void getPaymentUser() {
        long storeId = 1;
        long userId = 2;
        Store store = Store.of("store@facam.com", "가맹점1", 0L);
        User user = User.of("user@facam.com", "사용자1", 25000L);

        given(storeToUserRepository.findByStore_StoreIdAndUser_UserId(storeId, userId)).willReturn(Optional.of(StoreToUser.of(store, user)));
        given(paymentRepository.findByStore_StoreIdAndUser_UserIdOrderByCreatedAtDesc(storeId, userId)).willReturn(List.of(
            Payment.of(store, user, 10000L, PaymentStatusType.WAITING),
            Payment.of(store, user, 20000L, PaymentStatusType.SUCCESS)
        ));

        userService.getPaymentUser(storeId, userId);

        then(storeToUserRepository).should().findByStore_StoreIdAndUser_UserId(storeId, userId);
        then(paymentRepository).should().findByStore_StoreIdAndUser_UserIdOrderByCreatedAtDesc(storeId, userId);
    }
}