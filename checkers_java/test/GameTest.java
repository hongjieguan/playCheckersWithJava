import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testSwitchSide() throws Exception {
        Game.switchSide();
        assertEquals(1, Game.getPlayer());
    }

    @Test
    public void testCheckSide() throws Exception {

        assertEquals("(RED)", Game.checkSide(0));
        assertEquals("(white)", Game.checkSide(1));
        assertEquals("error!", Game.checkSide(3));
        assertEquals("error!", Game.checkSide(4));
    }
}
