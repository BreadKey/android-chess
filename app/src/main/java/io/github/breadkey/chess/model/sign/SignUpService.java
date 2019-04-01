package io.github.breadkey.chess.model.sign;

public abstract class SignUpService {
    public abstract void requestAuth(AuthenticationCallback callback);

    public boolean isUserAlreadySignedUp(String id) {
         return false;
    }

    public boolean isNicknameAlreadyExist(String nickName) {
        return false;
    }

    public void sign(String id, String nickname, SignCallback signCallback) {
        signCallback.signSuccess();
    }
}