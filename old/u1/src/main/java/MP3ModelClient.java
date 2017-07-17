import classes.SongClass;
import classes.SongListClass;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.rmi.RemoteException;


public class MP3ModelClient {

    /**     =====================================
     *      -*- VARIABLEN DER KLASSE MP3ModelClient -*-
     *      =====================================
     *
     *
     * File ordner ODER String ordnerpath, ausgewählter Ordner
     *  => bei File ordner: mögliches Update der SongListClass mp3dateien?
     *      => Abfrage alle paar mal Eventloop-Durchlauf? Falls möglich.
     *  => bei String ordnerPath: kein Update der SongListClass mp3dateien
     *      => nur Speicherung Ordnerpfad für das Label, Update bei Änderung
     *
     * SongListClass mp3dateien, mit allen MP3-Dateien in Ordner
     *  => wird nicht geupdatet bei Hinzufügen während Programm offen
     *  => Soll Observable gemacht werden? Für Autoupdate der View?
     *
     * SongListClass playlist, mit allen MP3-Dateien in der Playlist
     *  => Soll Observable gemacht werden? Für Autoupdate der View?
     *
     * Song(Class) [je nachdem was <ListView>.getSelectionModel().getSelectedItem() zurückgibt] auswahlMp3Song,
     *  das per Mausclick ausgewählte Item in LstMp3 (Handeln wenn Liste wenig Items hat!)
     *  => um das Item in die LstPl zu packen
     *  => ändert sich immer, wenn man was anderes anclickt
     *  => ändert sich, wenn man neuen Ordner auswählt usw.
     *
     * Song(Class) [je nachdem was <ListView>.getSelectionModel().getSelectedItem() zurückgibt] auswahlPlSong,
     *  das per Mausclick ausgewählte Item in LstMp3 (Handeln wenn Liste wenig Items hat!)
     *  => um das Item aus LstPl zu löschen, abzuspielen etc.
     *  => ändert sich immer, wenn man was anderes anclickt
     *  => ändert sich, wenn man die Playlist löscht, das Item aus der Playlist löscht usw.
     *
     * Song(Class) momentanerSong, der Song der gerade im Player ist, also aktiv
     *  => ändert sich, wenn man anderen Song auswählt (=> auswahlPlSong) UND abgespielt
     *  => ändert sich, wenn momentanerSong durchgelaufen automatisch auf nächsten gewechselt
     *  => ändert sich, wenn Button BtnSletzt/ BtnSnaechs gedrückt wurde
     *
     * MediaPlayer player, dafür da um darüber Songs abzuspielen,
     *  => Handler für Knöpfe BtnSstart, BtnSletzt, BtnSnaechs
     */

    private File ordner; // wie gesagt vielleicht auch nur den Pfad (?)
    private SongListClass mp3dateien;
    private SongListClass playlist;

    private SongClass auswahlMp3Song;
    private SongClass auswahlPlSong;
    private SongClass momentanerSong;

    private MediaPlayer player;


    public MP3ModelClient() {
        this.ordner = null;
        this.mp3dateien = new SongListClass();
        this.playlist = new SongListClass();
        this.auswahlMp3Song = null;
        this.auswahlPlSong = null;
        this.momentanerSong = null;

    }

    public File getOrdner() { return this.ordner; }
    public void setOrdner(File ordner) {
        this.ordner = ordner;
    }


    public SongListClass getMp3dateien() {
        return this.mp3dateien;
    }

    public void setMp3dateien(SongListClass dateien) throws RemoteException{
        this.mp3dateien = dateien;
    }

    public void delMp3dateien() throws RemoteException{
        this.mp3dateien.deleteAllSongs();
    }

    public SongListClass getPlaylist() {
        return this.playlist;
    }

    public void setPlaylist(SongListClass playlist) {
        this.playlist = playlist;
    }

    public void delPlaylist() throws RemoteException { this.playlist.deleteAllSongs(); }

    public void deleteElementFromPlaylist(String path) throws RemoteException {
        this.playlist.deleteSong(this.playlist.findSongByPath(path));
    }

    public void addElementToPlaylist(SongClass song) throws RemoteException{
        this.playlist.addSong(song);
    }

    public SongClass getAuswahlMp3Song() { return this.auswahlMp3Song; }

    public void setAuswahlMp3Song(SongClass auswahlMp3Song) { this.auswahlMp3Song = auswahlMp3Song; }

    public SongClass getAuswahlPlSong() { return auswahlPlSong; }

    public void setAuswahlPlSong(SongClass auswahlPlSong) { this.auswahlPlSong = auswahlPlSong; }

    public SongClass getMomentanerSong() {
        return momentanerSong; }

    public void setMomentanerSong(SongClass momentanerSong) {
        this.momentanerSong = momentanerSong;}

    public MediaPlayer getPlayer() { return player; }
    public void setPlayer(MediaPlayer player) { this.player = player; }

}
