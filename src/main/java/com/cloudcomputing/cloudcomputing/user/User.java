package com.cloudcomputing.cloudcomputing.user;

import com.cloudcomputing.cloudcomputing.business.Business;
import com.cloudcomputing.cloudcomputing.order.Order;
import com.cloudcomputing.cloudcomputing.user.enums.Gender;
import com.cloudcomputing.cloudcomputing.user.enums.UserRole;
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
public class User {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "DOB")
    private Date dob;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "PASSWORD")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Business business;
}
