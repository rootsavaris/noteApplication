package savaris.com.noteapplication.data.source;

import android.support.annotation.NonNull;

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
        notesDatasourceRemote = checkNotNull(notesDatasourceRemote);

    }

    @Override
    public void getNotes(@NonNull LoadNotesLoaded callback) {

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
}
