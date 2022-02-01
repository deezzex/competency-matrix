package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Matrix;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class MatrixDto {

    private Long id;

    private String name;

    private String description;

    private List<CompetencyDto> competencies;

    private List<LevelDto> levels;

    public static MatrixDto from(Matrix matrix) {
        return MatrixDto.builder()
                .id(matrix.getId())
                .name(matrix.getName())
                .description(matrix.getDescription())
                .competencies(matrix.getCompetencies().stream().map(CompetencyDto::from).collect(Collectors.toList()))
                .levels(matrix.getLevels().stream().map(LevelDto::from).collect(Collectors.toList()))
                .build();
    }
}
