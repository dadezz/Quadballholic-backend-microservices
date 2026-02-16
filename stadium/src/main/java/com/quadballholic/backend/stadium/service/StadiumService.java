package com.quadballholic.backend.stadium.service;

import com.quadballholic.backend.stadium.model.EntityStadium;

import java.util.List;

public interface StadiumService {

    EntityStadium createStadium(EntityStadium stadium);

    List<EntityStadium> findAllStadiums();

    EntityStadium findStadiumById(Long id);

    EntityStadium updateStadium(Long id, EntityStadium stadiumDetails);

    void deleteStadium(Long id);
}