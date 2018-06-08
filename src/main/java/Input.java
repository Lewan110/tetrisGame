import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class Input implements EventHandler<KeyEvent>{
    private final Tetris tetris;
    
    public Input(Tetris tetris) {
        this.tetris=tetris;
    }
    

    public Akcje mapKeysToGameAction(KeyEvent t) {

        if (t.getCode() == KeyCode.LEFT) {
            return Akcje.GO_LEFT;
        } else if (t.getCode() == KeyCode.RIGHT) {
            return Akcje.GO_RIGHT;
        } else if (t.getCode() == KeyCode.SPACE) {
            return Akcje.ROTATE;
        } else if (t.getCode() == KeyCode.DOWN) {
            return Akcje.GO_DOWN;
        }
        else if (t.getCode() == KeyCode.P) {
            return Akcje.PAUSE;
        }
        else if (t.getCode() == KeyCode.R) {
            return Akcje.RESET;
        }
        return Akcje.NONE;
    }

    @Override
    public void handle(KeyEvent event) {        
        tetris.HandleAction(mapKeysToGameAction(event));
    }
}
