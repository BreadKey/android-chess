package io.github.breadkey.chess.model.sign;

public abstract class SignUpService {
    protected String id;
    protected String nickname;
    abstract void setId();

    public boolean isUserAlreadySignedUp() {
        setId();

        return false;
    }

    public boolean isNicknameAlreadyExist(String nickname) {
        return false;
    }
}