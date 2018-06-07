import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class WynikController implements Initializable {
   
    @FXML
    private TableColumn<Gracz, String> Nick;
    @FXML
    private TableColumn<Gracz, Integer> Wynik;
    @FXML
    private TableColumn<Gracz, Integer> Poziom;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    Nick.setCellValueFactory(
                new PropertyValueFactory<>("name"));

    Wynik.setCellValueFactory(
                new PropertyValueFactory<>("score"));

    Poziom.setCellValueFactory(
                new PropertyValueFactory<>("level"));
   
    }

}
