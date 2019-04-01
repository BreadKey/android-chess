package io.github.breadkey.chess.view.sign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kakao.auth.Session;

import io.github.breadkey.chess.R;
import io.github.breadkey.chess.presenter.KakaoLoginPresenter;
import io.github.breadkey.chess.view.LoadingActivity;

public class LoginActivity extends AppCompatActivity {
    private KakaoLoginPresenter kakaoLoginPresenter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startActivity(new Intent(this, LoadingActivity.class));
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE);
        int loginWithValue = sharedPreferences.getInt("login_with", -1);
        if (loginWithValue != -1) {
            LoginWith.setCurrentLoginWith(LoginWith.fromValue(loginWithValue));
        }

        kakaoLoginPresenter = new KakaoLoginPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kakaoLoginPresenter.getSessionCallback());
    }

    public void redirectSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    public void setCurrentLoginWith(LoginWith loginWith) {
        sharedPreferences.edit().putInt("login_with", loginWith.getValue());
        LoginWith.setCurrentLoginWith(loginWith);
    }
}
