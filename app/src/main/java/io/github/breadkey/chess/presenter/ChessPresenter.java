package io.github.breadkey.chess.presenter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.breadkey.chess.ChessPieceImageFactory;
import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.PlayChessController;
import io.github.breadkey.chess.model.Player;
import io.github.breadkey.chess.model.chess.ChessBoard;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.ChessRuleManager;
import io.github.breadkey.chess.model.chess.Coordinate;
import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.match.PlayerMatcherFactory;
import io.github.breadkey.chess.view.BakeryInformation;
import io.github.breadkey.chess.view.ChessActivity;
import io.github.breadkey.chess.view.InformationActionListener;
import io.github.breadkey.chess.view.SquareLayout;

public class ChessPresenter extends PlayChessController {
    ChessActivity view;
    View matchPlayerLayout;
    List<Coordinate> coordinatesPieceCanMoveCache;
    ProgressBar whitePlayerTimer;
    ProgressBar blackPlayerTimer;

    public ChessPresenter(ChessActivity view) {
        this.view = view;
        matchPlayerLayout = view.findViewById(R.id.match_player_layout);
        whitePlayerTimer = view.findViewById(R.id.white_player_timer);
        blackPlayerTimer = view.findViewById(R.id.black_player_timer);
        coordinatesPieceCanMoveCache = new ArrayList<>();
        view.findViewById(R.id.play_in_real_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchPlayerLayout.setVisibility(View.GONE);
                startFindEnemy(PlayerMatcherFactory.PlayerMatcherKey.VsInReal);
                startNewGame();
                updateChessBoard();
            }
        });

        view.findViewById(R.id.play_with_cpu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchPlayerLayout.setVisibility(View.GONE);
                startFindEnemy(PlayerMatcherFactory.PlayerMatcherKey.VsCPU);
                startNewGame();
                updateChessBoard();
            }
        });

        view.findViewById(R.id.play_on_line_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchPlayerLayout.setVisibility(View.GONE);
                startFindEnemy(PlayerMatcherFactory.PlayerMatcherKey.VsOnline);
                startNewGame();
                updateChessBoard();
            }
        });
        initView();
    }

    private void initView() {
        whitePlayerTimer.setVisibility(View.INVISIBLE);
        blackPlayerTimer.setVisibility(View.INVISIBLE);
        matchPlayerLayout.setVisibility(View.VISIBLE);
    }

    private void updateChessBoard() {
        view.clearSquares();

        for (ChessPiece whitePiece : getPlayChessService().getPieces(PlayChessService.Division.White)) {
            SquareLayout squareLayout = view.getSquareLayout(whitePiece.getCoordinate());
            squareLayout.getPieceButton().setBackgroundResource(ChessPieceImageFactory.createPieceImage(whitePiece));
        }

        for (ChessPiece blackPiece : getPlayChessService().getPieces(PlayChessService.Division.Black)) {
            SquareLayout squareLayout = view.getSquareLayout(blackPiece.getCoordinate());
            squareLayout.getPieceButton().setBackgroundResource(ChessPieceImageFactory.createPieceImage(blackPiece));
        }
    }

    public void coordinateSelected(Coordinate coordinate) {
        select(coordinate.getFile(), coordinate.getRank());
    }

    @Override
    public void enemyFounded(PlayerMatcherFactory.PlayerMatcherKey key) {
        super.enemyFounded(key);
        updateChessBoard();
    }

    @Override
    public void divisionDecided(Player whitePlayer, Player blackPlayer) {
        TextView whitePlayerNameTextView = view.findViewById(R.id.white_player_text);
        ImageView whitePlayerTierIconImageView = view.findViewById(R.id.white_player_tier_icon);
        whitePlayerNameTextView.setText(whitePlayer.nickName);
        whitePlayerTierIconImageView.setImageResource(ChessPieceImageFactory.createPieceImage(ChessPiece.Type.Pawn, PlayChessService.Division.White));

        TextView blackPlayerNameTextView = view.findViewById(R.id.black_player_text);
        ImageView blackPlayerTierIconImageView = view.findViewById(R.id.black_player_tier_icon);
        blackPlayerNameTextView.setText(blackPlayer.nickName);
        blackPlayerTierIconImageView.setImageResource(ChessPieceImageFactory.createPieceImage(ChessPiece.Type.Pawn, PlayChessService.Division.Black));
    }

    @Override
    public void pieceMoved(Move move) {
        unShowCanMoveCoordinates();
        view.movePiece(move.getFromCoordinate(), move.getToCoordinate());
        int sound = view.PIECE_SELECT_SOUND;

        for (ChessRuleManager.Rule rule : move.getRules()) {
            switch (rule) {
                case KingSideCastling: {
                    char rookFromFile = ChessBoard.files.get(ChessBoard.files.size() - 1);
                    char rookToFile = (char) (move.getToCoordinate().getFile() - 1);
                    int rookRank = move.getToCoordinate().getRank();
                    view.movePiece(new Coordinate(rookFromFile, rookRank), new Coordinate(rookToFile, rookRank));
                    break;
                }
                case QueenSideCastling: {
                    char rookFromFile = ChessBoard.files.get(0);
                    char rookToFile = (char) (move.getToCoordinate().getFile() + 1);
                    int rookRank = move.getToCoordinate().getRank();
                    view.movePiece(new Coordinate(rookFromFile, rookRank), new Coordinate(rookToFile, rookRank));
                    break;
                }
                case Check: {
                    sound = view.PIECE_CHECK_SOUND;
                    break;
                }
                case Checkmate: {
                    sound = view.PIECE_CHECKMATE_SOUND;
                    break;
                }
            }
        }

        view.playSound(sound);
        view.addMoveRow(move);
    }

    @Override
    public void canNotMoveThatCoordinates(char fromFile, char toFile, int fromRank, int toRank) {
        Toast.makeText(view.getApplicationContext(), "그 곳으로 움직일 수 없습니다", Toast.LENGTH_SHORT).show();
        unShowCanMoveCoordinates();
    }

    @Override
    public void moveUndid(Move move) {

    }

    @Override
    public void killHappened(ChessPiece pieceWillMove, ChessPiece pieceWillDead, char toFile, int toRank) {

    }

    @Override
    public void coordinatesPieceCanMoveFounded(List<Coordinate> coordinates) {
        unShowCanMoveCoordinates();
        coordinatesPieceCanMoveCache = coordinates;
        for (Coordinate coordinate : coordinates) {
            ChessPiece enemyPiece = getPlayChessService().getPieceAt(coordinate.getFile(), coordinate.getRank());
            if (enemyPiece != null) {
                view.getSquareLayout(coordinate).setBackgroundColor(Color.argb(127, 255, 0, 0));
            }
            else {
                view.getSquareLayout(coordinate).setBackgroundColor(Color.argb(127, 0, 255, 0));
            }
        }
    }

    @Override
    public void gameEnded(PlayChessService.Division winner) {
        super.gameEnded(winner);
        InformationActionListener listener = new InformationActionListener() {
            @Override
            public void action() {
                initView();
            }
        };

        Player winnerPlayer = getPlayChessService().getCurrentPlayer();
        String titleString = "체크메이트! ";
        String informationString = winnerPlayer.nickName + "의 승리!";
        BakeryInformation.showInformation((ViewGroup) view.findViewById(R.id.main), titleString, informationString,  listener, listener);

        view.clearMoveTable();
    }

    @Override
    public boolean requestPlayerAcceptPlay() {
        return true;
    }

    private void unShowCanMoveCoordinates() {
        for (Coordinate coordinate : coordinatesPieceCanMoveCache) {
            view.getSquareLayout(coordinate).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void turnChanged(PlayChessService.Division turn) {
        if (turn == PlayChessService.Division.White) {
            whitePlayerTimer.setVisibility(View.VISIBLE);
            blackPlayerTimer.setVisibility(View.INVISIBLE);
        }
        else {
            blackPlayerTimer.setVisibility(View.VISIBLE);
            whitePlayerTimer.setVisibility(View.INVISIBLE);
        }
    }
}
