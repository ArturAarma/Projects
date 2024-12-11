package com.example.movie.DTO;

import lombok.*;

import java.time.LocalDate;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieResponseActorDto {
    private Long id;
    private String name;
    private LocalDate birthDate;

}
