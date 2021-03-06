package com.teamtreehouse.model;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {
  private static final long serialVersionUID = 1L;

  private String firstName;
  private String lastName;
  private int heightInInches;
  private boolean previousExperience;

  public Player(String firstName, String lastName, int heightInInches, boolean previousExperience) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.heightInInches = heightInInches;
    this.previousExperience = previousExperience;
  }

/********************************
INFERRED PROPERTIES
*******************************/


  public String inchesToFeet(int inchesHeight) {
    //Converts player heights to feet and inches
    int feet = inchesHeight / 12;
    int inches = inchesHeight % 12;
    String height = feet + "'" + inches + "\"";
    return height;
  }

/***************************
OVERRIDE FUNCTIONS
  ***************************/

  @Override
  public int compareTo(Player other) {
    // We always want to sort by last name then first name
    if (lastName.compareTo(other.lastName) < 0) {
      return -1;
    } else if (lastName.compareTo(other.lastName) > 0) {
      return +1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    String name = lastName + ", " + firstName;
    String height = inchesToFeet(heightInInches);
    String experience = (previousExperience ? "Y" : "N");
    return String.format("%-30s%-15s%10s", name, height, experience) ;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Player)) return false;

    Player player = (Player) o;

    if (heightInInches != player.heightInInches) return false;
    if (previousExperience != player.previousExperience) return false;
    if (!firstName.equals(player.firstName)) return false;
    return lastName.equals(player.lastName);

  }

  @Override
  public int hashCode() {
    int result = firstName.hashCode();
    result = 31 * result + lastName.hashCode();
    result = 31 * result + heightInInches;
    result = 31 * result + (previousExperience ? 1 : 0);
    return result;
  }

/************************
GETTERS
************************/

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getHeightInInches() {
    return heightInInches;
  }



  public boolean isPreviousExperience() {
    return previousExperience;
  }


}
