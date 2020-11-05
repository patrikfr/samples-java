package io.serialized.samples.guessinggame.api;

import io.serialized.client.projection.ProjectionClient;
import io.serialized.client.projection.ProjectionResponse;
import io.serialized.client.projection.query.ProjectionQuery;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static io.serialized.client.projection.query.ProjectionQueries.single;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("queries")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class QueryResource {

  private final ProjectionClient projectionClient;

  public QueryResource(ProjectionClient projectionClient) {
    this.projectionClient = projectionClient;
  }

  @GET
  @Path("games/{gameId}")
  public Response getGame(@PathParam("gameId") String gameId) {

    ProjectionQuery query = single("games").id(gameId).build(GameProjection.class);
    ProjectionResponse<GameProjection> response = projectionClient.query(query);
    GameProjection projection = response.data();
    return Response.ok(projection).build();
  }

}
