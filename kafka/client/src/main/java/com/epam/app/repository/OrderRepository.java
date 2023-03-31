package com.epam.app.repository;

import com.epam.app.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Override
    @EntityGraph(attributePaths = "orderItems")
    @NonNull
    Optional<OrderEntity> findById(@NonNull Long id);
}
