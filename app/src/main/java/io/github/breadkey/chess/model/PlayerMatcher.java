package io.github.breadkey.chess.model;

public interface PlayerMatcher {
    void startFindEnemy(Player playerWantToPlay);
    Player getEnemy();
}
