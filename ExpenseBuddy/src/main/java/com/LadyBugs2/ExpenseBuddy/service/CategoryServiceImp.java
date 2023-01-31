package com.LadyBugs2.ExpenseBuddy.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.LadyBugs2.ExpenseBuddy.models.dto.UserDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.Record;
import com.LadyBugs2.ExpenseBuddy.models.entity.User;
import com.LadyBugs2.ExpenseBuddy.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.LadyBugs2.ExpenseBuddy.controller.exception.RecordNotFoundException;
import com.LadyBugs2.ExpenseBuddy.models.dto.CategoryDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.Category;
import com.LadyBugs2.ExpenseBuddy.repository.CategoryRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<CategoryDTO> getAllCategoriesFromUser(long id) {
        List<Category> list = categoryRepository.findByUserId(id);
        List<CategoryDTO> categoryList = list.stream().map(this::mapToDTO).collect(Collectors.toList());
        System.out.println(categoryList.stream().count());
        return categoryList;
    }

    public CategoryDTO getById(long id){
        return mapToDTO(categoryRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User not found for the id :" + id)));
    }

    public void deleteById(long id){
        Category existingCategory = categoryRepository.findById(id).orElseThrow();
        categoryRepository.delete(existingCategory);
    }

    public CategoryDTO update(long id, CategoryDTO categoryDTO){
        Category existingCategory = categoryRepository.findById(id).orElseThrow();
        existingCategory.setName(categoryDTO.getName() != null ? categoryDTO.getName() : existingCategory.getName());
        return mapToDTO(categoryRepository.save(existingCategory));
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws ParseException{
        Category newCategory = modelMapper.map(categoryDTO, Category.class);
        return mapToDTO(categoryRepository.save(newCategory));
    }

    public CategoryDTO mapToDTO(Category category){
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    private List<Object> getRecordSumByCategory( Category category) {
        double sum = 0;
        List<Record> records = category.getRecords();
        for (Record record : records) {
            sum = Double.sum(record.getAmount().doubleValue(),sum);
        }
        return List.of(category.getName(), sum);
    }

    public List<List<Object>> getChartData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        List<Category> list = categoryRepository.findByUserId(user.getId());
        List data = new ArrayList<>();
        for (Category category : list ) {
            data.add(getRecordSumByCategory(category));
        }
        return data;
    }
}