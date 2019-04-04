package io.github.breadkey.chess.model.sign;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import io.github.breadkey.chess.view.sign.LoginWith;

public class LogoutManager {
    private static final LogoutManager instance = new LogoutManager();

    public static LogoutManager getInstance() {
        return instance;
    }

    private LogoutManager() {
    }

    public void logout(final LogoutCallback callback) {
        switch (LoginWith.getCurrentLoginWith()) {
            case KAKAO:
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        callback.onComplete();
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        super.onFailure(errorResult);
                    }
                });
        }
    }
}
