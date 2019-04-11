package io.github.breadkey.chess.model.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.github.breadkey.chess.model.ChessPlayObserver;
import io.github.breadkey.chess.model.Player;
import io.github.breadkey.chess.model.chess.chessPieces.King;
import io.github.breadkey.chess.model.chess.chessPieces.Pawn;

public class PlayChessService {
    private Move newMove;
    private RuleList newRules;
    private KillLog newKillLog;
    private Pawn pawnWillPromote;

    public enum Division {
        Black, White
    }
    private HashMap<PlayChessService.Division, Player> players;

    private ChessBoard chessBoard;

    private Division currentTurn;
    private ChessRuleManager ruleManager = ChessRuleManager.getInstance();
    private List<ChessPlayObserver> chessPlayObservers;

    private List<Move> moves;
    private List<KillLog> killLogs;

    private Division winner;

    public PlayChessService() {
        chessPlayObservers = new ArrayList<>();
    }

    public void startNewGame(Player player1, Player player2) {
        clearChessBoard();
        chessBoard.setChessBoard();
        setCurrentTurn(Division.White);
        decideDivision(player1, player2);
        moves = new ArrayList<>();
        killLogs = new ArrayList<>();
    }

    private void decideDivision(Player player1, Player player2) {
        boolean dice = new Random().nextBoolean();
        if (dice) {
            players = new HashMap<>();
            players.put(PlayChessService.Division.White, player1);
            players.put(PlayChessService.Division.Black, player2);
        }
        else {
            players = new HashMap<>();
            players.put(PlayChessService.Division.White, player2);
            players.put(PlayChessService.Division.Black, player1);
        }

        for (ChessPlayObserver observer: chessPlayObservers) {
            observer.divisionDecided(players.get(Division.White), players.get(Division.Black));
        }
    }

    public ChessPiece getPieceAt(char file, int rank) {
        return chessBoard.getPieceAt(file, rank);
    }

    public void tryMove(char fromFile, int fromRank, char toFile, int toRank) {
        ChessPiece pieceWillMove = getPieceAt(fromFile, fromRank);
        if (pieceWillMove != null) {
            List coordinatesCanMove = ruleManager.findSquareCoordinateCanMove(chessBoard, fromFile, fromRank);
            coordinatesCanMove = ruleManager.filterKingCanDead(chessBoard, coordinatesCanMove, pieceWillMove);
            if (pieceWillMove.type == ChessPiece.Type.King) {
                if (!coordinatesCanMove.contains(new Coordinate((char) (pieceWillMove.getFile() + 1), pieceWillMove.getRank()))) {
                    coordinatesCanMove.remove(new Coordinate((char) (pieceWillMove.getFile() + 2), pieceWillMove.getRank()));
                }
                if (!coordinatesCanMove.contains(new Coordinate((char) (pieceWillMove.getFile() - 1), pieceWillMove.getRank()))) {
                    coordinatesCanMove.remove(new Coordinate((char) (pieceWillMove.getFile() - 2), pieceWillMove.getRank()));
                }
            }

            if (coordinatesCanMove.contains(new Coordinate(toFile, toRank))) {
                move(pieceWillMove, toFile, toRank);
                return;
            }
        }

        for (ChessPlayObserver chessPlayObserver : chessPlayObservers) {
            chessPlayObserver.canNotMoveThatCoordinates(fromFile, toFile, fromRank, toRank);
        }
    }

    private void move(ChessPiece pieceToMove, char toFile, int toRank) {
        King king = chessBoard.getKing(pieceToMove.division);
        king.setChecked(false);

        ChessPiece pieceWillDead = getPieceAt(toFile, toRank);
        if (pieceWillDead != null) {
            pieceToMove.killScore++;
            chessBoard.getPieces(pieceWillDead.division).remove(pieceWillDead);
            newKillLog = new KillLog(pieceToMove, pieceWillDead,toFile, toRank);
            killLogs.add(newKillLog);

            for (ChessPlayObserver chessPlayObserver : chessPlayObservers) {
                chessPlayObserver.killHappened(pieceToMove, pieceWillDead, toFile, toRank);
            }
        }

        char fromFile = pieceToMove.getFile();
        int fromRank = pieceToMove.getRank();

        chessBoard.placePiece(toFile, toRank, pieceToMove);
        chessBoard.placePiece(fromFile, fromRank, null);
        pieceToMove.moveCount++;

        newMove = new Move(pieceToMove.division, pieceToMove.type, new Coordinate(fromFile, fromRank), new Coordinate(toFile, toRank));
        newRules = new RuleList();
        ChessRuleManager.Rule castling = ruleManager.findCastling(chessBoard, newMove);
        newRules.add(castling);
        if (castling == ChessRuleManager.Rule.KingSideCastling) {
            char kingSideRookFile = ChessBoard.files.get(ChessBoard.files.size() - 1);
            ChessPiece kingSideRook = getPieceAt(kingSideRookFile, toRank);
            chessBoard.placePiece((char) (toFile - 1), toRank, kingSideRook);
            chessBoard.placePiece(kingSideRookFile, toRank, null);

        }
        else if (castling == ChessRuleManager.Rule.QueenSideCastling) {
            char queenSideRookFile = ChessBoard.files.get(0);
            ChessPiece queenSideRook = getPieceAt(queenSideRookFile, toRank);
            chessBoard.placePiece((char) (toFile + 1), toRank, queenSideRook);
            chessBoard.placePiece(queenSideRookFile, toRank, null);
        }

        ChessRuleManager.Rule pawnRule = ruleManager.findPawnRule(chessBoard, newMove);

        if (pawnRule == ChessRuleManager.Rule.Promotion) {
            newRules.add(pawnRule);
            for (ChessPlayObserver observer : chessPlayObservers) {
                observer.selectTypeToPromotion();
            }
            pawnWillPromote = (Pawn) pieceToMove;

            /*
            newMove.setPromotedType(promoteTo);
            chessBoard.promote((Pawn) pieceToMove, promoteTo);

            if (killLog != null) {
                killLog.killer = chessBoard.getPieceAt(toFile, toRank);
            }
            */
            return;
        }

       endMove(pieceToMove);
    }

    public void promote(ChessPiece.Type promoteTo) {
        if (pawnWillPromote == null) {
            return;
        }

        newMove.setPromotedType(promoteTo);
        chessBoard.promote(pawnWillPromote, promoteTo);

        if (newKillLog != null) {
            newKillLog.killer = chessBoard.getPieceAt(pawnWillPromote.getFile(), pawnWillPromote.getRank());
        }

        endMove(pawnWillPromote);
    }

    private void endMove(ChessPiece pieceToMove) {
        ChessRuleManager.Rule check = ruleManager.findCheck(chessBoard, newMove);
        newRules.add(check);

        newMove.setRules(newRules);
        moves.add(newMove);

        for (ChessPlayObserver chessPlayObserver : chessPlayObservers) {
            chessPlayObserver.pieceMoved(newMove);
        }

        Division enemyDivision = pieceToMove.division == Division.White? Division.Black : Division.White;

        if (check == ChessRuleManager.Rule.Check) {
            King enemyKing = chessBoard.getKing(enemyDivision);
            enemyKing.setChecked(true);
        }
        else if (check == ChessRuleManager.Rule.Checkmate) {
            endGame(pieceToMove.division);
        }

        newMove = null;
        newRules = null;
        newKillLog = null;
        pawnWillPromote = null;
        changeTurn();
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    private void changeTurn() {
        if (currentTurn == Division.Black) {
            setCurrentTurn(Division.White);
        }
        else {
            setCurrentTurn(Division.Black);
        }
    }

    public void undoMoves(PlayChessService.Division requesterDivision) {
        List<Move> reversedMoves = new ArrayList<>(moves);
        Collections.reverse(reversedMoves);
        int lastMoveIndex = 0;
        for (Move lastMove: reversedMoves) {
            if (lastMove.getDivision() == requesterDivision) {
                lastMoveIndex = reversedMoves.indexOf(lastMove);
                break;
            }
        }

        List<Move> movesHaveToUndo = reversedMoves.subList(0, lastMoveIndex + 1);
        for (Move moveHaveToUndo: movesHaveToUndo) {
            undoMove(moveHaveToUndo);
        }
    }

    public void undoMove(Move moveHaveToUndo) {
        Coordinate currentCoordinate = moveHaveToUndo.getToCoordinate();
        Coordinate previousCoordinate = moveHaveToUndo.getFromCoordinate();
        ChessPiece piece = getPieceAt(currentCoordinate.getFile(), currentCoordinate.getRank());

        getChessBoard().placePiece(previousCoordinate.getFile(), previousCoordinate.getRank(), piece);
        getChessBoard().placePiece(currentCoordinate.getFile(), currentCoordinate.getRank(), null);

        piece.moveCount--;

        List<KillLog> reversedKillLogs = new ArrayList<>(killLogs);
        Collections.reverse(reversedKillLogs);
        for (KillLog killLog : reversedKillLogs) {
            if (isKiller(piece, killLog, currentCoordinate)) {
                piece.killScore--;
                placeNewPiece(currentCoordinate.getFile(), currentCoordinate.getRank(), killLog.dead);
                killLogs.remove(killLog);
                break;
            }
        }

        for (ChessRuleManager.Rule rule : moveHaveToUndo.getRules()) {
            if (rule == ChessRuleManager.Rule.Promotion) {
                chessBoard.demote(piece);
            }
        }

        moves.remove(moveHaveToUndo);
        setCurrentTurn(moveHaveToUndo.getDivision());
    }

    private boolean isKiller(ChessPiece piece, KillLog killLog, Coordinate atCoordinate) {
        if (killLog.killer == piece && killLog.atFile == atCoordinate.getFile() && killLog.atRank == atCoordinate.getRank()) {
            return true;
        }
        return false;
    }

    public Division getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Division turn) {
        currentTurn = turn;
        for (ChessPlayObserver observer : chessPlayObservers) {
            observer.turnChanged(turn);
        }
    }

    public void attachGameObserver(ChessPlayObserver gameObserver) {
        if (!chessPlayObservers.contains(gameObserver)) {
            chessPlayObservers.add(gameObserver);
        }
    }

    public void detachGameObserver(ChessPlayObserver gameObserver) {
        chessPlayObservers.remove(gameObserver);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(chessBoard);
        stringBuilder.append("\n현재 차례: " + currentTurn);

        return stringBuilder.toString();
    }

    public void clearChessBoard() {
        chessBoard = new ChessBoard();
    }

    public void placeNewPiece(char file, int rank, ChessPiece piece) {
        chessBoard.placeNewPiece(file, rank, piece);
    }

    public List<ChessPiece> getPieces(Division division) {
        return chessBoard.getPieces(division);
    }

    public King getKing(Division division) {
        return chessBoard.getKing(division);
    }

    private void endGame(Division winner) {
        this.winner = winner;
        for (ChessPlayObserver observer : chessPlayObservers) {
            observer.gameEnded(winner);
        }
    }

    public Division getWinner() {
        return winner;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Player getCurrentPlayer() {
        return players.get(currentTurn);
    }

    public List<ChessPlayObserver> getChessPlayObservers() {
        return chessPlayObservers;
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
