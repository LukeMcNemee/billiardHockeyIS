/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.util.Objects;
import java.util.Set;

/**
 *
 * @author LukeMcNemee
 */
public class Team {

    private Long id;
    private String name;
    private String coach;
    private Set<Player> players;
    private Set<Match> playedMatches;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Team other = (Team) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Match> getPlayedMatches() {
        return playedMatches;
    }

    public void setPlayedMatches(Set<Match> playedMatches) {
        this.playedMatches = playedMatches;
    }
    
    public int getNumMatchesPlayed(){
        //TODO
        return 0;
    }
    
    public Score getPlayedMatchesScore(){
        //TODO
        return null;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void playMatch(Match match) {
        playedMatches.add(match);
    }

    public double getCoefitient() {
        //TODO
        return 0;
    }

    public double getPenaltyCoefitient() {
        //TODO
        return 0;
    }

    public Score getScore() {
        //TODO
        return null;
    }

    public Score getOwnGoalsScore() {
        //TODO
        return null;
    }

    public Score getTechnicalGoalsScore() {
        //TODO
        return null;
    }

    public Score agetAdvantageGoalsScore() {
        //TODO
        return null;
    }

    public Score getContumationGoalsScore() {
        //TODO
        return null;
    }

    @Override
    public String toString() {
        return "Team{" + "id=" + id + ", name=" + name + ", coach=" + coach + ", players=" + players + ", playedMatches=" + playedMatches + '}';
    }

}
