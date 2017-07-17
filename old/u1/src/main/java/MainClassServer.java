import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainClassServer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        /**
         * In der FXML-Datei wird der Controller mit angegeben und verwendet
         *  => keine eigene Initialisierung nötig!
         */

//        MediaPlayer p = new MediaPlayer( new Media(new File("C:\\Queen.mp3").toURI().toString()));
//        p.play();




        Parent root = FXMLLoader.load(getClass().getResource("fpt-uebung-1.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
