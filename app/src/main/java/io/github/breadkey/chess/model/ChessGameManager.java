package io.github.breadkey.chess.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;
import io.github.breadkey.chess.model.chess.Coordinate;

public abstract class ChessGameManager implements ChessGameObserver {
    private ChessGame chessGame;
    private HashMap<ChessGame.Division, Player> players;
    private int moveCount;
    private List<Move> moves;
    private Coordinate currentSelectedCoordinate;

    public void startNewChess(Player player1, Player player2) {
        chessGame = new ChessGame();
        chessGame.attachGameObserver(this);
        decideDivision(player1, player2);
        moves = new ArrayList<>();
    }

    private void decideDivision(Player player1, Player player2) {
        int dice = new Random().nextInt(2);
        if (dice == 0) {
            players = new HashMap<>();
            players.put(ChessGame.Division.White, player1);
            players.put(ChessGame.Division.Black, player2);
        }
        else {
            players = new HashMap<>();
            players.put(ChessGame.Division.White, player2);
            players.put(ChessGame.Division.Black, player1);
        }
        divisionDecided(players.get(ChessGame.Division.White), players.get(ChessGame.Division.Black));
    }

    public void undoMove(ChessGame.Division divisionOfRequester) {
        for (int moveIndex = moves.size() - 1; moveIndex <= 0; moveIndex--) {
            ChessGame.Division divisionOfMove = moves.get(moveIndex).getDivision();
            if (divisionOfMove == divisionOfRequester) {
                moves = moves.subList(0, moveIndex - 1);
                undoMoveAction();
                break;
            }
        }
    }

    abstract void divisionDecided(Player whitePlayer, Player blackPlayer);
    abstract void pieceMoveAction(Move move);
    abstract void canNotMoveAction(char fromFile, int fromRank, char toFile, int toRank);
    abstract void undoMoveAction();

    @Override
    public void pieceMoved(char fromFile, int fromRank, char toFile, int toRank, ChessPiece movedPiece) {
        Move newMove = new Move(movedPiece.division, movedPiece.type, new Coordinate(fromFile, fromRank), new Coordinate(toFile, toRank));
        moves.add(newMove);
        pieceMoveAction(newMove);
    }

    @Override
    public void canNotMoveThatCoordinates(char fromFile, char toFile, int fromRank, int toRank) {
        canNotMoveAction(fromFile, fromRank, toFile, toRank);
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public Player getCurrentPlayer() {
        return players.get(chessGame.getCurrentTurn());
    }

    public void select(char file, int rank) {
        if (currentSelectedCoordinate == null) {
            ChessPiece selectedPiece = chessGame.getPieceAt(file, rank);
            if (getChessGame().getPieceAt(file, rank) != null) {
                if (selectedPiece.division == chessGame.getCurrentTurn()) {
                    currentSelectedCoordinate = new Coordinate(file, rank);
                }
            }
        }
        else {
            chessGame.move(currentSelectedCoordinate.getFile(), currentSelectedCoordinate.getRank(), file, rank);
            currentSelectedCoordinate = null;
        }
    }

    public List<Move> getMoves() {
        return moves;
    }
}
