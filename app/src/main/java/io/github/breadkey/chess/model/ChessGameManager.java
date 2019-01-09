package io.github.breadkey.chess.model;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<KillLog> killLogs;
    private Coordinate currentSelectedCoordinate;

    public void startNewChess(Player player1, Player player2) {
        chessGame = new ChessGame();
        chessGame.attachGameObserver(this);
        decideDivision(player1, player2);
        moves = new ArrayList<>();
        killLogs = new ArrayList<>();
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

    public void undoMove() {
        Move lastMove = moves.remove(moves.size() - 1);
        Coordinate currentCoordinate = lastMove.getToCoordinate();
        Coordinate previousCoordinate = lastMove.getFromCoordinate();
        ChessPiece piece = chessGame.getPieceAt(currentCoordinate.getFile(), currentCoordinate.getRank());

        chessGame.getChessBoard().placePiece(previousCoordinate.getFile(), previousCoordinate.getRank(), piece);
        chessGame.getChessBoard().placePiece(currentCoordinate.getFile(), currentCoordinate.getRank(), null);

        List<KillLog> reversedKillLogs = new ArrayList<>(killLogs);
        Collections.reverse(reversedKillLogs);
        for (KillLog killLog : reversedKillLogs) {
            if (isKiller(piece, killLog, currentCoordinate)) {
                piece.killScore--;
                chessGame.placeNewPiece(currentCoordinate.getFile(), currentCoordinate.getRank(), killLog.dead);
                killLogs.remove(killLog);
                break;
            }
        }

        undoMoveAction();
    }

    private boolean isKiller(ChessPiece piece, KillLog killLog, Coordinate atCoordinate) {
        if (killLog.killer == piece && killLog.atFile == atCoordinate.getFile() && killLog.atRank == atCoordinate.getRank()) {
            return true;
        }
        return false;
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
            chessGame.tryMove(currentSelectedCoordinate.getFile(), currentSelectedCoordinate.getRank(), file, rank);
            currentSelectedCoordinate = null;
        }
    }

    public List<Move> getMoves() {
        return moves;
    }

    @Override
    public void killHappened(ChessPiece killer, ChessPiece dead, char atFile, int atRank) {
        killLogs.add(new KillLog(killer, dead, atFile, atRank));
    }
}

class KillLog {
    ChessPiece killer;
    ChessPiece dead;
    char atFile;
    int atRank;

    KillLog(ChessPiece killer, ChessPiece dead, char atFile, int atRank) {
        this.killer = killer;
        this.dead = dead;
        this.atFile = atFile;
        this.atRank = atRank;
    }
}