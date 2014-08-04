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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author LukeMcNemee
 */
public class MatchMngrImpl implements MatchMngr {

    private DataSource dataSource;
    private PlayerMngrImpl playerMngr;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        playerMngr.setDataSource(dataSource);
    }

    @Override
    public void createMatch(Match match) {
        validate(match);
        checkDataSource();

        if (match.getId() != null) {
            throw new IllegalArgumentException("match already exists");
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("INSERT INTO MATCHES (hometeamid, awayteamid, date, homeowngoals, awayowngoals"
                        + ", homeadvantagegoals, awayadvantagegoals, homecontumationgoals, awaycontumationgoals, hometechnicalgoals, awaytechnicalgoals)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, match.getHomeTeam().getId());
            st.setLong(2, match.getAwayTeam().getId());
            st.setDate(3, match.getDatePlayed());
            st.setInt(4, match.getOwnGoals().getHomeScore());
            st.setInt(5, match.getOwnGoals().getAwayScore());
            st.setInt(6, match.getAdvantageGoals().getHomeScore());
            st.setInt(7, match.getAdvantageGoals().getAwayScore());
            st.setInt(8, match.getContumationGoals().getHomeScore());
            st.setInt(9, match.getContumationGoals().getAwayScore());
            st.setInt(10, match.getTechnicalGoals().getHomeScore());
            st.setInt(11, match.getTechnicalGoals().getAwayScore());

            int addedRows = st.executeUpdate();

            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert match " + match);
            }

            ResultSet key = st.getGeneratedKeys();
            Long id = key.getLong(1);
            match.setId(id);

            for (Player player : match.getGoalsScored().keySet()) {

                try (PreparedStatement st2 = conn.prepareStatement("INSERT INTO GOALS (playerid, matchid, amount) VALUES (?,?,?)")) {
                    st2.setLong(1, player.getId());
                    st2.setLong(2, id);
                    st2.setInt(3, match.getGoalsScored().get(player));

                    int addedRows2 = st2.executeUpdate();

                    if (addedRows2 != 1) {
                        throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert match " + match);
                    }

                } catch (SQLException ex) {
                    String msg = "error when inserting stage 2 match" + match;
                    Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
                    throw new ServiceFailureException(msg);
                }
            }

        } catch (SQLException ex) {
            String msg = "error when inserting stage 1 match" + match;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }

    }

    @Override
    public void updateMatch(Match match) {
        validate(match);
        checkDataSource();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("UPDATE MATCHES SET hometeamid = ?, awayteamid = ?, date = ?, homeowngoals = ?, awayowngoals = ?"
                        + ", homeadvantagegoals = ?, awayadvantagegoals = ?, homecontumationgoals = ?, awaycontumationgoals = ?, hometechnicalgoals = ?, awaytechnicalgoals = ?"
                        + " WHERE id = ?")) {
            st.setLong(1, match.getHomeTeam().getId());
            st.setLong(2, match.getAwayTeam().getId());
            st.setDate(3, match.getDatePlayed());
            st.setInt(4, match.getOwnGoals().getHomeScore());
            st.setInt(5, match.getOwnGoals().getAwayScore());
            st.setInt(6, match.getAdvantageGoals().getHomeScore());
            st.setInt(7, match.getAdvantageGoals().getAwayScore());
            st.setInt(8, match.getContumationGoals().getHomeScore());
            st.setInt(9, match.getContumationGoals().getAwayScore());
            st.setInt(10, match.getTechnicalGoals().getHomeScore());
            st.setInt(11, match.getTechnicalGoals().getAwayScore());
            st.setLong(12, match.getId());

            int addedRows = st.executeUpdate();

            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: failed when trying to update match " + match);
            }
            try (PreparedStatement st2 = conn.prepareStatement("DELETE FROM GOALS WHERE matchid = ?")) {
                st2.setLong(1, match.getId());

                st2.executeUpdate();

            } catch (SQLException ex) {
                String msg = "error when updating stage 2 match" + match;
                Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
                throw new ServiceFailureException(msg);
            }

            for (Player player : match.getGoalsScored().keySet()) {

                try (PreparedStatement st2 = conn.prepareStatement("INSERT INTO GOALS (playerid, matchid, amount) VALUES (?,?,?)")) {
                    st2.setLong(1, player.getId());
                    st2.setLong(2, match.getId());
                    st2.setInt(3, match.getGoalsScored().get(player));

                    int addedRows2 = st2.executeUpdate();

                    if (addedRows2 != 1) {
                        throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert match " + match);
                    }

                } catch (SQLException ex) {
                    String msg = "error when updating stage 3 match" + match;
                    Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
                    throw new ServiceFailureException(msg);
                }
            }

        } catch (SQLException ex) {
            String msg = "error when updating stage 1 match" + match;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public void deleteMatch(Match match) {
        validate(match);
        checkDataSource();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("DETELE FROM MATCHES WHERE id = ?")) {
            st.setLong(1, match.getId());

            int deletedRows = st.executeUpdate();

            if (deletedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows inserted when trying to delete match " + match);
            }

            try (PreparedStatement st2 = conn.prepareStatement("DELETE FROM GOALS WHERE matchid = ?")) {
                st2.setLong(1, match.getId());

                st2.executeUpdate();

            } catch (SQLException ex) {
                String msg = "error when deleting stage 2 match" + match;
                Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
                throw new ServiceFailureException(msg);
            }

        } catch (SQLException ex) {
            String msg = "error when deleting match" + match;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public Match findMatchById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Match> findMatchesByTeam(Team team) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Match> findMatchesByTeams(Team team1, Team team2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Map<Player, Integer> getGoalsScoredInMatch(Long MatchId) {
        //TODO
        return null;
    }

    private List<Match> resultSetToMatchList(ResultSet rs) throws SQLException {
        //TODO
        return null;
    }

    private Match resultSetToMatch(ResultSet rs) throws SQLException {
        //TODO
        return null;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    private void validate(Match match) {
        //TODO
    }
}
