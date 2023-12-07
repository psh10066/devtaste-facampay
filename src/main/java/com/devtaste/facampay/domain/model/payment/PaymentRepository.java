package com.devtaste.facampay.domain.model.payment;

import com.devtaste.facampay.domain.model.payment.type.PaymentStatusType;
import com.devtaste.facampay.domain.model.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Payment> findByPaymentId(Long paymentId);

    List<Payment> findByUserOrderByCreatedAtDesc(User user);

    List<Payment> findByStore_StoreIdAndUser_UserIdOrderByCreatedAtDesc(Long userId, Long storeId);

    List<Payment> findByStore_StoreIdAndUser_UserIdAndPaymentStatus(Long userId, Long storeId, PaymentStatusType paymentStatus);
}
