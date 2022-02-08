package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.access.EvaluatorProgressAccess;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluatorProgressAccessDto {
    private Long id;

    private String evaluatorName;

    private Long progressId;

    public static EvaluatorProgressAccessDto from(EvaluatorProgressAccess access){
        return EvaluatorProgressAccessDto.builder()
                .id(access.getId())
                .evaluatorName(access.getEvaluator().getFirstName())
                .progressId(access.getProgress().getId())
                .build();
    }
}
