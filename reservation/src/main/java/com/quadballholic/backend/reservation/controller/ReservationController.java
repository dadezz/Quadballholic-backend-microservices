package com.quadballholic.backend.reservation.controller;

import com.quadballholic.backend.reservation.entity.ReservationEntity;
import com.quadballholic.backend.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('SPECTATOR', 'ORGANIZATION_MANAGER', 'TEAM_MANAGER')")
    public ResponseEntity<ReservationEntity> createReservation(@RequestBody ReservationEntity reservation) {
        return new ResponseEntity<>(reservationService.createReservation(reservation), HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<List<ReservationEntity>> getAllReservations() {
        return new ResponseEntity<>(reservationService.findAllReservations(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SPECTATOR', 'ORGANIZATION_MANAGER', 'TEAM_MANAGER')")
    public ResponseEntity<ReservationEntity> getReservationById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reservationService.findReservationById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('SPECTATOR', 'ORGANIZATION_MANAGER', 'TEAM_MANAGER')")
    public ResponseEntity<List<ReservationEntity>> getUserReservations(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(reservationService.findReservationsByUserId(userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SPECTATOR', 'ORGANIZATION_MANAGER', 'TEAM_MANAGER')")
    public ResponseEntity<ReservationEntity> updateReservation(@PathVariable("id") Long id, @RequestBody ReservationEntity reservation) {
        return ResponseEntity.ok(reservationService.updateReservation(id, reservation));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SPECTATOR', 'ORGANIZATION_MANAGER', 'TEAM_MANAGER')")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
