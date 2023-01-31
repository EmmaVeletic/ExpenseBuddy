package com.LadyBugs2.ExpenseBuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LadyBugs2.ExpenseBuddy.models.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long>{

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);
}
