package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.*;
import com.nerdysoft.competencymatrix.entity.privilege.Role;
import com.nerdysoft.competencymatrix.exception.UserAlreadyExistException;
import com.nerdysoft.competencymatrix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {
    private final UserRepository repository;
    private final MatrixService matrixService;
    private final TopicProgressService topicProgressService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, MatrixService matrixService, TopicProgressService topicProgressService, @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.matrixService = matrixService;
        this.topicProgressService = topicProgressService;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) throws UserAlreadyExistException {
        User maybeUser = getByUsername(user.getUsername());

        if (maybeUser != null){
            throw new UserAlreadyExistException("User already exists");
        }

        Set<Role> userRoles = Set.of(new Role(1L));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

        return repository.save(user);
    }

    public Optional<User> findUserById(Long id){
        return repository.findById(id);
    }

    public List<User> findAllUsers(){
        return repository.findAll();
    }

    @Transactional
    public User addMatrixToUser(Long userId, Long matrixId){
        Optional<User> maybeUser = findUserById(userId);
        Optional<Matrix> maybeMatrix = matrixService.findMatrixById(matrixId);
        if (maybeUser.isPresent() && maybeMatrix.isPresent()){
            User user = maybeUser.get();
            Matrix matrix = maybeMatrix.get();

            user.addMatrix(matrix);

            return user;
        }else
            return new User();
    }

    @Transactional
    public User addProgressToUser(Long userId, Long progressId){
        Optional<User> maybeUser = findUserById(userId);
        Optional<TopicProgress> maybeProgress = topicProgressService.findProgressById(progressId);
        if (maybeUser.isPresent() && maybeProgress.isPresent()){
            User user = maybeUser.get();
            TopicProgress progress = maybeProgress.get();

            user.addProgress(progress);

            return user;
        }else
            return new User();
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<Matrix> findUserMatrices(UserDetails user) {
        return repository.findUserMatrix(user.getUsername());
    }
}
