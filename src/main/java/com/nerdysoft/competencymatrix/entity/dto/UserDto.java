package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.Matrix;
import com.nerdysoft.competencymatrix.entity.TopicProgress;
import com.nerdysoft.competencymatrix.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private List<MatrixDto> matrices;

    private List<TopicProgressDto> topicProgressList;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .matrices(user.getMatrices().stream().map(MatrixDto::from).collect(Collectors.toList()))
                .topicProgressList(user.getTopicProgressList().stream().map(TopicProgressDto::fromForAdding).collect(Collectors.toList()))
                .build();
    }

}
