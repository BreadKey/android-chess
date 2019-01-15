package io.github.breadkey.chess.model.match;

public interface MatchPlayerObserver {
    void enemyFounded(PlayerMatcherFactory.PlayerMatcherKey key);
}
