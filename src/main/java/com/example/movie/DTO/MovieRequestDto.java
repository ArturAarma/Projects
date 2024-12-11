package com.example.movie.DTO;

import lombok.*;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieRequestDto {


    private Long id;

    private String title;

    private int releaseYear;

    private int duration;

    private int genreId;

    private Set<Long> actorIds;
}
