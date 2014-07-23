/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package backend;

/**
 *
 * @author LukeMcNemee
 */
public class Score {
    private final int homeScore;
    private final int awayScore;

    public Score(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }
    
    public int getScoreDifference(){
        return homeScore - awayScore;
    }
}
