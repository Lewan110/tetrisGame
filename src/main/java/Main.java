import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends Application {

    MainController controller ;

    public Main()  {
        try {
            controller = new MainController();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root;
        Scene scene;
        
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("glowny.fxml"));
        controller.getTetris().setObserver(controller);
        loader.setController(controller);
        controller.getTetris().addObserver(controller);
        loader.load();

        root = loader.getRoot();
        scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        primaryStage.setTitle("TETRIS");
        controller.animationTimer.start();
        
    }
   
}
