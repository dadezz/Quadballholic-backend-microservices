package com.quadballholic.backend.reservation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path= "/api/users")
public interface UserClient {

    @GetMapping("/{id}/exists")
    Boolean existsById(@PathVariable("id") Long id);

    @GetMapping("/{id}/has-role")
    Boolean hasRole(@PathVariable("id") Long id, @RequestParam("roleName") String roleName);

    @GetMapping("/{id}/email")
    String getEmailById(@PathVariable("id") Long id);

    @GetMapping("/find-by-email")
    Long getIdByEmail(@RequestParam("email") String email);
}
