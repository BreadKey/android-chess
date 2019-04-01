package io.github.breadkey.chess.model.sign;

public interface AuthenticationCallback {
    void onSuccess(String id, String nickname);
    void onFailure();
}
