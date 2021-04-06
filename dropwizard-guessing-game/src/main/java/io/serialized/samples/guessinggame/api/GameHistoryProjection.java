package io.serialized.samples.guessinggame.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
public record GameHistoryProjection(List<Round> rounds) {
  public static record Round(int guess, @JsonProperty(access = WRITE_ONLY) long guessedAt) {
    public String getTimestamp() {
      return Instant.ofEpochMilli(guessedAt).toString();
    }
  }
}
