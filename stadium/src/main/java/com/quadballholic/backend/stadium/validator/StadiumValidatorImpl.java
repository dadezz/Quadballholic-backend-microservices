package com.quadballholic.backend.stadium.validator;

import com.quadballholic.backend.common.contracts.StadiumValidator;
import com.quadballholic.backend.stadium.model.EntityStadium;
import com.quadballholic.backend.stadium.service.StadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StadiumValidatorImpl implements StadiumValidator {

    private final StadiumService stadiumService;

    @Override
    public boolean exists(Long stadiumId) {
        return stadiumService.findStadiumById(stadiumId) != null;
    }

    @Override
    public boolean hasCapacity(Long stadiumId, Integer numberOfSeats) {
        EntityStadium s = stadiumService.findStadiumById(stadiumId);
        return s.getCapacity() >= numberOfSeats;
    }
}
