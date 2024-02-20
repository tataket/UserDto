package com.mindera.users.controller;

import com.mindera.users.dto.UserDto;
import com.mindera.users.entity.User;
import com.mindera.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable Long  userId) {
        return userService.findById(userId);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long  userId, @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long  userId) {
        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUserDetail(@PathVariable Long  userId, @RequestBody UserDto userDto) {
       return userService.updateUserDetail(userId, userDto);
    }
}

