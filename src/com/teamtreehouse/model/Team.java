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
  private int mShortPlayers;
  private int mMediumPlayers;
  private int mTallPlayers;


  public Team(String teamName, String coach) {
    mTeamName = teamName;
    mCoach = coach;
    mPlayers = new TreeMap<Integer, Player>();
    mReader = new BufferedReader(new InputStreamReader(System.in));
    mShortPlayers = 0;
    mMediumPlayers = 0;
    mTallPlayers = 0;
  }

  public Team createTeam() throws IOException {
    //Creates a new team
    System.out.print("Please enter team name:  ");
    String teamName = mReader.readLine();
    System.out.print("Please enter team coach:  ");
    String coach = mReader.readLine();
    Team team = new Team(teamName, coach);
    return team;
  }

  public void addPlayer(Integer index, Player player) {
    // Adds player to team and increases no of players in that height bracket
    mPlayers.put(index, player);
    if (player.getHeightInInches() < 40) {
      mShortPlayers++;
    } else if (player.getHeightInInches() > 44) {
      mTallPlayers++;
    } else {
      mMediumPlayers++;
    }
  }

  public void removePlayer(int player) {
    //Removes player from team and decrease no of players in that height bracket
    if (mPlayers.get(player).getHeightInInches() < 40) {
      mShortPlayers--;
    } else if (mPlayers.get(player).getHeightInInches() > 44) {
      mTallPlayers--;
    } else {
      mMediumPlayers--;
    }
    mPlayers.remove(player);

  }

  public void printTeamPlayers() {
    //Prints a list of all players on the team
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
    System.out.printf("Average Height:\t%d%n", averageHeight());
  }

  public int teamExperience() {
    //Returns number of players on team with previous experience
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
    //Returns average height of team
    int teamHeight = 0;
    int players = mPlayers.size();
    int average = 0;
    for (Map.Entry<Integer,Player> entry : mPlayers.entrySet()) {
      Player player = entry.getValue();
      teamHeight += player.getHeightInInches();
    }
    if (players > 0) {
      average = teamHeight / players;
    } else {
      average = 0;
    }
    return average;
  }

/**********************
OVERRIDE FUNCTIONS
**********************/

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

@Override
  public String toString() {
    // Team Name
    //Team Coach
    //No of assigned Players
    //Team Experience
    //Averge team height
    int players = mPlayers.size();
    int height = averageHeight();
    int experience = teamExperience();
    return String.format("Team Name:\t\t%s%nCoach:\t\t\t%s%n%nAverage height:\t%d inches%nPlayers shorter than 3'4\":\t%d%nPlayers between 3'4\" and 3'8\":\t%d%nPlayers taller than 3'8\":\t%d%n%d out of %d players have experience.%n%n", mTeamName, mCoach, height, mShortPlayers, mMediumPlayers, mTallPlayers, experience, players);

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
