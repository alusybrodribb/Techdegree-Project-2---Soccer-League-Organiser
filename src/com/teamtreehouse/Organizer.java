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

  public Organizer(Teams teams) {
    mAssignedPlayers = new TreeMap<Integer, Player>();
    mUnassignedPlayers = new TreeMap<Integer, Player>();
    mReader = new BufferedReader(new InputStreamReader(System.in));
    mMenu = new HashMap<String, String>();
    mainMenu();
    mPlayers = Players.load();
    mTeams = teams;
    mLeagueExperience = 0;
  }

  public String prompt() throws IOException {
      System.out.printf("%nThere are currently %d registered players.%n", mPlayers.length);
      System.out.printf("%d players have been assigned to teams, and %d players are unassigned.%n", mAssignedPlayers.size(),mUnassignedPlayers.size());
      System.out.printf("You have created %d of %d required teams.%n", mTeams.getNumberOfTeams(), mTeams.getMaxTeams());
      for (Map.Entry<String, String> option : mMenu.entrySet()) {
        System.out.printf("%s - %s %n", option.getKey(), option.getValue());
      }
      System.out.print("What do you want to do: ");
      String choice = mReader.readLine();
      return choice.trim().toLowerCase();
  }

  public void importPlayers() {
    Arrays.sort(mPlayers);
    for (int i = 0; i < (mPlayers.length); i++) {
      mUnassignedPlayers.put(i+1, mPlayers[i]);
      if (mPlayers[i].isPreviousExperience()) {
        mLeagueExperience++;
      }

    }

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
          if (teamNo== -1) {
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
          case "teams":
          case "t":

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
      mMenu.put("(T)eams","View Teams");
      mMenu.put("(Q)uit", "Quit the program.");
    }

    public int teamMenu() throws IOException {
      System.out.printf("The league currently has the following teams:%n%n");
        int menuInd = 1;
        int teamNo = 0;
        boolean teamCreated = false;
        for (Team team : mTeams.getTeams()) {
          if (team != null) {
          System.out.printf("%d) %s%n", menuInd, team.getTeamName());
          teamCreated = true;
          }
        menuInd++;
      } if (teamCreated) {
          System.out.printf("%nWhich team did you want to add players to?%n(please enter team number, or q to go to previous menu)%n");
        String choice = mReader.readLine();

        if (choice.toLowerCase() == "q") {
          teamNo = -1;
        } else {
            try {
              teamNo = Integer.parseInt(choice) - 1;

            } catch (NumberFormatException nfc) {
              System.out.printf("Please enter the number of the team that you want to update.");
            }

          }
      } else {
        System.out.printf("No Teams available.%n%nPlease add teams before assigning players.%n%n");
        teamNo = -1;
      }
      return teamNo;
    }

/******************
MENU FUNCTIONS
******************/

    public void addPlayers(Team team) throws IOException {

      System.out.printf("Please enter the players' player numbers, separated by a space.%n");
      String playerIndexAsString = mReader.readLine();
      String[] playerIndex  = playerIndexAsString.split(" ");
      if ((team.getPlayers().size() + playerIndex.length + 1) > 11) {
        int playersAdded = 11 - team.getPlayers().size();
        System.out.printf("You have added too many players. Only the first %d players have been added.%n%n", playersAdded);
      }
      System.out.printf("The following players have now been added:%n");
        for (int i = 0; (i < playerIndex.length) && (team.getPlayers().size() < 11); i++) {
            int index = Integer.parseInt(playerIndex[i]);
            if (mUnassignedPlayers.containsKey(index)) {
              System.out.printf("%d\t %s%n", index, mUnassignedPlayers.get(index).toString());
              team.addPlayer(index, mUnassignedPlayers.get(index));
              mAssignedPlayers.put(index, mUnassignedPlayers.get(index));
              mUnassignedPlayers.remove(index);
            }
        }
      System.out.printf("%nTeam Details as per below%n%n");
      team.printTeam();
  }

  public void removePlayers(Team team) throws IOException {
    System.out.printf("The following players have been assigned to this team:%n");
    team.printTeam();
    System.out.printf("%nWhich players do you want to remove from this team?%n");
    System.out.printf("Please enter the players' player numbers, separated by a space.%n");
    String playerIndexAsString = mReader.readLine();
    String[] playerIndex  = playerIndexAsString.split(" ");
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
    unassignedList();
    System.out.printf("%n%n");
    assignedList();
    System.out.printf("%n%d of %d players in the league have previous experience.%n", mLeagueExperience, mPlayers.length);
  }

  public void teamBalance() {
    
  }

  public void unassignedList() {
    System.out.printf("%nUnassigned players%n%n");
    System.out.printf("No.\t%-30s%-15s%15s%n%n", "Name", "Height", "Experience");
    for (Map.Entry<Integer,Player> entry : mUnassignedPlayers.entrySet()) {
      Integer key = entry.getKey();
      Player player = entry.getValue();
      System.out.printf("%d\t %s%n", key, player.toString());
    }
  }

  public void assignedList() {
    System.out.printf("Assigned players%n%n");
    System.out.printf("No.\t%-30s%-15s%15s%n%n", "Name", "Height", "Experience");
    for (Map.Entry<Integer,Player> entry : mAssignedPlayers.entrySet()) {
      Integer key = entry.getKey();
      Player player = entry.getValue();
      System.out.printf("%d\t %s%n", key, player.toString());
    }
  }


}
