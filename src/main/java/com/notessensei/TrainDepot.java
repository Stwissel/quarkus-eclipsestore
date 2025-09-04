package com.notessensei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TrainDepot {

  private final Map<String, Train> trains = new HashMap<>();

  public void addTrain(Train train) {
    trains.put(train.name(), train);
  }

  public List<Train> getTrainList() {
    return trains.values().stream().toList();
  }

  public Map<String, Train> getTrains() {
    return trains;
  }

  public Optional<Train> getTrainByName(String name) {
    return Optional.ofNullable(trains.get(name));
  }

}
