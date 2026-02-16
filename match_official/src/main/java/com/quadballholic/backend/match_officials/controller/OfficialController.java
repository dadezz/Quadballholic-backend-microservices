package com.quadballholic.backend.match_officials.controller;

import com.quadballholic.backend.match_officials.model.EntityMatchOfficials;
import com.quadballholic.backend.match_officials.service.OfficialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/officials")
@RequiredArgsConstructor
public class OfficialController {

    private final OfficialService officialService;

    /**
     * POST endpoint to save a new official.
     * Maps to POST http://localhost:8080/api/officials
     */
    @PostMapping("")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<EntityMatchOfficials> createOfficial(@RequestBody EntityMatchOfficials official) {
        EntityMatchOfficials savedOfficial = officialService.saveOfficial(official);
        return new ResponseEntity<>(savedOfficial, HttpStatus.CREATED);
    }

    /**
     * GET endpoint to retrieve all official.
     * Maps to GET http://localhost:8080/api/officials
     */
    @GetMapping("")
    public ResponseEntity<List<EntityMatchOfficials>> getAllOfficials() {
        List<EntityMatchOfficials> officials = officialService.getAllOfficials();
        return new ResponseEntity<>(officials, HttpStatus.OK);
    }

    /**
     * GET endpoint to retrieve official by ID.
     * Maps to GET http://localhost:8080/api/officials/{id}
     */
    @GetMapping("/{id}")
    // PUBLIC
    public ResponseEntity<EntityMatchOfficials> getOfficialById(@PathVariable("id") Long id){
        // Service handles the "Not Found" exception logic now
        EntityMatchOfficials official = officialService.getOfficialById(id);
        return new ResponseEntity<>(official, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<EntityMatchOfficials> updateOfficialById(@PathVariable("id") Long id, @RequestBody EntityMatchOfficials official) {
        EntityMatchOfficials updatedOfficial = officialService.updateOfficial(id, official);
        return new ResponseEntity<>(updatedOfficial, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION_MANAGER')")
    public ResponseEntity<EntityMatchOfficials> deleteOfficialById(@PathVariable("id") Long id) {
        officialService.deleteOfficialById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

