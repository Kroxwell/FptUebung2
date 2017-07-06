import classes.SongClass;
import classes.SongListClass;
import interfaces.Song;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.commons.lang3.SystemUtils;


public class MP3Controller {

    @FXML private Label LblOrdN;

    @FXML private TextField TxtTitel;
    @FXML private TextField TxtInterp;
    @FXML private TextField TxtAlbum;

    @FXML private ListView listViewMP3;
    @FXML private ListView listViewPlaylist;

    private MP3Model model;
    private MP3View view;

    /* DEBUG KOMMT WEG */
    ObservableList<Song> songlist;
    /* END DEBUG */


    public MP3Controller() {
        this.model = new MP3Model();
        this.view = new MP3View();
    }


    @FXML protected void onBtnPlayListNeu(ActionEvent event) throws RemoteException {
        System.out.println(this.model); // gibt null zurück
        this.model.delPlaylist();
    }

    @FXML protected void onBtnPlaylistLaden(ActionEvent event) {

        System.out.println("Noch nicht implementiert");
    }

    @FXML protected void onBtnPlLöschen(ActionEvent event) {

        System.out.println("Siehe Button PlNeu!");
    }

    @FXML protected void onBtnOrdnerWahl(ActionEvent event) throws Exception {
        DirectoryChooser chooser = new DirectoryChooser();
        File ordner;

        if (SystemUtils.IS_OS_MAC) {
            chooser.setInitialDirectory(new File(System.getenv("HOME")));
        } else if (SystemUtils.IS_OS_WINDOWS) {
            chooser.setInitialDirectory(new File(System.getenv("USERPROFILE")));
        }
        chooser.setTitle("Ordner mit MP3-Dateien wählen");

        ordner = chooser.showDialog(((Node) event.getSource()).getScene().getWindow());
        if (ordner == null) { // z.B. wenn man auf "cancel" drückt
            // Handeln: Gar nichts tun? Bisher ist das nur Platzhalter!
            throw new Exception("Fehler, keinen Ordner ausgewählt!");
        }

        this.LblOrdN.setText(ordner.toString());

        SongListClass mp3s_ordner = new SongListClass();
        File[] inhalt = ordner.listFiles();

        for (File datei : inhalt) {
            if (datei.getName().endsWith(".mp3")) {
                mp3s_ordner.addSong(new SongClass(datei.getPath())); // hier schon Metadaten auslesen + unique ID erstellen (HASH?)
            }
        }

        // Gucken ob das weniger Aufwand ist ein Aufruf mit allen oder alle einzeln
        this.model.setMp3dateien(mp3s_ordner);

        // Nicht schön aber selten!
        // Dabei wird aus den Songs in der MP3-Liste nur der String .toString() benutzt

        this.songlist = FXCollections.observableList(this.model.getMp3dateien().getList());
        if (this.songlist == null) { throw new Exception("SongList ist empty"); }
        this.listViewMP3.setItems(songlist);
    }

    @FXML protected void onButtonHinzufuegen(ActionEvent event) {
        System.out.print("onButtonHinzufuegen");
            this.listViewPlaylist.getItems().add(this.model.getAuswahlMp3Song());
            this.model.setAuswahlMp3Song(null);

    }

    @FXML protected void onBtnSloe(ActionEvent event) throws RemoteException {
        System.out.print("Sloe");
        this.model.getPlaylist().deleteSong(this.model.getAuswahlPlSong());

        // View Updaten
    }

    @FXML protected void onBtnMetaSp(ActionEvent event) throws RemoteException{
        // ÜBERARBEITEN ! FUNKTIONIERT NOCH NICHT
        String album = this.TxtAlbum.getText();
        String interp = this.TxtAlbum.getText();
        String titel = this.TxtTitel.getText();

        ArrayList<Song> pl = this.model.getPlaylist().getList();
        int index = pl.indexOf(this.model.getMomentanerSong());
        pl.get(index).setTitle(titel);
        pl.get(index).setAlbum(album);
        pl.get(index).setInterpret(interp);
    }

    @FXML protected void letztesLied(ActionEvent event) throws RemoteException{

        // Prüfen ob der Player null ist!

        if ((this.model.getMomentanerSong() != null) && (this.model.getPlaylist().sizeOfList() > 0)) {
            ArrayList<Song> playlist = this.model.getPlaylist().getList(); // vlt überarbeiten -.-
            int index = playlist.indexOf(this.model.getMomentanerSong());
            MediaPlayer.Status altersong_status = this.model.getPlayer().getStatus();

            if (index > 0) {
                this.model.setMomentanerSong((SongClass) playlist.get(index-1));
            } else {
                this.model.setMomentanerSong((SongClass) playlist.get(playlist.size()-1));
            }

            this.model.setPlayer(new MediaPlayer(new Media(this.model.getMomentanerSong().getPath())));

            if ((altersong_status == MediaPlayer.Status.PLAYING)
                    || (altersong_status == MediaPlayer.Status.HALTED)) {
                this.model.getPlayer().play();
            }

        } else if ((this.model.getMomentanerSong() != null) && (this.model.getPlaylist().sizeOfList() == 0)) {
            // das sollte nicht passieren, ist eigentlich gar nicht möglich
            // kann höchstens passieren wenn die Pl gelöscht wird aber noch ein Song spielt (?)
            this.model.setMomentanerSong(null);
        } else if ((this.model.getMomentanerSong() == null) && (this.model.getPlaylist().sizeOfList() > 0)) {
            // sollte eigentlich nicht passieren aber igel
            // einfach aktiverSong auf den letzten aus der Playlist setzen
            ArrayList<Song> playlist = this.model.getPlaylist().getList();
            this.model.setMomentanerSong((SongClass) playlist.get(playlist.size()-1));
        }
    }

    @FXML protected void play(ActionEvent event) throws RemoteException {

        this.listViewPlaylist.getItems().addAll(songlist);
       // this.model.getPlayer().get

    }

    @FXML protected void buttonPause(){
        this.model.playingBT();
        System.out.print("bulb");
    }

    @FXML protected void nächstesLied(ActionEvent event) throws RemoteException {

        if ((this.model.getMomentanerSong() != null) && (this.model.getPlaylist().sizeOfList() > 0)) {
            ArrayList<Song> playlist = this.model.getPlaylist().getList();
            int index = playlist.indexOf(this.model.getMomentanerSong());
            MediaPlayer.Status altersong_status = this.model.getPlayer().getStatus();

            if (index <= playlist.size()-1) {
                this.model.setMomentanerSong((SongClass) playlist.get(index+1));
            } else {
                this.model.setMomentanerSong((SongClass) playlist.get(0));
            }

            this.model.setPlayer(new MediaPlayer(new Media(this.model.getMomentanerSong().getPath())));

            if ((altersong_status == MediaPlayer.Status.PLAYING)
                    || (altersong_status == MediaPlayer.Status.HALTED)) {
                this.model.getPlayer().play();
            }

        } else if ((model.getMomentanerSong() != null) && (this.model.getPlaylist().sizeOfList() == 0)) {
            // das sollte nicht passieren, ist eigentlich gar nicht möglich
            // kann höchstens passieren wenn die Pl gelöscht wird aber noch ein Song spielt (?)
            this.model.setMomentanerSong(null);
        } else if ((this.model.getMomentanerSong() == null) && (this.model.getPlaylist().sizeOfList() > 0)) {
            // sollte eigentlich nicht passieren aber igel
            // einfach aktiverSong auf den ersten aus der Playlist setzen
            this.model.setMomentanerSong((SongClass) this.model.getPlaylist().getList().get(0));
        }
    }


    @FXML protected void onMp3MsPressed(MouseEvent event) throws RemoteException {
        System.out.println("onMp§MsPressed");
        SongClass ausgewaehlter_song =  (SongClass) this.listViewMP3.getFocusModel().getFocusedItem();
        this.model.setAuswahlMp3Song(ausgewaehlter_song);

        // DEBUG
        System.out.println(this.model.getMp3dateien());
    }


    @FXML protected void onPlMsPressed(MouseEvent event) {
        System.out.println("onPlMsPressed");
        SongClass ausgewaehlter_song =  (SongClass) this.listViewPlaylist.getFocusModel().getFocusedItem();
        try {
            this.model.getPlaylist().addSong(ausgewaehlter_song);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Metadatenanzeige ändern, wenn man auf einen anderen Song clickt
    }
}
