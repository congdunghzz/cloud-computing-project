package com.cloudcomputing.cloudcomputing.product;

import com.cloudcomputing.cloudcomputing.business.Business;
import com.cloudcomputing.cloudcomputing.category.Category;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @Column(name = "PRODUCT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PRODUCT_NAME")
    private String name;

    @Column(name = "PRODUCT_PRICE")
    private double price;

    @Column(name = "PRODUCT_STOCK")
    private int stock;

    @Column(name = "PRODUCT_IMAGE")
    private String image;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn (name = "CATEGORY_ID")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "BUSINESS_ID")
    private Business business;
}
