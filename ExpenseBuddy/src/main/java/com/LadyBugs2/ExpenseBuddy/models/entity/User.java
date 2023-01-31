package com.LadyBugs2.ExpenseBuddy.models.entity;

import com.LadyBugs2.ExpenseBuddy.models.entity.Category;
import com.LadyBugs2.ExpenseBuddy.models.entity.Record;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password", unique = true)
    private String password;

   /* @Column(name = "user_role")
    private String role;*/

    @Column(name = "phone")
    private String phone;

    @Column(name = "daily_limit")
    private Double dailyLimit;

    @Column(name = "weekly_limit")
    private Double weeklyLimit;

    @Column(name = "monthly_limit")
    private Double monthlyLimit;

    @Column(name = "yearly_limit")
    private Double yearlyLimit;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Record> records;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Category> categories;
}