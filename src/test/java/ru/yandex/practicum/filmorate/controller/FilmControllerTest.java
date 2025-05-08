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
class FilmControllerTest {

    public static final String PATH = "/films";

    @Autowired
    private MockMvc mockMvc;

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void findall() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                                jsonFromFileReader("controller/film/create/response/films.json")
                        )
                );
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/film/create/request/film.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                                jsonFromFileReader("controller/film/create/response/film.json")
                        )
                );
    }

    @Test
    void createDescriptionEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/film/create/request/description-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createDurationZero() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/film/create/request/duration-zero.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createNameEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/film/create/request/name-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createReleaseDateEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/film/create/request/release-date-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/film/create/request/film.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/film/create/request/update-film.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                                jsonFromFileReader("controller/film/create/response/update-film.json")
                        )
                );
    }

    @Test
    void addLike() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/user/create/request/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFromFileReader("controller/film/create/request/film.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/2/like/6"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        jsonFromFileReader("controller/film/create/response/film-likes.json")));
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