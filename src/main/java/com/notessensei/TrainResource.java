
package com.notessensei;

import java.util.List;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class TrainResource {

    @Inject
    TrainStorage trainStorage;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/trains")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Train> getTrains() {
        return trainStorage.depot.trains;
    }

    @POST
    @Path("/trains")
    @Produces(MediaType.APPLICATION_JSON)
    public Train createTrain(Train train) {
        trainStorage.addTrain(train);
        return train;
    }

    @POST
    @Path("/shutdown")
    @Produces(MediaType.APPLICATION_JSON)
    public String shutdown() {
        trainStorage.shutdown();
        return "{\"status\":\"shutdown\"}";
    }
}
