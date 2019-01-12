package io.github.breadkey.chess.model;

import java.util.List;

import io.github.breadkey.chess.model.chess.Move;
import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Coordinate;

public abstract class PlayChessController implements ChessPlayObserver {
    private Coordinate currentSelectedCoordinate;
    private PlayChessService playChessService;

    abstract void divisionDecided(Player whitePlayer, Player blackPlayer);
    abstract void pieceMoveAction(Move move);
    abstract void canNotMoveAction(char fromFile, int fromRank, char toFile, int toRank);
    abstract void undoMoveAction();

    @Override
    public void pieceMoved(char fromFile, int fromRank, char toFile, int toRank, ChessPiece movedPiece) {

    }

    @Override
    public void canNotMoveThatCoordinates(char fromFile, char toFile, int fromRank, int toRank) {
        canNotMoveAction(fromFile, fromRank, toFile, toRank);
    }

    public void select(char file, int rank) {
        if (currentSelectedCoordinate == null) {
            ChessPiece selectedPiece = playChessService.getPieceAt(file, rank);
            if (playChessService.getPieceAt(file, rank) != null) {
                if (selectedPiece.division == playChessService.getCurrentTurn()) {
                    currentSelectedCoordinate = new Coordinate(file, rank);
                }
            }
        }
        else {
            playChessService.tryMove(currentSelectedCoordinate.getFile(), currentSelectedCoordinate.getRank(), file, rank);
            currentSelectedCoordinate = null;
        }
    }

    @Override
    public void killHappened(ChessPiece killer, ChessPiece dead, char atFile, int atRank) {

    }

    public PlayChessService getPlayChessService() {
        return playChessService;
    }
}

