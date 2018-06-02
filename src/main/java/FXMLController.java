import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FXMLController implements Initializable, Observer {

    @FXML
    Pane boardPanel;
    @FXML
    Pane scorePanel;
    @FXML
    Pane welcomePanel;
    @FXML
    Pane nextPanel;
    @FXML
    AnchorPane mainPane;
    @FXML
    Label levelLabel;
    @FXML 
    ImageView pausedImage;
    @FXML
    Label currentScoreLabel;
    @FXML
    Label hiScoreLabel;
    @FXML
     Slider paceSlider;
            
    AnimationTimer  animationTimer;   
    Canvas          canvas          = new Canvas(20 * 10, 20 * 20);
    Canvas          canvas2         = new Canvas(80, 80);
    JavaFxCanvas    canvasFX        = new JavaFxCanvas(canvas2);
    Canvas          background      = new Canvas(canvas.getWidth(), canvas.getHeight());
    Tetris          tetris;
    Image           image           = new Image("tetris-background.png");
    HiScores        hiscores        = new HiScores();
    Node          scoresDialogBox;
    Node          welcomeBox;
    int gameStarted=0;
    Gamer gamer = new Gamer();
    
    public FXMLController() throws IOException {
        tetris = new Tetris(10, 20, new JavaFxCanvas(canvas));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvasFX.init(4, 4);        
        boardPanel.getChildren().add(background);
        boardPanel.getChildren().add(canvas);
        nextPanel.getChildren().add(canvas2);      
        scorePanel.setFocusTraversable(true);
        welcomePanel.setFocusTraversable(true);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tetris.enterGameLoop(now);

            }
        };
        hiscores.getTopGamers();
        try {
            welcomeBox = FXMLLoader.load(FXMLController.class.getResource("welcome.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            scoresDialogBox = FXMLLoader.load(FXMLController.class.getResource("scores.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        drawBackground();

        paceSlider.setMin(0);
        paceSlider.setMax(1000);
        paceSlider.setValue(0);
    }

    @FXML
    private void HandleKeyAction(KeyEvent e) {
        tetris.inputManager.handle(e);
    }

    private void drawBackground() {
        GraphicsContext gc = background.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, background.getWidth(), background.getHeight());
    }

    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof Tetris) {
            GameState g = ((Tetris) o).getCurrentState();
            if(gameStarted==0){
                stopGame();
                showWelcome();
                gameStarted=1;
            }
            if (g.gameOver) {
                stopGame();              
                updateHallOfFame(g);            
            }
            tetris.setPace(paceSlider.getValue());
            currentScoreLabel.setText("score : " + g.score);
            hiScoreLabel.setText("top " + Math.max(hiscores.getGamers()[0].getScore(), g.score) );
            levelLabel.setText(Integer.toString(g.level));
            pausedImage.setVisible(g.gamePaused);
            canvasFX.drawTetrimino(g.tetrimino);
        } 
    }
   
    public void startGame() {
        animationTimer.start();
    }

    public void stopGame() {
        animationTimer.stop();
    }

    public void showWelcome(){
        final TextField gamerName = (TextField) welcomeBox.lookup("#gamerName");
        Button okBtn= (Button) welcomeBox.lookup("#welcomeSaveBtn");
        Button cancelBtn = (Button) welcomeBox.lookup("#WelcomeCancelBtn");
        mainPane.getChildren().add(welcomeBox);
        okBtn.setOnAction((ActionEvent event) -> {
            gamer.setName(gamerName.getText());

            hideWelcomeBox();
            tetris.HandleAction(GameAction.RESET);
            startGame();
        });

        cancelBtn.setOnAction((ActionEvent event) -> {
            gamer.setName("no name");
            hideWelcomeBox();
            tetris.HandleAction(GameAction.RESET);
            startGame();
        });

    }
    public void showScoresDialog(final GameState gameState, final int row) {
        final ObservableList<Gamer> gamersData = FXCollections.observableArrayList();
                
            //fill the tableview with the names of best gamers
            final TableView scoresTable = (TableView) scoresDialogBox.lookup("#scoresTable");
            Button continueBtn = (Button) scoresDialogBox.lookup("#continue");
            //todo dodaje sie tylko po przycisku, trzeba zrobic by sie dodawalo po przejsciu na tablice wynikow



            Gamer[] t = hiscores.getGamers();
            gamersData.addAll(t);
            scoresTable.setItems(gamersData);  
            mainPane.getChildren().add(scoresDialogBox);

        continueBtn.setOnAction((ActionEvent event) -> {
            if (gameState!=null) {
                gamer.setScore(gameState.score);
                gamer.setLevel(gameState.level);
                gamersData.remove(HiScores.WALL_OF_FAME_LENGTH-1);
                gamersData.add(row, gamer);

                Gamer[] g=new Gamer[gamersData.size()];
                for (int i = 0; i < gamersData.size(); i++) {
                    g[i]=gamersData.get(i);
                }
                hiscores.setGamers(g);
                hiscores.saveTopGamers();
            }
            hideScoresDialogBox();
            tetris.HandleAction(GameAction.RESET);
            startGame();
        });
            
    }

    private void updateHallOfFame(GameState gameState) {
        int pos;
        if ((pos = hiscores.findGamerPosition(gameState.score)) != -1) {
            showScoresDialog(gameState, pos);           
        }
    }

    Tetris getTetris() {
        return tetris;
    }

    private void hideScoresDialogBox() {
         mainPane.getChildren().remove(scoresDialogBox);
    }
    private void hideWelcomeBox() {
        mainPane.getChildren().remove(welcomeBox);
    }
    
}
