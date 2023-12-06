package com.devtaste.facampay.domain.model.payment;

import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.store.Store;
import com.devtaste.facampay.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserOrderByCreatedAtDesc(User user);

    List<Payment> findByStore_StoreIdAndUser_UserIdOrderByCreatedAtDesc(Long userId, Long storeId);

    List<Payment> findByStore_StoreIdAndUser_UserIdAndPaymentStatus(Long userId, Long storeId, PaymentStatusType paymentStatus);
}
