package io.serialized.samples.guessinggame.api;

public record GameProjection(int guessCount, String hint, Integer correctAnswer, String result) {
}
