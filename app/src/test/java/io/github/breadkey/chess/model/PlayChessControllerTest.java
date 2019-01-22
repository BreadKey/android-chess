package io.github.breadkey.chess.model;

import org.junit.Before;
import org.junit.Test;

import io.github.breadkey.chess.mock.*;
import io.github.breadkey.chess.model.chess.PlayChessService;
import io.github.breadkey.chess.model.match.PlayerMatcherFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlayChessControllerTest {
    PlayChessController playChessController = new MockPlayChessController();
    @Before
    public void setUp() {
        playChessController.setPlayer(new MockPlayerYK());
        playChessController.startFindEnemy(PlayerMatcherFactory.PlayerMatcherKey.VsInReal);
        playChessController.setEnemy(new MockPlayerHJ());
        playChessController.startNewGame();
    }

    @Test
    public void move_a2PawnTo_a4() {
        playChessController.select('a', 2);
        playChessController.select('a', 4);

        assertNull(playChessController.getPlayChessService().getPieceAt('a', 2));
    }

    @Test
    public void tryMoveBlackPieceAtFirst() {
        playChessController.select('b', 7);
        playChessController.select('b', 5);

        assertNull(playChessController.getPlayChessService().getPieceAt('b', 5));
    }

    @Test
    public void kill_b7PawnWith_a2Pawn() {
        playChessController.select('a', 2);
        playChessController.select('a', 4);
        playChessController.select('b', 7);
        playChessController.select('b', 5);
        playChessController.select('a', 4);
        playChessController.select('b', 5);

        assertEquals(PlayChessService.Division.White, playChessController.getPlayChessService().getPieceAt('b', 5).division);
        assertEquals(1, playChessController.getPlayChessService().getPieceAt('b', 5).killScore);
    }

    @Test
    public void move_a2PawnTo_b3WhereCanNotGo() {
        playChessController.select('a', 2);
        playChessController.select('b',3);

        assertNull(playChessController.getPlayChessService().getPieceAt('b', 3));
        assertEquals(0, playChessController.getPlayChessService().getMoves().size());
    }

    @Test
    public void gameAfterGame() {
        playChessController.startNewGame();
        assertEquals(1, playChessController.getPlayChessService().getChessPlayObservers().size());
    }
}