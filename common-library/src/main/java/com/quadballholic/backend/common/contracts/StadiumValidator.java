package com.quadballholic.backend.common.contracts;

public interface StadiumValidator {
    boolean exists(Long stadiumId);
    boolean hasCapacity(Long stadiumId, Integer numberOfSeats);
}
