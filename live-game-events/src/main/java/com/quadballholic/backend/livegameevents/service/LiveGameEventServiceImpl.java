package com.quadballholic.backend.livegameevents.service;

import com.quadballholic.backend.livegameevents.client.MatchClient;
import com.quadballholic.backend.livegameevents.client.MatchPlayerClient;
import com.quadballholic.backend.livegameevents.client.MatchTournamentClient;
import com.quadballholic.backend.livegameevents.dto.LiveGameEventDTO;
import com.quadballholic.backend.livegameevents.dto.MatchDetails;
import com.quadballholic.backend.livegameevents.dto.MatchPlayerDetails;
import com.quadballholic.backend.livegameevents.dto.RosterRequest;
import com.quadballholic.backend.livegameevents.enums.EnumLiveGameEventType;
import com.quadballholic.backend.livegameevents.model.EntityLiveGameEvent;
import com.quadballholic.backend.livegameevents.repository.LiveGameEventRepository;
import com.quadballholic.backend.livegameevents.service.state.MatchState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j // <--- 1. Use Lombok Logger
@Service
@RequiredArgsConstructor
public class LiveGameEventServiceImpl implements LiveGameEventService {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final LiveGameEventPublisher eventPublisher;
    private final LiveGameEventRepository liveGameEventRepository;
    private final Random random = new SecureRandom();
    private final MatchClient matchClient;
    private final MatchPlayerClient matchPlayerClient;
    private final MatchTournamentClient matchTournamentClient;


    @Override
    public void startMatchSimulation(Long matchId) {
        log.info("Starting simulation request for match {}", matchId);

        liveGameEventRepository.deleteAllByMatchId(matchId);
        matchClient.resetMatchSimulationById(matchId);

        executorService.submit(() -> runGameLoop(matchId));
    }

    @Override
    public void runGameLoop(Long matchId) {
        log.info("Game Loop Started for Match ID: {}", matchId);
        MatchState matchState;

        try {
            matchState = initializeMatchState(matchId);
        } catch (Exception e) {
            log.error("Failed to initialize match state for match {}", matchId, e);
            return;
        }

        handleStartMatchEvent(matchState);

        while (matchState.isGameRunning()) {
            try {
                Thread.sleep(2000);
                matchState.increaseMatchMinute();
                log.debug("Match: {}, Minute: {}", matchId, matchState.getMatchMinute());

                if (matchState.getMatchMinute() >= 20 && !matchState.isSnitchReleased()) {
                    log.info("Snitch Released for Match {}", matchId);
                    matchState.setSnitchReleased(true);
                }

                // Safety Break
                if (matchState.getMatchMinute() >= 40) {
                    log.info("Forced timeout reached (40 mins). Ending match {}", matchId);
                    handleSnitchCatchEvent(matchState);
                    handleMatchEndEvent(matchState);
                    matchState.setGameRunning(false);
                    break;
                }

                double randomDouble = random.nextDouble();

                // 2. Wrap logic in Try-Catch to catch specific event failures without killing the loop
                try {
                    if (randomDouble < 0.40) {
                        handleScoreEvent(matchState);
                    } else if (randomDouble < 0.70) {
                        handleSubstitutionEvents(matchState, null);
                    } else if (randomDouble < 0.80) {
                        handleYellowCardEvent(matchState);
                    } else if (randomDouble < 0.83) {
                        handleRedCardEvent(matchState, null);
                    } else if ((matchState.isSnitchReleased() && randomDouble < 0.90)) {
                        log.info("Random Snitch Catch triggered");
                        matchState.setGameRunning(false);
                        handleSnitchCatchEvent(matchState);
                        handleMatchEndEvent(matchState);
                        break;
                    }
                } catch (Exception e) {
                    log.error("Error processing event at minute {}: {}", matchState.getMatchMinute(), e.getMessage(), e);
                }

            } catch (InterruptedException e) {
                log.warn("Simulation interrupted for match {}", matchId);
                Thread.currentThread().interrupt();
                matchState.setGameRunning(false);
            } catch (Exception e) {
                // 3. Catch Unexpected Crashes (like NPEs)
                log.error("CRITICAL: Unexpected error in game loop for match {}", matchId, e);
                matchState.setGameRunning(false);
            }
        }
        log.info("Match simulation ended for Match ID: {}", matchId);
    }


    @Override
    public List<LiveGameEventDTO> getAllEventsForMatch(Long matchId) {
        MatchDetails details = matchClient.getById(matchId);
        return liveGameEventRepository.findAllByMatchId(matchId)
                .stream()
                .map(event -> event.toDTO(details.homeTeamId(), details.awayTeamId()))
                .toList();
    }

    private MatchState initializeMatchState(Long matchId) {
        MatchDetails details = matchClient.getById(matchId);
        Long homeTeamId = details.homeTeamId();
        Long awayTeamId = details.awayTeamId();
        if(homeTeamId == null || awayTeamId == null) {
            throw new RuntimeException("Match is not ready to be started, bracket is not complete");
        }
        MatchState matchState = new MatchState(matchId, homeTeamId, awayTeamId);

        List<MatchPlayerDetails> matchPlayers = matchPlayerClient.getMatchPlayersByMatchId(matchId);
        for (MatchPlayerDetails p : matchPlayers) {
            matchState.addPlayerToRoster(p);
        }
        return matchState;
    }

    private void handleStartMatchEvent(MatchState matchState) {
        matchState.setGameRunning(true);
        log.info("Match {} started", matchState.getMatchId());
        createAndPublishEvent(matchState, null, null, EnumLiveGameEventType.MATCH_START);
    }

    private void handleScoreEvent(MatchState matchState) {
        Long homeTeamId = matchState.getHomeTeamId();
        Long awayTeamId = matchState.getAwayTeamId();
        Long attackingTeam = random.nextDouble() < 0.5 ? homeTeamId : awayTeamId;

        // SAFE GUARD
        List<MatchPlayerDetails> onField = matchState.getOnFieldPlayers(attackingTeam);
        if (onField == null || onField.isEmpty()) {
            log.warn("Cannot handle score: No players on field for team {}", attackingTeam);
            return;
        }

        Optional<MatchPlayerDetails> mp = onField.stream()
                .filter(matchPlayer -> matchPlayer.getPlayerPosition().equals("CHASER"))
                .findAny();

        if (mp.isPresent()) {
            MatchPlayerDetails randomScorer = mp.get();
            randomScorer = matchPlayerClient.updatePlayerStats(randomScorer.getMatchId(), randomScorer.getPlayerId(), "SCORE");
            randomScorer.scored();
            matchState.updateScore(randomScorer.getTeamId(), 10);
            matchClient.updateMatchScore(matchState.getMatchId(), matchState.getMatchScore().get(homeTeamId), matchState.getMatchScore().get(awayTeamId));
            createAndPublishEvent(matchState, randomScorer.getTeamId(), randomScorer.getPlayerId(), EnumLiveGameEventType.SCORE);
            log.info("GOAL! Player {} scored for Team {}", randomScorer.getPlayerId(), randomScorer.getTeamId());
        }
    }

    private void handleSubstitutionEvents(MatchState matchState, MatchPlayerDetails leavingPlayer) {
        Long substitutionTeamId;

        if (leavingPlayer == null) {
            substitutionTeamId = random.nextDouble() < 0.5 ? matchState.getHomeTeamId() : matchState.getAwayTeamId();
            List<MatchPlayerDetails> possibleLeavingPlayers = matchState.getOnFieldPlayers(substitutionTeamId);

            // CRITICAL FIX: Check for empty list
            if (possibleLeavingPlayers == null || possibleLeavingPlayers.isEmpty()) {
                log.debug("Skipping sub: No players on field for team {}", substitutionTeamId);
                return;
            }
            leavingPlayer = possibleLeavingPlayers.get(random.nextInt(possibleLeavingPlayers.size()));
        }

        String leavingPlayerPosition = leavingPlayer.getPlayerPosition();
        List<MatchPlayerDetails> bench = matchState.getBenchPlayers(leavingPlayer.getTeamId());

        // CRITICAL FIX: Bench might be null/empty
        if (bench == null) {
            log.debug("Skipping sub: Bench is null for team {}", leavingPlayer.getTeamId());
            return;
        }

        List<MatchPlayerDetails> possibleEnteringPlayers = bench.stream()
                .filter(matchPlayer -> matchPlayer.getPlayerPosition().equals(leavingPlayerPosition) && !matchPlayer.isReceivedRedCard())
                .toList();

        if (!possibleEnteringPlayers.isEmpty()) {
            MatchPlayerDetails randomEnteringPlayer = possibleEnteringPlayers.get(random.nextInt(possibleEnteringPlayers.size()));

            matchState.substitutePlayer(leavingPlayer, randomEnteringPlayer);
            randomEnteringPlayer = matchPlayerClient.updatePlayerStats(randomEnteringPlayer.getMatchId(), randomEnteringPlayer.getPlayerId(), "SUBSTITUTION_IN");
            leavingPlayer = matchPlayerClient.updatePlayerStats(leavingPlayer.getMatchId(), leavingPlayer.getPlayerId(), "SUBSTITUTION_OUT");

            log.info("SUBSTITUTION: Out {} -> In {}", leavingPlayer.getPlayerId(), randomEnteringPlayer.getPlayerId());

            createAndPublishEvent(matchState, randomEnteringPlayer.getTeamId(), randomEnteringPlayer.getPlayerId(), EnumLiveGameEventType.SUBSTITUTION_IN);
            createAndPublishEvent(matchState, leavingPlayer.getTeamId(), leavingPlayer.getPlayerId(), EnumLiveGameEventType.SUBSTITUTION_OUT);

        } else if (leavingPlayer.isReceivedRedCard()) {
            // Player must leave, no sub
            log.info("RED CARD EXIT: Player {} leaving field with no replacement", leavingPlayer.getPlayerId());
            matchState.removePlayerFromField(leavingPlayer);
            leavingPlayer = matchPlayerClient.updatePlayerStats(leavingPlayer.getMatchId(), leavingPlayer.getPlayerId(), "SUBSTITUTION_OUT");
            createAndPublishEvent(matchState, leavingPlayer.getTeamId(), leavingPlayer.getPlayerId(), EnumLiveGameEventType.SUBSTITUTION_OUT);
        } else {
            log.debug("No valid sub found for player {} (Position: {})", leavingPlayer.getPlayerId(), leavingPlayerPosition);
        }
    }

    private void handleYellowCardEvent(MatchState matchState) {
        Long homeTeamId = matchState.getHomeTeamId();
        Long awayTeamId = matchState.getAwayTeamId();
        Long randomTeamId = random.nextDouble() < 0.5 ? homeTeamId : awayTeamId;

        List<MatchPlayerDetails> onField = matchState.getOnFieldPlayers(randomTeamId);

        // CRITICAL FIX
        if (onField == null || onField.isEmpty()) {
            log.warn("Cannot give yellow card: No players on field for team {}", randomTeamId);
            return;
        }

        List<MatchPlayerDetails> possiblePlayers = onField.stream()
                .filter(matchPlayer -> !(matchPlayer.isReceivedYellowCard() && matchPlayer.getPlayerPosition().equals("SEEKER")))
                .toList();

        if (possiblePlayers.isEmpty()) {
            log.debug("No eligible players for Yellow Card on team {}", randomTeamId);
            return;
        }

        MatchPlayerDetails yellowCardPlayer = possiblePlayers.get(random.nextInt(possiblePlayers.size()));

        if (yellowCardPlayer.isReceivedYellowCard()) {
            log.info("Second Yellow -> Red for Player {}", yellowCardPlayer.getPlayerId());
            handleRedCardEvent(matchState, yellowCardPlayer);
        } else {
            log.info("Yellow Card for Player {}", yellowCardPlayer.getPlayerId());
            yellowCardPlayer = matchPlayerClient.updatePlayerStats(yellowCardPlayer.getMatchId(), yellowCardPlayer.getPlayerId(), "YELLOW_CARD");
            createAndPublishEvent(matchState, yellowCardPlayer.getTeamId(), yellowCardPlayer.getPlayerId(), EnumLiveGameEventType.YELLOW_CARD);
        }
    }

    private void handleRedCardEvent(MatchState matchState, MatchPlayerDetails redCardPlayer) {
        Long homeTeamId = matchState.getHomeTeamId();
        Long awayTeamId = matchState.getAwayTeamId();
        Long randomTeamId = random.nextDouble() < 0.5 ? homeTeamId : awayTeamId;

        if (redCardPlayer == null) {
            List<MatchPlayerDetails> onField = matchState.getOnFieldPlayers(randomTeamId);

            // CRITICAL FIX
            if (onField == null || onField.isEmpty()) {
                log.warn("Cannot give Red Card: No players on field for team {}", randomTeamId);
                return;
            }

            List<MatchPlayerDetails> possiblePlayers = onField.stream()
                    .filter(matchPlayer -> !matchPlayer.getPlayerPosition().equals("SEEKER"))
                    .toList();

            if (possiblePlayers.isEmpty()) {
                log.debug("No eligible players for Red Card on team {}", randomTeamId);
                return;
            }

            redCardPlayer = possiblePlayers.get(random.nextInt(possiblePlayers.size()));
        }

        log.info("Red Card for Player {}", redCardPlayer.getPlayerId());
        redCardPlayer = matchPlayerClient.updatePlayerStats(redCardPlayer.getMatchId(), redCardPlayer.getPlayerId(), "RED_CARD");
        createAndPublishEvent(matchState, redCardPlayer.getTeamId(), redCardPlayer.getPlayerId(), EnumLiveGameEventType.RED_CARD);
        handleSubstitutionEvents(matchState, redCardPlayer);
    }

    private void handleSnitchCatchEvent(MatchState matchState) {
        Long homeTeamId = matchState.getHomeTeamId();
        Long awayTeamId = matchState.getAwayTeamId();

        List<MatchPlayerDetails> allPlayers = new ArrayList<>();
        if (matchState.getOnFieldPlayers(homeTeamId) != null) allPlayers.addAll(matchState.getOnFieldPlayers(homeTeamId));
        if (matchState.getOnFieldPlayers(awayTeamId) != null) allPlayers.addAll(matchState.getOnFieldPlayers(awayTeamId));

        Optional<MatchPlayerDetails> mp = allPlayers.stream()
                .filter(matchPlayer -> matchPlayer.getPlayerPosition().equals("SEEKER"))
                .findAny();

        if (mp.isPresent()) {
            MatchPlayerDetails catcher = mp.get();
            log.info("SNITCH CAUGHT by Player {} (Team {})", catcher.getPlayerId(), catcher.getTeamId());
            matchState.updateScore(catcher.getTeamId(), 30);
            catcher.caughtSnitch();
            catcher = matchPlayerClient.updatePlayerStats(catcher.getMatchId(), catcher.getPlayerId(), "SNITCH_CATCH");

            matchClient.updateMatchSnitchCaught(matchState.getMatchId(),catcher.getTeamId());
            matchClient.updateMatchScore(matchState.getMatchId(), matchState.getMatchScore().get(homeTeamId), matchState.getMatchScore().get(awayTeamId));
            createAndPublishEvent(matchState, catcher.getTeamId(), catcher.getPlayerId(), EnumLiveGameEventType.SNITCH_CAUGHT);
        } else {
            log.warn("Snitch Catch triggered but no Seekers found on field!");
        }
    }

    private void handleMatchEndEvent(MatchState matchState) {
        log.info("Handling Match End Event for Match {}", matchState.getMatchId());
        matchState.endMatch();

        try {
            Long nextMatchId = matchTournamentClient.getNextTournamentMatchIdById(matchState.getMatchId());

            if (nextMatchId != null) {
                matchClient.setNextMatchTeamId(nextMatchId, matchState.getWinnerTeamId());

                List<MatchPlayerDetails> winnerTeamPlayers = new ArrayList<>();

                // Safety check
                if (matchState.getOnFieldPlayers(matchState.getWinnerTeamId()) != null) {
                    winnerTeamPlayers.addAll(matchState.getOnFieldPlayers(matchState.getWinnerTeamId()));
                }
                // Safety check
                if (matchState.getBenchPlayers(matchState.getWinnerTeamId()) != null) {
                    winnerTeamPlayers.addAll(matchState.getBenchPlayers(matchState.getWinnerTeamId()));
                }

                Collections.shuffle(winnerTeamPlayers);

                List<Long> startingIds = new ArrayList<>();

                startingIds.addAll(winnerTeamPlayers.stream().filter(p -> p.getPlayerPosition().equals("KEEPER")).limit(1).map(MatchPlayerDetails::getPlayerId).toList());
                startingIds.addAll(winnerTeamPlayers.stream().filter(p -> p.getPlayerPosition().equals("CHASER")).limit(3).map(MatchPlayerDetails::getPlayerId).toList());
                startingIds.addAll(winnerTeamPlayers.stream().filter(p -> p.getPlayerPosition().equals("BEATER")).limit(2).map(MatchPlayerDetails::getPlayerId).toList());
                startingIds.addAll(winnerTeamPlayers.stream().filter(p -> p.getPlayerPosition().equals("SEEKER")).limit(1).map(MatchPlayerDetails::getPlayerId).toList());

                List<Long> benchIds = new ArrayList<>(winnerTeamPlayers.stream()
                        .map(MatchPlayerDetails::getPlayerId)
                        .filter(id -> !startingIds.contains(id))
                        .toList());

                matchPlayerClient.setMatchRoster(nextMatchId, new RosterRequest(
                    matchState.getWinnerTeamId(),
                    startingIds,
                    benchIds
                    )
                );
            }
        } catch (Exception e) {
            log.warn("Tournament advancement skipped for Match {} (likely already processed): {}", matchState.getMatchId(), e.getMessage());
        }
        createAndPublishEvent(matchState, null, null, EnumLiveGameEventType.MATCH_END);
    }

    private void createAndPublishEvent(MatchState matchState, Long teamId, Long playerId, EnumLiveGameEventType eventType) {
        Long homeTeamId = matchState.getHomeTeamId();
        Long awayTeamId = matchState.getAwayTeamId();

        // Safe Score Retrieval
        int homeScore = matchState.getMatchScore().getOrDefault(homeTeamId, 0);
        int awayScore = matchState.getMatchScore().getOrDefault(awayTeamId, 0);

        EntityLiveGameEvent event = new EntityLiveGameEvent(
                matchState.getMatchId(),
                teamId,
                playerId,
                eventType,
                matchState.getMatchMinute(),
                homeScore,
                awayScore,
                Instant.now()
        );

        liveGameEventRepository.save(event);

        LiveGameEventDTO dto = event.toDTO(homeTeamId, awayTeamId);
        eventPublisher.publishEvent(matchState.getMatchId(), dto);
    }
}