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
    
    public void createPlayer(Player player);
    
    public void updatePlayer(Player player);
    
    public void detetePlayer(Player player);
    
    public List<Player> filterPlayersByPosition(List<Player> players, PlayerPosition position);
}
