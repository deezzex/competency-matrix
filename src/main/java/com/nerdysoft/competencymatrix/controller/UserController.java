package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Matrix;
import com.nerdysoft.competencymatrix.entity.User;
import com.nerdysoft.competencymatrix.entity.dto.MatrixDto;
import com.nerdysoft.competencymatrix.entity.dto.TopicDto;
import com.nerdysoft.competencymatrix.entity.dto.UserDto;
import com.nerdysoft.competencymatrix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasAuthority('CAN_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOneUser(@PathVariable Long id){
        Optional<User> maybeUser = userService.findUserById(id);

        if (maybeUser.isPresent()){
            User user = maybeUser.get();

            return new ResponseEntity<>(UserDto.from(user), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_ADD_MATRIX')")
    @PostMapping("/{userId}/add/matrix/{matrixId}")
    public ResponseEntity<UserDto> addMatrixToUser(@PathVariable Long userId, @PathVariable Long matrixId){
        User user = userService.addMatrixToUser(userId, matrixId);

        if(Objects.nonNull(user.getId())){
            return new ResponseEntity<>(UserDto.from(user), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_ADD_PROGRESS')")
    @PostMapping("/{userId}/add/progress/{progressId}")
    public ResponseEntity<UserDto> addProgressToUser(@PathVariable Long userId, @PathVariable Long progressId){
        User user = userService.addProgressToUser(userId, progressId);

        if(Objects.nonNull(user.getId())){
            return new ResponseEntity<>(UserDto.from(user), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_READ')")
    @GetMapping("/my/matrix")
    public ResponseEntity<List<MatrixDto>> getMyMatrix(@AuthenticationPrincipal UserDetails user) {
        List<Matrix> userMatrices = userService.findUserMatrices(user);
        List<MatrixDto> dtos = userMatrices.stream().map(MatrixDto::from).collect(Collectors.toList());

        if (dtos.isEmpty()){
            return new ResponseEntity<>(NO_CONTENT);
        }

        return new ResponseEntity<>(dtos, OK);
    }
}
