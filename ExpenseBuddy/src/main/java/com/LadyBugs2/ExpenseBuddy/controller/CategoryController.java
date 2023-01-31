package com.LadyBugs2.ExpenseBuddy.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.LadyBugs2.ExpenseBuddy.models.dto.CategoryDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.ExpenseFilterDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.RecordDTO;
import com.LadyBugs2.ExpenseBuddy.models.dto.UserDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.Category;
import com.LadyBugs2.ExpenseBuddy.models.entity.Record;
import com.LadyBugs2.ExpenseBuddy.service.CategoryService;
import com.LadyBugs2.ExpenseBuddy.service.RecordService;
import com.LadyBugs2.ExpenseBuddy.service.UserService;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping("/categories")
    public String showExpenseList(Model model) {
        UserDTO existingUser = userService.getLoggedInUser();
        List<CategoryDTO> list = categoryService.getAllCategoriesFromUser(existingUser.getId());
        model.addAttribute("categories", list);
        List<List<Object>> data = categoryService.getChartData();
        model.addAttribute("data",data);
        //model.addAttribute("filter", new CategoryFilterDTO()):
        return "categories-list";
    }

    @GetMapping("/create-category")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "category-form";
    }

    @GetMapping("/delete-category")
    public String deleteCategory(@RequestParam long id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }

    @GetMapping("/update-category")
    public String updateCategory(@ModelAttribute("category") CategoryDTO categoryDTO, @RequestParam long id) {
        categoryService.update(id, categoryDTO);
        return "category-form";
    }

    @PostMapping("/saveOrUpdateCategory")
    public String saveOrUpdateCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO, BindingResult result) throws ParseException {
        System.out.println("Printing the Category DTO: " + categoryDTO);
        if(result.hasErrors()) {
            return "category-form";
        }
        if(categoryDTO.getId()==null) {
            categoryDTO.setCustom(true);
            UserDTO existingUser = userService.getLoggedInUser();
            categoryDTO.setUserId(existingUser.getId()); 
            categoryService.createCategory(categoryDTO);
            return "redirect:/categories";
        }
        categoryService.update(categoryDTO.getId(), categoryDTO);
        return "redirect:/categories";
    }
}
