package com.quadballholic.backend.livegameevents.service.state;

import com.quadballholic.backend.livegameevents.dto.MatchPlayerDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class MatchState {
    private Long matchId;
    private int matchMinute = 0;

    private Map<Long,Integer> matchScore = new HashMap<>();

    private Long homeTeamId;
    private Long awayTeamId;
    private Long winnerTeamId;
    private Long snitchCatcherTeamId;

    private Map<Long, Map<String,List<MatchPlayerDetails>>> matchPlayers = new HashMap<>();

    private boolean isGameRunning = false;
    private boolean isSnitchReleased = false;

    public MatchState(Long matchId, Long homeTeamId, Long awayTeamId) {
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.matchPlayers.put(homeTeamId, new HashMap<>());
        this.matchPlayers.put(awayTeamId, new HashMap<>());
        this.matchPlayers.get(homeTeamId).put("playing",new ArrayList<>());
        this.matchPlayers.get(homeTeamId).put("bench",new ArrayList<>());
        this.matchPlayers.get(awayTeamId).put("playing",new ArrayList<>());
        this.matchPlayers.get(awayTeamId).put("bench",new ArrayList<>());
        this.matchScore.put(homeTeamId,0);
        this.matchScore.put(awayTeamId,0);
    }

    public void addPlayerToRoster(MatchPlayerDetails matchPlayer){
        matchPlayers.get(matchPlayer.getTeamId())
                .get(matchPlayer.isStarter()?"playing":"bench")
                .add(matchPlayer);
    }

    public void substitutePlayer(MatchPlayerDetails leavingPlayer, MatchPlayerDetails enteringPlayer){
        Long teamId = leavingPlayer.getTeamId();

        Map<String, List<MatchPlayerDetails>> teamLists = matchPlayers.get(teamId);
        teamLists.get("playing").add(enteringPlayer);
        teamLists.get("playing").remove(leavingPlayer);
        teamLists.get("bench").add(leavingPlayer);
        teamLists.get("bench").remove(enteringPlayer);

    }

    public void removePlayerFromField(MatchPlayerDetails leavingPlayer){
        Long teamId = leavingPlayer.getTeamId();
        Map<String, List<MatchPlayerDetails>> teamLists = matchPlayers.get(teamId);
        teamLists.get("playing").remove(leavingPlayer);
        teamLists.get("bench").add(leavingPlayer);
    }

    public void increaseMatchMinute(){
        this.matchMinute++;
        System.out.println("Game minute: " + matchMinute);
    }

    public List<MatchPlayerDetails> getOnFieldPlayers(Long teamId){
        return this.matchPlayers.get(teamId).get("playing");
    }

    public List<MatchPlayerDetails> getBenchPlayers(Long teamId){
        return this.matchPlayers.get(teamId).get("bench");
    }

    public void updateScore(Long teamId, int score){
        matchScore.replace(teamId,matchScore.get(teamId) + score);
        if(score == 30) snitchCatcherTeamId = teamId;
    }

    public void endMatch(){
        isGameRunning = false;
        winnerTeamId = matchScore.get(homeTeamId) > matchScore.get(awayTeamId)?
                homeTeamId:Objects.equals(matchScore.get(homeTeamId), matchScore.get(awayTeamId))?
                snitchCatcherTeamId:awayTeamId;
    }
}
