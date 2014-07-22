/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package backend;

import java.sql.Date;
import java.util.Map;

/**
 *
 * @author LukeMcNemee
 */
public class Match {
    private Long id;
    private Team homeTeam;
    private Team awayTeam;
    private java.sql.Date datePlayed;
    private MatchResult result;
    private Map<Player, Integer> goalsScored;
    private Map<Player, Integer> successfulPenalty;
    private Map<Player, Integer> unsuccessfulPenalty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Date getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(Date datePlayed) {
        this.datePlayed = datePlayed;
    }

    public MatchResult getResult() {
        return result;
    }
    
    public Team getWinner(){
        //TODO
        return null;
    }
    
    public int awayScore(){
        //TODO
        return 0;
    }
    
    public int homeScore(){
        //TODO;
        return 0;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public Map<Player, Integer> getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(Map<Player, Integer> goalsScored) {
        this.goalsScored = goalsScored;
    }
    
    public void addGoalScored(Player player, Integer num){
        if(goalsScored.containsKey(player)){
            goalsScored.put(player, num + goalsScored.get(player));
        } else {
            goalsScored.put(player, num);
        }
    }

    public Map<Player, Integer> getSuccessfulPenalty() {
        return successfulPenalty;
    }

    public void setSuccessfulPenalty(Map<Player, Integer> successfulPenalty) {
        this.successfulPenalty = successfulPenalty;
    }

    public void addSuccessfulPenalty(Player player, Integer num){
        if(successfulPenalty.containsKey(player)){
            successfulPenalty.put(player, num + successfulPenalty.get(player));
        } else {
            successfulPenalty.put(player, num);
        }
    }

    public Map<Player, Integer> getUnsuccessfulPenalty() {
        return unsuccessfulPenalty;
    }
    
    public void addUnsuccessfulPenalty(Player player, Integer num){
        if(unsuccessfulPenalty.containsKey(player)){
            unsuccessfulPenalty.put(player, num + unsuccessfulPenalty.get(player));
        } else {
            unsuccessfulPenalty.put(player, num);
        }
    }

    public void setUnsuccessfulPenalty(Map<Player, Integer> unsuccessfulPenalty) {
        this.unsuccessfulPenalty = unsuccessfulPenalty;
    }
}
