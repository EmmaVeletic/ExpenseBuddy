package com.LadyBugs2.ExpenseBuddy.controller;


import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.LadyBugs2.ExpenseBuddy.models.dto.CategoryDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.ExpenseFilterDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.RecordDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.UserDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.Record;
import com.LadyBugs2.ExpenseBuddy.models.entity.User;
import com.LadyBugs2.ExpenseBuddy.service.CategoryService;
import com.LadyBugs2.ExpenseBuddy.service.RecordService;
import com.LadyBugs2.ExpenseBuddy.service.UserService;
import com.LadyBugs2.ExpenseBuddy.util.DateTimeUtil;
import com.LadyBugs2.ExpenseBuddy.validator.ExpenseValidator;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class RecordController {
	
	private final RecordService recordService;
	private final UserService userService;
	private final CategoryService categoryService;
	
	/*@GetMapping("/records")
	public List<Record> getAll() {
		return recordService.getAll();
	}*/
	
	@GetMapping("/records/{id}")
	public Record getById(@PathVariable long id) {
		return recordService.readRecord(id);
	}	
	
	/*@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/records/{id}")
	public void deleteRecordById(@PathParam(value="id") long id) {
		recordService.deleteById(id);	
	}*/
	
	
	/*@PostMapping("/records")
	public Record add(@Valid @RequestBody Record record) {
		return recordService.add(record);
	}*/
	
	/*@PutMapping("/records/{id}")
	public Record update(@RequestBody Record record,@PathVariable long id) {
		return recordService.update(id, record);
	}*/

	@GetMapping("/records")
	public String showExpenseList(Model model) {
		UserDTO existingUser = userService.getLoggedInUser();
		List<RecordDTO> list = recordService.getAllRecords(existingUser.getId());
		model.addAttribute("records", list);
		model.addAttribute("filter", new ExpenseFilterDTO(DateTimeUtil.getCurrentMonthStartDate(), DateTimeUtil.getCurrentMonthDate()));
		BigDecimal total = recordService.totalExpenses(list);
		model.addAttribute("totalExpenses", total);
		return "expenses-list";
	}
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@GetMapping("/createRecord")
	public String showRecordForm(Model model) {
		UserDTO user = userService.getLoggedInUser();
		model.addAttribute("record", new RecordDTO());
		model.addAttribute("categories", categoryService.getAllCategoriesFromUser(user.getId()));
		return "expense-form";
	}
	
	@PostMapping("/saveOrUpdateRecord")
	public String saveOrUpdateExpense(@Valid @ModelAttribute("record") RecordDTO recordDTO, BindingResult result) throws ParseException{
		System.out.println("Printing the Record DTO: " + recordDTO);
		new ExpenseValidator().validate(recordDTO,result);
		if(result.hasErrors()) {
			return "expense-form";
		}
		UserDTO existingUser = userService.getLoggedInUser();
		recordDTO.setUserId(existingUser.getId());
		recordService.createRecord(recordDTO);
		return "redirect:/records";
	}
	
	//@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@GetMapping("/deleteRecord")
	public String deleteRecord(@RequestParam long id) {
		System.out.println("Printing the record id: " + id);
		recordService.deleteById(id);
		return "redirect:/records";
	}
	
	@GetMapping("/updateRecord")
	public String updateExpense(@RequestParam long id, Model model) {
		System.out.println("Printing the record Id inside update method:"+id);
		RecordDTO recordDTO = recordService.getRecordById(id);
		UserDTO user = userService.getLoggedInUser();
		model.addAttribute("record", recordDTO);
		model.addAttribute("categories", categoryService.getAllCategoriesFromUser(user.getId()));
		return "update-expense";
	}

	@GetMapping("/chart")
	public String categoryChart(Model model) {
		List<List<Object>> data = recordService.getChartData();
		System.out.println(data);
		model.addAttribute("data",data);
		return "chart";
	}
}	
