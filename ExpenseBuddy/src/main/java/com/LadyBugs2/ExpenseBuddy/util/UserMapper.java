package com.LadyBugs2.ExpenseBuddy.util;

import com.LadyBugs2.ExpenseBuddy.models.dto.UserDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromDto(UserDTO dto, @MappingTarget User entity);
}