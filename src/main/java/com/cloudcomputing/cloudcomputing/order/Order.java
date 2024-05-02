package com.cloudcomputing.cloudcomputing.order;

import com.cloudcomputing.cloudcomputing.orderDetail.OrderDetail;
import com.cloudcomputing.cloudcomputing.business.Business;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @Column(name = "ORDER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUSTOMER_NAME")
    private String name;

    @Column(name = "ORDER_ADDRESS")
    private String address;

    @Column(name = "CUSTOMER_PHONE")
    private String phone;

    @Column(name = "ORDER_DATE")
    private Date orderDate;

    @Column(name = "TOTAL_COST")
    private double totalCost;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "BUSINESS_ID")
    @JsonIgnore
    private Business business;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}