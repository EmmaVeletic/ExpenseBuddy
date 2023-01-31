package com.LadyBugs2.ExpenseBuddy.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.LadyBugs2.ExpenseBuddy.models.entity.Record;


public interface RecordRepository extends JpaRepository<Record, Long>,JpaSpecificationExecutor<Record> {

	//Optional<Record> findByRecordId(long id);
	
	List<Record> findByNameContainingAndDateBetweenAndUserId(String keyword, Date startDate, Date endDate, long id);
	
	List<Record> findByDateBetweenAndUserId(Date startDate, Date endDate, long id);

	//List<Record> findAllByOrderByAvailableAsc(Boolean incomeOrOutcome);
	
	//List<Record> findAll(Specification<Record> specification);
	
	//List<Record> findByDateBetweenAndUserId(Date startDate, Date endDate, Long id);
	
	
}
