package com.quadballholic.backend.teamService.service;

import com.quadballholic.backend.teamService.entity.EntityTeam;
import com.quadballholic.backend.teamService.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestTeamServiceImpl implements TestTeamService {

    private final TeamRepository teamRepository;

    @Override
    public List<EntityTeam> init(){
        if(teamRepository.count() == 0){
            List<EntityTeam> teams = List.of(
                    new EntityTeam("Italy Quadball","Venezia","Italy",1L),
                    new EntityTeam("Turkey Quadball","Ankara","Turkey",2L),
                    new EntityTeam("USA Quadball", "New York", "USA", 3L),
                    new EntityTeam("UK Quadball", "London", "UK", 4L),
                    new EntityTeam("France Quadball", "Paris", "France", 5L),
                    new EntityTeam("Germany Quadball", "Berlin", "Germany", 6L),
                    new EntityTeam("Spain Quadball", "Madrid", "Spain", 7L),
                    new EntityTeam("Belgium Quadball", "Brussels", "Belgium", 8L)
            );
            return teamRepository.saveAll(teams);
        }
        return teamRepository.findAll();

    }

}
