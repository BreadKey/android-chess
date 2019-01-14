package io.github.breadkey.chess.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;

import java.util.HashMap;

import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.chess.ChessBoard;
import io.github.breadkey.chess.model.chess.Coordinate;
import io.github.breadkey.chess.presenter.ChessPresenter;

public class ChessActivity extends AppCompatActivity {
    ChessPresenter presenter;
    GridLayout chessSquareLayout;
    HashMap<Coordinate, SquareLayout> squareButtonHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);
        chessSquareLayout = findViewById(R.id.chess_square_layout);
        createSquareButtons();
        presenter = new ChessPresenter(this);
    }

    private void createSquareButtons() {
        squareButtonHashMap = new HashMap<>();

        for(int rank: ChessBoard.ranks) {
            for(char file: ChessBoard.files) {
                SquareLayout squareLayout = new SquareLayout(this, file, rank);
                squareLayout.setBackgroundColor(Color.TRANSPARENT);

                squareButtonHashMap.put(new Coordinate(file, rank), squareLayout);
                chessSquareLayout.addView(squareLayout);
            }
        }
    }

    public SquareLayout getSquareLayout(Coordinate coordinate) {
        return squareButtonHashMap.get(coordinate);
    }
}

