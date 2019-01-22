package io.github.breadkey.chess.model.match;

;

public class PlayerMatcherFactory {
    public enum PlayerMatcherKey {
        VsInReal,
        VsCPU,
        VsOnline
    }

    public static PlayerMatcher createPlayerMatcher(PlayerMatcherKey key) {
        return new PlayerMatcherInReal();
    }
}
