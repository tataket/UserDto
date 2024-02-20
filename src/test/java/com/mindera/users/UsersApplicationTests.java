package com.mindera.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.users.entity.User;
import com.mindera.users.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UsersApplicationTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UsersRepository usersRepository;

    List<User> userList;
//    User user1 = new User(1L,"bruna@mail.com","Bruna","pass123");
//    User user2 = new User(2L,"chico@mail.com","Chico","pass123");
//    User user3 = new User(3L,"rodrigo@mail.com","Rodrigo","pass123");


    User user1 = User.builder()
            .id(1L)
            .username("Bruna")
            .password("111")
            .email("bruna@gmail.com")
            .build();
    User user2 = User.builder()
            .id(2L)
            .username("Chico")
            .password("222")
            .email("chico@gmail.com")
            .build();
    User user3 = User.builder()
            .id(3L)
            .username("Rodrigo")
            .password("333")
            .email("rodrigo@gmail.com")
            .build();




    @BeforeEach
    public void setup() {
        userList = new ArrayList<>(Arrays.asList(user1, user2, user3));
        Mockito.mock(UsersRepository.class);
    }

    @Test
    void getAllUserSuccess() throws Exception {
        User user = new User(1L, "teste", "lol", "lol");
        Mockito.when(usersRepository.findAll()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].username", is("Bruna")))
                .andExpect(jsonPath("$[1].username", is("Chico")))
                .andExpect(jsonPath("$[2].username", is("Rodrigo")))
                .andDo(e -> System.out.println(e.getResponse().getContentAsString()));
    }

    @Test
    void getAllUserNoSuccess_NotFound() throws Exception {
        Mockito.when(usersRepository.findAll()).thenReturn(userList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addUserSuccess() throws Exception {
        User userAdd = User.builder()
                .id(4L)
                .username("Fatima")
                .password("4444")
                .build();

        Mockito.when(usersRepository.save(userAdd)).thenReturn(userAdd);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userAdd));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is("Fatima")))
                .andDo(e -> System.out.println(e.getResponse().getContentAsString()));

    }

    @Test
    void addUserNoSuccess_MissArgs() throws Exception {
        User userTestAdd = User.builder()
                .id(5L)
                .build();
        Mockito.when(usersRepository.save(userTestAdd)).thenReturn(userTestAdd);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(userTestAdd)))
                .andExpect(status().isBadRequest());


    }

    @Test
    void getByIdUserSuccess() throws Exception {
        Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.ofNullable((user1)));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(e -> System.out.println(e.getResponse().getContentAsString()))
                .andReturn();

    }

    @Test
    void getByIdUserNoSuccess_NotFound() throws Exception {
        Mockito.when(usersRepository.getReferenceById(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(e -> System.out.println(e.getResponse().getContentAsString()));
    }

    @Test
    void updatePutUserSuccess() throws Exception {
        User userPutUpdated = User.builder()
                .id(1L)
                .username("UpdatedName")
                .password("UpdatedPass")
                .email("UpdatedEmail")
                .build();
        Mockito.when(usersRepository.save(userPutUpdated)).thenReturn(userPutUpdated);
        Mockito.when(usersRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(userPutUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is("UpdatedName")))
                .andExpect(jsonPath("$.password", is("UpdatedPass")))
                .andExpect(jsonPath("$.email", is("UpdatedEmail")));
    }


    /*
     * null null
     * UserName, password
     * null, password
     * UserName, mull
     * arrumar uma maneira de fazer as 4 verificações para att no patch
     */
    @Test
    void updatePatchUserSuccess() throws Exception {
        User userPatchUpdated = User.builder()
                .id(3L)
                .username("null")
                .password("null")
                .build();

        Mockito.when(usersRepository.findById(user3.getId())).thenReturn(Optional.ofNullable(user3));
        Mockito.when(usersRepository.save(userPatchUpdated)).thenReturn(userPatchUpdated);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .patch("/user/{id}", 3L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userPatchUpdated));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk());

    }

    @Test
    void updatePatchUser_NotFound() throws Exception {
        User userNoPass = User.builder()
                .id(1L)
                .username("Bruna")
                .build();
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .patch("/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userNoPass));
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isNotFound())
                .andDo(e -> System.out.println(e.getResponse().getContentAsString()));
    }

    private static Stream<Arguments> testUpdatePatchUserIsOkArgs(){
        return Stream.of(
                Arguments.of("username", null),
                Arguments.of(null, "password"),
                Arguments.of("username", "password")
        );
    }

    @ParameterizedTest
    @MethodSource("testUpdatePatchUserIsOkArgs")
    void testUpdatePatchUserIsOk(String username, String password) throws Exception {
        long idUpdated = 1L;
        User userUpdatedPatch = User.builder()
                .username(username)
                .password(password)
                .build();

        Mockito.when(usersRepository.findById(idUpdated)).thenReturn(Optional.ofNullable(userList.get((int) idUpdated)));
        Mockito.when(usersRepository.save(userList.get((int) idUpdated))).thenReturn(userUpdatedPatch);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .patch("/user/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userUpdatedPatch));
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andDo(e -> System.out.println(e.getResponse().getContentAsString()));
    }

    @Test
    void updatePutUserNotFound() throws Exception {
        User userNoId = User.builder()
                .username("Bruna")
                .build();
        Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(usersRepository.save(userNoId)).thenReturn(userNoId);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(userNoId)))
                .andExpect(status().isNotFound())
                .andDo(e -> System.out.println(e.getResponse().getContentAsString()));
    }

    @Test
    void deleteUserByIdSuccess() throws Exception {
        long id = 2L;
        Mockito.when(usersRepository.findById(2L)).thenReturn(Optional.ofNullable(user2));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(usersRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteUserByIdNoSuccess_NotFound() throws Exception {
        long idTest = 4L;
        Mockito.when(usersRepository.findById(idTest)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        Mockito.verify(usersRepository, Mockito.times(0)).deleteById(idTest);
    }

    //-----------------------> unit tests <-------------------------//

    @Test
    void getAllUsersWithUnitTestsSuccess() {
        User userTest = new User(1L, "bruna@gmail.com", "Bruna", "111");



    }
}


