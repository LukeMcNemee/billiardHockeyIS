/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package backend;

import java.util.List;

/**
 *
 * @author LukeMcNemee
 */
public interface MatchMngr {
    
    public void createMatch(Match match);
    
    public void updateMatch(Match match);
    
    public void deleteMatch(Match match);
    
    public Match findMatchById(Long id);
    
    public List<Match> findMatchesByTeam(Team team);
    
    public List<Match> findMatchesByTeams(Team team1, Team team2);
    
    
}
