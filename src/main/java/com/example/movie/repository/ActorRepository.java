package com.example.movie.repository;

import com.example.movie.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = "SELECT * FROM actor LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Actor> findAllWithPagination(int limit, int offset);
}

