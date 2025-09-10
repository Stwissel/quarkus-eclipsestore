package com.notessensei;

import java.nio.file.Path;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import jakarta.inject.Singleton;

@Singleton
public class TrainStorage {
  private final EmbeddedStorageManager storageManager;
  public final TrainDepot depot;

  public TrainStorage() {

    this.storageManager = EmbeddedStorage.start(Path.of("traindepot"));
    System.out.println("Storage initialized");

    if (this.storageManager.root() != null) {
      this.depot = (TrainDepot) this.storageManager.root();
      System.out.println("Loaded existing storage");
      System.out.println("Current trains: " + this.depot.trains.size());
    } else {
      System.out.println("No existing storage found, starting fresh");
      this.depot = new TrainDepot();
      this.storageManager.setRoot(this.depot);
      this.storageManager.storeRoot();
    }
  }



  public void addTrain(Train train) {
    this.depot.addTrain(train);
    System.out.println("Added train: " + train.getName());
    this.storageManager.store(this.depot.trains);
  }

  public void shutdown() {
    storageManager.shutdown();
    System.out.println("Storage shutdown successfully");
  }
}
