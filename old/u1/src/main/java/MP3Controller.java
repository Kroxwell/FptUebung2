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
import java.util.Collections;

import org.apache.commons.lang3.SystemUtils;


public class MP3Controller {

    /**     ==================================
     *      -*- INHALT DER KLASSE MP3Model -*-
     *      ==================================
     *
     *
     * Alle FXML-Elemente, die es gibt
     *  => später alles unnötige rauswerfen
     *
     * MP3Mode model, das alle Dateien beinhaltet
     *  => Aufruf von Methoden zur Änderung der Daten
     *
     * MP3View view, überflüssig aber ich lass das erstmal drinnen
     *  => falls FXML nicht geht und zusätzlich als Hardcode-Möglichkeit
     */

    @FXML private Label LblOrdN;
    @FXML private Label LblTitle;
    @FXML private Label LblInterp;
    @FXML private Label LblAlbum;
    @FXML private Label LblZeit;

    @FXML private TextField TxtTitel;
    @FXML private TextField TxtInterp;
    @FXML private TextField TxtAlbum;

    @FXML private ListView LstMp3;
    @FXML private ListView LstPl;

    private MP3Model model;
    private MP3View view;

    /* DEBUG KOMMT WEG */
    ObservableList<Song> songlist;
    /* END DEBUG */


    public MP3Controller() {
        this.model = new MP3Model();
        this.view = new MP3View(); // löschen
    }


    /**
     * Handler onBtnPlNeu
     *  => löscht alle Elemente aus LstPl für eine neue
     */
    @FXML protected void onBtnPlNeu(ActionEvent event) throws RemoteException {
        System.out.println(this.model); // gibt null zurück
        this.model.delPlaylist();
    }

    /**
     * Handler onBtnPlLad
     *  => noch nicht implementiert (Fällt vlt weg)
     */
    @FXML protected void onBtnPlLad(ActionEvent event) {
        System.out.println("Noch nicht implementiert");
    }

    /**
     * Handler onBtnPlLoe
     *  => sollte wie Button PlNeu sein, wird zusammengepackt
     */
    @FXML protected void onBtnPlLoe(ActionEvent event) {
        System.out.println("Siehe Button PlNeu!");
    }

    /**
     * Handler onBtnOrdW (getriggert wenn man auf "Ordner auswählen" klickt)
     *  => Ordner auswählen mit den MP3-Dateien drin
     *  => Ordner als File ordner oder String ordnerPath im Model speichern
     *  => LstMp3 und LstPl anpassen und davon abhängige Variablen (alle)
     *  => View updaten mit den neusten Sachen
     */
    @FXML protected void onBtnOrdW(ActionEvent event) throws Exception {
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
        this.LstMp3.setItems(songlist);
    }

    /**
     * Funktion onBtnShin (getriggert wenn man auf den Button "Song hinzufügen" clickt)
     *  => fügt den per Mausklick ausgewählten Song aus der Mp3Lst in PlLst hinzu
     */
    @FXML protected void onButtonHinzufuegen(ActionEvent event) {
        System.out.print("onButtonHinzufuegen");
            this.LstPl.getItems().add(this.model.getAuswahlMp3Song());
            this.model.setAuswahlMp3Song(null);

    }

    /**
     * Funktion onBtnSloe (getriggert wenn man auf den Button "Song löschen" klickt)
     *  => löscht den per Mausklick ausgewählten Song aus PlLst
     */
    @FXML protected void onBtnSloe(ActionEvent event) throws RemoteException {
        // auf Null testen
        // falls es momentanerSong ist umändern auf nächsten, wenn am ende auf den ersten setzen
        this.model.getPlaylist().deleteSong(this.model.getAuswahlPlSong());

        // View Updaten
    }

    /**
     * Funktion onBtnMetaSp (getriggert wenn man auf den Button "Metadaten speichern" klickt)
     *  => speichert die (geänderten) Metadaten des in der PlLst ausgewählten Songs
     */
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

    /**
     * Funktion onBtnSletzt (getriggert wenn man auf den "Letzten Song abspielen" klickt)
     *  => setzt den aktiven Song auf den vorherigen UND
     *      => spielt ab wenn der momentane läuft,
     *      => setzt den nur (oder nochmal auf Pause) wenn der andere pausiert,
     *      => spielt ab oder setzt auf Pause wenn momentaner beendet ist (?) [sollte eigentlich nie passieren]
     */
    @FXML protected void onBtnSletzt(ActionEvent event) throws RemoteException{

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

    /**
     * Funktion onBtnSstart (getriggert wenn man auf "Song abspielen/ pausieren" klickt)
     *  => spielt den aktuellen Song ab (wenn vorhanden)
     *  => setzt auf Pause wenn aktueller Song gerade läuft
     *  => startet von vorne, wenn aktueller Song fertig ist? [sollte nicht da Autoplay nächster]
     */
    @FXML protected void play(ActionEvent event) throws RemoteException {


    }

    @FXML protected void buttonPause(){
        this.model.playingBT();
        System.out.print("bulb");
    }


    /**
     * Funktion onBtnSnaechs (getriggert wenn man auf den "Nächsten Song abspielen" klickt)
     *  => setzt den aktiven Song auf den nächsten UND
     *      => spielt ab wenn der momentane läuft,
     *      => setzt den nur (oder nochmal auf Pause) wenn der andere pausiert,
     *      => spielt ab oder setzt auf Pause wenn momentaner beendet ist (?) [sollte eigentlich nie passieren]
     */
    @FXML protected void onBtnSnaechs(ActionEvent event) throws RemoteException {

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

    /**
     * Handler onMp3MsPressed (getriggert, wenn man in die ListView Mp3List klickt)
     *  => setzt ausgewählten Song Mp3 auf den, auf den geklickt wurde
     *      => wenn daneben geklickt/ ListView leer nix machen (?)
     *  !!! Funktion wird nicht aufgerufen wenn man reinklickt !!!
     */
    @FXML protected void onMp3MsPressed(MouseEvent event) throws RemoteException {
        SongClass ausgewaehlter_song =  (SongClass) this.LstMp3.getFocusModel().getFocusedItem();
        this.model.setAuswahlMp3Song(ausgewaehlter_song);

        // DEBUG
        System.out.println(this.model.getMp3dateien());
    }

    /**
     * Handler onPlMsPressed (getriggert, wenn man in die ListView Mp3List klickt)
     *  => setzt ausgewählten Song Pl auf den, auf den geklickt wurde
     *      => wenn daneben geklickt/ ListView leer nix machen (?)
     *  !!! Funktion wird nicht aufgerufen wenn man reinklickt !!!
     */
    @FXML protected void onPlMsPressed(MouseEvent event) {
        SongClass ausgewaehlter_song =  (SongClass) this.LstPl.getFocusModel().getFocusedItem();
        this.model.setAuswahlPlSong(ausgewaehlter_song);

        // Metadatenanzeige ändern, wenn man auf einen anderen Song clickt
    }
}
