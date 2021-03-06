package io.github.breadkey.chess.view.chess;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

import io.github.breadkey.chess.ChessPieceImageFactory;
import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.Player;
import io.github.breadkey.chess.model.chess.ChessBoard;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Coordinate;
import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.sign.LogoutCallback;
import io.github.breadkey.chess.model.sign.LogoutManager;
import io.github.breadkey.chess.presenter.ChessPresenter;
import io.github.breadkey.chess.view.sign.LoginActivity;

public class ChessActivity extends AppCompatActivity {
    ChessPresenter presenter;
    GridLayout chessSquareLayout;
    HashMap<Coordinate, SquareLayout> squareLayoutHashMap;
    TableLayout moveTableLayout;
    int moveCount;
    HashMap<Integer, TableRow> moveRowHashMap;
    SoundPool soundPool;
    public final int PIECE_SELECT_SOUND = 0;
    public final int PIECE_CHECK_SOUND = 1;
    public final int PIECE_CHECKMATE_SOUND = 2;
    private int pieceSelectSoundId;
    private int pieceCheckSoundId;
    private int pieceCheckmateSoundId;
    private HashMap<Integer, Integer> soundIdHashMap;

    private TextView whitePlayerNameTextView;
    private ImageView whitePlayerTierIconImageView;
    private TextView blackPlayerNameTextView;
    private ImageView blackPlayerTierIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moveCount = 0;
        moveRowHashMap = new HashMap<>();
        setContentView(R.layout.activity_chess);

        whitePlayerNameTextView = findViewById(R.id.white_player_text);
        whitePlayerTierIconImageView = findViewById(R.id.white_player_tier_icon);
        blackPlayerNameTextView = findViewById(R.id.black_player_text);
        blackPlayerTierIconImageView = findViewById(R.id.black_player_tier_icon);

        soundIdHashMap = new HashMap<>();
        soundPool = new SoundPool(2, AudioManager.STREAM_ALARM, 0);
        pieceSelectSoundId = soundPool.load(this, R.raw.piece_select_sound, 1);
        pieceCheckSoundId = soundPool.load(this, R.raw.piece_check_sound, 1);
        pieceCheckmateSoundId = soundPool.load(this, R.raw.piece_checkmate_sound, 1);
        soundIdHashMap.put(PIECE_SELECT_SOUND, pieceSelectSoundId);
        soundIdHashMap.put(PIECE_CHECK_SOUND, pieceCheckSoundId);
        soundIdHashMap.put(PIECE_CHECKMATE_SOUND, pieceCheckmateSoundId);

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

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutManager.getInstance().logout(new LogoutCallback() {
                    @Override
                    public void onComplete() {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
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

    public void movePiece(Coordinate fromCoordinate, Coordinate toCoordinate) {
        SquareLayout fromSquare = getSquareLayout(fromCoordinate);
        SquareLayout toSquare = getSquareLayout(toCoordinate);

        Drawable pieceBackground = fromSquare.getPieceButton().getBackground();
        fromSquare.getPieceButton().setBackgroundColor(Color.TRANSPARENT);
        toSquare.getPieceButton().setBackgroundDrawable(pieceBackground);
    }

    public void playSound(int sound) {
        int soundId = soundIdHashMap.get(sound);
        float volume = 1f;
        if (soundId == pieceCheckSoundId) { volume = 2f; }
        soundPool.play(soundId, volume, volume, 1, 0, 1f);
    }

    public void setPlayer(PlayChessService.Division division, Player player) {
        if (division == PlayChessService.Division.White) {
            whitePlayerNameTextView.setText(player.nickName);
            whitePlayerTierIconImageView.setImageResource(ChessPieceImageFactory.createPieceImage(ChessPiece.Type.Pawn, PlayChessService.Division.White));
        } else {
            blackPlayerNameTextView.setText(player.nickName);
            blackPlayerTierIconImageView.setImageResource(ChessPieceImageFactory.createPieceImage(ChessPiece.Type.Pawn, PlayChessService.Division.Black));
        }
    }

    public void showPromotion(ChessPiece promotedPiece) {
        SquareLayout square = getSquareLayout(promotedPiece.getCoordinate());
        square.getPieceButton().setBackgroundResource(ChessPieceImageFactory.createPieceImage(promotedPiece.type, promotedPiece.division));
    }

    public void showSelectPromotionType() {
        final ViewGroup main = findViewById(R.id.main);
        LayoutInflater inflater = (LayoutInflater) main.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View selectPromotionTypeView = inflater.inflate(R.layout.select_promotion_type, main, false);
        main.addView(selectPromotionTypeView);
        main.findViewById(R.id.select_queen_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.promote(ChessPiece.Type.Queen);
                main.removeView(selectPromotionTypeView);
            }
        });
        main.findViewById(R.id.select_rook_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.promote(ChessPiece.Type.Rook);
                main.removeView(selectPromotionTypeView);
            }
        });
        main.findViewById(R.id.select_bishop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.promote(ChessPiece.Type.Bishop);
                main.removeView(selectPromotionTypeView);
            }
        });
        main.findViewById(R.id.select_knight_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.promote(ChessPiece.Type.Knight);
                main.removeView(selectPromotionTypeView);
            }
        });
    }
}

