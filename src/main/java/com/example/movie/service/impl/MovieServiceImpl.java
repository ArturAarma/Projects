package com.example.movie.service.impl;

import com.example.movie.DTO.*;
import com.example.movie.entity.Actor;
import com.example.movie.entity.Genre;
import com.example.movie.entity.Movie;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.ActorRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.GenreRepository;
import com.example.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ActorRepository actorRepository;

    // Method to create a new Movie
    @Override
    public MovieResponseDto createMovie(MovieRequestDto movieRequestDto) {
       logger.debug("Received request to create movie: {}", movieRequestDto);
       logger.debug("Received MovieRequestDto: {}", movieRequestDto);

       if (movieRequestDto.getGenreId() <= 0) {
    throw new IllegalArgumentException("Genre ID must be greater than 0");
}

    


       logger.debug("Fetching Genre with ID: {}", movieRequestDto.getGenreId());
       Genre genre = genreRepository.findById((long) movieRequestDto.getGenreId())
           .orElseThrow(() -> new ResourceNotFoundException("Genre not found with ID: " + movieRequestDto.getGenreId()));
       logger.debug("Fetched Genre from database: {}", genre);
       

        List<Actor> actors = actorRepository.findAllById(movieRequestDto.getActorIds());
        logger.debug("Fetched Actors from database: {}", actors);

        Movie movie = Movie.builder()
        .title(movieRequestDto.getTitle())
        .releaseYear(movieRequestDto.getReleaseYear())
        .duration(movieRequestDto.getDuration())
        .genre(genre)
        .actors(actors)
        .build();
    logger.debug("Movie entity to be saved: {}", movie);

Movie savedMovie = movieRepository.save(movie);
logger.debug("Saved Movie: {}", savedMovie);

return convertMovieToDto(savedMovie);
    }

    // Method to get a movie by its ID
    @Override
    public MovieResponseDto getMovieById(Long id) {
        logger.debug("Fetching movie with ID: {}", id);
        Movie movie = getMovieByIdFromDb(id);
        return convertMovieToDto(movie);
    }

    // Method to get all movies with optional pagination
    @Override
    public Page<MovieResponseDto> getAllMovies(Pageable pageable) {
        logger.debug("Fetching all movies with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        return moviePage.map(this::convertMovieToDto);
    }

    // Method to update an existing movie
    @Override
    public MovieResponseDto updateMovie(Long id, MovieRequestDto movieDetails) {
        logger.debug("Updating movie with ID: {}, details: {}", id, movieDetails);
        Movie movie = getMovieByIdFromDb(id);

        if (movieDetails.getTitle() != null) {
            movie.setTitle(movieDetails.getTitle());
        }
        if (movieDetails.getReleaseYear() != 0) {
            movie.setReleaseYear(movieDetails.getReleaseYear());
        }
        if (movieDetails.getDuration() != 0) {
            movie.setDuration(movieDetails.getDuration());
        }
        if (movieDetails.getGenreId() != 0) {
            movie.setGenre(genreRepository.findById((long) movieDetails.getGenreId())
                    .orElseThrow(() -> {
                        logger.error("Genre not found with ID: {}", movieDetails.getGenreId());
                        return new ResourceNotFoundException("Genre not found with ID: " + movieDetails.getGenreId());
                    }));
        }
        if (movieDetails.getActorIds() != null && !movieDetails.getActorIds().isEmpty()) {
            movie.setActors(actorRepository.findAllById(movieDetails.getActorIds()));
        }

        logger.debug("Saving updated movie: {}", movie);
        return convertMovieToDto(movieRepository.save(movie));
    }

    // Method to delete a movie by ID
    @Override
    public void deleteMovie(Long id, boolean cascade) {
        logger.debug("Deleting movie with ID: {}, cascade: {}", id, cascade);
        Movie movie = getMovieByIdFromDb(id);
        if (!cascade && (movie.getActors() != null && !movie.getActors().isEmpty())) {
            logger.error("Cannot delete movie with actors associated unless cascade=true");
            throw new RuntimeException("Cannot delete movie with actors associated. Set cascade=true to delete.");
        }
        movieRepository.delete(movie);
    }

    @Override
    public Page<MovieResponseDto> searchMoviesByTitle(String title, Pageable pageable) {
        logger.debug("Searching movies by title: {}", title);
        Page<Movie> moviesPage = movieRepository.findByTitleContainingIgnoreCase(title, pageable);
        return moviesPage.map(this::convertMovieToDto);
    }

    @Override
    public Page<MovieResponseDto> getMoviesByGenreId(Long genreId, Pageable pageable) {
        logger.debug("Fetching movies by genre ID: {}", genreId);
        genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with ID: " + genreId));
        Page<Movie> moviesPage = movieRepository.findByGenreId(genreId, pageable);
        return moviesPage.map(this::convertMovieToDto);
    }

    @Override
    public Page<MovieResponseDto> getMoviesByReleaseYear(int releaseYear, Pageable pageable) {
        logger.debug("Fetching movies by release year: {}", releaseYear);
        Page<Movie> moviesPage = movieRepository.findByReleaseYear(releaseYear, pageable);
        return moviesPage.map(this::convertMovieToDto);
    }

    @Override
    public List<MovieResponseDto> getMoviesByActorId(Long actorId) {
        logger.debug("Fetching movies by actor ID: {}", actorId);
        Actor actor = getActorByIdFromDb(actorId);
        return actor.getMovies().stream()
                .map(this::convertMovieToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActorResponseDto> getAllActorsInMovie(Long movieId) {
        logger.debug("Fetching actors in movie ID: {}", movieId);
        Movie movie = getMovieByIdFromDb(movieId);
        return movie.getActors().stream()
                .map(this::convertActorToDto)
                .collect(Collectors.toList());
    }

    // Helper methods
    private Movie getMovieByIdFromDb(Long id) {
        logger.debug("Fetching movie by ID: {}", id);
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id));
    }

    private Actor getActorByIdFromDb(Long id) {
        logger.debug("Fetching actor by ID: {}", id);
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with ID: " + id));
    }

    private MovieResponseDto convertMovieToDto(Movie movie) {
        logger.debug("Converting movie to DTO: {}", movie);
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

    private ActorResponseDto convertActorToDto(Actor actor) {
        logger.debug("Converting actor to DTO: {}", actor);
        return ActorResponseDto.builder()
                .id(actor.getId())
                .name(actor.getName())
                .birthDate(actor.getBirthDate())
                .build();
    }
}
