package com.example.movie.repository;


import com.example.movie.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query(value = "SELECT * FROM genre LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Genre> findGenresWithPagination(@Param("limit") int limit, @Param("offset") int offset);
}
