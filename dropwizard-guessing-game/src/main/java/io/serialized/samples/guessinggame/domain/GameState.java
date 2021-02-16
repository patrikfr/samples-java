package io.serialized.samples.guessinggame.domain;

import io.serialized.client.aggregate.Event;
import io.serialized.samples.guessinggame.domain.event.GameFinished;
import io.serialized.samples.guessinggame.domain.event.GameStarted;
import io.serialized.samples.guessinggame.domain.event.HintAdded;
import io.serialized.samples.guessinggame.domain.event.PlayerGuessed;

public record GameState(int number, int guessCount, boolean started, boolean finished) {

  public GameState() {
    this(0, 0, false, false);
  }

  public GameState handleGameStarted(Event<GameStarted> event) {
    return new GameState(event.data().number(), 0, true, finished);
  }

  public GameState handlePlayerGuessed(Event<PlayerGuessed> event) {
    return new GameState(number, guessCount + 1, started, finished);
  }

  public GameState handleHintAdded(Event<HintAdded> event) {
    return new GameState(number, guessCount, started, finished);
  }

  public GameState handleGameFinished(Event<GameFinished> event) {
    return new GameState(number, guessCount, started, true);
  }
}
