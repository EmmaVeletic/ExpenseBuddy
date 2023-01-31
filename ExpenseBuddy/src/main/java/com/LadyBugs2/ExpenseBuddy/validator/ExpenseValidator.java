package com.LadyBugs2.ExpenseBuddy.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.LadyBugs2.ExpenseBuddy.models.dto.RecordDTO;

public class ExpenseValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return RecordDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
        RecordDTO expenseDTO = (RecordDTO) target;
        if (expenseDTO.getDate() == null) {
            errors.rejectValue("date",
                    null,
                    "Please provide a date");
        }
    }	
}
