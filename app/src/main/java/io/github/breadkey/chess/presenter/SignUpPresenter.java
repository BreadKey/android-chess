package io.github.breadkey.chess.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import io.github.breadkey.chess.view.LoadingActivity;
import io.github.breadkey.chess.view.sign.LoginWith;
import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.sign.SignUpService;
import io.github.breadkey.chess.model.sign.SignUpServiceFactory;
import io.github.breadkey.chess.view.sign.SignUpActivity;
import io.github.breadkey.chess.view.chess.ChessActivity;

public class SignUpPresenter {
    private SignUpActivity view;
    private SignUpService signUpService;

    public SignUpPresenter(final SignUpActivity view) {
        this.view = view;

        signUpService = SignUpServiceFactory.createSignUpService(LoginWith.getCurrentLoginWith());
        if (signUpService.isUserAlreadySignedUp()) {
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    view.startActivity(new Intent(view, LoadingActivity.class));
                }
            });
            //view.startActivity(new Intent(view, ChessActivity.class));
        } else {
            view.showEnterNickname();
        }
    }

    public void nicknameEntered(String nickname) {
        if (signUpService.isNicknameAlreadyExist(nickname)) {
            view.showNicknameAlreadyExist();
        } else {
            view.startActivity(new Intent(view, ChessActivity.class));
        }
    }
}
