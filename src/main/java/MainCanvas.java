import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *Klasa obslugujaca obszar gry
 */
public class MainCanvas {

    javafx.scene.canvas.Canvas canvas;

    Image[] sprites;

    Dimension2D recDimension;


    /**
     *konstruktor klasy
     */
    public MainCanvas(javafx.scene.canvas.Canvas canvas) {
        this.canvas = canvas; 
        sprites=new Image[7];
        //test

        sprites[0]=new Image("img/1.png");
        sprites[1]=new Image("img/2.png");
        sprites[2]=new Image("img/3.png");
        sprites[3]=new Image("img/4.png");
        sprites[4]=new Image("img/5.png");
        sprites[5]=new Image("img/6.png");
        sprites[6]=new Image("img/7.png");
    }
    /**
     *metoda inicjujaca obszar gry
     * @param cols - liczba kolumn
     * @param rows - liczba wierszy
     */
    public void init(int cols, int rows) {
        recDimension = new Dimension2D(canvas.getWidth() / cols, canvas.getHeight() / rows);
    }


    /**
     *metoda inicjujaca obszar gry
     * @param col - liczba kolumn
     * @param line - liczba wierszy
     * @param type - typ
     */
    public void drawCell(int col, int line, int type) {       
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (type!=-1) {            
         gc.drawImage(sprites[type], recDimension.getWidth() * (col), recDimension.getHeight() * (line));   
        }
        

    }


    /**
     *metoda czyszczaca obszar gry
     *
     */
    public void clearBackground() {
       GraphicsContext backGround = canvas.getGraphicsContext2D();
       backGround.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    /**
     *metoda czyszczaca obszar gry
     * @param tetrimino -tetromino do narysowania na obszarze gry
     */
    public void drawTetrimino(Tetrimino tetrimino){
        clearBackground();      //wyczyść
        if (tetrimino!=null)
        for (int i = 0; i < tetrimino.cells.length; i++) {
            for (int j = 0; j < tetrimino.cells[i].length; j++) {
                if (tetrimino.cells[i][j]==1)   //tam gdzie w tablicy tetromino jest 1 to rysuj kwadrat
                drawCell(j, i, tetrimino.type);
            }            
        }        
    }
}
