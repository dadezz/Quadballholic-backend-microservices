package com.quadballholic.backend.stadium.service;

import com.quadballholic.backend.stadium.model.EntityStadium;
import com.quadballholic.backend.stadium.repository.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestStadiumServiceImpl implements  TestStadiumService {

    private final StadiumRepository stadiumRepository;

    @Override
    public void init(){

        if(stadiumRepository.count() == 0){
            List<EntityStadium> stadiumEntities = List.of(
                    new EntityStadium("San Siro","Milano",100),
                    new EntityStadium("Giuseppe Meazza","Milano",100),
                    new EntityStadium("Stadio Olimpico","Roma",500)
            );
            stadiumRepository.saveAll(stadiumEntities);
        }
    }
}
