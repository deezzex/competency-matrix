package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.Competency;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CompetencyDto {

    private Long id;

    private String name;

    private String description;

    private List<CategoryDto> categories;

    public static CompetencyDto from(Competency competency) {
        return CompetencyDto.builder()
                .id(competency.getId())
                .name(competency.getName())
                .description(competency.getDescription())
                .categories(competency.getCategories().stream().map(CategoryDto::from).collect(Collectors.toList()))
                .build();
    }
}
