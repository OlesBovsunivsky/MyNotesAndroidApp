package com.example.mynotesandroidapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynotesandroidapp.DAO.NotesDAO;
import com.example.mynotesandroidapp.DAO.NotesFileDAO;

public class NoteDetailsActivity extends AppCompatActivity {

    /**
     * Field of note
     */
    private EditText editText;

    private Button button;
    private Intent intent;

    /**
     * Name note
     */
    private String nameNote = null;
    private String content = "";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        editText = findViewById(R.id.id_editNoteText);

        initActivity();

    }

    /**
     * Method init content of this activity
     */

    private void initActivity() {
        intent = getIntent();
        String s = intent.getStringExtra("content_note");
        String buffer = intent.getStringExtra("name_note");

        if (s != null) {
            content = s;
        }

        if (buffer != null) {
            nameNote = buffer;
        }

        editText.setText(s);
    }

    /**
     * The method handles clicks on the buttons @Back and @Delete
     *
     * @param view
     */
    public void onClick(View view) {

        switch (view.getId()) {

            // Button @Back
            case R.id.id_backMyButton:
                button = findViewById(R.id.id_backMyButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveByClick();
                    }
                });

                // Button @Delet
            case R.id.id_deleteNoteButton:
                button = findViewById(R.id.id_deleteNoteButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new NotesFileDAO(context).deleteDataFile(nameNote);
                        Toast.makeText(context, "Note Delete", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);

                        Toast.makeText(context, "Note Delete", Toast.LENGTH_SHORT).show();


                    }
                });
        }
    }

    /**
     * Adding the functionality to the buttons @Home and @Back
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_HOME == event.getKeyCode()) {
            saveByClick();
            return super.onKeyDown(keyCode, event);
        }

        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            saveByClick();
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

    /**
     * A method for saving a clickable content note
     * If the content has not changed then there will be no cached note file
     * If you delete all characters in an existing note, the note will also be deleted
     */

    private void saveByClick() {

        if (nameNote == null) {
            nameNote = String.valueOf(System.currentTimeMillis());
        }

        String contentEditText = editText.getText().toString();

        if (!content.equals(contentEditText)) {

            NotesDAO notesDAO = new NotesFileDAO(this);

            System.out.println();
            if (contentEditText.length() != 0) {
                notesDAO.writeInDateFile(
                        nameNote, contentEditText);

                Toast.makeText(context, "Note Save", Toast.LENGTH_SHORT).show();

            } else {
                notesDAO.deleteDataFile(nameNote);
                Toast.makeText(context, "Note Delete", Toast.LENGTH_SHORT).show();
            }

        }

        intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);

    }


}
