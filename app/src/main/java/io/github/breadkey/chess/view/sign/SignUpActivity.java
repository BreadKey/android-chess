package io.github.breadkey.chess.view.sign;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import io.github.breadkey.chess.R;
import io.github.breadkey.chess.presenter.SignUpPresenter;
import io.github.breadkey.chess.view.BakeryInformation;
import io.github.breadkey.chess.view.InformationActionListener;

public class SignUpActivity extends AppCompatActivity {
    private SignUpPresenter presenter;
    private View inputContainer;
    private EditText nicknameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        inputContainer = findViewById(R.id.input_container);
        nicknameInput = findViewById(R.id.nickname_input);
        inputContainer.setVisibility(View.GONE);

        presenter = new SignUpPresenter(this);

        Button enterButton = findViewById(R.id.enter_nickname_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(nicknameInput.getWindowToken(), 0);
                presenter.nicknameEntered(nicknameInput.getText().toString());
            }
        });
    }

    public void showEnterNickname(String defaultNickname) {
        if (defaultNickname != null) {
            nicknameInput.setText(defaultNickname);
        }
        inputContainer.setVisibility(View.VISIBLE);
    }

    public void showNicknameAlreadyExist() {
        inputContainer.setVisibility(View.GONE);
        InformationActionListener listener = new InformationActionListener() {
            @Override
            public void action() {
                inputContainer.setVisibility(View.VISIBLE);
            }
        };
        BakeryInformation.showInformation((ViewGroup) findViewById(R.id.sign_up_main),
                "중복된 닉네임 입니다!",
                "\""+ nicknameInput.getText().toString() + "\"" + "는(은) 이미 존재하는 닉네임 입니다.",
                listener, listener);
    }
}
