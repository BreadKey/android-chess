package io.github.breadkey.chess.model.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.github.breadkey.chess.model.ChessPlayObserver;
import io.github.breadkey.chess.model.Player;
import io.github.breadkey.chess.model.chess.chessPieces.Bishop;
import io.github.breadkey.chess.model.chess.chessPieces.King;
import io.github.breadkey.chess.model.chess.chessPieces.Knight;
import io.github.breadkey.chess.model.chess.chessPieces.Pawn;
import io.github.breadkey.chess.model.chess.chessPieces.Queen;
import io.github.breadkey.chess.model.chess.chessPieces.Rook;

public class PlayChessService {
    public enum Division {
        Black, White
    }

    private HashMap<PlayChessService.Division, Player> players;

    private ChessBoard chessBoard;
    private HashMap<Division, King> kingHashMap;
    private HashMap<Division, List<ChessPiece>> piecesHashMap;

    private Division currentTurn;
    private ChessRuleManager ruleManager = ChessRuleManager.getInstance();
    private List<ChessPlayObserver> chessPlayObservers;

    private List<Move> moves;
    private List<KillLog> killLogs;

    private Division winner;

    public void startNewGame(Player player1, Player player2) {
        chessBoard = new ChessBoard();
        chessPlayObservers = new ArrayList<>();
        kingHashMap = new HashMap<>();
        piecesHashMap = new HashMap<>();
        piecesHashMap.put(Division.White, new ArrayList<ChessPiece>(16));
        piecesHashMap.put(Division.Black, new ArrayList<ChessPiece>(16));

        setChessBoard();
        currentTurn = Division.White;
        decideDivision(player1, player2);
        moves = new ArrayList<>();
        killLogs = new ArrayList<>();
    }

    private void decideDivision(Player player1, Player player2) {
        int dice = new Random().nextInt(2);
        if (dice == 0) {
            players = new HashMap<>();
            players.put(PlayChessService.Division.White, player1);
            players.put(PlayChessService.Division.Black, player2);
        }
        else {
            players = new HashMap<>();
            players.put(PlayChessService.Division.White, player2);
            players.put(PlayChessService.Division.Black, player1);
        }
    }

    private void setChessBoard() {
        placePawns();
        placeRooks();
        placeKnights();
        placeBishops();
        placeQueens();
        placeKings();
    }

    private void placePawns() {
        for (char file: ChessBoard.files) {
            placeNewPiece(file, 2, new Pawn(Division.White));
            placeNewPiece(file, 7, new Pawn(Division.Black));
        }
    }

    private void placeRooks() {
        placeNewPiece('a', 1, new Rook(Division.White));
        placeNewPiece('h', 1, new Rook(Division.White));
        placeNewPiece('a', 8, new Rook(Division.Black));
        placeNewPiece('h', 8, new Rook(Division.Black));
    }

    private void placeKnights() {
        placeNewPiece('b', 1, new Knight(Division.White));
        placeNewPiece('g', 1, new Knight(Division.White));
        placeNewPiece('b', 8, new Knight(Division.Black));
        placeNewPiece('g', 8, new Knight(Division.Black));
    }

    private void placeBishops() {
        placeNewPiece('c', 1, new Bishop(Division.White));
        placeNewPiece('f', 1, new Bishop(Division.White));
        placeNewPiece('c', 8, new Bishop(Division.Black));
        placeNewPiece('f', 8, new Bishop(Division.Black));
    }

    private void placeQueens() {
        placeNewPiece('d', 1, new Queen(Division.White));
        placeNewPiece('d', 8, new Queen(Division.Black));
    }

    private void placeKings() {
        placeNewPiece('e', 1, new King(Division.White));
        placeNewPiece('e', 8, new King(Division.Black));
    }

    public ChessPiece getPieceAt(char file, int rank) {
        return chessBoard.getPieceAt(file, rank);
    }

    public void tryMove(char fromFile, int fromRank, char toFile, int toRank) {
        ChessPiece pieceWillMove = getPieceAt(fromFile, fromRank);
        if (pieceWillMove != null) {
            List coordinatesCanMove = ruleManager.findSquareCoordinateCanMove(chessBoard, fromFile, fromRank);
            coordinatesCanMove = filterKingCanDead(coordinatesCanMove, pieceWillMove);

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
        King king = kingHashMap.get(pieceToMove.division);
        king.setChecked(false);

        ChessPiece pieceWillDead = getPieceAt(toFile, toRank);
        if (pieceWillDead != null) {
            pieceToMove.killScore++;
            piecesHashMap.get(pieceWillDead.division).remove(pieceWillDead);
            killLogs.add(new KillLog(pieceToMove, pieceWillDead, toFile, toRank));

            for (ChessPlayObserver chessPlayObserver : chessPlayObservers) {
                chessPlayObserver.killHappened(pieceToMove, pieceWillDead, toFile, toRank);
            }
        }

        char fromFile = pieceToMove.getFile();
        int fromRank = pieceToMove.getRank();

        chessBoard.placePiece(toFile, toRank, pieceToMove);
        chessBoard.placePiece(fromFile, fromRank, null);
        pieceToMove.moveCount++;

        changeTurn();
        Move newMove = new Move(pieceToMove.division, pieceToMove.type, new Coordinate(fromFile, fromRank), new Coordinate(toFile, toRank));

        Division enemyDivision = pieceToMove.division == Division.White? Division.Black : Division.White;

        if (isCheckmate(enemyDivision)) {
            endGame(pieceToMove.division);
        }

        else if (isCheck(toFile, toRank, enemyDivision)) {

        }

        moves.add(newMove);

        for (ChessPlayObserver chessPlayObserver : chessPlayObservers) {
            chessPlayObserver.pieceMoved(newMove);
        }
    }

    private List filterKingCanDead(List<Coordinate> coordinatesCanMove, ChessPiece pieceWillMove) {
        List<Coordinate> filteredCoordinates = new ArrayList<>(coordinatesCanMove);

        King king = kingHashMap.get(pieceWillMove.division);

        char currentPieceFile = pieceWillMove.getFile();
        int currentPieceRank = pieceWillMove.getRank();
        Division enemyDivision = pieceWillMove.division == Division.White? Division.Black : Division.White;

        chessBoard.placePiece(currentPieceFile, currentPieceRank, null);
        for (Coordinate coordinate : coordinatesCanMove) {
            ChessPiece pieceAlreadyPlace = getPieceAt(coordinate.getFile(), coordinate.getRank());
            chessBoard.placePiece(coordinate.getFile(), coordinate.getRank(), pieceWillMove);
            if (arePiecesCanMove(enemyDivision, king.getCoordinate())) {
                filteredCoordinates.remove(coordinate);
            }
            chessBoard.placePiece(coordinate.getFile(), coordinate.getRank(), pieceAlreadyPlace);
        }
        chessBoard.placePiece(currentPieceFile, currentPieceRank, pieceWillMove);

        return filteredCoordinates;
    }

    boolean arePiecesCanMove(Division division, Coordinate coordinate) {
        List<ChessPiece> pieces = getPieces(division);
        for (ChessPiece piece : pieces) {
            List<Coordinate> coordinatesCanMove = ruleManager.findSquareCoordinateCanMove(chessBoard, piece.getFile(), piece.getRank());
            if (coordinatesCanMove.contains(coordinate)) {
                return true;
            }
        }

        return false;
    }


    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    private void changeTurn() {
        if (currentTurn == Division.Black) {
            currentTurn = Division.White;
        }
        else {
            currentTurn = Division.Black;
        }
    }

    private boolean isCheck(char movedFile, int movedRank, Division enemyDivision) {
        List<Coordinate> coordinatesCanMoveNextTurn = ruleManager.findSquareCoordinateCanMove(chessBoard, movedFile, movedRank);
        King enemyKing = kingHashMap.get(enemyDivision);
        if (coordinatesCanMoveNextTurn.contains(enemyKing.getCoordinate())) {
            enemyKing.setChecked(true);
            return true;
        }

        return false;
    }

    private boolean isCheckmate(Division enemyDivision) {
        List<Coordinate> coordinatesEnemyCanMove = new ArrayList<>();

        for (ChessPiece enemyPiece : getPieces(enemyDivision)) {
            List<Coordinate> enemyPieceCanMoveCoordinates = ruleManager.findSquareCoordinateCanMove(chessBoard, enemyPiece.getFile(), enemyPiece.getRank());
            for (Coordinate coordinate : coordinatesEnemyCanMove) {
                if (enemyPieceCanMoveCoordinates.contains(coordinate)) {
                    enemyPieceCanMoveCoordinates.remove(coordinate);
                }
            }
            coordinatesEnemyCanMove.addAll(filterKingCanDead(enemyPieceCanMoveCoordinates,  enemyPiece));
        }

        return coordinatesEnemyCanMove.size() == 0;
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

    public void setCurrentTurn(Division division) {
        currentTurn = division;
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
        kingHashMap.clear();
        piecesHashMap.get(Division.White).clear();
        piecesHashMap.get(Division.Black).clear();
    }

    public void placeNewPiece(char file, int rank, ChessPiece piece) {
        if (piece.type == ChessPiece.Type.King) {
            kingHashMap.put(piece.division, (King) piece);
        }

        Division pieceDivision = piece.division;
        piecesHashMap.get(pieceDivision).add(piece);

        chessBoard.placePiece(file, rank, piece);
    }

    public List<ChessPiece> getPieces(Division division) {
        return piecesHashMap.get(division);
    }

    public King getKing(Division division) {
        return kingHashMap.get(division);
    }

    private void endGame(Division winner) {
        this.winner = winner;
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
