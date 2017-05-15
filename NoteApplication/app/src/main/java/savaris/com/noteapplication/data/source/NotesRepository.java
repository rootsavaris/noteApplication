package savaris.com.noteapplication.data.source;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;

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
    public void getNotes(@NonNull final LoadNotesLoaded callback) {

        checkNotNull(callback);

        if (cachedNotes != null && !cacheIsDirty){
            callback.onNotesLoaded(new ArrayList<Note>(cachedNotes.values()));
            return;
        }

        if (cacheIsDirty){
            getNotesFromRemoteDatasource(callback);
        }





    }

    @Override
    public void getNote(@NonNull String noteId, @NonNull GetNoteCallback callback) {

    }

    @Override
    public void saveNote(@NonNull Note note) {

    }

    @Override
    public void completeNote(@NonNull Note note) {

    }

    @Override
    public void completeNote(@NonNull String noteId) {

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

    private void getNotesFromRemoteDatasource(@NonNull LoadNotesLoaded callback){

        notesDatasourceRemote.getNotes(new LoadNotesLoaded() {

            @Override
            public void onNotesLoaded(List<Note> notes) {
                refreshCache(notes);
                refreshLocalDataSource(notes);
            }

            @Override
            public void onDataNotAvailable() {

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



    }

}
