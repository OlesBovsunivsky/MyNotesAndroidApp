package com.example.mynotesandroidapp.DAO;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for working with a place to store notes
 */
public class INotesFileDAO implements INotesDAO {

    private Context context;

    /**
     * The class constructor takes on the input context,
     * as it is needed to work with data streams
     * @param context
     */

    public INotesFileDAO(Context context) {
        this.context = context;
    }

    /**
     * Opens a file with a given name
     * @param nameFile - note file name
     * @return - Returns the specified:     1: date of creation/change file;
     *                                      2: filename;
     *                                      3: file contents.
     *          All parameters are listed through the metric '&'
     */
    @Override
    public String openDataFile(String nameFile) {

        FileInputStream fin = null;
        String text = "";
        byte[] bytes;
        try {
            fin = context.openFileInput(startNameNote + nameFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            bytes = new byte[fin.available()];
            fin.read(bytes);
            fin.close();
            text = new String(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dateFile(nameFile)+ "&" +nameFile + "&" + text;
    }

    /**
     * Open all notes
     * @param nameAllNote - ArrayList of name file note
     * @return - List with content all notes
     */

    @Override
    public ArrayList<String> openAllNotes(List nameAllNote) {

        ArrayList arrayList = new ArrayList();

        for (int i = 0; i < nameAllNote.size(); i++) {
            arrayList.add(openDataFile(String.valueOf(nameAllNote.get(i))));
        }

        return arrayList;
    }

    /**
     * Write content in file
     * @param nameFile - Name of new/old file
     * @param content
     */

    @Override
    public void writeInDateFile(String nameFile, String content) {

        if (nameFile == null) {
            nameFile = String.valueOf(System.currentTimeMillis());
        }

        FileOutputStream outputStream = null;

        try {
            outputStream = context.openFileOutput(startNameNote + nameFile, context.MODE_PRIVATE);
            try {
                outputStream.write(content.getBytes());
            } catch (IOException e) {
                Toast.makeText(context,"File not write", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(context,"File not write", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Delete note
     * @param nameFile
     */

    @Override
    public void deleteDataFile(String nameFile) {
        context.deleteFile(startNameNote + nameFile);
    }

    /**
     * Method delete all note.
     * This time don`t use in this App
     */
    @Override
    public void deleteAllFileNote() {
        ArrayList<Long> arrayList = getAllNameNotes();

        for (int i = 0; i < arrayList.size(); i++) {
            deleteDataFile(String.valueOf(arrayList.get(i)));
        }
    }


    /**
     * Method to get the date of creation / change of note
     * @param nameFile
     * @return - date in milliseconds
     */
    @Override
    public long dateFile(String nameFile) {


        Path filePath = new File(nameFileInFileSystem + nameFile).toPath();
        BasicFileAttributes attr = null;

        try {

            attr = Files.readAttributes(filePath, BasicFileAttributes.class);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return attr.lastModifiedTime().toMillis();
    }

    /**
     * the method returns all the names of all existing notes that correspond to the given template
     * @return list notes name
     */
    @Override
    public ArrayList<Long> getAllNameNotes() {

        String nameFileInFileSystem = INotesDAO.nameFileInFileSystem;
        File[] file = context.getFilesDir().listFiles();

        ArrayList<Long> nameFileList = new ArrayList();

        for (File t : file) {
            if (t.toString().contains(nameFileInFileSystem)) {
                long buffer = Long.parseLong(t.toString().replace(nameFileInFileSystem, ""));
                nameFileList.add(buffer);
            }
        }
        return nameFileList;
    }

}
