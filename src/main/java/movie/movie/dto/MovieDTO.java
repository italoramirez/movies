package movie.movie.dto;

import lombok.Data;

@Data
public class MovieDTO {
    private String film;
    private String genre;
    private String studio;
    private Integer score;
    private Integer year;
}
