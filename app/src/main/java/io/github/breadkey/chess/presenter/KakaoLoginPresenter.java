package io.github.breadkey.chess.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import io.github.breadkey.chess.view.sign.LoginWith;
import io.github.breadkey.chess.R;
import io.github.breadkey.chess.view.sign.LoginActivity;

public class KakaoLoginPresenter {
    private LoginActivity view;

    private SessionCallback sessionCallback;

    public KakaoLoginPresenter(LoginActivity view) {
        this.view = view;

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            view.setCurrentLoginWith(LoginWith.KAKAO);
            view.redirectSignUpActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    public SessionCallback getSessionCallback() {
        return sessionCallback;
    }
}
