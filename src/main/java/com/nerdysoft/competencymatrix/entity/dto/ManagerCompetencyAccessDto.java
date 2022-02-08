package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.access.ManagerCompetencyAccess;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagerCompetencyAccessDto {
    private Long id;

    private String managerName;

    private Long competencyId;

    public static ManagerCompetencyAccessDto from(ManagerCompetencyAccess access){
        return ManagerCompetencyAccessDto.builder()
                .id(access.getId())
                .managerName(access.getManager().getFirstName())
                .competencyId(access.getCompetency().getId())
                .build();
    }
}
