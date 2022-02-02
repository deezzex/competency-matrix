package com.nerdysoft.competencymatrix.entity.dto;


import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.TopicProgress;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicProgressDto {

    private Long id;

    private String comment;

    private Integer mark;

    private boolean finished;

    private TopicDto topic;

    public static TopicProgressDto from(TopicProgress topicProgress) {
        return TopicProgressDto.builder()
                .id(topicProgress.getId())
                .comment(topicProgress.getComment())
                .mark(topicProgress.getMark())
                .finished(topicProgress.isFinished())
                .build();
    }

    public static TopicProgressDto fromForAdding(TopicProgress topicProgress) {
        return TopicProgressDto.builder()
                .id(topicProgress.getId())
                .comment(topicProgress.getComment())
                .mark(topicProgress.getMark())
                .finished(topicProgress.isFinished())
                .topic(TopicDto.from(topicProgress.getTopic()))
                .build();
    }
}
