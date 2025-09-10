package com.notessensei;

public class Train {
  private final String name;
  private final TrainColor color;
  private final int numberOfCars;

  public Train(String name, TrainColor color, int numberOfCars) {
    this.name = name;
    this.color = color;
    this.numberOfCars = numberOfCars;
  }

  public String getName() {
    return name;
  }

  public TrainColor getColor() {
    return color;
  }

  public int getNumberOfCars() {
    return numberOfCars;
  }
}
