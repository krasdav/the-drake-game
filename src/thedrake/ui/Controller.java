package thedrake.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button endBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onExit(){
        System.out.println("Application closed.");
        System.exit(0);
    }
}
