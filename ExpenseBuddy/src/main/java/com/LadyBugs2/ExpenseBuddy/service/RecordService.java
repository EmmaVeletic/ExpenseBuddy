package com.LadyBugs2.ExpenseBuddy.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.LadyBugs2.ExpenseBuddy.models.dto.ExpenseFilterDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.RecordDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.Record;

public interface RecordService {

	
	List<RecordDTO> getAllRecords(long id);
	
	Record update(long id, RecordDTO record);
	
	RecordDTO createRecord(RecordDTO recordDTO) throws ParseException;
	
	Record readRecord(long id);
	
	void deleteById(long id);
	
	RecordDTO getRecordById(long id);
	
	RecordDTO mapToDTO(Record record);
	
	List<RecordDTO> getFilteredExpenses(ExpenseFilterDTO expenseFilterDTO) throws ParseException;
	
	BigDecimal totalExpenses(List<RecordDTO> records);

	public List<List<Object>> getChartData();
}

