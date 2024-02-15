package com.mindera.users;

import com.mindera.users.dto.DtoGetAllUser;
import com.mindera.users.entity.User;
import com.mindera.users.repository.UsersRepository;
import com.mindera.users.service.UserService;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserApplicationUnitTests {

    @Mock
    UsersRepository usersRepository;

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService(usersRepository);
    }

    @Test
    public void getAllUsers() throws Exception {
        User user1 = new User(1L, "bruna@gmail.com", "Bruna", "1234");
        User user2 = new User(2L, "pedro@gmail.com", "Pedro", "4321");
        List<User> userList = Arrays.asList(user1, user2);

        Mockito.when(usersRepository.findAll()).thenReturn(userList);

        List<DtoGetAllUser> dtoGetAllUserList = userService.getUsers();

        assertEquals(2, dtoGetAllUserList.size());
        assertEquals(1, dtoGetAllUserList.get(0).getId());
        assertEquals("Bruna", dtoGetAllUserList.get(0).getUsername());
        assertEquals(2, dtoGetAllUserList.get(1).getId());
        assertEquals("Pedro", dtoGetAllUserList.get(1).getUsername());
    }

    @Test
    public void getUserById() throws Exception {

    }
}
