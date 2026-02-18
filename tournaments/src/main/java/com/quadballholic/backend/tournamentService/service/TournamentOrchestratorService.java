package com.quadballholic.backend.tournamentService.service;

import com.quadballholic.backend.tournamentService.client.MatchClient;
import com.quadballholic.backend.tournamentService.client.StadiumClient;
import com.quadballholic.backend.tournamentService.dto.MatchDto;
import com.quadballholic.backend.tournamentService.dto.StadiumDto;
import com.quadballholic.backend.tournamentService.entity.MatchTournamentEntity;
import com.quadballholic.backend.tournamentService.service.MatchTournamentService;
import com.quadballholic.backend.tournamentService.service.TournamentService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TournamentOrchestratorService {

    private final MatchClient matchService;
    private final StadiumClient stadiumService;

    private final MatchTournamentService mtService;
    private final TournamentService tournamentService;
    private final MatchClient matchClient;

    @Transactional
    public MatchDto createMatchAndLinkToTournament(MatchDto match, int round, int index, Long nextMatchId) {
        MatchDto createdMatch = matchService.createMatch(match);
        mtService.createMatchTournament(match.getTournamentId(), createdMatch.getId(), round, index, nextMatchId);
        return createdMatch;
    }

    public List<List<MatchDto>> findAllMatchesByTournamentId(Long tournamentId) {
        List<MatchTournamentEntity> relations = mtService.findAllMatchesByTournament(tournamentId);

        if (relations.isEmpty()) return new ArrayList<>();

        // extraction of match ids
        List<Long> matchIds = relations.stream()
                .map(MatchTournamentEntity::getMatchId)
                .toList();

        // extraction of all match entities
        List<MatchDto> matches = matchService.findAllMatchesByIds(matchIds);

        // Map id->entity to search
        Map<Long, MatchDto> matchMap = matches.stream()
                .collect(Collectors.toMap(MatchDto::getId, m -> m));

        // group by round
        Map<Integer, List<MatchTournamentEntity>> roundMap = relations.stream()
                .collect(Collectors.groupingBy(MatchTournamentEntity::getRound));


        int maxRound = roundMap.keySet().stream().max(Integer::compare).orElse(0);
        List<List<MatchDto>> bracketStructure = new ArrayList<>();

        for (int i = 0; i <= maxRound; i++) {
            List<MatchTournamentEntity> roundRelations = roundMap.getOrDefault(i, new ArrayList<>());

            // reorder for bracket index
            roundRelations.sort(Comparator.comparingInt(MatchTournamentEntity::getBracketIndex));

            // from relations to match entities
            List<MatchDto> roundMatches = roundRelations.stream()
                    .map(rel -> matchMap.get(rel.getMatchId()))
                    .filter(Objects::nonNull) // sicurezza
                    .toList();

            bracketStructure.add(roundMatches);
        }

        return bracketStructure;
    }

    @Transactional
    public List<List<MatchDto>> startTournament(@NotNull List<Long> teamIds, LocalDate startDate, LocalDate endDate, Long tournamentId) {
        /* it will return something like that:
        [       -> list of rounds
          [     -> list of matches of that round
            {
              ... match details
            },
            {
              ... match details
            }
          ],
          [
            {
              ... match details
            }
          ]
        ]*/

        // it works because teamIds is power of 2 (and if not, preliminary check wil throw an exception)
        int necessaryRounds = Integer.numberOfTrailingZeros(teamIds.size());
        preliminaryChecks(teamIds, startDate, endDate, necessaryRounds, tournamentId);

        List<StadiumDto> stadiums = stadiumService.findAllStadiums();
        if (stadiums.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No stadiums found");
        }

        Map<LocalDate, Set<Long>> occupancyMap = new HashMap<>(); // to know if stadium is occupied
        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 inclusive
        int daysPerRound = totalDays / necessaryRounds;

        // list of the rounds, each containing the scheduled matches
        List<List<MatchDto>> scheduledMatches = new ArrayList<>();
        // take trace of previous round matches
        List<MatchDto> previousRoundMatches;

        // --- ROUND 0 ---
        Collections.shuffle(teamIds);
        LocalDate r0End = startDate.plusDays(daysPerRound - 2); // rest days

        List<Long> allCreatedMatches = new ArrayList<>(); // needed to try a rollback if something goes wrong

        List<MatchDto> currentRoundMatches = new ArrayList<>();
        int bracketIndex = 0;
        try {
            for (int i = 0; i < teamIds.size(); i += 2) {
                MatchDto match = new MatchDto();
                match.setTournamentId(tournamentId);
                match.setHomeTeamId(teamIds.get(i));
                match.setAwayTeamId(teamIds.get(i + 1));
                match.setHomeScore(null);
                match.setAwayScore(null);

                assignSlot(match, startDate, r0End, stadiums, occupancyMap);
                match = createMatchAndLinkToTournament(match, 0, bracketIndex++, null); // Round 0, NextMatch null per ora

                currentRoundMatches.add(match);
                allCreatedMatches.add(match.getId());
            }

            scheduledMatches.add(currentRoundMatches);
            previousRoundMatches = currentRoundMatches;


            // --- SUBSEQUENT ROUNDS ---
            for (int r = 1; r < necessaryRounds; r++) {
                LocalDate roundStart = startDate.plusDays((long) r * daysPerRound);
                LocalDate roundEnd = roundStart.plusDays(daysPerRound - 2);

                currentRoundMatches = new ArrayList<>();
                bracketIndex = 0;

                // two matches on the previous round converge to one match in this one
                for (int i = 0; i < previousRoundMatches.size(); i += 2) {

                    MatchDto nextMatch = new MatchDto();
                    nextMatch.setTournamentId(tournamentId);
                    nextMatch.setHomeTeamId(null);
                    nextMatch.setAwayTeamId(null);
                    nextMatch.setHomeScore(null);
                    nextMatch.setAwayScore(null);
                    assignSlot(nextMatch, roundStart, roundEnd, stadiums, occupancyMap);

                    nextMatch = createMatchAndLinkToTournament(nextMatch, r, bracketIndex++, null);
                    currentRoundMatches.add(nextMatch);
                    allCreatedMatches.add(nextMatch.getId());

                    // tree linking
                    MatchDto child1 = previousRoundMatches.get(i);
                    MatchDto child2 = previousRoundMatches.get(i+1);

                    mtService.updateNextMatch(child1.getId(), nextMatch.getId());
                    mtService.updateNextMatch(child2.getId(), nextMatch.getId());
                }

                scheduledMatches.add(currentRoundMatches);
                previousRoundMatches = currentRoundMatches;
            }
        } catch (Exception e) {
            Collections.reverse(allCreatedMatches); // LIFO to prevent database error
            for (Long matchId : allCreatedMatches) {
                try {
                    matchClient.deleteMatchById(matchId);
                } catch (Exception de) {
                    System.out.println("Rollback failed for matchId "
                            + matchId
                            + ". Caused By: "
                            + de.getMessage());
                }
            }
            throw e;
        }
        tournamentService.setStarted(tournamentId);
        return scheduledMatches;
    }

    private void assignSlot(MatchDto match, LocalDate startWindow, LocalDate endWindow,
                            List<StadiumDto> stadiums, Map<LocalDate, Set<Long>> occupancyMap) {
        LocalDate currentDate = startWindow;
        boolean assigned = false;

        while (!currentDate.isAfter(endWindow) && !assigned) {

            Set<Long> busyStadiumsIds = occupancyMap.getOrDefault(currentDate, new HashSet<>());
            StadiumDto freeStadium = null;
            for (StadiumDto s : stadiums) {
                if (!busyStadiumsIds.contains(s.getId())) {
                    freeStadium = s;
                    break;
                }
            }

            if (freeStadium != null) {
                match.setDate(currentDate);
                match.setStadiumId(freeStadium.getId());

                busyStadiumsIds.add(freeStadium.getId());
                occupancyMap.put(currentDate, busyStadiumsIds);
                assigned = true;
            } else {
                currentDate = currentDate.plusDays(1);
            }
        }

        if (!assigned) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Impossible to schedule: no free stadiums or time window too short starting from " + startWindow);
        }
    }

    private void preliminaryChecks(List<Long> teamIds, LocalDate startDate, LocalDate endDate, int necessaryRounds, Long tournamentId) {

        Set<Long> teamIdSet = new HashSet<>(teamIds);
        if (tournamentService.hasStarted(tournamentId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tournament has already started");
        }
        if (teamIdSet.size() != teamIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are teams repeated");
        }
        if (!isPowerTwo(teamIds)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The number of teams is not power two");
        }
        if (endDate.isBefore(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }
        if (!areDaysEnough(necessaryRounds, startDate, endDate)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The period is too narrow");
        }
    }

    private boolean isPowerTwo(List<Long> teamIds) {
        int size = teamIds.size();
        return size > 0 && (size & (size - 1)) == 0;
    }

    private int getTotalDays(LocalDate startDate, LocalDate endDate){
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    private boolean areDaysEnough(int necessaryRounds, LocalDate startDate, LocalDate endDate) {
        int minimum = necessaryRounds + (necessaryRounds - 1) * 2; // 1 day for each match and 2 days of rest

        // +1 because start and end dates are inclusive
        int totalDays = getTotalDays(startDate, endDate);
        return totalDays >= minimum;
    }

}
