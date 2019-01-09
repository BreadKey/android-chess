package io.github.breadkey.chess.model.chess.chessPieces;

import io.github.breadkey.chess.model.chess.ChessGame;
import io.github.breadkey.chess.model.chess.ChessPiece;

public class King extends ChessPiece {
    private char file;
    private  int rank;
    private boolean isChecked;

    public King(ChessGame.Division division) {
        super(division);
        type = Type.King;
        isChecked = false;
    }

    public void setCoordinate(char file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public char getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
