package com.quadballholic.backend.reservation.service;

import com.quadballholic.backend.common.service.EmailService;
import com.quadballholic.backend.reservation.client.MatchClient;
import com.quadballholic.backend.reservation.client.UserClient;
import com.quadballholic.backend.reservation.entity.ReservationEntity;
import com.quadballholic.backend.reservation.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final MatchClient matchClient;
    private final UserClient userClient;
    private final EmailService emailService;

    @Override
    public List<ReservationEntity> findAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public ReservationEntity findReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found with id: " + id));
    }

    @Override
    public List<ReservationEntity> findReservationsByUserId(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }
        return reservationRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public ReservationEntity createReservation(ReservationEntity reservation) {
        validateReservation(reservation);
        reservation.setId(null);

        ReservationEntity savedReservation = reservationRepository.save(reservation);

        try {
            String userEmail = userClient.getEmailById(savedReservation.getUserId());
            String matchDetails = String.valueOf(matchClient.getMatchDate(savedReservation.getMatchId()));

            emailService.sendReservationConfirmEmail(userEmail, matchDetails);
        } catch (Exception e) {
            // Se l'email fallisce, logghiamo l'errore ma non interrompiamo il flusso
            System.err.println("Email service error: " + e.getMessage());
        }

        return savedReservation;
    }

    @Override
    public ReservationEntity updateReservation(Long id, ReservationEntity reservationDetails) {
        if (reservationDetails.getId() != null && !id.equals(reservationDetails.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path does not match ID in body");
        }
        validateReservation(reservationDetails);

        ReservationEntity existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found for update"));

        existing.setMatchId(reservationDetails.getMatchId());
        existing.setUserId(reservationDetails.getUserId());
        existing.setSeatNumber(reservationDetails.getSeatNumber());

        return reservationRepository.save(existing);
    }

    @Override
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete: Reservation not found");
        }
        reservationRepository.deleteById(id);
    }

    private void validateReservation(ReservationEntity reservation) {
        if (reservation == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation is null");
        }
        if (reservation.getUserId() == null || !userClient.existsById(reservation.getUserId()) || !userClient.hasRole(reservation.getUserId(),"ROLE_SPECTATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is null or user is not a spectator");
        }
        if (reservation.getMatchId() == null || !matchClient.existsById(reservation.getMatchId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match ID is required");
        }
    }

    @Override
    @Transactional
    public void init() {
        createTestRes("mario.rossi@test.com", 1L, "A10");
        createTestRes("mario.rossi@test.com", 6L, "B22");
        createTestRes("luca.bianchi@test.com", 5L, "C04");
        createTestRes("luca.bianchi@test.com", 7L, "A01");
    }

    private void createTestRes(String email, Long matchId, String seat) {
        Long userId = userClient.getIdByEmail(email);
        if (reservationRepository.findByUserId(userId).stream()
                .noneMatch(r -> r.getMatchId().equals(matchId))) {
            ReservationEntity res = new ReservationEntity();
            res.setUserId(userId);
            res.setMatchId(matchId);
            res.setSeatNumber(seat);

            reservationRepository.save(res);
        }

    }
}