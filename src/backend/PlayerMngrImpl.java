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
public class PlayerMngrImpl implements PlayerMngr {

    public static final Logger logger = Logger.getLogger(PlayerMngrImpl.class.getName());
    private DataSource dataSource;

    @Override
    public List<Player> getPlayersByTeam(Team team) {
        checkDataSource();
        if (team == null) {
            throw new IllegalArgumentException("id is null");
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT id,name,surname,number,teamid,home,away,position FROM PLAYERS WHERE teamid = ?")) {
            st.setLong(1, team.getId());

            ResultSet rs = st.executeQuery();
            
            
            return resultSetToPlayerList(rs);
        } catch (SQLException ex) {
            String msg = "error when selecting players with team" + team;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public Player getPlayerById(Long id) {
        checkDataSource();
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT idname,surname,number,teamid,home,away,position FROM PLAYERS WHERE id = ?")) {
            st.setLong(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Player player = resultSetToPlayer(rs);

                if (rs.next()) {
                    throw new ServiceFailureException("Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + player + " and " + resultSetToPlayer(rs));
                }
                return player;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            String msg = "error when selecting player with id" + id;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public List<Player> getPlayersByTeamHome(Team team) {
        checkDataSource();
        if (team == null) {
            throw new IllegalArgumentException("id is null");
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT id,name,surname,number,teamid,home,away,position FROM PLAYERS WHERE teamid = ? AND home = 1")) {
            st.setLong(1, team.getId());

            ResultSet rs = st.executeQuery();
            
            
            return resultSetToPlayerList(rs);
        } catch (SQLException ex) {
            String msg = "error when selecting players with team home" + team;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public List<Player> getPlayersByTeamAway(Team team) {
checkDataSource();
        if (team == null) {
            throw new IllegalArgumentException("id is null");
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT id,name,surname,number,teamid,home,away,position FROM PLAYERS WHERE teamid = ? AND away = 1")) {
            st.setLong(1, team.getId());

            ResultSet rs = st.executeQuery();
            
            
            return resultSetToPlayerList(rs);
        } catch (SQLException ex) {
            String msg = "error when selecting players with team away" + team;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }  
    }

    @Override
    public void createPlayer(Player player) {
        checkDataSource();
        validate(player);

        if (player.getId() != null) {
            throw new IllegalArgumentException("Player already exists " + player);
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("INSERT INTO PLAYERS (name, surname, number, teamid, home, away, position) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, player.getName());
            st.setString(2, player.getSurname());
            st.setInt(3, player.getPlayerNumber());
            st.setLong(4, player.getTeamID());
            st.setInt(5, player.isHome()?1:0);
            st.setInt(6, player.isAway()?1:0);
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
        checkDataSource();
        validate(player);

        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("UPDATE PLAYERS SET name = ?, surname = ?, number = ?, teamid = ?, home = ?, away = ?, position = ? WHERE id = ?")) {

            st.setString(1, player.getName());
            st.setString(2, player.getSurname());
            st.setInt(3, player.getPlayerNumber());
            st.setLong(4, player.getTeamID());
            st.setBoolean(5, player.isHome());
            st.setBoolean(6, player.isAway());
            st.setString(7, player.getPlayerPosition().toString());
            st.setLong(8, player.getId());

            int updatedRows = st.executeUpdate();
            if (updatedRows != 1) {
                throw new ServiceFailureException("Internal Error: failed when trying to update player " + player);
            }

        } catch (SQLException ex) {
            String msg = "error when updating team" + player;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public void detetePlayer(Player player
    ) {
        checkDataSource();
        validate(player);

        try (Connection conn = dataSource.getConnection(); PreparedStatement st = conn.prepareStatement("DELETE FROM PLAYERS WHERE id = ?")) {
            st.setLong(1, player.getId());

            int deletedRows = st.executeUpdate();

            if (deletedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows deleted when trying to delete player " + player);
            }

        } catch (SQLException ex) {
            String msg = "error when deleting player" + player;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }

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

    private Player resultSetToPlayer(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setId(rs.getLong("id"));
        player.setName(rs.getString("name"));
        player.setSurname(rs.getString("surname"));
        player.setPlayerNumber(rs.getByte("number"));
        player.setTeamID(rs.getLong("teamid"));
        player.setHome(rs.getInt("home") != 0);
        player.setAway(rs.getInt("away") != 0);
        switch (rs.getString("position")){
            case "GOALTENDER" :
                player.setPlayerPosition(PlayerPosition.GOALTENDER);
                break;
            case "DEFENCEMAN" :
                player.setPlayerPosition(PlayerPosition.DEFENCEMAN);
                break;
            case "FORWARD" :
                player.setPlayerPosition(PlayerPosition.FORWARD);
                break;
            case "ALTERNATE" :
                player.setPlayerPosition(PlayerPosition.ALTERNATE);
                break;            
        }
        
        return player;
    }

    private List<Player> resultSetToPlayerList(ResultSet rs) throws SQLException {
         List<Player> result = new ArrayList<>();
        while (rs.next()) {
            Player player = resultSetToPlayer(rs);
            result.add(player);
        }        
        return result;        
    }

}
