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
public interface TeamMngr {
    
    public Team createTeam(Team team);
    
    public Team deteleTeam(Team team);
    
    public Team updateTeam(Team team);
    
    public Team findTeamByID(Long id);
    
    public List<Team> findAllTeams();
}
