import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class JavaFxCanvas  {

    javafx.scene.canvas.Canvas canvas;
    
    Image[] sprites;
    
    Dimension2D recDimension;
    

    public JavaFxCanvas(javafx.scene.canvas.Canvas canvas) {
        this.canvas = canvas; 
        sprites=new Image[7];
        //test

        sprites[0]=new Image("1.png");
        sprites[1]=new Image("2.png");
        sprites[2]=new Image("3.png");
        sprites[3]=new Image("4.png");
        sprites[4]=new Image("5.png");
        sprites[5]=new Image("6.png");
        sprites[6]=new Image("7.png");
    }


    public void init(int cols, int rows) {        
        recDimension = new Dimension2D(canvas.getWidth() / cols, canvas.getHeight() / rows);
    }

   

    public void drawCell(int col, int line, int type) {       
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (type!=-1) {            
         gc.drawImage(sprites[type], recDimension.getWidth() * (col), recDimension.getHeight() * (line));   
        }
        

    }


    public void clearBackground() {
       GraphicsContext gc = canvas.getGraphicsContext2D();              
       gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    public void drawTetrimino(Tetrimino tetrimino){
        clearBackground();
        if (tetrimino!=null) 
        for (int i = 0; i < tetrimino.cells.length; i++) {
            for (int j = 0; j < tetrimino.cells[i].length; j++) {
                if (tetrimino.cells[i][j]==1) 
                drawCell(j, i, tetrimino.type);
            }            
        }        
    }
}
