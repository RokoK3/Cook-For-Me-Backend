package com.teamprogress.backend.persistance.repository;

import com.teamprogress.backend.persistance.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    boolean existsByCook_idAndName(Long cook_id, String name);

    List<Dish> findAllByCook_Id(Long cookId);

}
