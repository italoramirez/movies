package movie.movie.controller;

import movie.movie.config.SecurityTestConfig;
import movie.movie.model.Movie;
import movie.movie.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
@Import(SecurityTestConfig.class)
class MovieControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    MovieService service;

    @Test
    void testGetMovie() throws Exception {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setFilm("Inception");

        when(service.getById(1)).thenReturn(movie);

        mockMvc.perform(get("/api/movie").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.film").value("Inception"));
    }

    @Test
    void testListMovies() throws Exception {

        // se asigna ID porque tu servicio ordena por ID
        Movie m1 = new Movie(); m1.setId(1); m1.setScore(9);
        Movie m2 = new Movie(); m2.setId(2); m2.setScore(7);

        // Se ordenará asc → 1 luego 2
        when(service.getOrdered(2, "asc")).thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/api/movies")
                        .param("total", "2")
                        .param("order", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(9));
    }

    @Test
    void testAddMovie() throws Exception {
        doNothing().when(service).addMovie(any(Movie.class));

        mockMvc.perform(post("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"film":"Matrix","genre":"Action","score":9,"studio":"WB","year":1999}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("La película fue creada con éxito"));
    }
}