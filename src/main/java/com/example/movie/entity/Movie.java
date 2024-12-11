package com.example.movie.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Movie title is required")
    private String title;

    @Min(value = 1888, message = "Release year should be valid")
    private int releaseYear;

    @Positive(message = "Duration must be positive")
    private int duration;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    @JsonBackReference // Breaks the circular reference on the Movie side
    private Genre genre;

    @ManyToMany
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )

    private List<Actor> actors;

}
