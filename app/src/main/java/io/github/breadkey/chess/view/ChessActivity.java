package io.github.breadkey.chess.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import java.util.HashMap;

import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.chess.ChessBoard;
import io.github.breadkey.chess.model.chess.Coordinate;
import io.github.breadkey.chess.presenter.ChessPresenter;

public class ChessActivity extends AppCompatActivity {
    ChessPresenter presenter;
    GridLayout chessSquareLayout;
    HashMap<Coordinate, SquareLayout> squareLayoutHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LoadingActivity.class));
        setContentView(R.layout.activity_chess);
        chessSquareLayout = findViewById(R.id.chess_square_layout);
        presenter = new ChessPresenter(this);
        createSquareButtons();
    }

    private void createSquareButtons() {
        squareLayoutHashMap = new HashMap<>();

        for(int rank: ChessBoard.ranks) {
            for(char file: ChessBoard.files) {
                SquareLayout squareLayout = new SquareLayout(this);
                squareLayout.setBackgroundColor(Color.TRANSPARENT);
                final Coordinate coordinate = new Coordinate(file, rank);
                squareLayoutHashMap.put(coordinate, squareLayout);
                squareLayout.getPieceButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.coordinateSelected(coordinate);
                    }
                });
                chessSquareLayout.addView(squareLayout);
            }
        }
    }

    public SquareLayout getSquareLayout(Coordinate coordinate) {
        return squareLayoutHashMap.get(coordinate);
    }

    public void clearSquares() {
        for (SquareLayout squareLayout: squareLayoutHashMap.values()) {
            squareLayout.getPieceButton().setBackgroundColor(Color.TRANSPARENT);
        }
    }
}

