import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;


public class MainClassClient extends Application {

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
