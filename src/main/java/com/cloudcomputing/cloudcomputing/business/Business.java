package com.cloudcomputing.cloudcomputing.business;

import com.cloudcomputing.cloudcomputing.order.Order;
import com.cloudcomputing.cloudcomputing.product.Product;
import com.cloudcomputing.cloudcomputing.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "BUSINESS")
public class Business {
    @Id
    @Column(name = "BUSINESS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BUSINESS_NAME", unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

}
