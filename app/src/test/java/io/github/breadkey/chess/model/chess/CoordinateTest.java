package io.github.breadkey.chess.model.chess;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinateTest {
    Coordinate coordinate;
    @Before
    public void setUp() {
        coordinate = new Coordinate('a', 1);
    }

    @Test
    public void string() {
        assertEquals("a1", coordinate.toString());
    }

    @Test
    public void compareRightFile() {
        assertEquals(-1, coordinate.compareTo(new Coordinate('b', 1)));
    }

    @Test
    public void compareLeftFile() {
        Coordinate rightFile = new Coordinate('b', 1);
        assertEquals(1, rightFile.compareTo(coordinate));
    }

    @Test
    public void compareRank() {
        Coordinate upperRankCoordinate = new Coordinate('a', 2);
        assertEquals(1, upperRankCoordinate.compareTo(coordinate));
    }

    @Test
    public void equals() {
        assertEquals(new Coordinate('a', 1), coordinate);
    }

    @Test
    public void notEqualsNull() {
        assertFalse(coordinate.equals(null));
    }
}