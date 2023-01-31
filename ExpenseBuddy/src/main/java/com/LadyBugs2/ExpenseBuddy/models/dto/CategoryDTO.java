package com.LadyBugs2.ExpenseBuddy.models.dto;

import java.util.List;
import com.LadyBugs2.ExpenseBuddy.models.entity.Record;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CategoryDTO {

    Long id;

    @NotBlank(message = "name field should not be blank")
    String name;

    boolean custom;

    Long userId;

    List<Record> records;

    public CategoryDTO(String name, boolean custom, long userId) {
        setName(name);
        setCustom(custom);
        setUserId(userId);
    }
}