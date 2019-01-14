package io.github.breadkey.chess.presenter;

import android.view.View;
import android.widget.Button;

import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.view.ChessActivity;

public class ChessPresenter {
    ChessActivity view;
    View matchPlayerLatour;
    PlayChessService playChessService;

    public ChessPresenter(ChessActivity view) {
        this.view = view;
        matchPlayerLatour = view.findViewById(R.id.match_player_layout);
        playChessService = new PlayChessService();
        initView();
    }

    private void initView() {
        view.findViewById(R.id.play_in_real_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchPlayerLatour.setVisibility(View.GONE);
            }
        });
    }
}
