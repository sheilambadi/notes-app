package com.sheilambadi.android.noteapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sheilambadi.android.noteapp.database.model.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //database versions
    private static final int DATABASE_VERSION = 1;

    //database name
    private static final String DATABASE_NAME = "notes_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create database table
        sqLiteDatabase.execSQL(Note.CREATE_TABLE);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //drop old table if it exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  Note.TABLE_NAME);

        //create tables again
        onCreate(sqLiteDatabase);
    }

    public long insertNote(String note){
        //get writable database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //define note column to be inserted ~ only one as the rest are auto-generated
        ContentValues contentValues = new ContentValues();
        contentValues.put(Note.COLUMN_NOTE, note);

        //insert row ~ id returned
        long id = sqLiteDatabase.insert(Note.TABLE_NAME, null, contentValues);

        //close db connection
        sqLiteDatabase.close();

        //return newly inserted row id
        return id;
    }

    public Note getNote(long id){
        //get readable database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        //prepare note object
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP))
        );

        //close the db connection
        cursor.close();

        //return note
        return note;
    }

    public List<Note> getAllNotes(){
        List<Note> notes = new ArrayList<>();

        //select all query
        String selectAllQuery = "SELECT * FROM " + Note.TABLE_NAME + " ORDER BY " + Note.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectAllQuery, null);

        //looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        //close db connection
        sqLiteDatabase.close();
        cursor.close();

        return notes;
    }

    public int getNotesCount(){
        String countQuery = "SELECT * FROM " + Note.TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateNote(Note note){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues =  new ContentValues();
        contentValues.put(Note.COLUMN_NOTE, note.getNote());

        //updating row
        return sqLiteDatabase.update(Note.TABLE_NAME, contentValues, Note.COLUMN_ID + " = ? ",
            new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ? ",
                new String[]{String.valueOf(note.getId())});
        sqLiteDatabase.close();
    }



}
