package com.sheilambadi.android.noteapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sheilambadi.android.noteapp.R;
import com.sheilambadi.android.noteapp.database.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder>{
    private Context context;
    private List<Note> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView note;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View itemView) {
            super(itemView);

            note = itemView.findViewById(R.id.note);
            dot = itemView.findViewById(R.id.dot);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }

    public NotesAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.note.setText(note.getNote());
        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));
        holder.timestamp.setText(formatDate(note.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {
            Log.e("dateError", e.toString());
        }

        return "";
    }
}
