package com.teamprogress.backend.persistance.repository;

import com.teamprogress.backend.persistance.entity.UserOrder;
import com.teamprogress.backend.persistance.entity.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> findAllByUser_Id(Long userId);

    List<UserOrder> findAllByStatus(OrderStatus status);

}
