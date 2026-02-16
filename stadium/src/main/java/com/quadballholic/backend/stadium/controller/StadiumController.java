package com.quadballholic.backend.stadium.controller;

import com.quadballholic.backend.common.contracts.StadiumValidator;
import com.quadballholic.backend.stadium.model.EntityStadium;
import com.quadballholic.backend.stadium.service.StadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/stadiums")
@RequiredArgsConstructor
public class StadiumController {

    private final StadiumService stadiumService;
    private final StadiumValidator stadiumValidator;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<EntityStadium> createStadium(@RequestBody EntityStadium stadiumEntity) {
        EntityStadium created = stadiumService.createStadium(stadiumEntity);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EntityStadium>> getAllStadiums() {
        return ResponseEntity.ok(stadiumService.findAllStadiums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityStadium> getStadiumById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(stadiumService.findStadiumById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<EntityStadium> updateStadium(@PathVariable("id") Long id, @RequestBody EntityStadium stadiumEntity) {
        EntityStadium updated = stadiumService.updateStadium(id, stadiumEntity);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<Void> deleteStadium(@PathVariable("id") Long id) {
        stadiumService.deleteStadium(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/has-capacity")
    public ResponseEntity<Boolean> checkStadiumCapacity(
            @PathVariable("id") Long id,
            @RequestParam("seats") Integer seats) {

        boolean hasCapacity = stadiumValidator.hasCapacity(id, seats);
        return ResponseEntity.ok(hasCapacity);
    }
}