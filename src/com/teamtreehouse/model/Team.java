package com.teamtreehouse.model;

import java.io.*;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeMap;

public class Team implements Comparable<Team>, Serializable {
  private String mTeamName;
  private String mCoach;
  private Map<Integer, Player> mPlayers;
  private BufferedReader mReader;


  public Team(String teamName, String coach) {
    mTeamName = teamName;
    mCoach = coach;
    mPlayers = new TreeMap<Integer, Player>();
    mReader = new BufferedReader(new InputStreamReader(System.in));

  }

  public Team createTeam() throws IOException {
    System.out.print("Please enter team name:  ");
    String teamName = mReader.readLine();
    System.out.print("Please enter team coach:  ");
    String coach = mReader.readLine();
    Team team = new Team(teamName, coach);
    return team;
  }

  public void addPlayer(Integer index, Player player) {
    mPlayers.put(index, player);
  }

  public void removePlayer(int player) {
    mPlayers.remove(player);
  }

  public void printTeam() {
    System.out.printf("Team Name:\t%s%n",mTeamName);
    System.out.printf("Coach:\t\t%s%n%n", mCoach);
    System.out.printf("Players%n");
    System.out.printf("No.\t%-30s%-15s%15s%n%n", "Name", "Height", "Experience");
    for (Map.Entry<Integer,Player> entry : mPlayers.entrySet()) {
      Integer key = entry.getKey();
      Player player = entry.getValue();
      System.out.printf("%d\t %s%n", key, player.toString());
    }
    System.out.printf("%n%d of %d players have previous experience%n%n", teamExperience(), mPlayers.size());
  }

  public int teamExperience() {
    int experiencedPlayers = 0;
    for (Map.Entry<Integer,Player> entry : mPlayers.entrySet()) {
      Player player = entry.getValue();
      if (player.isPreviousExperience()) {
        experiencedPlayers++;
      }
    }
    return experiencedPlayers;
  }

  public int averageHeight() {
    int teamHeight = 0;
    int players = mPlayers.size();
    for (Map.Entry<Integer,Player> entry : mPlayers.entrySet()) {
      Player player = entry.getValue();
      teamHeight += player.getHeightInInches();
    }
    int average = teamHeight / players;
    return average;
  }

  @Override
  public int compareTo(Team other) {
    // Sort by team name
      if (mTeamName.compareTo(other.mTeamName) < 0) {
        return -1;
      } else if (mTeamName.compareTo(other.mTeamName) > 0) {
        return +1;
      } else {
        return 0;
      }
  }




/****************
Getters
*****************/
  public Map<Integer, Player> getPlayers() {
    return mPlayers;
  }

  public String getTeamName() {
    return mTeamName;
  }

}
