package com.cloudcomputing.cloudcomputing.product;


import com.cloudcomputing.cloudcomputing.business.Business;
import com.cloudcomputing.cloudcomputing.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategory(Category category, Pageable pageable);
    Page<Product> findAllByBusiness(Business business, Pageable pageable);
    Page<Product> findAllByCategoryAndBusiness(Category category, Business business, Pageable pageable);
    Page<Product> findAllByNameStartingWithIgnoreCase(String name, Pageable pageable);
    Page<Product> findAllByOrderByIdDesc(Pageable pageable);


}
