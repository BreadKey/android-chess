package io.github.breadkey.chess.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.chess.ChessBoard;
import io.github.breadkey.chess.presenter.ChessPresenter;

public class ChessActivity extends AppCompatActivity {
    ChessPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        presenter = new ChessPresenter(this);
    }
}
