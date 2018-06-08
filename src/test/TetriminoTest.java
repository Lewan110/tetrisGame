import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TetriminoTest {
    public static final byte[][] Tetrimino = {
            {0, 0, 0},
            {0, 0, 1},
            {1, 1, 1},};
    public static final byte[][] Tetrimino_rotated = {
            {0, 1, 1},
            {0, 0, 1},
            {0, 0, 1},};

    Tetrimino tetrimino1 = new Tetrimino(Tetrimino, 0);
    Tetrimino tetrimino2 = new Tetrimino(Tetrimino_rotated, 0);

    @Before
    public void setUp() throws  Exception{
    }
    @Test
    public void rotateClockWise() {
        assertEquals(true,tetrimino1.rotateClockWise().equals(tetrimino2));

    }

}