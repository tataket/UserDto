package com.mindera.users.service;

import com.mindera.users.dto.UserDto;
import com.mindera.users.entity.User;
import com.mindera.users.exception.InvalidRequestException;
import com.mindera.users.exception.NotFoundException;
import com.mindera.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    private final UsersRepository repository;

    public List<UserDto> getUsers() {
        return repository.findAll().stream()
                .map(e -> new UserDto(e.getUsername(), e.getPassword(), e.getEmail(), e.getAddress())).toList();
    }

    public UserDto addUser(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getPassword() == null || userDto.getEmail() == null)
            throw new InvalidRequestException("miss args");

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(userDto.getPassword());
        newUser.setEmail(userDto.getEmail());
        repository.save(newUser);
        userDto.setPassword(null);
        return userDto;
    }


    public UserDto findById(Long userId) {
        Optional<User> user = Optional.ofNullable(repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!")));
        return new UserDto(user.get().getUsername(), null, user.get().getEmail(), user.get().getAddress());
    }

    public UserDto updateUser(Long userId, UserDto userDto) {

        Optional<User> userOp = repository.findById(userId);

        if (userOp.isEmpty()) throw new NotFoundException("Invalid User body request");

        if (userDto.getUsername() != null && userDto.getPassword() != null && userDto.getEmail() != null && userDto.getAddress() != null) {
            userOp.get().setUsername(userDto.getUsername());
            userOp.get().setPassword(userDto.getPassword());
            userOp.get().setEmail(userDto.getEmail());
            userOp.get().setAddress(userDto.getAddress());
            User user = userOp.get();
            repository.save(user);
        }
        return userDto;
    }

    public void deleteUser(Long userId) {
        if (repository.findById(userId).isEmpty()) throw new NotFoundException("User not found");

        repository.deleteById(userId);
    }


    public UserDto updateUserDetail(Long userId, UserDto userDto) {
        Optional<User> userExist = Optional.ofNullable(repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Invalid User body request")));

        if (userDto.getUsername() != null) userExist.get().setUsername(userDto.getUsername());

        if (userDto.getPassword() != null) userExist.get().setPassword(userDto.getPassword());

        if (userDto.getEmail() != null) userExist.get().setEmail(userDto.getEmail());

        repository.save(User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .address(userDto.getAddress()).build());

        return new UserDto(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getAddress());
    }
}
