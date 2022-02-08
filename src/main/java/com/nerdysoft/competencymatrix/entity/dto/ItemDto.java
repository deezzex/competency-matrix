package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.Item;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ItemDto {

    private Long id;

    private String label;

    private List<ResourceDto> resources;

    public static ItemDto from(Item item){
        return ItemDto.builder()
                .id(item.getId())
                .label(item.getLabel())
                .resources(item.getResources().stream().map(ResourceDto::from).collect(Collectors.toList()))
                .build();
    }
}
