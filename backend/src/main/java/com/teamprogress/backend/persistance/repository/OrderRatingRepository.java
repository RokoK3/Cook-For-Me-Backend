package com.teamprogress.backend.persistance.repository;

import com.teamprogress.backend.persistance.entity.OrderRating;
import com.teamprogress.backend.persistance.entity.OrderRatingKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRatingRepository extends JpaRepository<OrderRating, OrderRatingKey> {

}
