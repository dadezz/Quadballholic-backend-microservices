package com.quadballholic.backend.match_officials.service;

import com.quadballholic.backend.match_officials.enums.EnumRole;
import com.quadballholic.backend.match_officials.model.EntityMatchOfficials;
import com.quadballholic.backend.match_officials.repository.OfficialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestOfficialServiceImpl implements TestOfficialService {

    private final OfficialRepository officialRepository;

    @Override
    public void init(){

        if(officialRepository.count() == 0){
            List<EntityMatchOfficials> referees = List.of(
                    // HEAD REFEREES (The most senior officials)
                    new EntityMatchOfficials("Matt", "Bateman", EnumRole.HEAD_REFEREE), // QuadballUK Director, veteran official
                    new EntityMatchOfficials("Christian", "Barnes", EnumRole.HEAD_REFEREE), // USQ Referee Coordinator
                    new EntityMatchOfficials("Sasha", "Ribayrol", EnumRole.HEAD_REFEREE), // FQF (France) President/Official

                    // ASSISTANT REFEREES (Often experienced players/admins)
                    new EntityMatchOfficials("Fatih", "Aykurt", EnumRole.ASSISTANT_REFEREE), // TQD (Turkey) Rep & Veteran Player
                    new EntityMatchOfficials("Hanne", "Van Tichelt", EnumRole.ASSISTANT_REFEREE), // BQF (Belgium) Representative
                    new EntityMatchOfficials("Melis", "Ercan", EnumRole.ASSISTANT_REFEREE), // TQD (Turkey) Representative

                    // FLAG REFEREE (Watches the Seeker/Flag Runner interaction)
                    new EntityMatchOfficials("Michael", "Škácha", EnumRole.FLAG_REFEREE), // Czech Republic Representative

                    // FLAG RUNNER (Neutral athlete wearing the flag)
                    new EntityMatchOfficials("Kaci", "Erwin", EnumRole.FLAG_RUNNER), // Known for high-physicality play

                    // TABLE & GOAL JUDGES (Support staff)
                    new EntityMatchOfficials("Matthias", "Reittinger", EnumRole.SCORE_TABLE), // QAT (Austria) Representative
                    new EntityMatchOfficials("Dina", "Fjellmyr", EnumRole.GOAL_JUDGES) // NRF (Norway) Representative
            );

            officialRepository.saveAll(referees);
        }

    }
}
