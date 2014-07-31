/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import common.ServiceFailureException;
import common.ValidationException;
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
public class TeamMngrImpl implements TeamMngr {

    public static final Logger logger = Logger.getLogger(TeamMngrImpl.class.getName());
    private DataSource dataSource;

    @Override
    public void createTeam(Team team) {
        checkDataSource();
        validate(team);

        if (team.getId() != null) {
            throw new IllegalArgumentException("Team aleready exists" + team.toString());
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("INSERT INTO TEAMS (name, coach, status) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, team.getName());
            st.setString(2, team.getCoach());
            st.setString(3, team.getStatus().toString());

            int addedRows = st.executeUpdate();

            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert team " + team.toString());
            }

            ResultSet key = st.getGeneratedKeys();
            Long id = key.getLong(1);

            if (key.next()) {
                throw new IllegalArgumentException("Given ResultSet contains more rows");
            }
            team.setId(id);

        } catch (SQLException ex) {
            String msg = "error when inserting team" + team.toString();
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public void deteleTeam(Team team) {
        checkDataSource();
        validate(team);

        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("DELETE FROM TEAMS WHERE id = ?")) {
            st.setLong(1, team.getId());

            int deletedRows = st.executeUpdate();

            if (deletedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows deleted when trying to delete team " + team.toString());
            }

        } catch (SQLException ex) {
            String msg = "error when deleting team" + team.toString();
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public void updateTeam(Team team) {
        checkDataSource();
        validate(team);

        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("UPDATE TEAMS SET name = ?, coach = ?, status = ? WHERE id = ?")) {
            st.setString(1, team.getName());
            st.setString(2, team.getCoach());
            st.setString(3, team.getStatus().toString());
            st.setLong(4, team.getId());

            int updatedRows = st.executeUpdate();
            if (updatedRows != 1) {
                throw new ServiceFailureException("Internal Error: failed when trying to update team" + team.toString());
            }

        } catch (SQLException ex) {
            String msg = "error when updating team" + team.toString();
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public Team findTeamByID(Long id) {
        checkDataSource();
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT id,name,coach,status FROM TEAMS WHERE id = ?")) {
            st.setLong(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Team team = resultSetToTeam(rs);

                if (rs.next()) {
                    throw new ServiceFailureException("Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + team + " and " + resultSetToTeam(rs));
                }
                return team;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            String msg = "error when selecting team with id" + id;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }

    }

    @Override
    public List<Team> findAllTeams() {
        checkDataSource();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT id,name,coach,status FROM TEAMS")) {

            ResultSet rs = st.executeQuery();

            return resultSetToTeamList(rs);

        } catch (SQLException ex) {
            String msg = "error when selecting all teams";
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    static private void validate(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("team is null");
        }
        if (team.getName() == null || team.getName().isEmpty()) {
            throw new ValidationException("no name");
        }
        if (team.getCoach() == null || team.getCoach().isEmpty()) {
            throw new ValidationException("no coach");
        }
        if (team.getPlayers() == null || team.getPlayers().isEmpty()) {
            throw new ValidationException("no players");
        }
        if (team.getStatus() == null) {
            throw new ValidationException("no status");
        }
    }

    private Team resultSetToTeam(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setId(rs.getLong("id"));
        team.setName(rs.getString("name"));
        team.setCoach(rs.getString("coach"));
        switch (rs.getString("status")) {
            case "REFEREE":
                team.setStatus(TeamStatus.REFEREE);
                break;
            case "REFEREE_REQUIRED":
                team.setStatus(TeamStatus.REFEREE_REQUIRED);
                break;
            case "NOVICE":
                team.setStatus(TeamStatus.NOVICE);
                break;
            case "INDEPENDENT":
                team.setStatus(TeamStatus.INDEPENDENT);
                break;
        }
        return team;
    }

    private List<Team> resultSetToTeamList(ResultSet rs) throws SQLException {
        List<Team> result = new ArrayList<>();
        while (rs.next()) {
            Team team = resultSetToTeam(rs);
            result.add(team);
        }
        return result;
    }

    @Override
    public List<Team> findAllTeamsWithStatus(TeamStatus status) {
        checkDataSource();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT id,name,coach,status FROM TEAMS WHERE status = ?")) {
            st.setString(1, status.toString());
            
            ResultSet rs = st.executeQuery();

            return resultSetToTeamList(rs);

        } catch (SQLException ex) {
            String msg = "error when selecting all teams with status " + status.toString();
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }
}
