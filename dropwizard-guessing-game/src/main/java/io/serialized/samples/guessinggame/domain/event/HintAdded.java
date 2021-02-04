package io.serialized.samples.guessinggame.domain.event;

import io.serialized.client.aggregate.Event;

import java.util.UUID;

import static io.serialized.client.aggregate.Event.newEvent;

public record HintAdded(UUID gameId, String hint, long addedAt) {
  public static Event<HintAdded> hintAdded(final UUID gameId, final String hint, final long addedAt) {
    return newEvent(new HintAdded(gameId, hint, addedAt)).build();
  }
}
