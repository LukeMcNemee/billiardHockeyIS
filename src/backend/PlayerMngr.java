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
public interface PlayerMngr {
    
    public List<Player> getPlayersByTeam(Team team);
    
    public Player getPlayerById(Long id);
    
    public List<Player> getPlayersByTeamHome(Team team);
    
    public List<Player> getPlayersByTeamAway(Team team);
    
    public Player createPlayer(Player player);
    
    public Player updatePlayer(Player player);
    
    public Player detetePlayer(Player player);
    
    public List<Player> filterPlayersByPosition(List<Player> players, PlayerPosition position);
}
