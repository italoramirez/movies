package movie.movie.service;

import movie.movie.model.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MovieService {
    /**
     *
     * @param id
     * @return
     */
    Movie getById(int id);

    /**
     *
     * @param total
     * @param order
     * @return
     */
    List<Movie> getOrdered(int total, String order);

    /**
     *
     * @param movie
     */
    void addMovie(Movie movie);

    /**
     *
     * @param file
     * @throws Exception
     */
    void saveCsv(MultipartFile file) throws Exception;

}
