# Quarkus and EclipseStore

This project uses [Quarkus](https://quarkus.io/) and [EclipseStore](https://eclipsestore.io/).

## What it does

It is a simple train managemnt application. One and add a train and list all trains. 4 endpoints are provided:

- GET `/hello` returns a String "Hello"
- GET `/trains` returns a JSON Array with trains
- POST `/trains` create a train (see example)
- POST `/shutdown` (no body) shuts down the EclipseStore

## Running it

Start with `mvnwquarkus:dev` or, when you want to first wipe the database to start over `mvn clean quarkus:dev`

To set the `env` variablw right use `./runit.sh`

Then try things like that:

```shell
# Say hello
curl http://localhost:8080/hello
# List of trains
curl http://localhost:8080/hello/trains
#create a train
curl --request POST http://localhost:8080/hello/trains \
  --header 'content-type: application/json' \
  --data '{
  "name": "Thomas",
  "color": "RED",
  "numberIfCars": 7
}'
# SHutdown
curl --request POST http://localhost:8080/hello/shutdown
```

## Problems encountered

- When launching without a database prsesent I can get th empty list of trains as empty `[]` and see the storage directoy created in target/trains. When I then try to add a train I get:

```log
Exception in TrainStorage.java:16
          14      this.depot = new TrainDepot();
          15
        → 16      this.storageManager = EmbeddedStorage.start(this.depot, Path.of("target/trains"));
          17      System.out.println("Storage initialized successfully");
          18    }

Exception in TrainResource.java:35
          33      @Produces(MediaType.APPLICATION_JSON)
          34      public Train createTrain(Train train) {
        → 35          trainStorage.addTrain(train);
          36          return train;
          37      }: org.eclipse.store.storage.exceptions.StorageExceptionInitialization: Active storage for org.eclipse.store.storage.types.Database$Default@283a11af "Eclipse Serializer@target/trains" already exists.
        at org.eclipse.store.storage.types.Database$Default.guaranteeNoActiveStorage(Database.java:105)
        at org.eclipse.store.storage.types.Databases$Default.ensureStoragelessDatabase(Databases.java:87)
        at org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation$Default.ensureDatabase(EmbeddedStorageFoundation.java:752)
        at org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation$Default.createEmbeddedStorageManager(EmbeddedStorageFoundation.java:780)
        at org.eclipse.store.storage.embedded.types.EmbeddedStorage.createAndStartStorageManager(EmbeddedStorage.java:607)
        at org.eclipse.store.storage.embedded.types.EmbeddedStorage.start(EmbeddedStorage.java:466)
        at com.notessensei.TrainStorage.<init>(TrainStorage.java:16)
        at com.notessensei.TrainStorage_Bean.doCreate(Unknown Source)
        at com.notessensei.TrainStorage_Bean.create(Unknown Source)
        at com.notessensei.TrainStorage_Bean.create(Unknown Source)
        at io.quarkus.arc.impl.AbstractSharedContext.createInstanceHandle(AbstractSharedContext.java:119)
        at io.quarkus.arc.impl.AbstractSharedContext$1.get(AbstractSharedContext.java:38)
        at io.quarkus.arc.impl.AbstractSharedContext$1.get(AbstractSharedContext.java:35)
        at io.quarkus.arc.generator.Default_jakarta_enterprise_context_ApplicationScoped_ContextInstances.c2(Unknown Source)
        at io.quarkus.arc.generator.Default_jakarta_enterprise_context_ApplicationScoped_ContextInstances.computeIfAbsent(Unknown Source)
        at io.quarkus.arc.impl.AbstractSharedContext.get(AbstractSharedContext.java:35)
        at io.quarkus.arc.impl.ClientProxies.getApplicationScopedDelegate(ClientProxies.java:23)
        at com.notessensei.TrainStorage_ClientProxy.arc$delegate(Unknown Source)
        at com.notessensei.TrainStorage_ClientProxy.addTrain(Unknown Source)
        at com.notessensei.TrainResource.createTrain(TrainResource.java:35)

```

I have set `JAVA_OPTS="--add-exports java.base/jdk.internal.misc=ALL-UNNAMED"`

- The other way around: when no database exists, tyring to create a train results in the same error above, but the db got created.

- trying to get the list of trains after a restart results in:

```log
14      this.depot = new TrainDepot();
          15
        → 16      this.storageManager = EmbeddedStorage.start(this.depot, Path.of("target/trains"));
          17      System.out.println("Storage initialized successfully");
          18    }: java.lang.RuntimeException: Error injecting com.notessensei.TrainStorage com.notessensei.TrainResource.trainStorage
        at com.notessensei.TrainResource_Bean.doCreate(Unknown Source)
        at com.notessensei.TrainResource_Bean.create(Unknown Source)
        at com.notessensei.TrainResource_Bean.create(Unknown Source)
        at io.quarkus.arc.impl.AbstractSharedContext.createInstanceHandle(AbstractSharedContext.java:119)
        at io.quarkus.arc.impl.AbstractSharedContext$1.get(AbstractSharedContext.java:38)
        at io.quarkus.arc.impl.AbstractSharedContext$1.get(AbstractSharedContext.java:35)
        at io.quarkus.arc.impl.LazyValue.get(LazyValue.java:32)
        at io.quarkus.arc.impl.ComputingCache.computeIfAbsent(ComputingCache.java:69)
        at io.quarkus.arc.impl.ComputingCacheContextInstances.computeIfAbsent(ComputingCacheContextInstances.java:19)
        at io.quarkus.arc.impl.AbstractSharedContext.get(AbstractSharedContext.java:35)
        at com.notessensei.TrainResource_Bean.get(Unknown Source)
        at com.notessensei.TrainResource_Bean.get(Unknown Source)
        at io.quarkus.arc.impl.ArcContainerImpl.beanInstanceHandle(ArcContainerImpl.java:570)
        at io.quarkus.arc.impl.ArcContainerImpl.beanInstanceHandle(ArcContainerImpl.java:550)
        at io.quarkus.arc.impl.ArcContainerImpl.beanInstanceHandle(ArcContainerImpl.java:583)
        at io.quarkus.arc.impl.ArcContainerImpl$3.get(ArcContainerImpl.java:337)
        at io.quarkus.arc.impl.ArcContainerImpl$3.get(ArcContainerImpl.java:334)
        at io.quarkus.arc.runtime.BeanContainerImpl$1.create(BeanContainerImpl.java:62)
        at io.quarkus.resteasy.reactive.common.runtime.ArcBeanFactory.createInstance(ArcBeanFactory.java:27)
        at org.jboss.resteasy.reactive.server.handlers.InstanceHandler.handle(InstanceHandler.java:26)
        at io.quarkus.resteasy.reactive.server.runtime.QuarkusResteasyReactiveRequestContext.invokeHandler(QuarkusResteasyReactiveRequestContext.java:181)
        at org.jboss.resteasy.reactive.common.core.AbstractResteasyReactiveContext.run(AbstractResteasyReactiveContext.java:147)
        at io.quarkus.vertx.core.runtime.VertxCoreRecorder$15.runWith(VertxCoreRecorder.java:645)
        at org.jboss.threads.EnhancedQueueExecutor$Task.doRunWith(EnhancedQueueExecutor.java:2651)
        at org.jboss.threads.EnhancedQueueExecutor$Task.run(EnhancedQueueExecutor.java:2630)
        at org.jboss.threads.EnhancedQueueExecutor.runThreadBody(EnhancedQueueExecutor.java:1622)
        at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1589)
        at org.jboss.threads.DelegatingRunnable.run(DelegatingRunnable.java:11)
        at org.jboss.threads.ThreadLocalResettingRunnable.run(ThreadLocalResettingRunnable.java:11)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: org.eclipse.serializer.persistence.exceptions.PersistenceException: Missing runtime type for required type handler for type: com.notessensei.TrainDepot
        at org.eclipse.serializer.persistence.types.PersistenceTypeHandlerManager$Default.validateExistingType(PersistenceTypeHandlerManager.java:392)
        at org.eclipse.serializer.persistence.types.PersistenceTypeHandlerManager$Default.ensureTypeHandler(PersistenceTypeHandlerManager.java:441)
        at org.eclipse.serializer.persistence.types.PersistenceTypeHandlerManager$Default.lambda$ensureTypeHandlers$2(PersistenceTypeHandlerManager.java:488)
        at org.eclipse.serializer.collections.ChainStorageStrong.iterate(ChainStorageStrong.java:1316)
        at org.eclipse.serializer.collections.HashEnum.iterate(HashEnum.java:687)
        at org.eclipse.serializer.persistence.types.PersistenceTypeHandlerManager$Default.ensureTypeHandlers(PersistenceTypeHandlerManager.java:487)
        at org.eclipse.serializer.persistence.types.PersistenceTypeHandlerManager$Default.ensureTypeHandlersByTypeIds(PersistenceTypeHandlerManager.java:479)
        at org.eclipse.store.storage.embedded.types.EmbeddedStorageManager$Default.ensureRequiredTypeHandlers(EmbeddedStorageManager.java:350)
        at org.eclipse.store.storage.embedded.types.EmbeddedStorageManager$Default.start(EmbeddedStorageManager.java:257)
        at org.eclipse.store.storage.embedded.types.EmbeddedStorageManager$Default.start(EmbeddedStorageManager.java:99)
        at org.eclipse.store.storage.embedded.types.EmbeddedStorage.createAndStartStorageManager(EmbeddedStorage.java:609)
        at org.eclipse.store.storage.embedded.types.EmbeddedStorage.start(EmbeddedStorage.java:466)
        at com.notessensei.TrainStorage.<init>(TrainStorage.java:16)
        at com.notessensei.TrainStorage_ClientProxy.<init>(Unknown Source)
        at com.notessensei.TrainStorage_Bean.proxy(Unknown Source)
        at com.notessensei.TrainStorage_Bean.get(Unknown Source)
        at com.notessensei.TrainStorage_Bean.get(Unknown Source)
        ... 31 more
```

**WHAT DO I MISS?**
