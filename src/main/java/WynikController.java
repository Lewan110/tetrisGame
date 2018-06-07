import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class WynikController implements Initializable {
   
    @FXML
    private TableColumn<Gracz, String> name;
    @FXML
    private TableColumn<Gracz, Integer> score;
    @FXML
    private TableColumn<Gracz, Integer> level;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    name.setCellValueFactory(
                new PropertyValueFactory<>("name"));

    score.setCellValueFactory(
                new PropertyValueFactory<>("score"));

    level.setCellValueFactory(
                new PropertyValueFactory<>("level"));
   
    }

}
