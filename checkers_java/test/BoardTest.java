import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

//    private Board b;

    @Before
    public void setUp(){
        Board.testMove();
    }

    @org.junit.Test
    public void isNull() {
        Assertions.assertTrue(Board.isNull(0, 0), "board is null");
    }

    @org.junit.Test
    public void isNotNull() {
        Assertions.assertTrue(Board.isNotNull(0, 3));
    }

    @org.junit.Test
    public void isRed() {
        Assertions.assertTrue(Board.isRed(0, 5));
        assertFalse(Board.isRed(0, 0));
    }

    @org.junit.Test
    public void isWhite() {
        Assertions.assertTrue(Board.isWhite(0, 3));
        assertFalse(Board.isWhite(0, 0));
    }

    @org.junit.Test
    public void isKing() {
        Assertions.assertTrue(Board.isKing(0, 3));
        assertFalse(Board.isKing(0,0));
    }

    @org.junit.Test
    public void check() {
        Board.boardClear();
        assertTrue(Board.gameOver());
        Board.testMove();
        List<Integer> jump = List.of(0,5,2,7,0);
        List<Integer> jumpWrong = List.of(0,5,2,3,0);
        List<Integer> move = List.of(6,1,7,0,0);
        List<Integer> moveWrong = List.of(6,1,7,3,0);
        Assertions.assertTrue(Board.check(jump));
        assertFalse(Board.check(moveWrong));
        assertFalse(Board.check(jumpWrong));
        assertFalse(Board.check(move));
        Board.execute(jump);
        assertFalse(Board.checkMultiJump(2,7));
        assertFalse(Board.ifMultipleJump());
    }

    @Test
    public void executeJump(){
        List<Integer> jump = List.of(0,3,2,1,1);
        Board.execute(jump);
        Assertions.assertTrue(Board.isNull(0,3));
        Assertions.assertTrue(Board.isNull(1,2));
        Assertions.assertTrue(Board.isWhite(2,1));
        Assertions.assertTrue(Board.checkMultiJump(2,1));
        Assertions.assertTrue(Board.ifMultipleJump());
        assertFalse(Board.gameOver());
    }

    @org.junit.Test
    public void execute() {
        List<Integer> move = List.of(6,1,7,0,0);

        Board.execute(move);
        Assertions.assertTrue(Board.isRed(7,0));
        Assertions.assertTrue(Board.isNull(6,1));

        Board.initBoard();
        List<Integer> move1 = List.of(5,6,4,7,1);
        List<Integer> move2 = List.of(5,6,6,7,1);
        Assertions.assertTrue(Board.check(move1));
        assertFalse(Board.check(move2));
    }

    @org.junit.Test
    public void message() {
        assertEquals("You can only move the same piece during a multiple jump", Board.message(7));
        assertEquals("Please continue to jump this piece", Board.message(8));
        assertEquals("error", Board.message(99));
    }

    @org.junit.Test
    public void ifMultipleJump() {
    }

}
