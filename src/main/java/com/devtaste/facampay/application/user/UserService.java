package com.devtaste.facampay.application.user;

import com.devtaste.facampay.application.payment.dto.PaymentDTO;
import com.devtaste.facampay.application.user.dto.UserDTO;
import com.devtaste.facampay.domain.model.payment.PaymentRepository;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUser;
import com.devtaste.facampay.domain.model.storeToUser.StoreToUserRepository;
import com.devtaste.facampay.domain.model.user.UserRepository;
import com.devtaste.facampay.infrastructure.exception.CustomException;
import com.devtaste.facampay.infrastructure.exception.response.type.ErrorType;
import com.devtaste.facampay.presentation.user.request.UserListRequest;
import com.devtaste.facampay.presentation.user.response.UserPaymentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final StoreToUserRepository storeToUserRepository;

    public List<UserDTO> getUserList(Long storeId, UserListRequest request) {
        return userRepository.searchStoreToUserList(storeId, request);
    }

    public UserPaymentListResponse getPaymentUser(Long storeId, Long userId) {
        StoreToUser storeToUser = storeToUserRepository.findByStore_StoreIdAndUser_UserId(storeId, userId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_STORE_TO_USER));

        List<PaymentDTO> paymentList = paymentRepository.findByStore_StoreIdAndUser_UserIdOrderByCreatedAtDesc(storeId, userId).stream()
            .map(PaymentDTO::from)
            .toList();

        return UserPaymentListResponse.of(UserDTO.from(storeToUser.getUser()), paymentList);
    }
}
