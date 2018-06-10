
/**
 *klasa przechowujaca aktualny status gry
 *
 */
public class StatusGry {

    public StatusGry() {
        tetrimino=null;
        score=0;
        level=1;
        linesCleared=0;
        gameOver=false;
        gamePaused=false;
        
    }
    
   Tetrimino tetrimino ;
   int score;
   int level;
   int linesCleared;
   boolean gameOver;
   boolean gamePaused;
}
