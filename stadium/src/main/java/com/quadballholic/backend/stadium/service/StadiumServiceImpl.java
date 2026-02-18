package com.quadballholic.backend.stadium.service;

import com.quadballholic.backend.stadium.model.EntityStadium;
import com.quadballholic.backend.stadium.repository.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    @Override
    public EntityStadium createStadium(EntityStadium stadium) {
        validateContent(stadium);
        return stadiumRepository.save(stadium);
    }

    @Override
    public List<EntityStadium> findAllStadiums() {
        return stadiumRepository.findAll();
    }

    @Override
    public EntityStadium findStadiumById(Long id) {
        return stadiumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stadium not found with id: " + id));
    }

    @Override
    public EntityStadium updateStadium(Long id, EntityStadium stadiumDetails) {
        validateContent(stadiumDetails);

        EntityStadium existingStadium = findStadiumById(id);

        existingStadium.setName(stadiumDetails.getName());
        existingStadium.setAddress(stadiumDetails.getAddress());
        existingStadium.setCapacity(stadiumDetails.getCapacity());

        return stadiumRepository.save(existingStadium);
    }

    @Override
    public void deleteStadium(Long id) {
        EntityStadium stadium = findStadiumById(id);
        stadiumRepository.delete(stadium);
    }

    private void validateContent(EntityStadium stadium) {
        if (stadium == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stadium body cannot be null");
        }
        if (stadium.getName() == null || stadium.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stadium name is required");
        }
        if (stadium.getCapacity() == null || stadium.getCapacity() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Capacity must be a non-negative integer");
        }
    }
}