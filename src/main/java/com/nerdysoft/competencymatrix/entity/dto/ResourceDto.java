package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.Resource;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceDto {
    private Long id;
    private String name;
    private String description;
    private String url;

    public static ResourceDto from(Resource resource){
        return ResourceDto.builder()
                .id(resource.getId())
                .name(resource.getName())
                .description(resource.getDescription())
                .url(resource.getUrl())
                .build();
    }
}
