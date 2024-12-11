package com.example.movie.service;

import com.example.movie.DTO.ActorRequestDto;
import com.example.movie.DTO.ActorResponseDto;
import com.example.movie.DTO.MovieResponseDto;
import com.example.movie.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActorService {
    ActorResponseDto createActor(ActorRequestDto actorRequestDto);
    ActorResponseDto getActorById(Long id);
    List<ActorResponseDto> getAllActors(Integer page, Integer size);
    ActorResponseDto updateActor(Long id, ActorRequestDto actorDetails);
    void deleteActor(Long id, boolean cascade);
    Page<Actor> searchActorsByName(String name, Pageable pageable);
    List<MovieResponseDto> getAllMoviesByActorId(Long actorId);
    // New method to get all movies for a given actor

}