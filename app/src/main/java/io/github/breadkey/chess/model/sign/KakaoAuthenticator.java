package io.github.breadkey.chess.model.sign;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import java.util.ArrayList;

public class KakaoAuthenticator implements Authenticator {
    @Override
    public void requestAuthentication(final AuthenticationCallback callback) {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("properties.nickname");

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                callback.onFailure();
            }

            @Override
            public void onSuccess(MeV2Response result) {
                callback.onSuccess(String.valueOf(result.getId()), result.getNickname());
            }
        });
    }
}
