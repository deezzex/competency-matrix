package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.Level;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class LevelDto {
    private Long id;

    private String name;

    private String description;

    private List<TopicDto> topics;

    public static LevelDto from(Level level) {
        return LevelDto.builder()
                .id(level.getId())
                .name(level.getName())
                .description(level.getDescription())
                .topics(level.getTopics().stream().map(TopicDto::from).collect(Collectors.toList()))
                .build();
    }
}
