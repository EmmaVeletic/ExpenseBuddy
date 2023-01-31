package com.LadyBugs2.ExpenseBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LadyBugs2.ExpenseBuddy.models.entity.Category;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByUserId(long id);
}
