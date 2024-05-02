package com.cloudcomputing.cloudcomputing.order;

import com.cloudcomputing.cloudcomputing.business.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
    List<Order> findByBusinessIdOrderByOrderDateDesc(Long userId);


}
