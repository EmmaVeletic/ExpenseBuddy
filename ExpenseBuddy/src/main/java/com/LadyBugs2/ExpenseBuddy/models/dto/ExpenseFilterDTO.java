package com.LadyBugs2.ExpenseBuddy.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseFilterDTO {

		private String keyword;
		
		private String sortBy;
		
		//private String sortByBool;
		
		private String startDate;
		
		private String endDate;
		
		public ExpenseFilterDTO(String startDate, String endDate) {
	        this.startDate = startDate;
	        this.endDate = endDate;
	    }
				
}
