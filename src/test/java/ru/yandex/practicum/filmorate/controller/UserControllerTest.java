package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    public static final String PATH = "/users";

    @Autowired
    private MockMvc mockMvc;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                                jsonFromFileReader("controller/user/create/response/users.json")
                        )
                );
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                                jsonFromFileReader("controller/user/create/response/user.json")
                        )
                );
    }

    @Test
    void birthdayInPast() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/birthday-in-past.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void emailEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/email-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void emailNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/email-not-valid.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void loginEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/login-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void loginNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/login-not-valid.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void nameEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/name-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                                jsonFromFileReader("controller/user/create/response/name-empty.json")
                        )
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/update-user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                                jsonFromFileReader("controller/user/create/response/update-user.json")
                        )
                );
    }

    @Test
    void addFriend() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/user-friend.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/3/friends/4"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                                jsonFromFileReader("controller/user/create/response/user-friends.json")
                        )
                );
    }

    private String jsonFromFileReader(String filename) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + filename).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RuntimeException("Не открывается файл ", exception);
        }
    }
}