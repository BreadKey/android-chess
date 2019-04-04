package io.github.breadkey.chess.model.sign;

import io.github.breadkey.chess.view.sign.LoginWith;

public class AuthenticatorFactory {
    public static Authenticator createSignUpService(LoginWith loginWith) {
        switch (loginWith) {
            case KAKAO:
                return new KakaoAuthenticator();
        }

        return null;
    }
}
