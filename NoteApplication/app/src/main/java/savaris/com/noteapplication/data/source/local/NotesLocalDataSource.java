package savaris.com.noteapplication.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesDatasource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 16/05/2017.
 */

public class NotesLocalDataSource implements NotesDatasource {

    private static NotesLocalDataSource INSTANCE;

    private NotesDbHelper notesDbHelper;

    private NotesLocalDataSource(@NonNull Context context){
        checkNotNull(context);
        notesDbHelper = new NotesDbHelper(context);
    }

    public static NotesDatasource getInstance(@NonNull Context context){

        if (INSTANCE == null){
            INSTANCE = new NotesLocalDataSource(context);
        }

        return INSTANCE;

    }

    @Override
    public void getNotes(@NonNull LoadNotesCallback callback) {

        List<Note> notes = new ArrayList<>();

        SQLiteDatabase db = notesDbHelper.getReadableDatabase();

        String[] selection = {
                NotesPersistenceContract.NotesEntry.COLUMN_NAME_ENTRY_ID,
                NotesPersistenceContract.NotesEntry.COLUMN_NAME_TITLE,
                NotesPersistenceContract.NotesEntry.COLUMN_NAME_TEXT,
                NotesPersistenceContract.NotesEntry.COLUMN_NAME_MARKED

        };

        Cursor cursor = db.query(NotesPersistenceContract.NotesEntry.TABLE_NAME, selection, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0){

            while (cursor.moveToNext()){

                String id = cursor.getString(cursor.getColumnIndexOrThrow(NotesPersistenceContract.NotesEntry.COLUMN_NAME_ENTRY_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesPersistenceContract.NotesEntry.COLUMN_NAME_TITLE));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(NotesPersistenceContract.NotesEntry.COLUMN_NAME_TEXT));

                boolean marked = cursor.getInt(cursor.getColumnIndexOrThrow(NotesPersistenceContract.NotesEntry.COLUMN_NAME_MARKED)) == 1;

                Note note = new Note(title, text, id, marked);

                notes.add(note);

            }

        }

        if (cursor != null){
            cursor.close();
        }

        db.close();

        if (notes.isEmpty()){
            callback.onDataNotAvailable();
        } else {
            callback.onNotesLoaded(notes);
        }

    }

    @Override
    public void getNote(@NonNull String noteId, @NonNull GetNoteCallback callback) {

        SQLiteDatabase db = notesDbHelper.getReadableDatabase();

        String[] fields = {
                NotesPersistenceContract.NotesEntry.COLUMN_NAME_ENTRY_ID,
                NotesPersistenceContract.NotesEntry.COLUMN_NAME_TITLE,
                NotesPersistenceContract.NotesEntry.COLUMN_NAME_TEXT,
                NotesPersistenceContract.NotesEntry.COLUMN_NAME_MARKED

        };

        String selection = NotesPersistenceContract.NotesEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";

        String[] selectionArgs = {noteId};

        Cursor cursor = db.query(NotesPersistenceContract.NotesEntry.TABLE_NAME, fields, selection, selectionArgs, null, null, null);

        Note note = null;

        if (cursor != null && cursor.getCount() > 0){

            cursor.moveToFirst();

            String id = cursor.getString(cursor.getColumnIndexOrThrow(NotesPersistenceContract.NotesEntry.COLUMN_NAME_ENTRY_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesPersistenceContract.NotesEntry.COLUMN_NAME_TITLE));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(NotesPersistenceContract.NotesEntry.COLUMN_NAME_TEXT));

            boolean marked = cursor.getInt(cursor.getColumnIndexOrThrow(NotesPersistenceContract.NotesEntry.COLUMN_NAME_MARKED)) == 1;

            note = new Note(title, text, id, marked);

        }

        if (cursor != null){
            cursor.close();
        }

        db.close();

        if (note != null){
            callback.onNoteLoaded(note);
        } else {
            callback.onDataNotAvailable();
        }

    }

    @Override
    public void saveNote(@NonNull Note note) {

        checkNotNull(note);

        SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NotesPersistenceContract.NotesEntry.COLUMN_NAME_ENTRY_ID, note.getId());
        values.put(NotesPersistenceContract.NotesEntry.COLUMN_NAME_TITLE, note.getTitle());
        values.put(NotesPersistenceContract.NotesEntry.COLUMN_NAME_TEXT, note.getText());
        values.put(NotesPersistenceContract.NotesEntry.COLUMN_NAME_MARKED, note.isMarked());

        db.insert(NotesPersistenceContract.NotesEntry.TABLE_NAME, null, values);

        db.close();

    }

    @Override
    public void markNote(@NonNull Note note) {

        SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(NotesPersistenceContract.NotesEntry.COLUMN_NAME_MARKED, true);

        String selection = NotesPersistenceContract.NotesEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";

        String[] selectionArgs = { note.getId() };

        db.update(NotesPersistenceContract.NotesEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        db.close();

    }

    @Override
    public void markNote(@NonNull String noteId) {

    }

    @Override
    public void clearMarkedNotes() {

        SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        String selection = NotesPersistenceContract.NotesEntry.COLUMN_NAME_MARKED + " LIKE ?";
        String[] selectionArgs = {"1"};

        db.delete(NotesPersistenceContract.NotesEntry.TABLE_NAME, selection, selectionArgs);

        db.close();

    }

    @Override
    public void refreshNotes() {

    }

    @Override
    public void deleteAllNotes() {

        SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        db.delete(NotesPersistenceContract.NotesEntry.TABLE_NAME, null, null);

        db.close();

    }

    @Override
    public void deleteNote(@NonNull String noteId) {

        SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        String selection = NotesPersistenceContract.NotesEntry.COLUMN_NAME_ENTRY_ID + "LIKE ?";
        String[] selectionArgs = {noteId};

        db.delete(NotesPersistenceContract.NotesEntry.TABLE_NAME, selection, selectionArgs);

        db.close();

    }
}
