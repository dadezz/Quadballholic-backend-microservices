package com.quadballholic.backend.reservation.service;

import com.quadballholic.backend.reservation.entity.ReservationEntity;
import java.util.List;

public interface ReservationService {
    List<ReservationEntity> findAllReservations();
    ReservationEntity findReservationById(Long id);
    List<ReservationEntity> findReservationsByUserId(Long userId);
    ReservationEntity createReservation(ReservationEntity reservation);
    ReservationEntity updateReservation(Long id, ReservationEntity reservationDetails);
    void deleteReservation(Long id);
    public void init();
}
