/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package backend;

import java.util.Objects;

/**
 *
 * @author LukeMcNemee
 */
public class Player {
    private Long id;
    private String name;
    private String surname;
    private byte playerNumber;
    private Long teamID;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.surname);
        hash = 89 * hash + this.playerNumber;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", name=" + name + ", surname=" + surname + ", playerNumber=" + playerNumber + ", team=" + teamID + ", playerPosition=" + playerPosition + '}';
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

    public Long getTeamID() {
        return teamID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
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
