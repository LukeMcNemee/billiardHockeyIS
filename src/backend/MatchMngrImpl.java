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
import java.util.HashMap;
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
    private TeamMngrImpl teamMngr;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        playerMngr.setDataSource(dataSource);
        teamMngr.setDataSource(dataSource);
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
                        + ", homeadvantagegoals, awayadvantagegoals, homecontumationgoals, awaycontumationgoals, hometechnicalgoals, awaytechnicalgoals, matchresult)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
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
            st.setString(12, match.getResult().toString());

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
                        + ", homeadvantagegoals = ?, awayadvantagegoals = ?, homecontumationgoals = ?, awaycontumationgoals = ?, hometechnicalgoals = ?, awaytechnicalgoals = ?, matchresult = ?"
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
            st.setString(12, match.getResult().toString());
            st.setLong(13, match.getId());

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
        checkDataSource();
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT id, hometeamid, awayteamid, date, homeowngoals, awayowngoals"
                        + ", homeadvantagegoals, awayadvantagegoals, homecontumationgoals, awaycontumationgoals, hometechnicalgoals, awaytechnicalgoals, matchresult"
                        + " FROM MATCHES WHERE id = ?")) {
            st.setLong(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Match match = resultSetToMatch(rs);

                if (rs.next()) {
                    throw new ServiceFailureException("Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + match + " and " + resultSetToMatch(rs));
                }
                return match;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            String msg = "error when selecting match with id" + id;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public List<Match> findMatchesByTeam(Team team) {
        checkDataSource();
        if (team == null) {
            throw new IllegalArgumentException("team is null");
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT id, hometeamid, awayteamid, date, homeowngoals, awayowngoals"
                        + ", homeadvantagegoals, awayadvantagegoals, homecontumationgoals, awaycontumationgoals, hometechnicalgoals, awaytechnicalgoals, matchresult"
                        + " FROM MATCHES WHERE hometeamid = ? OR awayteamid = ?")) {
            st.setLong(1, team.getId());
            st.setLong(2, team.getId());

            ResultSet rs = st.executeQuery();

            return resultSetToMatchList(rs);

        } catch (SQLException ex) {
            String msg = "error when selecting matches with team" + team;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    @Override
    public List<Match> findMatchesByTeams(Team team1, Team team2) {
        checkDataSource();
        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException("team is null");
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT id, hometeamid, awayteamid, date, homeowngoals, awayowngoals"
                        + ", homeadvantagegoals, awayadvantagegoals, homecontumationgoals, awaycontumationgoals, hometechnicalgoals, awaytechnicalgoals, matchresult"
                        + " FROM MATCHES WHERE hometeamid = ? AND awayteamid = ?")) {
            st.setLong(1, team1.getId());
            st.setLong(2, team2.getId());

            ResultSet rs = st.executeQuery();

            return resultSetToMatchList(rs);

        } catch (SQLException ex) {
            String msg = "error when selecting matches with teams" + team1 + " " + team2;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }
    }

    private List<Match> resultSetToMatchList(ResultSet rs) throws SQLException {
        List<Match> matches = new ArrayList<>();
        while (rs.next()) {
            Match match = resultSetToMatch(rs);
            matches.add(match);
        }
        return matches;
    }

    private Match resultSetToMatch(ResultSet rs) throws SQLException {
        Match match = new Match();
        match.setId(rs.getLong("id"));
        match.setHomeTeam(teamMngr.findTeamByID(rs.getLong("hometeamid")));
        match.setAwayTeam(teamMngr.findTeamByID(rs.getLong("awayteamid")));
        match.setDatePlayed(rs.getDate("date"));
        match.setOwnGoals(new Score(rs.getInt("homeowngoals"), rs.getInt("awayowngoals")));
        match.setAdvantageGoals(new Score(rs.getInt("homeadvantagegoals"), rs.getInt("awayadvantagegoals")));
        match.setAdvantageGoals(new Score(rs.getInt("homecontumationgoals"), rs.getInt("awaycontumationgoals")));
        match.setTechnicalGoals(new Score(rs.getInt("hometechnicalgoals"), rs.getInt("awaytechnicalgoals")));
        match.setGoalsScored(getGoalsForMatch(match.getId()));
        switch (rs.getString("matchresult")) {
            case "REGULAR_TIME":
                match.setResult(MatchResult.REGULAR_TIME);
                break;
            case "OVER_TIME":
                match.setResult(MatchResult.OVER_TIME);
                break;
            case "CONTUMATED":
                match.setResult(MatchResult.CONTUMATED);
                break;
            case "PENALTIES":
                match.setResult(MatchResult.PENALTIES);
                break;
            case "DRAW":
                match.setResult(MatchResult.DRAW);
                break;
        }
        return match;
    }

    private Map<Player, Integer> getGoalsForMatch(Long matchId) throws SQLException {
        Map<Player, Integer> goals = new HashMap<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement st = conn.prepareStatement("SELECT playerid, amount FROM GOALS WHERE matchid = ?")) {

            st.setLong(1, matchId);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Player player = playerMngr.getPlayerById(rs.getLong("playerid"));
                Integer amount = rs.getInt("amount");
                goals.put(player, amount);
            }

        } catch (SQLException ex) {
            String msg = "error when selecting goals for match with id" + matchId;
            Logger.getLogger(TeamMngrImpl.class.getName()).log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg);
        }

        return goals;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    private void validate(Match match) {
        if (match == null) {
            throw new ValidationException("match is null");
        }
        if (match.getHomeTeam() == null || match.getAwayTeam() == null) {
            throw new ValidationException("match team is null");
        }
        if (match.getResult() == null) {
            throw new ValidationException("match result null");
        }
        if (match.getDatePlayed() == null) {
            throw new ValidationException("match date null");
        }
        if (match.getResult() == null) {
            throw new ValidationException("match result null");
        }
    }
}
