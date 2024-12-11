package com.example.movie.DTO;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MovieResponseDto {
    private Long id;

    private String title;

    private int releaseYear;

    private int duration;

    private MovieResponseGenreDto genre;

    private List<MovieResponseActorDto> actors;
}
