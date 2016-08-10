package com.teamtreehouse.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class Teams {
  private Team[] mTeams;
  private int mMaxTeams;
  private int mNumberOfTeams;
  private BufferedReader mReader;

  public Teams() {
    mMaxTeams = (Players.load().length / 11);
    mTeams = new Team[mMaxTeams];
    mNumberOfTeams = 0;
    mReader = new BufferedReader(new InputStreamReader(System.in));
  }

  public void addTeam() throws IOException {
    if(mNumberOfTeams < mMaxTeams) {
      System.out.print("Please enter team name:  ");
      String teamName = mReader.readLine();
      System.out.print("Please enter team coach:  ");
      String coach = mReader.readLine();
      Team team = new Team(teamName, coach);
      System.out.printf("Adding new team: %s%n%n", team.getTeamName());
      mTeams[mNumberOfTeams] = team;
      mNumberOfTeams++;
      Arrays.sort(mTeams, 0, mNumberOfTeams);

    } else {
      System.out.printf("%nSorry - you have aready created enough teams for this competition.%n%n");
    }
  }

  public Team[] getTeams() {
    return mTeams;
  }

  public int getNumberOfTeams() {
      return mNumberOfTeams;
  }

public int getMaxTeams() {
    return mMaxTeams;
  }
  //
  // @Override
  // public String toString() {
  //
  //
  // }

}
