package savaris.com.noteapplication.data.source;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
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
    public void markNote(@NonNull Note note) {

        checkNotNull(note);

        notesDatasourceRemote.markNote(note);
        notesDatasourceLocal.markNote(note);

        Note marketNote = new Note(note.getTitle(), note.getText(), note.getId(), !note.isMarked());

        if (cachedNotes == null){
            cachedNotes = new LinkedHashMap<>();
        }

        cachedNotes.put(note.getId(), marketNote);

    }

    @Override
    public void markNote(@NonNull String noteId) {

        checkNotNull(noteId);

        markNote(getNoteWithId(noteId));

    }


    @Override
    public void clearMarkedNotes() {

        notesDatasourceRemote.clearMarkedNotes();
        notesDatasourceLocal.clearMarkedNotes();

        if (cachedNotes == null){
            cachedNotes = new LinkedHashMap<>();
        }

        Iterator<Map.Entry<String, Note>> iterator = cachedNotes.entrySet().iterator();

        while(iterator.hasNext()){

            Map.Entry<String, Note> entry = iterator.next();

            if (entry.getValue().isMarked()){
                iterator.remove();
            }

        }

    }

    @Override
    public void getNote(@NonNull final String noteId, @NonNull final GetNoteCallback callback) {

        checkNotNull(noteId);

        checkNotNull(callback);

        Note note = getNoteWithId(noteId);

        if (note != null){
            callback.onNoteLoaded(note);
            return;
        }

        notesDatasourceLocal.getNote(noteId, new GetNoteCallback() {

            @Override
            public void onNoteLoaded(Note note) {

                if (cachedNotes == null){
                    cachedNotes = new LinkedHashMap<String, Note>();
                }

                cachedNotes.put(noteId, note);

                callback.onNoteLoaded(note);

            }

            @Override
            public void onDataNotAvailable() {

                notesDatasourceRemote.getNote(noteId, new GetNoteCallback() {
                    @Override
                    public void onNoteLoaded(Note note) {

                        if (cachedNotes == null){
                            cachedNotes = new LinkedHashMap<String, Note>();
                        }

                        cachedNotes.put(noteId, note);

                        callback.onNoteLoaded(note);

                    }

                    @Override
                    public void onDataNotAvailable() {

                        callback.onDataNotAvailable();

                    }
                });

            }
        });

    }

    @Override
    public void refreshNotes() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAllNotes() {

        notesDatasourceRemote.deleteAllNotes();
        notesDatasourceLocal.deleteAllNotes();

        if (cachedNotes == null){
            cachedNotes = new LinkedHashMap<>();
        }

        cachedNotes.clear();

    }

    @Override
    public void deleteNote(@NonNull String noteId) {

        notesDatasourceRemote.deleteNote(checkNotNull(noteId));
        notesDatasourceLocal.deleteNote(checkNotNull(noteId));

        cachedNotes.remove(noteId);

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

        if (cachedNotes == null || cachedNotes.isEmpty()){
            return null;
        }

        return cachedNotes.get(id);

    }

}
