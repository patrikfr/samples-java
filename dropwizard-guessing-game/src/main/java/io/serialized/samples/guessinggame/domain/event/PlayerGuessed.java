package io.serialized.samples.guessinggame.domain.event;

import io.serialized.client.aggregate.Event;

import java.util.UUID;

import static io.serialized.client.aggregate.Event.newEvent;

public record PlayerGuessed(UUID gameId, int guess, long guessedAt) {
  public static Event<PlayerGuessed> playerGuessed(final UUID gameId, final int guess, final long guessedAt) {
    return newEvent(new PlayerGuessed(gameId, guess, guessedAt)).build();
  }
}
