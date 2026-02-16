package com.quadballholic.backend.tournamentService.client;

import com.quadballholic.backend.tournamentService.dto.StadiumDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "match-service", contextId = "matchClientForTournaments")
public interface StadiumClient {

    @GetMapping("/api/stadium/all")
    List<StadiumDto> findAllStadiums();

}