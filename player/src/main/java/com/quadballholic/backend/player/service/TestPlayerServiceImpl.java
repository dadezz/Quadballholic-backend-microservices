package com.quadballholic.backend.player.service;

import com.quadballholic.backend.player.client.TeamClient;
import com.quadballholic.backend.player.dto.TeamDTO;
import com.quadballholic.backend.player.enums.EnumPlayerPosition;
import com.quadballholic.backend.player.model.EntityPlayer;
import com.quadballholic.backend.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestPlayerServiceImpl implements TestPlayerService {

    private final PlayerRepository playerRepository;
    private final TeamClient teamClient;

    @Override
    public List<EntityPlayer> init() {
        if (playerRepository.count() > 0) {
            playerRepository.deleteAll();
            log.info("Players already exist. Deleting all players.");

        }
        log.info("Initializing Test Players... Fetching Teams from Team Service.");
        List<TeamDTO> teams;
        Map<String, Long> teamMap;
        try {
            System.out.println("Fetching Teams from Team Service.");
            teams = teamClient.getAllTeams();
            System.out.println("Fetched Teams from Team Service.");
            for (TeamDTO team : teams) {
                System.out.println(team.getId() + ": " +team.getName());
            }

            teamMap = teams.stream()
                    .collect(Collectors.toMap(TeamDTO::getName, TeamDTO::getId));
        } catch (Exception e) {
            log.error("Could not fetch teams from Team Service. Is it running?", e);// TODO: Delete this after team service is implemented
            teamMap = Map.of(
                    "Italy", 1L,
                    "Turkey", 2L,
                    "USA", 3L,
                    "UK", 4L,
                    "France", 5L,
                    "Germany", 6L,
                    "Spain", 7L,
                    "Belgium", 8L
            );
        }

        List<EntityPlayer> allPlayers = new ArrayList<>();
        for (String teamName : teamMap.keySet()) {
            long teamId = teamMap.get(teamName);
            if (teamName.contains("Italy")) allPlayers.addAll(getItalyPlayers(teamId));
            if (teamName.contains("Turkey")) allPlayers.addAll(getTurkeyPlayers(teamId));
            if (teamName.contains("USA")) allPlayers.addAll(getUSAPlayers(teamId));
            if (teamName.contains("UK")) allPlayers.addAll(getUKPlayers(teamId));
            if (teamName.contains("France")) allPlayers.addAll(getFrancePlayers(teamId));
            if (teamName.contains("Germany")) allPlayers.addAll(getGermanyPlayers(teamId));
            if (teamName.contains("Spain")) allPlayers.addAll(getSpainPlayers(teamId));
            if (teamName.contains("Belgium")) allPlayers.addAll(getBelgiumPlayers(teamId));
        }

        log.info("Saving {} test players to database.", allPlayers.size());
        return playerRepository.saveAll(allPlayers);
    }

    private List<EntityPlayer> getItalyPlayers(Long teamId) {
        return List.of(
                new EntityPlayer("Stefano Turrini", EnumPlayerPosition.KEEPER, teamId, 5),
                new EntityPlayer("Giovanni Pasqualotto", EnumPlayerPosition.KEEPER, teamId, 14),
                new EntityPlayer("Elena Pozzebon", EnumPlayerPosition.KEEPER, teamId, 1),
                new EntityPlayer("Giorgia Busetto", EnumPlayerPosition.CHASER, teamId, 6),
                new EntityPlayer("Davide Zambon", EnumPlayerPosition.CHASER, teamId, 9),
                new EntityPlayer("Rosa Gensale", EnumPlayerPosition.CHASER, teamId, 11),
                new EntityPlayer("Elisa Scorrano", EnumPlayerPosition.CHASER, teamId, 12),
                new EntityPlayer("Marco Bonetti", EnumPlayerPosition.CHASER, teamId, 22),
                new EntityPlayer("Michele Clabassi", EnumPlayerPosition.CHASER, teamId, 7),
                new EntityPlayer("Alessia Bruttini", EnumPlayerPosition.BEATER, teamId, 15),
                new EntityPlayer("Mirko Barbieri", EnumPlayerPosition.BEATER, teamId, 18),
                new EntityPlayer("Irene Velardi", EnumPlayerPosition.BEATER, teamId, 33),
                new EntityPlayer("Dario Di Giosaffatte", EnumPlayerPosition.BEATER, teamId, 44),
                new EntityPlayer("Larisa Dinulescu", EnumPlayerPosition.SEEKER, teamId, 97),
                new EntityPlayer("Lorenzo Guerrini", EnumPlayerPosition.SEEKER, teamId, 99)

        );

    }

    private List<EntityPlayer> getTurkeyPlayers(Long teamId) {
        return List.of(
                new EntityPlayer("Fatih Aykurt", EnumPlayerPosition.KEEPER, teamId, 1),
                new EntityPlayer("Alper Urgun", EnumPlayerPosition.KEEPER, teamId, 13),
                new EntityPlayer("Mehmet Akkurt", EnumPlayerPosition.KEEPER, teamId, 23),
                new EntityPlayer("Can Kaytaz", EnumPlayerPosition.CHASER, teamId, 7),
                new EntityPlayer("Ahmet Can Karakadılar", EnumPlayerPosition.CHASER, teamId, 10),
                new EntityPlayer("Sıla Yüksel", EnumPlayerPosition.CHASER, teamId, 8),
                new EntityPlayer("Mert Bekar", EnumPlayerPosition.CHASER, teamId, 4),
                new EntityPlayer("Ecem Satıcı", EnumPlayerPosition.CHASER, teamId, 12),
                new EntityPlayer("Erdem Er", EnumPlayerPosition.CHASER, teamId, 20),
                new EntityPlayer("Ruşen Sarı", EnumPlayerPosition.BEATER, teamId, 5),
                new EntityPlayer("Ekin Berkyürek", EnumPlayerPosition.BEATER, teamId, 18),
                new EntityPlayer("Umut Yeşilkaya", EnumPlayerPosition.BEATER, teamId, 99),
                new EntityPlayer("Zeynep Karataş", EnumPlayerPosition.BEATER, teamId, 45),
                new EntityPlayer("Deniz Bayan", EnumPlayerPosition.SEEKER, teamId, 97),
                new EntityPlayer("Can Kaya", EnumPlayerPosition.SEEKER, teamId, 88)
        );
    }

    private List<EntityPlayer> getBelgiumPlayers(Long teamId) {
        return List.of(
                new EntityPlayer("Seppe De Wit", EnumPlayerPosition.KEEPER, teamId, 1),
                new EntityPlayer("Louis Lermytte", EnumPlayerPosition.KEEPER, teamId, 12),
                new EntityPlayer("Tim van Moriël", EnumPlayerPosition.KEEPER, teamId, 13),
                new EntityPlayer("Elisabeth Reyniers", EnumPlayerPosition.CHASER, teamId, 7),
                new EntityPlayer("Nathan Dix", EnumPlayerPosition.CHASER, teamId, 10),
                new EntityPlayer("Laurens Lermytte", EnumPlayerPosition.CHASER, teamId, 9),
                new EntityPlayer("Jorim Ver Eecke", EnumPlayerPosition.CHASER, teamId, 14),
                new EntityPlayer("Hanne De Malsche", EnumPlayerPosition.CHASER, teamId, 11),
                new EntityPlayer("Pauline Vaneetvelde", EnumPlayerPosition.CHASER, teamId, 8),
                new EntityPlayer("Wilhelmina O'Reilly", EnumPlayerPosition.BEATER, teamId, 4),
                new EntityPlayer("Robin De Dela", EnumPlayerPosition.BEATER, teamId, 5),
                new EntityPlayer("Emile Aerts", EnumPlayerPosition.BEATER, teamId, 2),
                new EntityPlayer("Anaïs Van de Velde", EnumPlayerPosition.BEATER, teamId, 3),
                new EntityPlayer("Bram Algoet", EnumPlayerPosition.SEEKER, teamId, 6),
                new EntityPlayer("Rik De Boeck", EnumPlayerPosition.SEEKER, teamId, 19)
        );
    }

    private List<EntityPlayer> getSpainPlayers(Long teamId){
        return List.of(
                new EntityPlayer("Artur Martín", EnumPlayerPosition.KEEPER, teamId, 1),
                new EntityPlayer("Pedro González", EnumPlayerPosition.KEEPER, teamId, 13),
                new EntityPlayer("Diego Ávila", EnumPlayerPosition.KEEPER, teamId, 23),
                new EntityPlayer("Miguel Ángel Vázquez", EnumPlayerPosition.CHASER, teamId, 6),
                new EntityPlayer("Pau Pérez", EnumPlayerPosition.CHASER, teamId, 8),
                new EntityPlayer("Blanca Brenes", EnumPlayerPosition.CHASER, teamId, 5),
                new EntityPlayer("Jorge Vico", EnumPlayerPosition.CHASER, teamId, 21),
                new EntityPlayer("Marina Salvà", EnumPlayerPosition.CHASER, teamId, 10),
                new EntityPlayer("Héctor Cabrera", EnumPlayerPosition.CHASER, teamId, 9),
                new EntityPlayer("Ander Carbón", EnumPlayerPosition.BEATER, teamId, 4),
                new EntityPlayer("Paula Marmolejo", EnumPlayerPosition.BEATER, teamId, 3),
                new EntityPlayer("Sergio Siqueira", EnumPlayerPosition.BEATER, teamId, 2),
                new EntityPlayer("Maxime Lemesle", EnumPlayerPosition.BEATER, teamId, 18),
                new EntityPlayer("Daniel Williams", EnumPlayerPosition.SEEKER, teamId, 7),
                new EntityPlayer("Antonio Rodriguez", EnumPlayerPosition.SEEKER, teamId, 11)
        );
    }

    private List<EntityPlayer> getGermanyPlayers(Long teamId){
        return List.of(
                new EntityPlayer("Christian Zimpel", EnumPlayerPosition.KEEPER, teamId, 1),
                new EntityPlayer("Leon Büdel", EnumPlayerPosition.KEEPER, teamId, 22),
                new EntityPlayer("Patrick Guckgan", EnumPlayerPosition.KEEPER, teamId, 12),
                new EntityPlayer("Lisa Tietze", EnumPlayerPosition.CHASER, teamId, 25),
                new EntityPlayer("Saskia Vowinckel", EnumPlayerPosition.CHASER, teamId, 11),
                new EntityPlayer("Niklas Müller", EnumPlayerPosition.CHASER, teamId, 8),
                new EntityPlayer("Jonas Wöller", EnumPlayerPosition.CHASER, teamId, 10),
                new EntityPlayer("Nina Heise", EnumPlayerPosition.CHASER, teamId, 19),
                new EntityPlayer("Steffen Wirsching", EnumPlayerPosition.CHASER, teamId, 13),
                new EntityPlayer("Leander Troll", EnumPlayerPosition.BEATER, teamId, 5),
                new EntityPlayer("Ida Meyenberg", EnumPlayerPosition.BEATER, teamId, 17),
                new EntityPlayer("Christian Häuser", EnumPlayerPosition.BEATER, teamId, 6),
                new EntityPlayer("Lara Lebedinski", EnumPlayerPosition.BEATER, teamId, 2),
                new EntityPlayer("Baldur Brückner", EnumPlayerPosition.SEEKER, teamId, 20),
                new EntityPlayer("Marius Pitsch", EnumPlayerPosition.SEEKER, teamId, 18)
        );
    }

    private List<EntityPlayer> getFrancePlayers(Long teamId){
        return List.of(
                new EntityPlayer("Florian Allenet", EnumPlayerPosition.KEEPER, teamId, 1),
                new EntityPlayer("Tudor Wines", EnumPlayerPosition.KEEPER, teamId, 16),
                new EntityPlayer("Paul Bonnet", EnumPlayerPosition.KEEPER, teamId, 21),
                new EntityPlayer("Albert Bregeault", EnumPlayerPosition.CHASER, teamId, 7),
                new EntityPlayer("Célia Josse", EnumPlayerPosition.CHASER, teamId, 10),
                new EntityPlayer("Charles Eliott", EnumPlayerPosition.CHASER, teamId, 9),
                new EntityPlayer("Bastien Oltra", EnumPlayerPosition.CHASER, teamId, 6),
                new EntityPlayer("Olympe Portes", EnumPlayerPosition.CHASER, teamId, 13),
                new EntityPlayer("Valentin Foret", EnumPlayerPosition.CHASER, teamId, 8),
                new EntityPlayer("Etienne Poyer", EnumPlayerPosition.BEATER, teamId, 4),
                new EntityPlayer("Melina Gusmini", EnumPlayerPosition.BEATER, teamId, 5),
                new EntityPlayer("Abel Wuest", EnumPlayerPosition.BEATER, teamId, 2),
                new EntityPlayer("Denis Jourdan", EnumPlayerPosition.BEATER, teamId, 3),
                new EntityPlayer("Cédric Chillan", EnumPlayerPosition.SEEKER, teamId, 11),
                new EntityPlayer("Nicolas Siffrin", EnumPlayerPosition.SEEKER, teamId, 14)
        );
    }

    private List<EntityPlayer> getUKPlayers(Long teamId){
        return List.of(
                new EntityPlayer("Bill Orridge", EnumPlayerPosition.KEEPER, teamId, 1),
                new EntityPlayer("Seb Waters", EnumPlayerPosition.KEEPER, teamId, 12),
                new EntityPlayer("Tommy Kirkland", EnumPlayerPosition.KEEPER, teamId, 88),
                new EntityPlayer("Mark Hurrell", EnumPlayerPosition.CHASER, teamId, 7),
                new EntityPlayer("Bex McLaughlin", EnumPlayerPosition.CHASER, teamId, 8),
                new EntityPlayer("Ben Morton", EnumPlayerPosition.CHASER, teamId, 14),
                new EntityPlayer("Edott Spadaccini", EnumPlayerPosition.CHASER, teamId, 20),
                new EntityPlayer("Alice Walker", EnumPlayerPosition.CHASER, teamId, 19),
                new EntityPlayer("Carina Walsh", EnumPlayerPosition.CHASER, teamId, 5),
                new EntityPlayer("Kerry Aziz", EnumPlayerPosition.BEATER, teamId, 2),
                new EntityPlayer("Jan Mikolajczak", EnumPlayerPosition.BEATER, teamId, 3),
                new EntityPlayer("Luke Twist", EnumPlayerPosition.BEATER, teamId, 4),
                new EntityPlayer("Jacopo Sartori", EnumPlayerPosition.BEATER, teamId, 6),
                new EntityPlayer("Callum Lake", EnumPlayerPosition.SEEKER, teamId, 99),
                new EntityPlayer("Andy Hull", EnumPlayerPosition.SEEKER, teamId, 17)
        );
    }

    private List<EntityPlayer> getUSAPlayers(Long teamId){
        return List.of(
                new EntityPlayer("Austin Pile", EnumPlayerPosition.KEEPER, teamId, 7),
                new EntityPlayer("Tyler Trudeau", EnumPlayerPosition.KEEPER, teamId, 0),
                new EntityPlayer("Kody Marshall", EnumPlayerPosition.KEEPER, teamId, 13),
                new EntityPlayer("Carsen Knott", EnumPlayerPosition.CHASER, teamId, 10),
                new EntityPlayer("Lulu Xu", EnumPlayerPosition.CHASER, teamId, 8),
                new EntityPlayer("Rachel Heald", EnumPlayerPosition.CHASER, teamId, 14),
                new EntityPlayer("Miguel Esparza", EnumPlayerPosition.CHASER, teamId, 20),
                new EntityPlayer("Lindsay Marella", EnumPlayerPosition.CHASER, teamId, 19),
                new EntityPlayer("Kennedy Murphy", EnumPlayerPosition.CHASER, teamId, 5),
                new EntityPlayer("Max Havlin", EnumPlayerPosition.BEATER, teamId, 2),
                new EntityPlayer("Jackson Johnson", EnumPlayerPosition.BEATER, teamId, 3),
                new EntityPlayer("Peter Lawrence", EnumPlayerPosition.BEATER, teamId, 4),
                new EntityPlayer("Kyzer Polzin", EnumPlayerPosition.BEATER, teamId, 6),
                new EntityPlayer("Ryan Davis", EnumPlayerPosition.SEEKER, teamId, 99),
                new EntityPlayer("Josh Johnson", EnumPlayerPosition.SEEKER, teamId, 17)
        );
    }
}
