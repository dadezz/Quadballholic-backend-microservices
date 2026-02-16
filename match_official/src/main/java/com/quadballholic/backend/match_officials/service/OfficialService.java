package com.quadballholic.backend.match_officials.service;

import com.quadballholic.backend.match_officials.model.EntityMatchOfficials;
import java.util.List;


public interface OfficialService {
    EntityMatchOfficials saveOfficial(EntityMatchOfficials official);
    List <EntityMatchOfficials> getAllOfficials();
    EntityMatchOfficials getOfficialById(Long id);

    EntityMatchOfficials updateOfficial(Long id, EntityMatchOfficials officialDetails);

    void deleteOfficialById(Long id);
}
