package savaris.com.noteapplication.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesDatasource;

/**
 * Created by rafael.savaris on 16/05/2017.
 */

public class NotesRemoteDataSource implements NotesDatasource {

    private static NotesRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, Note> NOTES_SERVICE_DATA;

    static {

        NOTES_SERVICE_DATA = new LinkedHashMap<>(2);

        addNote("Annotaion1", "This is annotation 1");
        addNote("Annotaion2", "This is annotation 2");

    }

    public static NotesRemoteDataSource getInstance(){

        if (INSTANCE == null){
            INSTANCE = new NotesRemoteDataSource();
        }

        return INSTANCE;

    }

    private NotesRemoteDataSource(){}

    private static void addNote(String title, String text){
        Note note = new Note(title, text);
        NOTES_SERVICE_DATA.put(note.getId(), note);
    }

    @Override
    public void getNotes(final @NonNull LoadNotesCallback callback) {

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onNotesLoaded(Lists.newArrayList(NOTES_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);

    }

    @Override
    public void getNote(@NonNull String noteId, final @NonNull GetNoteCallback callback) {

        final Note note = NOTES_SERVICE_DATA.get(noteId);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onNoteLoaded(note);
            }
        }, SERVICE_LATENCY_IN_MILLIS);


    }

    @Override
    public void saveNote(@NonNull Note note) {
        NOTES_SERVICE_DATA.put(note.getId(), note);
    }

    @Override
    public void markNote(@NonNull Note note) {
        Note noteMarked = new Note(note.getTitle(), note.getText(), note.getId(), true);
        NOTES_SERVICE_DATA.put(note.getId(), noteMarked);
    }

    @Override
    public void markNote(@NonNull String noteId) {

    }

    @Override
    public void clearMarkedNotes() {

        Iterator<Map.Entry<String, Note>> iterator = NOTES_SERVICE_DATA.entrySet().iterator();

        while (iterator.hasNext()){

            Map.Entry<String, Note> entry = iterator.next();

            if (entry.getValue().isMarked()){
                iterator.remove();
            }

        }

    }

    @Override
    public void refreshNotes() {}

    @Override
    public void deleteAllNotes() {
        NOTES_SERVICE_DATA.clear();
    }

    @Override
    public void deleteNote(@NonNull String noteId) {
        NOTES_SERVICE_DATA.remove(noteId);
    }
}
