package com.mindera.users.controller;

import com.mindera.users.dto.DtoGetAllUser;
import com.mindera.users.dto.UserDto;
import com.mindera.users.entity.User;
import com.mindera.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<DtoGetAllUser> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto user) {
        UserDto newUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newUser);
    }

    @GetMapping("/{userId}")
    public Optional<User> findById(@PathVariable Long  userId) {
        return userService.findById(userId);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long  userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long  userId) {
        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public User updateUserDetail(@PathVariable Long  userId, @RequestBody User user) {
       return userService.updateUserDetail(userId, user);
    }
}

