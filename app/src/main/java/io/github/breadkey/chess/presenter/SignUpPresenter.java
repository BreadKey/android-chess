package io.github.breadkey.chess.presenter;

import android.content.Intent;

import io.github.breadkey.chess.model.sign.Authenticator;
import io.github.breadkey.chess.model.sign.SignUpCallback;
import io.github.breadkey.chess.model.sign.AuthenticationCallback;
import io.github.breadkey.chess.model.sign.User;
import io.github.breadkey.chess.view.sign.LoginWith;
import io.github.breadkey.chess.model.sign.SignUpService;
import io.github.breadkey.chess.model.sign.AuthenticatorFactory;
import io.github.breadkey.chess.view.sign.SignUpActivity;
import io.github.breadkey.chess.view.chess.ChessActivity;

public class SignUpPresenter {
    private String id;
    private String nickname;
    private SignUpActivity view;
    private SignUpService signUpService;

    public SignUpPresenter(final SignUpActivity view) {
        this.view = view;

        signUpService = new SignUpService();
        Authenticator authenticator = AuthenticatorFactory.createSignUpService(LoginWith.getCurrentLoginWith());
        authenticator.requestAuthentication(new AuthenticationCallback() {
            @Override
            public void onSuccess(String id, String nickname) {
                setId(id);
                setNickname(nickname);

                if (signUpService.isUserAlreadySignedUp(id)) {
                    startChessActivity();
                } else {
                    view.showEnterNickname(nickname);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void setId(String id) { this.id = id; }
    private void setNickname(String nickname) { this.nickname = nickname; }

    public void nicknameEntered(final String nickname) {
        if (signUpService.isNicknameDuplicated(nickname)) {
            view.showNicknameAlreadyExist();
        } else {
            signUpService.signUp(id, nickname, new SignUpCallback() {
                @Override
                public void signUpSuccess() {
                    setId(id);
                    setNickname(nickname);
                    startChessActivity();
                }

                @Override
                public void signFailure() {

                }
            });
        }
    }

    private void startChessActivity() {
        User.getInstance().setNickname(nickname);
        view.startActivity(new Intent(view, ChessActivity.class));
        view.finish();
    }
}
