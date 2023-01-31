package com.LadyBugs2.ExpenseBuddy.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.LadyBugs2.ExpenseBuddy.models.entity.Category;
import com.LadyBugs2.ExpenseBuddy.models.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.LadyBugs2.ExpenseBuddy.controller.exception.RecordNotFoundException;
import com.LadyBugs2.ExpenseBuddy.models.dto.ExpenseFilterDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.RecordDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.UserDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.Record;
import com.LadyBugs2.ExpenseBuddy.repository.RecordRepository;
import com.LadyBugs2.ExpenseBuddy.util.DateTimeUtil;


import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RecordServiceImp implements RecordService {
	
	private final RecordRepository recordRepo;
	private final ModelMapper modelMapper;
	private final UserService userService;
	
	/*@Override
	public List<Record> getAll(){
		return recordRepo.findAll();
	}*/
	
	@Override
	public List<RecordDTO> getAllRecords(long id) {
		//User user = userService.getLoggedInUser();
		List<Record> list = recordRepo.findByDateBetweenAndUserId(
				Date.valueOf(LocalDate.now().withDayOfMonth(1)),
				Date.valueOf(LocalDate.now()), id);
		List<RecordDTO> expenseList = list.stream().map(this::mapToDTO).collect(Collectors.toList());
		return expenseList;
	}
	
	
	@Override
	public Record readRecord(long id) { // prijasnja metoda getById i vraca samo Record
		//Optional <Record> record = recordRepo.findById(id);
		return recordRepo.findById(id).orElseThrow(() -> new RecordNotFoundException("Record not found for the id :" + id));
	}
	
	
	public RecordDTO getRecordById(long id) { //vraca RecordDTO
		Record exitingExpense = readRecord(id);
		return mapToDTO(exitingExpense);
	}

	@Override
	public void deleteById(long id) {
		Record existingRecord = readRecord(id); //mozda var
		recordRepo.delete(existingRecord);	
		
	}
	
	
	@Override
	public Record update(long id, RecordDTO record) {
		Record existingExpense = readRecord(id);
		existingExpense.setName(record.getName() != null ? record.getName() : existingExpense.getName());
		existingExpense.setDescription(record.getDescription() != null ? record.getDescription() : existingExpense.getDescription());
		existingExpense.setCategory(record.getCategory() != null ? record.getCategory() : existingExpense.getCategory());
		existingExpense.setDate(record.getDate() != null ? record.getDate() : existingExpense.getDate());
		existingExpense.setAmount(record.getAmount() != null ? record.getAmount() : existingExpense.getAmount());
		return recordRepo.save(existingExpense);
	}

	/*@Override
	public Record add(Record record) {
		return recordRepo.save(record);
	}*/
	
	public RecordDTO createRecord(RecordDTO recordDTO) throws ParseException {
		//Record newRecord = new Record();
		//newRecord.setDate(DateTimeUtil.convertStringToDate(recordDTO.getDateString())); 
		//BeanUtils.copyProperties(recordDTO, newRecord);
		Record newRecord = modelMapper.map(recordDTO, Record.class);
		if (!newRecord.getDate().before(new java.util.Date())) {
			throw new RuntimeException("Future date is not allowed");
		}
		recordRepo.save(newRecord);
		return mapToDTO(newRecord);
	}

	
	public RecordDTO mapToDTO(Record record) {
		RecordDTO recordDTO = modelMapper.map(record, RecordDTO.class);
		//recordDTO.setDateString(DateTimeUtil.convertDateToString(recordDTO.getDate()));
		return recordDTO;
	}
	

	public List<RecordDTO> getFilteredExpenses(ExpenseFilterDTO expenseFilterDTO) throws ParseException{
		String keyword = expenseFilterDTO.getKeyword();
		String sortBy = expenseFilterDTO.getSortBy();
		String startDateString = expenseFilterDTO.getStartDate();
		String endDateString = expenseFilterDTO.getEndDate();
		UserDTO user = userService.getLoggedInUser();
		
		Date startDate = !startDateString.isEmpty() ? DateTimeUtil.convertStringToDate(startDateString) : new Date(0);
		Date endDate = !endDateString.isEmpty() ? DateTimeUtil.convertStringToDate(endDateString) : new Date(System.currentTimeMillis());
		
		List<Record> list = recordRepo.findByNameContainingAndDateBetweenAndUserId(
				keyword,
				startDate,
				endDate,
				user.getId());
		List<RecordDTO> filteredList = list.stream().map(this::mapToDTO).collect(Collectors.toList());
		if(sortBy.equals("date")) {
			filteredList.sort((obj1,obj2) -> obj2.getDate().compareTo(obj1.getDate())); //sortirano po redu desc
		}
		else if(sortBy.equals("amount")) {
			filteredList.sort((obj1,obj2) -> obj2.getAmount().compareTo(obj1.getAmount()));
		}
		else if(sortBy.equals("incomeOrOutcome")){
			filteredList.sort((obj1,obj2) -> obj2.getIncomeOrOutcome().compareTo(obj1.getIncomeOrOutcome()));
		}
		/*if(sortByBool.equals("0")) {
			filteredList.sort((obj1,obj2) -> obj2.getIncomeOrOutcome().compareTo(obj1.getIncomeOrOutcome()));
		}*/
		return filteredList;
	}
	
	public BigDecimal totalExpenses(List<RecordDTO> records) { //kao for petlja koja zbraja sum
		BigDecimal sum = new BigDecimal(0);
		 
		BigDecimal outcome = records.stream().filter(x -> x.getIncomeOrOutcome() == false).map(x -> x.getAmount().add(sum))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal income = records.stream().filter(x -> x.getIncomeOrOutcome() == true).map(x -> x.getAmount().add(sum))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		MathContext mc = new MathContext(10);
		BigDecimal total = income.subtract(outcome, mc);
		return total;
	}

	private List<Object> getExpenseSumByMonth( List<Record> month, String monthName) {
		double sumExpense = 0;
		double sumIncome = 0;
		for (Record record : month) {
			if(record.getIncomeOrOutcome()) {
				sumExpense = Double.sum(record.getAmount().doubleValue(),sumIncome);
			} else {
				sumExpense = Double.sum(record.getAmount().doubleValue(),sumExpense);
			}
		}
		return List.of(monthName, sumIncome, sumExpense);
	}

	public List<List<Object>> getChartData() {
		UserDTO user = userService.getLoggedInUser();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		String startDate = "2023-01-01";
		String endDate = "2023-12-01";
		long millisecondsSinceEpochStart = LocalDate.parse(startDate, dateFormatter)
				.atStartOfDay(ZoneOffset.UTC)
				.toInstant()
				.toEpochMilli();
		long millisecondsSinceEpochEnd = LocalDate.parse(endDate, dateFormatter)
				.atStartOfDay(ZoneOffset.UTC)
				.toInstant()
				.toEpochMilli();
		List<Record> list = recordRepo.findByDateBetweenAndUserId(new Date(millisecondsSinceEpochStart),
																	new Date(millisecondsSinceEpochEnd),
																	user.getId());
		List data = new ArrayList<>();
		data.add(List.of("2023","Income","Outcome"));
		int[] months = {1,2,3,4,5,6,7,8,9,10,11,12};
		String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		for (final int i : months) {
			List month = new ArrayList<>();
			list.stream().forEach(record -> {
				Calendar cal = Calendar.getInstance();
				cal.setTime(record.getDate());
				Integer monthInDate = cal.get(Calendar.MONTH);
				if(monthInDate==i){
					month.add(record);
				}
			});
			String monthName = monthNames[i-1];
			if(month.isEmpty()) {
				data.add(List.of(monthName,0,0));
			}
			data.add(getExpenseSumByMonth(month,monthName));
		}
		return data;
	}
}

