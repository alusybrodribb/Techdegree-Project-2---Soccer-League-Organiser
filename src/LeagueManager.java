import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import com.teamtreehouse.model.Teams;
import com.teamtreehouse.Organizer;

import java.io.IOException;

public class LeagueManager {

  public static void main(String[] args) throws IOException {
    Teams teams = new Teams();
    Organizer organizer = new Organizer(teams);
    organizer.importPlayers();
    organizer.run();
    System.exit(0);
  }

}
