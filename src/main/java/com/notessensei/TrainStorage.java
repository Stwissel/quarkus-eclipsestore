package com.notessensei;

import java.nio.file.Path;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TrainStorage {
  private final EmbeddedStorageManager storageManager;
  public final TrainDepot depot;

  public TrainStorage() {
    this.depot = new TrainDepot();

    this.storageManager = EmbeddedStorage.start(this.depot, Path.of("target/trains"));
    System.out.println("Storage initialized successfully");
  }

  public void store(Object obj) {
    storageManager.store(obj);
  }

  public void addTrain(Train train) {
    depot.addTrain(train);
    store(train);
  }

  public void shutdown() {
    storageManager.shutdown();
    System.out.println("Storage shutdown successfully");
  }
}
