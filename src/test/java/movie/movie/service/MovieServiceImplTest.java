package movie.movie.service;

import movie.movie.model.Movie;
import movie.movie.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    @Mock
    MovieRepository repository;

    @InjectMocks
    MovieServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Movie movie = new Movie();
        movie.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(movie));

        Movie result = service.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetOrderedAsc() {
        Movie m1 = new Movie(); m1.setId(5);
        Movie m2 = new Movie(); m2.setId(8);
        Movie m3 = new Movie(); m3.setId(3);

        when(repository.findAll()).thenReturn(List.of(m1, m2, m3));

        List<Movie> result = service.getOrdered(2, "asc");

        assertEquals(2, result.size());
        assertEquals(3, result.get(0).getId());
        assertEquals(5, result.get(1).getId());
    }

    @Test
    void testAddMovie() {
        Movie movie = new Movie();
        movie.setFilm("Avatar");

        service.addMovie(movie);

        verify(repository, times(1)).save(movie);
    }
}

