package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TopicDto {

    private Long id;

    private String name;

    private String description;

    private boolean required;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private List<ItemDto> items;

    private List<ResourceDto> resources;

    public static TopicDto from(Topic topic){
        return TopicDto.builder()
                .id(topic.getId())
                .name(topic.getName())
                .description(topic.getDescription())
                .required(topic.isRequired())
                .priority(topic.getPriority())
                .items(topic.getItems().stream().map(ItemDto::from).collect(Collectors.toList()))
                .resources(topic.getResources().stream().map(ResourceDto::from).collect(Collectors.toList()))
                .build();
    }
}
