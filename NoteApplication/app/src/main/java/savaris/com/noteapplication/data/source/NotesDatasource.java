package savaris.com.noteapplication.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import savaris.com.noteapplication.data.Note;

/**
 * Created by rafael.savaris on 12/05/2017.
 */

public interface NotesDatasource {

    interface LoadNotesCallback {

        void onNotesLoaded(List<Note> notes);

        void onDataNotAvailable();

    }

    interface GetNoteCallback {

        void onNoteLoaded(Note note);

        void onDataNotAvailable();

    }

    void getNotes(@NonNull LoadNotesCallback callback);

    void getNote(@NonNull String noteId, @NonNull GetNoteCallback callback);

    void saveNote(@NonNull Note note);

    void completeNote(@NonNull Note note);

    void completeNote(@NonNull String noteId);

    void markNote(@NonNull Note note);

    void markNote(@NonNull String noteId);

    void clearMarkedNotes();

    void refreshNotes();

    void deleteAllNotes();

    void deleteNote(@NonNull String noteId);

}
