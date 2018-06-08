import javafx.animation.AnimationTimer;
import javafx.application.Platform;
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

public class MainController implements Initializable, Observer {


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
    @FXML
    Label name;

    AnimationTimer  animationTimer;   
    Canvas          canvas          = new Canvas(200, 400); // obszar gry
    Canvas          canvas2         = new Canvas(80, 80);   //next panel
    MainCanvas canvasFX        = new MainCanvas(canvas2);
    Canvas          background      = new Canvas(canvas.getWidth(), canvas.getHeight());    //tło
    Tetris          tetris;
    Image           image           = new Image("img/tetris-background.png");       //obraz tłą
    Wyniki          hiscores        = new Wyniki();
    Node            scoresDialogBox;
    Node            welcomeBox;

    int gameStarted=0;
    Gracz gracz = new Gracz();
    
    public MainController() throws IOException {
        tetris = new Tetris(10, 20, new MainCanvas(canvas));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvasFX.init(4, 4);        
        boardPanel.getChildren().add(background);
        boardPanel.getChildren().add(canvas);
        nextPanel.getChildren().add(canvas2);
        boardPanel.setFocusTraversable(true);
        paceSlider.setFocusTraversable(false);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tetris.enterGameLoop(now);
            }
        };

        hiscores.getTopGamers();

        try {
            welcomeBox = FXMLLoader.load(MainController.class.getResource("welcome.fxml"));     //ładowanie okna ,,powitalnego"
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            scoresDialogBox = FXMLLoader.load(MainController.class.getResource("wyniki.fxml")); //Ładowanie okna wyników po zakończeniu gry
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        rysujTlo();
        paceSlider.setMin(0);
        paceSlider.setMax(1000);
        paceSlider.setValue(0);
    }

    @FXML
    private void HandleKeyAction(KeyEvent e) {
        tetris.input.handle(e);
    }

    private void rysujTlo() {
        GraphicsContext gc = background.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, background.getWidth(), background.getHeight());
    }

    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof Tetris) {
            StatusGry g = ((Tetris) o).getCurrentState();
            if(gameStarted==0){
                animationTimer.stop();
                showWelcome();
                gameStarted=1;
            }
            if (g.gameOver) {
                animationTimer.stop();
                updateHallOfFame(g);            
            }

            tetris.setPace(paceSlider.getValue());
            currentScoreLabel.setText("score : " + g.score);
            levelLabel.setText(Integer.toString(g.level));
            pausedImage.setVisible(g.gamePaused);
            name.setText(gracz.getName());
            canvasFX.drawTetrimino(g.tetrimino);
        } 
    }

    public void showWelcome(){
        final TextField gamerName = (TextField) welcomeBox.lookup("#gamerName");
        Button okBtn= (Button) welcomeBox.lookup("#welcomeSaveBtn");
        Button cancelBtn = (Button) welcomeBox.lookup("#WelcomeCancelBtn");

        mainPane.getChildren().add(welcomeBox);

        okBtn.setOnAction((ActionEvent event) -> {
            gracz.setName(gamerName.getText());
            if(gamerName.getText().length()<1){
                gracz.setName("no name");
            }

            hideWelcomeBox();
            tetris.HandleAction(Akcje.RESET);
            animationTimer.start();
        });

        cancelBtn.setOnAction((ActionEvent event) -> {
            gracz.setName("no name");
            hideWelcomeBox();
            tetris.HandleAction(Akcje.RESET);
            animationTimer.start();
        });

    }
    public void showScoresDialog(final StatusGry statusGry, final int nrWTabeli) {
        final ObservableList<Gracz> gamersData = FXCollections.observableArrayList();
                
            //Wypełnij tabele imion najlepszych graczy
            final TableView scoresTable = (TableView) scoresDialogBox.lookup("#scoresTable");
            Button endBtn = (Button) scoresDialogBox.lookup("#end");
            Button tryAgainBtn = (Button) scoresDialogBox.lookup("#try_again");

            Gracz[] t = hiscores.getGraczs();
            gamersData.addAll(t);
            scoresTable.setItems(gamersData);
            mainPane.getChildren().add(scoresDialogBox);

            gracz.setScore(statusGry.score);
            gracz.setLevel(statusGry.level);

            gamersData.add(nrWTabeli, gracz);

            Gracz[] g=new Gracz[gamersData.size()];
            for (int i = 0; i < gamersData.size(); i++) {
                g[i]=gamersData.get(i);
            }
            hiscores.setGraczs(g);
            hiscores.sortujGraczy();

            endBtn.setOnAction((ActionEventevent) -> {

                gamersData.remove(Wyniki.Długosc_Wall_Of_Fame -1);

            Platform.exit();
        });
        tryAgainBtn.setOnAction((ActionEventevent) -> {

            gamersData.remove(Wyniki.Długosc_Wall_Of_Fame -1);
            hideScoresDialogBox();
            tetris.HandleAction(Akcje.RESET);
            animationTimer.start();
        });
            
    }

    private void updateHallOfFame(StatusGry statusGry) {
        int pos = hiscores.znajdzPozycjeGraczaWTabeli(statusGry.score);
        if (pos != -1) {
            showScoresDialog(statusGry, pos);
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
