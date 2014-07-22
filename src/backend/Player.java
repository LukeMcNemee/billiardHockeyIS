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
public class Player {
    private Long id;
    private String name;
    private String surname;
    private byte playerNumber;
    private Team team;
    private boolean home;
    private boolean away;
    private PlayerPosition playerPosition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public byte getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(byte playerNumber) {
        if(playerNumber > 99 || playerNumber < 1){
            throw new IllegalArgumentException("player number out of bounds");
        }
        this.playerNumber = playerNumber;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getHattricks(){
        //TODO
        return 0;
    }
    
    public double getPenaltyRate(){
        //TODO
        return 0;
    }
    
    public int getScoredGoals(){
        //TODO
        return 0;
    }
    
    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public boolean isAway() {
        return away;
    }

    public void setAway(boolean away) {
        this.away = away;
    }

    public PlayerPosition getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(PlayerPosition playerPosition) {
        this.playerPosition = playerPosition;
    }
    
}
