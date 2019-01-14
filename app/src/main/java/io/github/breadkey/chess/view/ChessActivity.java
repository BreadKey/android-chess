package io.github.breadkey.chess.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import io.github.breadkey.chess.ChessPieceImageFactory;
import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.chess.ChessBoard;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.presenter.ChessPresenter;

public class ChessActivity extends AppCompatActivity {
    ChessPresenter presenter;
    GridLayout chessSquareLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);
        chessSquareLayout = findViewById(R.id.chess_square_layout);
        createSquareButtons();
        presenter = new ChessPresenter(this);
    }

    private void createSquareButtons() {
        for(int rank: ChessBoard.ranks) {
            for(char file: ChessBoard.files) {
                SquareButton squareButton = new SquareButton(this, file, rank);
                squareButton.setBackgroundColor(Color.TRANSPARENT);

                chessSquareLayout.addView(squareButton);
            }
        }
    }
}

class SquareButton extends AppCompatButton {
    char file;
    int rank;
    public SquareButton(Context context, char file, int rank) {
        super(context);
        this.file = file;
        this.rank = rank;
        float dp = context.getResources().getDisplayMetrics().density;
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = (int) (40 * dp);
        layoutParams.height = (int) (40 * dp);
        setLayoutParams(layoutParams);
    }
}
