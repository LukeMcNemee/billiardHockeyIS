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
    
    public Match createMatch(Match match);
    
    public Match updateMatch(Match match);
    
    public Match deleteMatch(Match match);
    
    public Match findMatchById(Long id);
    
    public List<Match> findMatchesByTeam(Team team);
    
    public List<Match> findMatchesByTeams(Team team1, Team team2);
    
    
}
