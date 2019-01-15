package io.github.breadkey.chess.model.match;

import io.github.breadkey.chess.model.Player;

;

public class PlayerMatcherFactory {
    public enum PlayerMatcherKey {
        VsInReal,
        VsCPU,
        VsOnline
    }

    public static PlayerMatcher createPlayerMatcher(PlayerMatcherKey key) {
        return new PlayerMatcher() {
            @Override
            public void startFindEnemy(Player playerWantToPlay) {

            }

            @Override
            public Player getEnemy() {
                return new Player("player2");
            }
        };
    }
}
