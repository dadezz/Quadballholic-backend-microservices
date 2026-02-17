package com.quadballholic.backend.tournamentService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", contextId = "userClientForTournaments")
public interface UserClient {

    @GetMapping("/api/users/{id}/has-role")
    Boolean hasRole(@PathVariable("id") Long id, @RequestParam("roleName") String roleName);

    @GetMapping("/api/users/{id}/exists")
    Boolean existsById(@PathVariable("id") Long id);
}
