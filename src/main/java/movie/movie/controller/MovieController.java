package movie.movie.controller;

import lombok.RequiredArgsConstructor;
import movie.movie.dto.MovieDTO;
import movie.movie.model.Movie;
import movie.movie.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService service;

    @GetMapping("/movie")
    public Movie getMovie(@RequestParam int id) {
        return service.getById(id);
    }

    @GetMapping("/movies")
    public List<Movie> listMovies(@RequestParam int total, @RequestParam String order) {
        return service.getOrdered(total, order);
    }

    @PostMapping("/movie")
    public Map<String, String> addMovie(@RequestBody MovieDTO movieDTO) {

        Movie movie = new Movie();
        movie.setFilm(movieDTO.getFilm());
        movie.setGenre(movieDTO.getGenre());
        movie.setStudio(movieDTO.getStudio());
        movie.setScore(movieDTO.getScore());
        movie.setYear(movieDTO.getYear());

        service.addMovie(movie);

        return Map.of("message", "La película fue creada con éxito");
    }

    /**
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            service.saveCsv(file);
            return ResponseEntity.ok("CSV cargado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error procesando el archivo: " + e.getMessage());
        }
    }

}
