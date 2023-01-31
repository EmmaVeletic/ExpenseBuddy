package com.LadyBugs2.ExpenseBuddy.service;

import java.text.ParseException;
import java.util.List;

import com.LadyBugs2.ExpenseBuddy.models.dto.CategoryDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.Category;

public interface CategoryService {

	List<CategoryDTO> getAllCategoriesFromUser(long id);

	CategoryDTO getById(long id);
	
	void deleteById(long id);

	CategoryDTO update(long id, CategoryDTO categoryDTO);

	CategoryDTO createCategory(CategoryDTO categoryDTO) throws ParseException;

	CategoryDTO mapToDTO(Category category);

	List<List<Object>> getChartData();

}
