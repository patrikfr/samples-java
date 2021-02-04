package io.serialized.samples.guessinggame.domain.event;

import io.serialized.client.aggregate.Event;

import java.util.UUID;

import static io.serialized.client.aggregate.Event.newEvent;

public record GameFinished(UUID gameId, int correctAnswer, String result, long finishedAt) {
  public static Event<GameFinished> gameFinished(final UUID gameId, final int correctAnswer, final String result, final long finishedAt) {
    return newEvent(new GameFinished(gameId, correctAnswer, result, finishedAt)).build();
  }
}
