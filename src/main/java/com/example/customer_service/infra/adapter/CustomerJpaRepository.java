package com.example.customer_service.infra.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
}
