package com.nerdysoft.competencymatrix.entity.dto;

import com.nerdysoft.competencymatrix.entity.User;
import com.nerdysoft.competencymatrix.entity.privilege.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Set<Role> roles;

    private List<MatrixDto> matrices;

    private List<TopicProgressDto> topicProgressList;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .matrices(user.getMatrices().stream().map(MatrixDto::from).collect(Collectors.toList()))
                .topicProgressList(user.getTopicProgressList().stream().map(TopicProgressDto::fromForAdding).collect(Collectors.toList()))
                .build();
    }

}
