package com.notessensei;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainDepot {

  public List<Train> trains = new ArrayList<>();

  public void addTrain(Train train) {
    trains.add(train);
  }

  public Optional<Train> getTrainByName(String name) {
    return trains.stream().filter(train -> train.getName().equals(name)).findFirst();
  }

}
