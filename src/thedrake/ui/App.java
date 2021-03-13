package thedrake.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("thedrake.fxml"));
        root.setId("pane");
        root.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        stage.setTitle("The Drake");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
