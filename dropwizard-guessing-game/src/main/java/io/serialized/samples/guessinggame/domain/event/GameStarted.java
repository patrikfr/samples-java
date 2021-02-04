package io.serialized.samples.guessinggame.domain.event;

import io.serialized.client.aggregate.Event;

import java.util.UUID;

import static io.serialized.client.aggregate.Event.newEvent;

public record GameStarted(UUID gameId, int number, long startedAt) {
  public static Event<GameStarted> gameStarted(final UUID gameId, final int number, final long startedAt) {
    return newEvent(new GameStarted(gameId, number, startedAt)).build();
  }
}
