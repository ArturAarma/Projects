package com.example.movie.service.impl;

import com.example.movie.DTO.*;
import com.example.movie.entity.Actor;
import com.example.movie.entity.Movie;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.ActorRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorServiceImpl implements ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    // Method to create a new actor
    @Override
    public ActorResponseDto createActor(ActorRequestDto actorRequestDto) {
        // Create an Actor entity from the DTO
        Actor actor = Actor.builder()
                .name(actorRequestDto.getName())
                .birthDate(actorRequestDto.getBirthDate())
                .build();

        // Check if the provided movie IDs are valid and set movies to the actor
        List<Movie> movies = new ArrayList<>();
        if (actorRequestDto.getMovieIds() != null && !actorRequestDto.getMovieIds().isEmpty()) {
            movies = movieRepository.findAllById(actorRequestDto.getMovieIds());
            if (movies.size() != actorRequestDto.getMovieIds().size()) {
                // Throw exception if one or more movie IDs are not found
                throw new ResourceNotFoundException("One or more movies not found");
            }
        }

        actor.setMovies(movies);

        // Save the actor entity to the database and convert it to a DTO
        return convertToDto(actorRepository.save(actor));
    }

    // Method to get an actor by their ID
    @Override
    public ActorResponseDto getActorById(Long id) {
        // Get actor from the database or throw exception if not found
        Actor actor = getActorByIdFromDb(id);
        // Convert the actor entity to a DTO and return
        return convertToDto(actor);
    }

    // Method to get all actors with optional pagination
    @Override
    public List<ActorResponseDto> getAllActors(Integer page, Integer size) {
        List<Actor> actors = new ArrayList<>();
        if (page != null && size != null) {
            // Calculate offset for pagination
            int offset = (page - 1) * size;
            // Get actors with pagination
            actors = actorRepository.findAllWithPagination(offset, size);
        } else {
            // Get all actors without pagination
            actors = actorRepository.findAll();
        }
        // Convert the list of actors to DTOs and return
        return actors.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Method to update an actor's details
    @Override
    public ActorResponseDto updateActor(Long id, ActorRequestDto actorDetails) {
        // Get the actor by ID or throw exception if not found
        Actor actor = getActorByIdFromDb(id);

        // Update actor's name if provided
        if (actorDetails.getName() != null) {
            actor.setName(actorDetails.getName());
        }
        // Update actor's birth date if provided
        if (actorDetails.getBirthDate() != null) {
            actor.setBirthDate(actorDetails.getBirthDate());
        }

        // Update actor's movies if movie IDs are provided
        List<Movie> movies = movieRepository.findAllById(actorDetails.getMovieIds());
        if (movies.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found");
        }
        actor.setMovies(movies);

        // Save the updated actor to the database and convert it to a DTO
        return convertToDto(actorRepository.save(actor));
    }

    // Method to delete an actor by ID, with an option to cascade delete
    @Override
    public void deleteActor(Long id, boolean cascade) {
        // Get the actor by ID or throw exception if not found
        Actor actor = getActorByIdFromDb(id);
        // If cascade is false and the actor is associated with movies, throw an exception
        if (!cascade && (actor.getMovies() != null && !actor.getMovies().isEmpty())) {
            throw new RuntimeException("Cannot delete actor '" + actor.getName() + "' because they are associated with "
                    + actor.getMovies().size() + " movies.");
        }
        // Delete the actor from the database
        actorRepository.delete(actor);
    }

    // Method to search actors by name (case insensitive) with pagination support
    @Override
    public Page<Actor> searchActorsByName(String name, Pageable pageable) {
        return actorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // Helper method to get actor by ID or throw ResourceNotFoundException
    private Actor getActorByIdFromDb(Long id) {
        return actorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Actor not found"));
    }

    // Method to get all movies an actor has appeared in
    @Override
    public List<MovieResponseDto> getAllMoviesByActorId(Long actorId) {
        // Get the actor by ID or throw an exception if not found
        Actor actor = getActorByIdFromDb(actorId);
        // Convert the list of movies to MovieResponseDto and return it
        return actor.getMovies().stream()
                .map(this::convertMovieToDto)
                .collect(Collectors.toList());
    }

    // Helper method to convert an Actor entity to ActorResponseDto
    private ActorResponseDto convertToDto(Actor actor) {
        return ActorResponseDto.builder()
                .id(actor.getId())
                .name(actor.getName())
                .birthDate(actor.getBirthDate())
                .build();
    }

    // Helper method to convert a Movie entity to MovieResponseDto
    private MovieResponseDto convertMovieToDto(Movie movie) {
        return movieResponseDto(movie);
    }

    // Static helper method to create a MovieResponseDto from a Movie entity
    static MovieResponseDto movieResponseDto(Movie movie) {
        return MovieResponseDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .releaseYear(movie.getReleaseYear())
                .duration(movie.getDuration())
                .genre(MovieResponseGenreDto.builder()
                        .id(movie.getGenre().getId())
                        .name(movie.getGenre().getName())
                        .build())
                .actors(
                        movie.getActors().stream()
                                .map(actor -> MovieResponseActorDto.builder()
                                        .id(actor.getId())
                                        .name(actor.getName())
                                        .birthDate(actor.getBirthDate())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }
}
