package com.example.mynotesandroidapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private RecyclerView notesList;
    private NotesAdapter notesAdapter;
    /**
     * Sort switch
     */
    private Switch sortOrder;
    private Context context;
    /**
     * Sort order
     */
    private int sort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initSort();
        initRecyclerView();
    }

    /**
     * Method for initializing the sorting of existing notes.
     * First, the notes with the files
     * that were created / edited by the most recent time are displayed
     */
    private void initSort() {
        sortOrder = findViewById(R.id.id_sortOrderSwitch);
        sortOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sortOrder.isChecked()){
                    sort=1;
                    initRecyclerView();
                    Toast.makeText(context,"Old Note -> New Note", Toast.LENGTH_SHORT).show();
                } else {
                    sort =0;
                    initRecyclerView();
                    Toast.makeText(context,"New Note -> Old Note", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }


    /**
     * Method to start creating a new note
     * Processed by click the button @+
     */

    public void plusNote(View view) {

        Intent intent = new Intent(context, NoteDetailsActivity.class);
        context.startActivity(intent);

        Toast.makeText(this, "New Note", Toast.LENGTH_SHORT).show();

    }


    /**
     * initializes RecycleView
     */
    private void initRecyclerView() {

        notesList = findViewById(R.id.idRv_Notes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notesList.setLayoutManager(layoutManager);


        notesAdapter = new NotesAdapter(this, sort);
        notesList.setAdapter(notesAdapter);

    }
}
