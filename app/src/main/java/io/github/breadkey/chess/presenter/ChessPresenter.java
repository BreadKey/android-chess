package io.github.breadkey.chess.presenter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.breadkey.chess.ChessPieceImageFactory;
import io.github.breadkey.chess.R;
import io.github.breadkey.chess.model.PlayChessController;
import io.github.breadkey.chess.model.Player;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Coordinate;
import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.view.ChessActivity;
import io.github.breadkey.chess.view.SquareLayout;

public class ChessPresenter extends PlayChessController {
    ChessActivity view;
    View matchPlayerLayout;
    List<Coordinate> coordinatesPieceCanMoveCache;

    public ChessPresenter(ChessActivity view) {
        this.view = view;
        matchPlayerLayout = view.findViewById(R.id.match_player_layout);
        coordinatesPieceCanMoveCache = new ArrayList<>();
        initView();
    }

    private void initView() {
        view.findViewById(R.id.play_in_real_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchPlayerLayout.setVisibility(View.GONE);
                playChessGameInReal();
            }
        });
    }

    private void playChessGameInReal() {
        setPlayer(new Player("Player1"));
        setEnemy(new Player("Player2"));
        startNewGame();
        updateChessBoard();
    }

    private void updateChessBoard() {
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
    public void findEnemy() {

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

        SquareLayout fromSquare = view.getSquareLayout(move.getFromCoordinate());
        SquareLayout toSquare = view.getSquareLayout(move.getToCoordinate());

        Drawable pieceBackground = fromSquare.getPieceButton().getBackground();
        fromSquare.getPieceButton().setBackgroundColor(Color.TRANSPARENT);
        toSquare.getPieceButton().setBackgroundDrawable(pieceBackground);
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

    private void unShowCanMoveCoordinates() {
        for (Coordinate coordinate : coordinatesPieceCanMoveCache) {
            view.getSquareLayout(coordinate).setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
