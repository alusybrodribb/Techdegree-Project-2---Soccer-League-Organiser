package com.teamtreehouse;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import com.teamtreehouse.model.Teams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

public class Organizer {
  private Map<Integer, Player> mAssignedPlayers;
  private Map<Integer, Player> mUnassignedPlayers;
  private BufferedReader mReader;
  private Map<String, String> mMenu;
  private Player[] mPlayers;
  private Teams mTeams;
  private int mLeagueExperience;
  private int mAverageHeight;

  public Organizer(Teams teams) {
    mAssignedPlayers = new TreeMap<Integer, Player>();
    mUnassignedPlayers = new TreeMap<Integer, Player>();
    mReader = new BufferedReader(new InputStreamReader(System.in));
    mMenu = new HashMap<String, String>();
    mainMenu();
    mPlayers = Players.load();
    mTeams = teams;
    mLeagueExperience = 0;
    mAverageHeight = 0;
  }

  public void importPlayers() {
    Arrays.sort(mPlayers);
    for (int i = 0; i < (mPlayers.length); i++) {
      mUnassignedPlayers.put(i+1, mPlayers[i]);
      mAverageHeight += mPlayers[i].getHeightInInches();
      if (mPlayers[i].isPreviousExperience()) {
        mLeagueExperience++;
      }
    }
    mAverageHeight = mAverageHeight / mPlayers.length;
  }




  public void run() {
    int teamNo = -1;
    Team team;
    String choice = "";
    do {
      try {
        choice = prompt();
        switch(choice) {
          case "new":
          case "n":
            mTeams.addTeam();
            break;
          case "add":
          case "a":
            teamNo = teamMenu();
            if (teamNo== -1 || teamNo >= mTeams.getMaxTeams()) {
              break;
            }
            team = mTeams.getTeams()[teamNo];
            addPlayers(team);
            break;
          case "r":
          case "remove":
            teamNo = teamMenu();
            if (teamNo== -1) {
              break;
            }
            team = mTeams.getTeams()[teamNo];
            removePlayers(team);
            break;
          case "players":
          case "p":
            showPlayers();
            break;
          case "quit":
          case "q":
            System.out.printf("%nLeaving program...%n%n");
            break;
          case "height":
          case "h":
            teamNo = teamMenu();
            team = mTeams.getTeams()[teamNo];
            heightReport(team);
            break;
          case "experience":
          case "e":
              teamNo = teamMenu();
              team = mTeams.getTeams()[teamNo];
              experienceReport(team);
              break;
          case "league":
          case "l":
            leagueBalanceReport();
            break;
          default:
            System.out.printf("Unknown command '%s', Try again %s%s", choice);
            break;
        }
      } catch(IOException ioe) {
        System.out.println("Problem with input");
        ioe.printStackTrace();
      }
    } while (!(choice.equals("quit") || choice.equals("q")));
  }


  /*********************
  MENUS
  ********************/

    public void mainMenu() {
      mMenu.put("(N)ew", "Add a new team to the league.");
      mMenu.put("(A)dd", "Add a player to a team.");
      mMenu.put("(R)emove", "Remove a player from a team.");
      mMenu.put("(P)layers","View Players");
      mMenu.put("(H)eight","View Team Height Report");
      mMenu.put("(E)xperience","View Team Experience Report");
      mMenu.put("(L)eague", "League Balance Report");
      mMenu.put("(Q)uit", "Quit the program.");
    }

    public int teamMenu() throws IOException {
      //For Selecting Teams
      System.out.printf("The league currently has the following teams:%n%n");
        int menuInd = 1;
        int teamNo = 0;
        String choice = -1;
        boolean teamCreated = false;
        for (Team team : mTeams.getTeams()) {
          if (team != null) {
          System.out.printf("%d) %s%n", menuInd, team.getTeamName());
          teamCreated = true;
          }
        menuInd++;
      } if (teamCreated) {
        do {
          System.out.printf("%nWhich team did you want to add players to?%n(please enter team number, or q to go to previous menu)%n");
        String choice = mReader.readLine().substring(0,1);
        if (choice.toLowerCase() == "q") {
          teamNo = -1;
          return teamNo;
        } else {
          try{
              teamNo = Integer.parseInt(choice) - 1;
              return teamNo;
            } catch (NumberFormatException nfe) {
              System.out.printf("Invalid command - please enter a team No.");
            }
          }
        } while (teamNo != -1);
      } else {
        System.out.printf("No Teams available.%n%nPlease add teams before assigning players.%n%n");
        teamNo = -1;
      }
      return teamNo;
    }

/*************************
MAIN MENU PROMPTER
***********************/

    public String prompt() throws IOException {
        System.out.printf("%nThere are currently %d registered players.%n", mPlayers.length);
        System.out.printf("%d players have been assigned to teams, and %d players are unassigned.%n", mAssignedPlayers.size(),mUnassignedPlayers.size());
        System.out.printf("%d players have previous experience", mLeagueExperience);
        System.out.printf("You have created %d of %d required teams.%n", mTeams.getNumberOfTeams(), mTeams.getMaxTeams());
        for (Map.Entry<String, String> option : mMenu.entrySet()) {
          System.out.printf("%s - %s %n", option.getKey(), option.getValue());
        }
        System.out.print("What do you want to do: ");
        String choice = mReader.readLine();
        System.out.printf("%n");
        return choice.trim().toLowerCase();
    }


/******************
MENU FUNCTIONS
******************/

    public void addPlayers(Team team) throws IOException {
      //Allows Organiser to add players to a team
      int index = 0;
      System.out.printf("The following players can be assigned to the team:%n");
      listPlayers("Unassigned", mUnassignedPlayers);
      System.out.printf("%n%nPlease enter the players' player numbers, separated by a space or comma.%n");

        String playerIndexAsString = mReader.readLine();
        String[] playerIndex  = playerIndexAsString.split("[,\\s]+");
        if ((team.getPlayers().size() + playerIndex.length + 1) > 11) {
          int playersAdded = 11 - team.getPlayers().size();
          System.out.printf("You have added too many players. Only the first %d players have been added.%n%n", playersAdded);
        }
        System.out.printf("The following players have now been added:%n");
          for (int i = 0; (i < playerIndex.length) && (team.getPlayers().size() < 11); i++) {
            try {
              index = Integer.parseInt(playerIndex[i]);
            } catch (NumberFormatException nfe) {
              index = -1;
            }
              if (mUnassignedPlayers.containsKey(index)) {
                System.out.printf("%d\t %s%n", index, mUnassignedPlayers.get(index).toString());
                team.addPlayer(index, mUnassignedPlayers.get(index));
                mAssignedPlayers.put(index, mUnassignedPlayers.get(index));
                mUnassignedPlayers.remove(index);
              }
          }
        System.out.printf("%nTeam Details as per below%n%n");
        team.printTeamPlayers();
    }


  public void removePlayers(Team team) throws IOException {
    //Allows organiser to remove players from a team
    System.out.printf("The following players have been assigned to this team:%n");
    team.printTeamPlayers();
    System.out.printf("%nWhich players do you want to remove from this team?%n");
    System.out.printf("Please enter the players' player numbers, separated by a space or comma.%n");
    String playerIndexAsString = mReader.readLine();
    String[] playerIndex  = playerIndexAsString.split("[,\\s]+");
    System.out.printf("The following players have now been removed from the team:%n%n");
    for (int i = 0; i < playerIndex.length; i++) {
        int index = Integer.parseInt(playerIndex[i]);
          if (team.getPlayers().containsKey(index)) {
          System.out.printf("%d\t %s%n", index, team.getPlayers().get(index).toString());
          team.removePlayer(index);
          mUnassignedPlayers.put(index, team.getPlayers().get(index));
          mAssignedPlayers.remove(index);
      }
    }
  }

  public void showPlayers() {
    //Displays players in the league
    //Divided into those already allocated to a team and those unallcoated
    listPlayers("Unassigned", mUnassignedPlayers);
    System.out.printf("%n%n");
    listPlayers("Assigned", mAssignedPlayers);
    System.out.printf("%n%d of %d players in the league have previous experience.%n", mLeagueExperience, mPlayers.length);
  }

  public void heightReport(Team team) {
    //Shows players in a team grouped by height
    System.out.printf(team.toString());
    System.out.printf("%nHeight report for %s%n%n", team.getTeamName());
    System.out.printf("Players less than 40 inches tall%n%n");
    for (Map.Entry<Integer,Player> entry : team.getPlayers().entrySet()) {
      if (entry.getValue().getHeightInInches() < 40) {
        Integer key = entry.getKey();
        Player player = entry.getValue();
        System.out.printf("%d\t %s%n", key, player.toString());
      }
    }
    System.out.printf("Players between 40 and 44 inches tall (inclusive)%n%n");
    for (Map.Entry<Integer,Player> entry : team.getPlayers().entrySet()) {
      if ((entry.getValue().getHeightInInches() > 40) && (entry.getValue().getHeightInInches() > 40)){
        Integer key = entry.getKey();
        Player player = entry.getValue();
        System.out.printf("%d\t %s%n", key, player.toString());
      }
    }
    System.out.printf("Players between over 44 inches tall%n%n");
    for (Map.Entry<Integer,Player> entry : team.getPlayers().entrySet()) {
      if (entry.getValue().getHeightInInches() > 44) {
        Integer key = entry.getKey();
        Player player = entry.getValue();
        System.out.printf("%d\t %s%n", key, player.toString());
      }
    }
    System.out.printf("%nTeam average height is %d%n%n", team.averageHeight());
    System.out.printf("League average height is %d%n%n", mAverageHeight);

  }

  public void experienceReport(Team team) {
    //Shows players in a team gouped by experience
    System.out.printf(team.toString());
    System.out.printf("%nExperience Report report for %s%n%n", team.getTeamName());
    System.out.printf("Players with experience%n%n");
    for (Map.Entry<Integer,Player> entry : team.getPlayers().entrySet()) {
      if (entry.getValue().isPreviousExperience()) {
        Integer key = entry.getKey();
        Player player = entry.getValue();
        System.out.printf("%d\t %s%n", key, player.toString());
      }
    }
    System.out.printf("%nPlayers without experience%n%n");
    for (Map.Entry<Integer,Player> entry : team.getPlayers().entrySet()) {
      if (!entry.getValue().isPreviousExperience()) {
        Integer key = entry.getKey();
        Player player = entry.getValue();
        System.out.printf("%d\t %s%n", key, player.toString());
      }
    }
    System.out.printf("%n%d of %d players in the league have previous experience.%n", mLeagueExperience, mPlayers.length);

  }

    public void listPlayers(String listName, Map<Integer, Player> map) {
      //Lists unassigned players
      System.out.printf("%n%s players%n%n", listName);
      System.out.printf("No.\t%-30s%-15s%15s%n%n", "Name", "Height", "Experience");
      for (Map.Entry<Integer,Player> entry : map.entrySet()) {
        Integer key = entry.getKey();
        Player player = entry.getValue();
        System.out.printf("%d\t %s%n", key, player.toString());
      }
    }


  public void leagueBalanceReport() {
    //Shows League Balance Report
    for (Team team : mTeams.getTeams()) {
      if (team != null) {
        System.out.printf(team.toString());
      }
    }
    System.out.printf("%nLeague Average Height:\t%d inches%n", mAverageHeight);
    System.out.printf("%n%d of %d players in the league have previous experience.%n", mLeagueExperience, mPlayers.length);
  }
}
