package io.serialized.samples.guessinggame.api;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record StartGameCommand(@NotNull UUID gameId) {
}
