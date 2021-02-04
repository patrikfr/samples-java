package io.serialized.samples.guessinggame.api;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record GuessNumberCommand(@NotNull UUID gameId, @Min(1) @Max(100) int number) {
}
