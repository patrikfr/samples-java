package io.serialized.samples.guessinggame.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import io.serialized.client.SerializedClientConfig;
import io.serialized.client.projection.ProjectionClient;
import io.serialized.client.projection.ProjectionResponse;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class QueryResourceTest {

  private static final ProjectionApiStub.ProjectionApiCallback projectionApiCallback = mock(ProjectionApiStub.ProjectionApiCallback.class);
  private static final DropwizardClientExtension dropwizard = new DropwizardClientExtension(new ProjectionApiStub(projectionApiCallback));

  private final ProjectionClient projectionClient = ProjectionClient.projectionClient(createConfig(dropwizard)).build();
  private final QueryResource queryResource = new QueryResource(projectionClient);

  private final ResourceExtension resources = ResourceExtension.builder()
      .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
      .addProvider(queryResource)
      .build();

  @BeforeEach
  public void setUp() {
    reset(projectionApiCallback);
    dropwizard.getObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  @Test
  public void shouldGetGame() {
    UUID gameId = UUID.randomUUID();

    GameProjection projection = new GameProjection(0, null, null, null);

    ProjectionResponse<GameProjection> projectionResponse = new ProjectionResponse<>(
        gameId.toString(), System.currentTimeMillis(), projection);

    when(projectionApiCallback.singleProjectionLoaded("games", gameId)).thenReturn(projectionResponse);

    Response response = resources.target("/queries/games/" + gameId.toString()).request().get();

    assertThat(response.getStatus()).isEqualTo(200);
    GameProjection payload = response.readEntity(GameProjection.class);
    assertThat(payload.guessCount()).isEqualTo(projection.guessCount());
  }

  @Test
  public void shouldGetGameHistory() throws JsonProcessingException {
    UUID gameId = UUID.randomUUID();

    record Round(int guess, long guessedAt) {
    }
    record GameHistoryProjectionResponse(List<Round> rounds) {
    }

    GameHistoryProjectionResponse projection = new GameHistoryProjectionResponse(
        List.of(
            new Round(50, Instant.parse("2021-02-04T12:15:30.00Z").toEpochMilli()),
            new Round(75, Instant.parse("2021-02-04T12:17:30.00Z").toEpochMilli()),
            new Round(63, Instant.parse("2021-02-04T12:18:30.00Z").toEpochMilli())));

    ProjectionResponse<GameHistoryProjectionResponse> projectionResponse = new ProjectionResponse<>(
        gameId.toString(), System.currentTimeMillis(), projection);

    when(projectionApiCallback.singleProjectionLoaded("game-history", gameId)).thenReturn(projectionResponse);

    Response response = resources.target("/queries/games/" + gameId.toString() + "/history").request().get();
    String expectedPayload = """
        {
          "rounds":[
            {
              "guess":50,
              "timestamp":"Thu Feb 04 13:15:30 CET 2021"
            },
            {
              "guess":75,
              "timestamp":"Thu Feb 04 13:17:30 CET 2021"
            },
            {
              "guess":63,
              "timestamp":"Thu Feb 04 13:18:30 CET 2021"
            }
          ]
        }""";

    JsonNode expectedJsonPayload = dropwizard.getObjectMapper().readTree(expectedPayload);
    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(dropwizard.getObjectMapper().readTree(response.readEntity(String.class))).isEqualTo(expectedJsonPayload);
  }

  public static SerializedClientConfig createConfig(DropwizardClientExtension dropwizard) {
    return new SerializedClientConfig.Builder()
        .rootApiUrl(dropwizard.baseUri() + "/api-stub")
        .accessKey("dummy")
        .secretAccessKey("dummy")
        .build();
  }

}
