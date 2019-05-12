package com.example.mynotesandroidapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mynotesandroidapp.DAO.INotesDAO;
import com.example.mynotesandroidapp.DAO.NotesFileDAO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context context;
    /**
     * The entire list of notes that are available
     */
    private ArrayList<String> listNotes;

    private INotesDAO INotesDAO;

    /**
     * Sort order
     */
    private int sortOrder;

    public NotesAdapter(Context context, int sortOrder) {

        listNotes = new ArrayList<>();
        INotesDAO = new NotesFileDAO(context);
        this.context = context;
        this.sortOrder = sortOrder;

        putAllNotes();

    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        int layoutForIdListNotes = R.layout.layout_note;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutForIdListNotes, viewGroup, false);

        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int i) {

//        String [] buffer = listNotes.get(i).split("&");
//        Instant instant = Instant.ofEpochMilli(Long.parseLong(buffer[0]));
//        String [] YYYY_MM_DD = instant.toString().split("T");
//        String hh_mm_ss = YYYY_MM_DD[1].split("\\.")[0];


//        if (buffer[1].length() >= 100) {
//            notesViewHolder.bind(YYYY_MM_DD[0]+" "+hh_mm_ss+"\n"+ buffer[1].substring(0, 100));
//        } else {
//            notesViewHolder.bind(YYYY_MM_DD[0]+" "+hh_mm_ss+"\n"+ buffer[1]);
//        }
        notesViewHolder.bind(i);

    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    /**
     * Method Get all the exist notes and add in listNote
     */

    private void putAllNotes() {

        ArrayList<Long> nameFileList = new ArrayList<>();
        nameFileList.addAll(INotesDAO.getAllNameNotes());

        sortList(nameFileList, 0);

        if (nameFileList.size() != 0) {
            listNotes.addAll(INotesDAO.openAllNotes(nameFileList));
            maxLastKeyNote(nameFileList.get(0), listNotes);

            sortList(listNotes, sortOrder);
        }


    }

    /**
     * The method sorts a note in the specified direction
     *
     * @param nameFileList - note what nead sort
     * @param orderSort    - sort order
     */
    private void sortList(ArrayList nameFileList, int orderSort) {

        if (orderSort == 0) {
            Collections.sort(nameFileList);
            Collections.reverse(nameFileList);
        } else {
            Collections.sort(nameFileList);
        }

    }


    /**
     * The method is created for users who "love" often change the time on their phone!)
     * for the correct display of notes in the order of creation
     * A cool movie "Back to the Future")
     */
    private void maxLastKeyNote(long maxKey, ArrayList<String> arrayList) {

        if (maxKey >= System.currentTimeMillis()) {

            INotesDAO.deleteAllFileNote();

            for (int i = listNotes.size() - 1; i >= 0; i--) {
                INotesDAO.writeInDateFile(String.valueOf(System.currentTimeMillis()), arrayList.get(i));
            }

            putAllNotes();

        }

    }


    public class NotesViewHolder extends RecyclerView.ViewHolder {

        TextView listNotesView;

        /**
         * When a click is made, the file name and its contents are transmitted
         *
         * @param itemView
         */

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            listNotesView = itemView.findViewById(R.id.tv_notes);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int positionIndex = getAdapterPosition();
                            String[] buffer = listNotes.get(positionIndex).split("&");


                            Intent intent = new Intent(context, NoteDetailsActivity.class);
                            intent.putExtra("name_note", buffer[1]);
                            intent.putExtra("content_note", buffer[2]);

                            context.startActivity(intent);

                        }
                    });

        }

        /**
         * Method of displaying content in a given template
         *
         * @param i - number of notes in @listNote
         */

        void bind(int i) {

            String[] buffer = listNotes.get(i).split("&");
            Instant instant = Instant.ofEpochMilli(Long.parseLong(buffer[0]));

            // get the date YYYY_MM_DD
            String[] YYYY_MM_DD = instant.toString().split("T");

            // get the time hh_mm_ss
            String hh_mm_ss = (YYYY_MM_DD[1].split("\\.")[0]).substring(0, 8);

            String content = "";

            if (buffer[1].length() >= 100) {
                content = YYYY_MM_DD[0] + " " + hh_mm_ss + "\n" + buffer[2].substring(0, 100);
            } else {
                content = YYYY_MM_DD[0] + " " + hh_mm_ss + "\n" + buffer[2];
            }

            listNotesView.setText(content);

        }

    }


}
