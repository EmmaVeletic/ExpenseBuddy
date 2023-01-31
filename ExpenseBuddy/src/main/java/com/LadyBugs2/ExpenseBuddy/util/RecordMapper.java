package com.LadyBugs2.ExpenseBuddy.util;

import com.LadyBugs2.ExpenseBuddy.models.dto.RecordDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.Record;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRecordFromDto(RecordDTO dto, @MappingTarget Record entity);
}
