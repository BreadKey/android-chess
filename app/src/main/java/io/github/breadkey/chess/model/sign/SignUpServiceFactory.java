package io.github.breadkey.chess.model.sign;

import io.github.breadkey.chess.view.sign.LoginWith;

public class SignUpServiceFactory {
    public static SignUpService createSignUpService(LoginWith loginWith) {
        switch (loginWith) {
            case KAKAO:
                return new KakaoSignUpService();
        }

        return null;
    }
}
