import java.util.Arrays;
import java.util.Objects;

/**
 *klasa do tworzenia oraz obslugi tetrominos
 *
 */
public class Tetrimino implements Cloneable{

    public static final byte[][] TetriminoI = {
        {1, 1, 1, 1},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},};
    public static final byte[][] TetriminoJ = {
        {0, 0, 0},
        {1, 0, 0},
        {1, 1, 1},};
    public static final byte[][] TetriminoL = {
        {0, 0, 0},
        {0, 0, 1},
        {1, 1, 1},};
    public static final byte[][] TetriminoO = {
        {1, 1},
        {1, 1}
    };
    public static final byte[][] TetriminoS = {
        {0, 0, 0},
        {0, 1, 1},
        {1, 1, 0},};
    public static final byte[][] TetriminoT = {
        {1, 1, 1},
        {0, 1, 0},
        {0, 0, 0},};
    public static final byte[][] TetriminoZ = {
        {0, 0, 0},
        {1, 1, 0},
        {0, 1, 1},};
    byte[][] cells;
    int type;

    public Tetrimino(byte[][] termino, int type) {
        this.cells = termino;
        this.type = type;

    }

    private Tetrimino(Tetrimino tetrimino) {
        this.cells = new byte[tetrimino.cells.length][tetrimino.cells[0].length];
        for (int i = 0; i < tetrimino.cells.length; i++) {
            System.arraycopy(tetrimino.cells[i], 0, this.cells[i], 0, tetrimino.cells[0].length);   //kopiowanie tablicy klocka
            
        }
        this.type = tetrimino.type;
    }

    /**
     *metoda obracajaca tetromino zgodnie z ruchem wskazowek zegara
     */
    public Tetrimino rotateClockWise() {
        int n = this.cells.length;
        byte tmp;

        for (int i = 0; i < n; i++) {               //n - długość tablicy klocka
            for (int j = i; j < n - i - 1; j++) {
                tmp = cells[i][j];                                  //zapisanie obracanego klocka
                cells[i][j] = cells[j][n - i - 1];
                cells[j][n - i - 1] = cells[n - i - 1][n - j - 1];
                cells[n - i - 1][n - j - 1] = cells[n - j - 1][i];
                cells[n - j - 1][i] = tmp;
            }
        }
        
        return this;
       
}

    @Override
   public Tetrimino clone(){
       Tetrimino t=new Tetrimino(this);
        return t;      
   }
    /**
     *metoda porownujaca dwa tetromino
     * @param o -objekt porownywany
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (this.type == ((Tetrimino) o).type) {
            if (this.cells == null) {
                return (((Tetrimino) o).cells == null);
            }
            if (((Tetrimino) o).cells == null) {
                return false;
            }
            if (this.cells.length != ((Tetrimino) o).cells.length) {
                return false;
            }

            for (int i = 0; i < this.cells.length; i++) {
                if (!Arrays.equals(this.cells[i], ((Tetrimino) o).cells[i])) {
                    return false;
                }
            }

        }
        return true;
    }



}

