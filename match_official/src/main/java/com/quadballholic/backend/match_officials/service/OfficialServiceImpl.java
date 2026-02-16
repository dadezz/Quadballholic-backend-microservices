package com.quadballholic.backend.match_officials.service;

import com.quadballholic.backend.match_officials.model.EntityMatchOfficials;
import com.quadballholic.backend.match_officials.repository.OfficialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OfficialServiceImpl implements OfficialService {
    private final OfficialRepository officialRepository;

    @Override
    public EntityMatchOfficials saveOfficial(EntityMatchOfficials official) {
        return officialRepository.save(official);
    }

    @Override
    public List<EntityMatchOfficials> getAllOfficials() {
        return officialRepository.findAll();
    }
    @Override
    public EntityMatchOfficials getOfficialById(Long id) {
        return officialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Official not found"));

    }

    @Override
    public EntityMatchOfficials updateOfficial(Long id, EntityMatchOfficials officialDetails) {
        EntityMatchOfficials official = getOfficialById(id);
        official.setFirstName(officialDetails.getFirstName());
        official.setLastName(officialDetails.getLastName());
        official.setEmail(officialDetails.getEmail());
        official.setPhone(officialDetails.getPhone());
        official.setBirthDate(officialDetails.getBirthDate());
        official.setRole(officialDetails.getRole());

        return saveOfficial(official);
    }

    @Override
    public void deleteOfficialById(Long id) {
        EntityMatchOfficials official = getOfficialById(id);
        officialRepository.delete(official);
    }
}
