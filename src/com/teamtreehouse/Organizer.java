package com.teamtreehouse;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import com.teamtreehouse.model.Teams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
    mMenu = new LinkedHashMap<String, String>();
    mainMenu();
    mPlayers = Players.load();
    mTeams = teams;
    mLeagueExperience = 0;
    mAverageHeight = 0;
  }

  public void importPlayers() {
    //imports player list into unassigned players
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

/**************************
MAIN MENU SWITCH
**************************/

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
          case "view":
          case "v":
            showPlayers();
            break;
          case "quit":
          case "q":
            System.out.printf("%nLeaving program...%n%n");
            break;
          case "height":
          case "h":
            teamNo = teamMenu();
            if (teamNo== -1) {
              break;
            }
            team = mTeams.getTeams()[teamNo];
            heightReport(team);
            break;
          case "league":
          case "l":
            leagueBalanceReport();
            break;
          case "print":
          case "p":
            teamNo = teamMenu();
            if (teamNo== -1) {
              break;
            }
            team = mTeams.getTeams()[teamNo];
            team.printTeamPlayers();
            break;
          default:
            System.out.printf("Unknown command '%s', Try again %n%n", choice);
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
      mMenu.put("(V)iew","View Players");
      mMenu.put("(H)eight","View Team Height Report");
      mMenu.put("(L)eague", "League Balance Report");
      mMenu.put("(P)rint", "Print Team");
      mMenu.put("(Q)uit", "Quit the program.");
    }

    public int teamMenu() throws IOException {
      //For Selecting Teams
      System.out.printf("The league currently has the following teams:%n%n");
        int menuInd = 1;
        int teamNo = -1;
        boolean teamCreated = false;
        for (Team team : mTeams.getTeams()) {
          if (team != null) {
          System.out.printf("%d) %s%n", menuInd, team.getTeamName());
          teamCreated = true;
          menuInd++;
          }
      }
      if (teamCreated) {
      teamNo = teamSelect(menuInd);
    } else {
        System.out.printf("No Teams available.%n%nPlease add teams before assigning players.%n%n");
      }
    return teamNo;
  }

    public int teamSelect(int menuItems) throws IOException {
      String choice = "";
      boolean choiceMade = false;
      int teamNo = -1;
      System.out.printf("%nPlease enter team number to select a team, or q to go to previous menu)%n");
      do {
        try {
          choice = mReader.readLine().trim().toLowerCase().substring(0,1);
          if (choice.equals("q")) {
            choiceMade = true;
            System.out.printf("Returning to previous menu.%n%n");
          } else {
            int choiceAsInt = Integer.parseInt(choice);
            if (choiceAsInt < menuItems && choiceAsInt > 0) {
              teamNo = choiceAsInt - 1;
              choiceMade = true;
            } else {
              System.out.printf("Please enter a valid number, or 'q' to return to previous menu.%n");
            }
          }
        } catch (StringIndexOutOfBoundsException | NumberFormatException  ex) {
          System.out.printf("Please enter a valid number, or 'q' to return to previous menu.%n");
        }
      } while (!choiceMade);
      return teamNo;
    }


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
      int index = -1;
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
    int index = -1;
    System.out.printf("The following players have been assigned to this team:%n");
    team.printTeamPlayers();
    System.out.printf("%nWhich players do you want to remove from this team?%n");
    System.out.printf("Please enter the players' player numbers, separated by a space or comma.%n");
    String playerIndexAsString = mReader.readLine();
    String[] playerIndex  = playerIndexAsString.split("[,\\s]+");
    System.out.printf("The following players have now been removed from the team:%n%n");
    for (int i = 0; i < playerIndex.length; i++) {
      try {
        index = Integer.parseInt(playerIndex[i]);
      } catch (NumberFormatException nfe) {
        index = -1;
      }
      if (team.getPlayers().containsKey(index)) {
        System.out.printf("%d\t %s%n", index, team.getPlayers().get(index).toString());
        mUnassignedPlayers.put(index, team.getPlayers().get(index));
        mAssignedPlayers.remove(index);
        team.removePlayer(index);
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
    System.out.printf("Players 3'4\" or shorter%n%n");
    for (Map.Entry<Integer,Player> entry : team.getPlayers().entrySet()) {
      if (entry.getValue().getHeightInInches() <= 40) {
        Integer key = entry.getKey();
        Player player = entry.getValue();
        System.out.printf("%d\t %s%n", key, player.toString());
      }
    }
    System.out.printf("%nPlayers between 3'4\" and 3'11\"%n%n");
    for (Map.Entry<Integer,Player> entry : team.getPlayers().entrySet()) {
      if ((entry.getValue().getHeightInInches() > 40) && (entry.getValue().getHeightInInches() < 47)){
        Integer key = entry.getKey();
        Player player = entry.getValue();
        System.out.printf("%d\t %s%n", key, player.toString());
      }
    }
    System.out.printf("%nPlayers 3'11\" or taller%n%n");
    for (Map.Entry<Integer,Player> entry : team.getPlayers().entrySet()) {
      if (entry.getValue().getHeightInInches() >= 47) {
        Integer key = entry.getKey();
        Player player = entry.getValue();
        System.out.printf("%d\t %s%n", key, player.toString());
      }
    }
    System.out.printf("%nTeam average height is %d inches%n%n", team.averageHeight());
    System.out.printf("League average height is %d inches%n%n", mAverageHeight);

  }

  public void experienceReport(Team team) {
    //Shows players in a team gouped by experience
    System.out.printf(team.toString());
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
    System.out.printf("%n%n", mLeagueExperience, mPlayers.length);
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
        experienceReport(team);
      }
    }
    System.out.printf("%nLeague Average Height:\t%d inches%n", mAverageHeight);
    System.out.printf("%n%d of %d players in the league have previous experience.%n", mLeagueExperience, mPlayers.length);
  }
}
