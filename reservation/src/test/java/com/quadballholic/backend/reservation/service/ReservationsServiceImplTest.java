package com.quadballholic.backend.reservation.service;

import com.quadballholic.backend.reservation.entity.ReservationEntity;
import com.quadballholic.backend.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    // --- CREATE TESTS ---

    @Test
    void createReservation_ShouldReturnSaved_WhenValid() {
        // Arrange
        ReservationEntity input = new ReservationEntity();
        input.setMatchId(10L);
        input.setUserId(5L);

        ReservationEntity saved = new ReservationEntity();
        saved.setId(1L);
        saved.setMatchId(10L);
        saved.setUserId(5L);

        when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(saved);

        // Act
        ReservationEntity result = reservationService.createReservation(input);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(reservationRepository).save(input);
    }

    @Test
    void createReservation_ShouldThrowBadRequest_WhenMatchIdMissing() {
        ReservationEntity input = new ReservationEntity();
        input.setUserId(5L);
        // matchId is null

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.createReservation(input);
        });
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createReservation_ShouldThrowBadRequest_WhenUserIdMissing() {
        ReservationEntity input = new ReservationEntity();
        input.setMatchId(10L);
        // userId is null

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.createReservation(input);
        });
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // --- UPDATE TESTS ---

    @Test
    void updateReservation_ShouldUpdate_WhenExistsAndValid() {
        Long id = 1L;
        ReservationEntity existing = new ReservationEntity();
        existing.setId(id);

        ReservationEntity updateInfo = new ReservationEntity();
        updateInfo.setMatchId(20L);
        updateInfo.setUserId(5L);

        when(reservationRepository.findById(id)).thenReturn(Optional.of(existing));
        when(reservationRepository.save(any(ReservationEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ReservationEntity result = reservationService.updateReservation(id, updateInfo);

        assertThat(result.getMatchId()).isEqualTo(20L);
    }

    @Test
    void updateReservation_ShouldThrowBadRequest_WhenIdMismatch() {
        Long pathId = 1L;
        ReservationEntity body = new ReservationEntity();
        body.setId(2L); // Mismatch!
        body.setMatchId(10L);
        body.setUserId(5L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.updateReservation(pathId, body);
        });
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateReservation_ShouldThrowNotFound_WhenNotExists() {
        Long id = 999L;
        ReservationEntity updateInfo = new ReservationEntity();
        updateInfo.setMatchId(10L);
        updateInfo.setUserId(5L);

        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.updateReservation(id, updateInfo);
        });
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // --- DELETE TESTS ---

    @Test
    void deleteReservation_ShouldDelete_WhenExists() {
        Long id = 1L;
        when(reservationRepository.existsById(id)).thenReturn(true);

        reservationService.deleteReservation(id);

        verify(reservationRepository).deleteById(id);
    }

    @Test
    void deleteReservation_ShouldThrowNotFound_WhenNotExists() {
        Long id = 999L;
        when(reservationRepository.existsById(id)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.deleteReservation(id);
        });
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}