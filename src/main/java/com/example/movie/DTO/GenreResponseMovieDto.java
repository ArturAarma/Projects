package com.example.movie.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenreResponseMovieDto {
    private Long id;
    private String title;
    private int releaseYear;
    private int duration;
}
