package io.github.breadkey.chess.model;

public class Player {
    private String id;
    public String nickName;

    public Player(String id) {
        this.id = id;
        this.nickName = id;
    }
}
