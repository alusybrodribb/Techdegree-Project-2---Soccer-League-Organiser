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
    //Adds a team to team registry
    boolean teamNamed = false;

    String name = "";
    if(mNumberOfTeams < mMaxTeams) {
      do {
        boolean teamNameExists = false;
        System.out.print("Please enter team name:  ");
        name = mReader.readLine();
        for (int i = 0; i < mTeams.length; i++) {
          if (mTeams[i] != null && mTeams[i].getTeamName().equals(name)) {
            teamNameExists = true;
            System.out.printf("Team name already exists - please choose another name.%n");
          }
        }
        teamNamed = !teamNameExists;
      } while (!teamNamed);
      System.out.print("Please enter team coach:  ");
      String coach = mReader.readLine();
      Team team = new Team(name, coach);
      System.out.printf("Adding new team: %s%n%n", team.getTeamName());
      mTeams[mNumberOfTeams] = team;
      mNumberOfTeams++;
      Arrays.sort(mTeams, 0, mNumberOfTeams);

    } else {
      System.out.printf("%nSorry - you have aready created enough teams for this competition.%n%n");
    }
  }



/*****************
GETTERS
*****************/

  public Team[] getTeams() {
    return mTeams;
  }

  public int getNumberOfTeams() {
      return mNumberOfTeams;
  }

public int getMaxTeams() {
    return mMaxTeams;
  }


}
