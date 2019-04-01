package io.github.breadkey.chess.model.sign;

import io.github.breadkey.chess.model.Player;

public class User extends Player{
    private static final User Instance = new User();

    public static User getInstance() {
        return Instance;
    }

    private User() {
        super("");
    }

    public void setNickname(String nickname) {
        this.nickName = nickname;
    }
}
