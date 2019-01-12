package io.github.breadkey.chess.mock;

import io.github.breadkey.chess.model.Player;
import io.github.breadkey.chess.model.PlayerMatcher;

public class MockPlayerMatcher implements PlayerMatcher {
    private Player enemy;

    @Override
    public void startFindEnemy(Player playerWantToPlay) {
        enemy = new MockPlayerHJ();
    }

    @Override
    public Player getEnemy() {
        return enemy;
    }
}
