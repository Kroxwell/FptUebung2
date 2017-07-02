package classes;

import interfaces.Song;
import javafx.beans.value.ObservableValue;

import javax.print.attribute.standard.Media;


public class SongClass implements Song {

    /**     =====================================
     *      -*- VARIABLEN DER KLASSE MP3Model -*-
     *      =====================================
     *
     *
     * String path, speichert den (ganzen) Dateipfad zu der MP3-Date
     *  => bei File ordner: mögliches Update der SongListClass mp3dateien?
     *      => Abfrage alle paar mal Eventloop-Durchlauf? Falls möglich.
     *  => bei String ordnerPath: kein Update der SongListClass mp3dateien
     *      => nur Speicherung Ordnerpfad für das Label, Update bei Änderung
     *
     * long id, speichert ID des Songs
     *  => generiert durch einen MD5-hash (?), sodass der unique ist
     *      => Updaten wenn man die Metadaten ändert
     *  => SPÄTER IMPLEMENTIEREN
     *
     * String album, speichert Album-Metadata (erst) in Klasseninstanz
     *  => wird durch "Metadaten Speichern"-Knopf geändert
     *      => später in Datei speichern
     *      => SPÄTER IMPLEMENTIEREN
     *
     * String interpret, speichert Interpreten-Metadata (erst) in Klasseninstanz
     *  => wird durch "Metadaten Speichern"-Knopf geändert
     *      => später in Datei speichern
     *      => SPÄTER IMPLEMENTIEREN
     *
     * String title, speichert Titel-Metadata (erst) in Klasseninstanz
     *  => wird durch "Metadaten Speichern"-Knopf geändert
     *      => später in Datei speichern
     *      => SPÄTER IMPLEMENTIEREN
     */

    private String path;
    private long id;
    private String album;
    private String interpret;
    private String title;


    public SongClass(String path) { this.path = path; }

    public SongClass(String path, long id) { this.path = path; this.id = id; }

    public SongClass(String path, String album, String interpret, String title) {
        this.path = path;
        this.album = null;
        this.interpret = null;
        this.title = title;
    }

    public SongClass(String path, long id, String album, String interpret, String title) {
        this.path = path;
        this.id = id;
        this.album = album;
        this.interpret = interpret;
        this.title = title;
    }


    public String getAlbum() { return this.album; }
    public void setAlbum(String album) {
        this.album = album;
    }

    public String getInterpret() {
        return this.interpret;
    }
    public void setInterpret(String interpret) {
        this.interpret = interpret;
    }

    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    /**
     * SPÄTER IMPLEMENTIEREN ODER FALLLEN LASSEN
     */
    public ObservableValue<String> pathProperty() {
        // Platzhalter
        return (ObservableValue<String>) new Object();
    }

    public ObservableValue<String> albumProperty() {
        // Platzhalter
        return (ObservableValue<String>) new Object();
    }

    public ObservableValue<String> interpretProperty() {
        // Platzhalter
        return (ObservableValue<String>) new Object();
    }

    /**
     * Wird von der ListView aufgerufen
     */
    public String toString() { // => das nutzt die ListView, also: Sinnvolles reinschreiben (?)
        // für Unix und für Windows testen
        //  => / teilt bei Unix, \ bei Windows (?)
        String[] pfad_teile = this.path.split("/");
        return (pfad_teile[pfad_teile.length-1]);
    }
}