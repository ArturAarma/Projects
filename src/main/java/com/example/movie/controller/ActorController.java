package com.example.movie.controller;

import com.example.movie.DTO.ActorRequestDto;
import com.example.movie.DTO.ActorResponseDto;
import com.example.movie.entity.Actor;
import com.example.movie.service.ActorService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    // Create a new actor and return HTTP status 201 (Created) if successful
    @PostMapping
    public ResponseEntity<ActorResponseDto> createActor(@Valid @RequestBody ActorRequestDto actorRequestDto) {
        ActorResponseDto newActor = actorService.createActor(actorRequestDto);
        return ResponseEntity.status(201).body(newActor); // Returning HTTP 201 Created
    }

    // Get actor by ID, return 200 (OK) if found or 404 (Not Found) if not
    @GetMapping("/{id}")
    public ResponseEntity<ActorResponseDto> getActorById(@PathVariable Long id) {
        ActorResponseDto actor = actorService.getActorById(id);
        if (actor != null) {
            return ResponseEntity.ok(actor); // Returning HTTP 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Returning HTTP 404 Not Found
        }
    }

    // Search actors by name, return paginated results with 200 OK
    @GetMapping("/search")
    public ResponseEntity<Page<Actor>> searchActorsByName(
            @RequestParam String name,
            Pageable pageable
    ) {
        Page<Actor> actors = actorService.searchActorsByName(name, pageable);
        return ResponseEntity.ok(actors); // Returning HTTP 200 OK
    }

    // Get all actors with pagination, return 200 OK
    @GetMapping
    public ResponseEntity<List<ActorResponseDto>> getAllActors(
            @RequestParam(required = false)@Max(100) Integer page,
            @RequestParam(required = false)@Min(0)@Max(20) Integer size
    ) {
        List<ActorResponseDto> actors = actorService.getAllActors(page, size);
        return ResponseEntity.ok(actors); // Returning HTTP 200 OK
    }

    // Update an actor by ID, return 200 (OK) if successful or 404 (Not Found) if actor not found
    @PatchMapping("/{id}")
    public ResponseEntity<ActorResponseDto> updateActor(@PathVariable Long id, @RequestBody ActorRequestDto actorDetails) {
        ActorResponseDto updatedActor = actorService.updateActor(id, actorDetails);
        if (updatedActor != null) {
            return ResponseEntity.ok(updatedActor); // Returning HTTP 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Returning HTTP 404 Not Found
        }
    }

    // Delete actor by ID, return 204 (No Content) when successfully deleted
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean cascade) {
        actorService.deleteActor(id, cascade);
        return ResponseEntity.noContent().build(); // Returning HTTP 204 No Content
    }
}

