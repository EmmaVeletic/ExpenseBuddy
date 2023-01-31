package com.LadyBugs2.ExpenseBuddy.models.dto;

import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.LadyBugs2.ExpenseBuddy.models.entity.Category;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RecordDTO {

	long id;
	
	@NotBlank(message = "Record name must not be null")
    @Size(min = 3, message = "Record name must be at least {min} characters")
	String name;

    String description;

    @NotNull(message = "Record amount should not be null")
    @Min(value = 1, message = "Record amount should not be less than 1")
    BigDecimal amount;

    @NotNull(message = "You must enter income or outcome")
    Boolean incomeOrOutcome;

    //@NotBlank(message = "Category should not be null")
    Category category;

    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;
    
    String dateString;
    
    Long userId;
    
    public RecordDTO(Long userId) {
        setUserId(userId);

    }
    
}
