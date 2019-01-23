package io.github.breadkey.chess.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.chess.ChessBoard;
import io.github.breadkey.chess.model.chess.Coordinate;
import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.presenter.ChessPresenter;

public class ChessActivity extends AppCompatActivity {
    ChessPresenter presenter;
    GridLayout chessSquareLayout;
    HashMap<Coordinate, SquareLayout> squareLayoutHashMap;
    TableLayout moveTableLayout;
    int moveCount;
    HashMap<Integer, TableRow> moveRowHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moveCount = 0;
        moveRowHashMap = new HashMap<>();
        startActivity(new Intent(this, LoadingActivity.class));
        setContentView(R.layout.activity_chess);
        chessSquareLayout = findViewById(R.id.chess_square_layout);
        presenter = new ChessPresenter(this);

        moveTableLayout = findViewById(R.id.move_table_layout);

        Button showMoveButton = findViewById(R.id.show_move_button);
        showMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout moveDrawerLayout = findViewById(R.id.move_drawer_layout);
                if (!moveDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    moveDrawerLayout.openDrawer(Gravity.LEFT);
                }
                else {
                    moveDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });
        createSquareButtons();
        drawRanks();
        drawFiles();
    }

    private void drawRanks() {
        LinearLayout rankLinearLayout = findViewById(R.id.rank_linear_layout);
        for (int rank : ChessBoard.ranks) {
            TextView rankTextView = new TextView(this);
            rankTextView.setGravity(Gravity.TOP);
            rankTextView.setText(String.valueOf(rank));
            if (rank % 2 == 0) {
                rankTextView.setTextColor(getResources().getColor(R.color.bread));
            }
            else {
                rankTextView.setTextColor(getResources().getColor(R.color.dough));
            }
            rankLinearLayout.addView(rankTextView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rankTextView.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.weight = 1;
            rankTextView.setLayoutParams(layoutParams);
        }
    }
    private void drawFiles() {
        LinearLayout fileLinearLayout = findViewById(R.id.file_linear_layout);
        for (int i = 0; i < ChessBoard.files.size(); i++) {
            char file = ChessBoard.files.get(i);
            TextView fileTextView = new TextView(this);
            fileTextView.setGravity(Gravity.RIGHT);
            fileTextView.setText(String.valueOf(file));
            if (i % 2 == 0) {
                fileTextView.setTextColor(getResources().getColor(R.color.dough));
            }
            else {
                fileTextView.setTextColor(getResources().getColor(R.color.bread));
            }
            fileLinearLayout.addView(fileTextView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fileTextView.getLayoutParams();
            layoutParams.width = 0;
            layoutParams.weight = 1;
            fileTextView.setLayoutParams(layoutParams);
        }
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

    public void addMoveRow(Move move) {
        moveCount++;
        if (isWhiteTurn()) {
            TableRow moveRow = new TableRow(this);
            int index = moveCount - (moveCount - 1) / 2;
            TextView indexTextView = new TextView(this);
            indexTextView.setText(String.valueOf(index) + ".");
            indexTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            TextView whiteMoveTextView = createMoveTextView(move.toString());

            moveRow.addView(indexTextView);
            moveRow.addView(whiteMoveTextView);

            moveTableLayout.addView(moveRow);
            float dp = getResources().getDisplayMetrics().density;
            int padding = (int) (2 * dp);
            moveRow.setPadding(0, padding, 0, padding);

            moveRowHashMap.put(index, moveRow);
        }
        else {
            TableRow moveRow = moveRowHashMap.get(moveCount / 2);
            TextView blackMoveTextView= createMoveTextView(move.toString());
            moveRow.addView(blackMoveTextView);
        }
    }

    private TextView createMoveTextView(String move) {
        TextView moveTextView = new TextView(this);
        moveTextView.setText(move);
        moveTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        return moveTextView;
    }

    private boolean isWhiteTurn() {
        return moveCount % 2 == 1;
    }

    public void clearMoveTable() {
        moveCount = 0;
        TableRow columnSpacer = (TableRow) moveTableLayout.getChildAt(0);
        moveTableLayout.removeAllViews();
        moveTableLayout.addView(columnSpacer);
    }
}

