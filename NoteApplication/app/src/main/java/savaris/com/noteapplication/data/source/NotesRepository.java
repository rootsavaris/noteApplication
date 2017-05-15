package savaris.com.noteapplication.data.source;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import savaris.com.noteapplication.data.Note;

/**
 * Created by rafael.savaris on 12/05/2017.
 */

public class NotesRepository implements NotesDatasource {

    private static NotesRepository INSTANCE = null;

    private final NotesDatasource notesDatasourceRemote;

    private final NotesDatasource notesDatasourceLocal;

    Map<String, Note> cachedNotes;

    boolean cacheIsDirty = false;

    private NotesRepository(@NonNull NotesDatasource notesDatasourceRemote, @NonNull NotesDatasource notesDatasourceLocal ){
        this.notesDatasourceRemote = checkNotNull(notesDatasourceRemote);
        this.notesDatasourceLocal = checkNotNull(notesDatasourceLocal);
    }

    public static NotesRepository getInstance(NotesDatasource notesDatasourceRemote, NotesDatasource notesDatasourceLocal){

        if (INSTANCE == null){
            INSTANCE = new NotesRepository(notesDatasourceRemote, notesDatasourceLocal);
        }

        return INSTANCE;

    }

    public static void destroyInstance(){
        INSTANCE = null;
    }

    @Override
    public void getNotes(@NonNull final LoadNotesCallback callback) {

        checkNotNull(callback);

        if (cachedNotes != null && !cacheIsDirty){
            callback.onNotesLoaded(new ArrayList<Note>(cachedNotes.values()));
            return;
        }

        if (cacheIsDirty){

            getNotesFromRemoteDatasource(callback);

        } else {

            notesDatasourceLocal.getNotes(new LoadNotesCallback() {

                @Override
                public void onNotesLoaded(List<Note> notes) {
                    refreshCache(notes);
                    callback.onNotesLoaded(new ArrayList<Note>(cachedNotes.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getNotesFromRemoteDatasource(callback);
                }
            });

        }

    }

    @Override
    public void saveNote(@NonNull Note note) {

        checkNotNull(note);

        notesDatasourceRemote.saveNote(note);
        notesDatasourceLocal.saveNote(note);

        if (cachedNotes == null){
            cachedNotes = new LinkedHashMap<>();
        }

        cachedNotes.put(note.getId(), note);

    }

    @Override
    public void completeNote(@NonNull Note note) {

        checkNotNull(note);

        notesDatasourceRemote.completeNote(note);
        notesDatasourceLocal.completeNote(note);

        Note completeNote = new Note(note.getTitle(), note.getText(), note.getId(), true);

        if (cachedNotes == null){
            cachedNotes = new LinkedHashMap<>();
        }

        cachedNotes.put(note.getId(), completeNote);

    }

    @Override
    public void completeNote(@NonNull String noteId) {

        checkNotNull(noteId);

        completeNote(getN);

    }

    @Override
    public void getNote(@NonNull String noteId, @NonNull GetNoteCallback callback) {

    }

    @Override
    public void markNote(@NonNull Note note) {

    }

    @Override
    public void markNote(@NonNull String noteId) {

    }

    @Override
    public void clearMarkedNotes() {

    }

    @Override
    public void refreshNotes() {

    }

    @Override
    public void deleteAllNotes() {

    }

    @Override
    public void deleteNote(@NonNull String noteId) {

    }

    private void getNotesFromRemoteDatasource(@NonNull final LoadNotesCallback callback){

        notesDatasourceRemote.getNotes(new LoadNotesCallback() {

            @Override
            public void onNotesLoaded(List<Note> notes) {
                refreshCache(notes);
                refreshLocalDataSource(notes);
                callback.onNotesLoaded(new ArrayList<Note>(cachedNotes.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    private void refreshCache(List<Note> notes){

        if (cachedNotes == null){
            cachedNotes = new LinkedHashMap<>();
        }

        cachedNotes.clear();

        for (Note note : notes){
            cachedNotes.put(note.getId(), note);
        }

        cacheIsDirty = false;

    }

    private void refreshLocalDataSource(List<Note> notes){

        notesDatasourceLocal.deleteAllNotes();

        for (Note note : notes){
            notesDatasourceLocal.saveNote(note);
        }

    }

    @Nullable
    private Note getNoteWithId(@NonNull String id){

        checkNotNull(id);

        if (cachedNotes)

    }

}
