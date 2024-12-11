package com.example.movie.service.impl;

import com.example.movie.DTO.GenreRequestDto;
import com.example.movie.DTO.GenreResponseDto;
import com.example.movie.DTO.GenreResponseMovieDto;
import com.example.movie.entity.Genre;
import com.example.movie.entity.Movie;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.GenreRepository;
import com.example.movie.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    // Method to create a new Genre
    @Override
    public GenreResponseDto createGenre(GenreRequestDto genreRequestDto) {
        // Creating Genre entity from GenreRequestDto
        Genre genre = new Genre();
        genre.setName(genreRequestDto.getName());

        // Saving the Genre entity and converting to GenreResponseDto
        return convertToDto(genreRepository.save(genre));
    }

    // Method to retrieve a Genre by its ID
    @Override
    public GenreResponseDto getGenreById(Long id) {
        // Fetching Genre by ID or throwing exception if not found
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));

        // Converting the Genre entity to GenreResponseDto
        return convertToDto(genre);
    }

    // Method to get all genres, with optional pagination
    @Override
    public List<GenreResponseDto> getAllGenres(Integer page, Integer size) {
        // List to store all retrieved genres
        List<Genre> genres;

        // If pagination parameters are provided, get a page of genres
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Genre> genrePage = genreRepository.findAll(pageable);
            genres = genrePage.getContent();
        } else {
            // If no pagination, retrieve all genres
            genres = genreRepository.findAll();
        }

        // Convert the list of Genre entities to GenreResponseDto
        return genres.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Method to update the details of an existing Genre
    @Override
    public GenreResponseDto updateGenre(Long id, GenreRequestDto genreRequestDto) {
        // Fetching the Genre entity by ID or throwing an exception if not found
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));

        // Update the name of the Genre if provided in request DTO
        if (genreRequestDto.getName() != null) {
            genre.setName(genreRequestDto.getName());
        }

        // Save the updated Genre and convert it to GenreResponseDto
        return convertToDto(genreRepository.save(genre));
    }

    // Method to delete a genre, with an option to cascade delete movies associated with the genre
    @Override
    public void deleteGenre(Long id, boolean cascade) {
        // Fetch the Genre entity by ID or throw exception if not found
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));

        // If cascade delete is not allowed and there are associated movies, throw an exception
        if (!cascade && (genre.getMovies() != null && !genre.getMovies().isEmpty())) {
            throw new RuntimeException("Cannot delete genre '" + genre.getName() + "' because it has "
                    + genre.getMovies().size() + " associated movies.");
        }

        // Delete the Genre entity
        genreRepository.delete(genre);
    }

    // Helper method to convert Genre entity to GenreResponseDto
    private GenreResponseDto convertToDto(Genre genre) {
        return GenreResponseDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .movies(genre.getMovies() != null ? genre.getMovies().stream()
                        .map(this::convertToMovieDto)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    // Helper method to convert Movie entity to GenreResponseMovieDto
    private GenreResponseMovieDto convertToMovieDto(Movie movie) {
        return GenreResponseMovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .releaseYear(movie.getReleaseYear())
                .duration(movie.getDuration())
                .build();
    }
}
