package com.mindera.users.service;

import com.mindera.users.dto.DtoGetAllUser;
import com.mindera.users.dto.UserDto;
import com.mindera.users.entity.User;
import com.mindera.users.exception.InvalidRequestException;
import com.mindera.users.exception.NotFoundException;
import com.mindera.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    private final UsersRepository repository;

    public List<DtoGetAllUser> getUsers() {
        return repository.findAll()
                .stream().sorted(Comparator.comparing(User::getId))
                .map(user -> new DtoGetAllUser(user.getId(), user.getUsername()))
                .toList();
    }

    public UserDto addUser(UserDto user) {
        if (user.getUsername() == null && user.getPassword() == null && user.getEmail() == null) {
            throw new InvalidRequestException("miss args");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        repository.save(newUser);
        return user;
    }


    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!")));
    }

    public User updateUser(Long userId, User user) {
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isPresent()) {
            User userUp = userOptional.get();
            if (user.getUsername() != null && user.getPassword() != null && user.getEmail() != null) {
                userUp.setUsername(user.getUsername());
                userUp.setPassword(user.getPassword());
                userUp.setEmail(user.getEmail());
            }
            repository.save(userUp);
            return userUp;
        }
        throw new NotFoundException("Invalid User body request");
    }

    public void deleteUser(Long userId) {
        if (repository.findById(userId).isEmpty()) {
            throw new NotFoundException("User not found");
        }
        repository.deleteById(userId);
    }


    public User updateUserDetail(Long userId, User user) {
        User userExist = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Invalid User body request"));

        if (user.getUsername() != null) {
            userExist.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            userExist.setPassword(user.getPassword());
        }
        if (user.getEmail() != null) {
            userExist.setEmail(user.getEmail());
        }
        repository.save(userExist);
        return userExist;
    }

}
