package com.quadballholic.backend.teamService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service") // Nome su Eureka
public interface UserClient {

    @GetMapping("/api/users/{id}/exists")
    Boolean existsById(@PathVariable("id") Long id);

    @GetMapping("/api/users/{id}/has-role")
    Boolean hasRole(@PathVariable("id") Long id, @RequestParam("roleName") String roleName);

    @GetMapping("/api/users/{id}/email")
    String getEmailById(@PathVariable("id") Long id);

    @GetMapping("/api/users/find-by-email")
    Long getIdByEmail(@RequestParam("email") String email);
}