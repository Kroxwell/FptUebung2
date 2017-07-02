import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;


public class MainClass extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        /**
         * In der FXML-Datei wird der Controller mit angegeben und verwendet
         *  => keine eigene Initialisierung nötig!
         */
/*
        String path = "C:\\Users\\Marco\\Desktop\\Uni\\FPT\\Musikplayer\\RustedfromtheRain.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer p = new MediaPlayer(media);
        p.play();
*/

        Parent root = FXMLLoader.load(getClass().getResource("fpt-uebung-1.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
