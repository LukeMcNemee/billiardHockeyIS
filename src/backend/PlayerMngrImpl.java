/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package backend;

import common.ServiceFailureException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author LukeMcNemee
 */
public class PlayerMngrImpl implements PlayerMngr{

    public static final Logger logger = Logger.getLogger(PlayerMngrImpl.class.getName());
    private DataSource dataSource;
    
    @Override
    public List<Player> getPlayersByTeam(Team team) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player getPlayerById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Player> getPlayersByTeamHome(Team team) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Player> getPlayersByTeamAway(Team team) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createPlayer(Player player) {
        checkDataSource();
        validate(player);
        
        if(player.getId() != null){
            throw new IllegalArgumentException("Player already exists " + player);
        }
          try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("INSERT INTO PLAYERS (name, surname, number, teamid, home, away, position) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
              st.setString(1, player.getName());
              st.setString(2, player.getSurname());
              st.setInt(3, player.getPlayerNumber());
              st.setLong(4, player.getId());
              st.setBoolean(5, player.isHome());
              st.setBoolean(6, player.isAway());
              st.setString(7, player.getPlayerPosition().toString());
              
              int addedRows = st.executeUpdate();

            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert player " + player);
            }

            ResultSet key = st.getGeneratedKeys();
            Long id = key.getLong(1);

            if (key.next()) {
                throw new IllegalArgumentException("Given ResultSet contains more rows");
            }
            player.setId(id);
            
        } catch (SQLException ex) {
            String msg = "error when inserting team" + player;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public void updatePlayer(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void detetePlayer(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Player> filterPlayersByPosition(List<Player> players, PlayerPosition position) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    private void validate(Player player) {

    }
    
    private Player resultSetToPlayer(ResultSet rs) throws SQLException{
        return null;
    }
    
    private List<Player> resultSetToPlayerList(ResultSet rs) throws SQLException{
       
        return null;
    }
    
}
