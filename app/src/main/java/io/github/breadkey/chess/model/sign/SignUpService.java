package io.github.breadkey.chess.model.sign;

public class SignUpService {
    public boolean isUserAlreadySignedUp(String id) {
         return false;
    }

    public boolean isNicknameDuplicated(String nickName) {
        return false;
    }

    public void signUp(String id, String nickname, SignUpCallback signUpCallback) {
        signUpCallback.signUpSuccess();
    }
}