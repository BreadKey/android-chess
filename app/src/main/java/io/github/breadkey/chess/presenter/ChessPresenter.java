package io.github.breadkey.chess.presenter;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.GridLayout;

import io.github.breadkey.chess.ChessPieceImageFactory;
import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.chess.ChessBoard;
import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.view.ChessActivity;

public class ChessPresenter {
    ChessActivity view;
    ChessGame chessGame;

    public ChessPresenter(ChessActivity view) {
        this.view = view;
        chessGame = new ChessGame();
        createSquareButtons();
    }

    private void createSquareButtons() {
        GridLayout chessSquareLayout = view.findViewById(R.id.chess_square_layout);
        for(int rank: ChessBoard.ranks) {
            for(char file: ChessBoard.files) {
                ChessPiece piece = chessGame.getPieceAt(file, rank);
                SquareButton squareButton = new SquareButton(view, file, rank);
                if (piece != null) {
                    squareButton.setBackgroundResource(ChessPieceImageFactory.createPieceImage(piece));
                }
                else {
                    squareButton.setBackgroundColor(Color.TRANSPARENT);
                }

                chessSquareLayout.addView(squareButton);
            }
        }
    }
}

class SquareButton extends Button {
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
