package io.github.breadkey.chess.model.chess;

import android.os.Build;
import android.support.annotation.RequiresApi;

public class Coordinate implements Comparable<Coordinate> {
    private char file;
    private int rank;

    public Coordinate(char file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public char getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return getFile() + String.valueOf(getRank());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(Coordinate o) {
        char thisFile = this.file;
        int thisRank = this.rank;
        char otherFile = o.file;
        int otherRank = o.rank;

        if (thisFile > otherFile) {
            return 1;
        }
        else if (thisFile < otherFile) {
            return  - 1;
        }
        else {
            return Integer.compare(thisRank, otherRank);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj == this) {
            return true;
        }
        Coordinate other = (Coordinate) obj;
        return file == other.file && rank == other.rank;
    }
}
