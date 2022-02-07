package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.User;
import com.nerdysoft.competencymatrix.entity.dto.UserDto;
import com.nerdysoft.competencymatrix.exception.UserAlreadyExistException;
import com.nerdysoft.competencymatrix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registration(@RequestBody UserDto userDto){
        try {
            User user = userService.createUser(User.from(userDto));
            return new ResponseEntity<>(UserDto.from(user), OK);
        }catch (UserAlreadyExistException e){
            return new ResponseEntity<>(BAD_REQUEST);
        }

    }
}
