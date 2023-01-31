package com.LadyBugs2.ExpenseBuddy.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.LadyBugs2.ExpenseBuddy.models.dto.ExpenseFilterDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.RecordDTO;
import com.LadyBugs2.ExpenseBuddy.service.RecordService;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ExpenseFilterController {
		
		
		private final RecordService recordService;
		
		@GetMapping("/filterExpenses")
	    public String filterExpenses(@ModelAttribute("filter") ExpenseFilterDTO expenseFilterDTO, Model model) throws ParseException {
	        System.out.println("Printing the filter dto:"+expenseFilterDTO);
	        List<RecordDTO> list = recordService.getFilteredExpenses(expenseFilterDTO);
	        model.addAttribute("records", list);
	        BigDecimal totalExpenses = recordService.totalExpenses(list);
	        model.addAttribute("totalExpenses", totalExpenses);
	        return "expenses-list";
	    }
}		
