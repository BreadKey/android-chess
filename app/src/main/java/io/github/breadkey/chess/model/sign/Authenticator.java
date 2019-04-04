package io.github.breadkey.chess.model.sign;

public interface Authenticator {
    void requestAuthentication(AuthenticationCallback authenticationCallback);
}
