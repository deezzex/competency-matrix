package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Matrix;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.User;
import com.nerdysoft.competencymatrix.entity.dto.LevelDto;
import com.nerdysoft.competencymatrix.entity.dto.MatrixDto;
import com.nerdysoft.competencymatrix.entity.dto.TopicDto;
import com.nerdysoft.competencymatrix.entity.dto.UserDto;
import com.nerdysoft.competencymatrix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        User user = userService.createUser(User.from(userDto));
        return new ResponseEntity<>(UserDto.from(user), OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOneUser(@PathVariable Long id){
        Optional<User> maybeUser = userService.findUserById(id);

        if (maybeUser.isPresent()){
            User user = maybeUser.get();

            return new ResponseEntity<>(UserDto.from(user), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PostMapping("/{userId}/add/matrix/{matrixId}")
    public ResponseEntity<UserDto> addMatrixToUser(@PathVariable Long userId, @PathVariable Long matrixId){
        User user = userService.addMatrixToUser(userId, matrixId);

        return new ResponseEntity<>(UserDto.from(user), OK);
    }

    @PostMapping("/{userId}/add/progress/{progressId}")
    public ResponseEntity<UserDto> addProgressToUser(@PathVariable Long userId, @PathVariable Long progressId){
        User user = userService.addProgressToUser(userId, progressId);

        return new ResponseEntity<>(UserDto.from(user), OK);
    }
}
