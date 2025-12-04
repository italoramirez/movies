package movie.movie.service;


import movie.movie.model.Movie;
import movie.movie.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class MovieServiceImpl implements MovieService {
    private final MovieRepository repository;

    public MovieServiceImpl(MovieRepository repository) {
        this.repository = repository;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Movie getById(int id) {
        return repository.findById(id).orElse(null);
    }

    /**
     *
     * @param total
     * @param order
     * @return
     */
    @Override
    public List<Movie> getOrdered(int total, String order) {
        List<Movie> movies = repository.findAll();

        movies = List.copyOf(movies);
        movies = new java.util.ArrayList<>(movies);   // <- mutable ✔

        if(order.equalsIgnoreCase("asc"))
            movies.sort(Comparator.comparing(Movie::getId));
        else
            movies.sort(Comparator.comparing(Movie::getId).reversed());

        return movies.stream().limit(total).toList(); // esto ya está bien
    }

    /**
     *
     * @param movie
     */
    @Override
    public void addMovie(Movie movie) {
        repository.save(movie);
    }

    /**
     *
     * @param file
     * @throws Exception
     */
    @Override
    public void saveCsv(MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new Exception("El archivo está vacío");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String headerLine = br.readLine();
            if (headerLine == null) throw new Exception("El archivo no contiene encabezados");

            String[] headers = headerLine.split(",");
            Map<String, Integer> columnIndex = new HashMap<>();

            for (int i = 0; i < headers.length; i++) {
                columnIndex.put(headers[i].trim().toLowerCase(), i); // lowercase para evitar problemas
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                Movie movie = new Movie();
                movie.setFilm(data[columnIndex.get("film")]);
                movie.setGenre(data[columnIndex.get("genre")]);
                movie.setStudio(data[columnIndex.get("studio")]);
                movie.setScore(Integer.parseInt(data[columnIndex.get("score")]));
                movie.setYear(Integer.parseInt(data[columnIndex.get("year")]));

                repository.save(movie);
            }
        }
    }

}
