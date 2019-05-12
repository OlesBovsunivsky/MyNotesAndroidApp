package com.example.mynotesandroidapp.DAO;

import java.util.List;



public interface INotesDAO {
    /**
     * Template starts the note name
     */
    public String startNameNote = "My_Note_";

    /**
     * The name of the note in the file system
     */
    public String nameFileInFileSystem = "/data/user/0/com.example.mynotesandroidapp/files/My_Note_";

    public String openDataFile(String nameFile);

    public List openAllNotes(List nameAllNote);

    public void writeInDateFile(String nameFile, String contents);

    public void deleteDataFile(String nameFile);

    public void deleteAllFileNote ();

    public long dateFile(String nameFile);

    public List<Long> getAllNameNotes();

}
