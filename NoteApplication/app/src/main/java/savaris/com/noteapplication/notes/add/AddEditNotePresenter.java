package savaris.com.noteapplication.notes.add;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesDatasource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 24/05/2017.
 */

public class AddEditNotePresenter implements AddEditNoteContract.Presenter, NotesDatasource.GetNoteCallback{

    @Nullable
    private String noteId;

    @NonNull
    private NotesDatasource notesDatasource;

    @NonNull
    private AddEditNoteContract.View noteView;

    private boolean isDataMissing;

    public AddEditNotePresenter(@Nullable String noteId, @NonNull NotesDatasource notesDatasource, @NonNull AddEditNoteContract.View noteView, boolean isDataMissing) {
        this.noteId = noteId;
        this.notesDatasource = checkNotNull(notesDatasource);
        this.noteView = checkNotNull(noteView);
        this.isDataMissing = isDataMissing;
        this.noteView.setPresenter(this);
    }

    @Override
    public void start() {

        if (!isNewNote() && isDataMissing){
            populateNote();
        }

    }

    @Override
    public void saveNote(String title, String text) {

        if (isNewNote()){
            createNote(title, text);
        } else {
            updateNote(title, text);
        }

    }

    @Override
    public void populateNote() {
        notesDatasource.getNote(noteId, this);

    }

    @Override
    public boolean isDataMissing() {
        return isDataMissing;
    }

    private boolean isNewNote(){
        return noteId == null;
    }

    private void createNote(String title, String text){

        Note note = new Note(title, text);

        if (note.isEmpty()){
            noteView.showEmptyNoteError();
        } else {
            notesDatasource.saveNote(note);
            noteView.showNoteList();
        }

    }

    private void updateNote(String title, String text){

        notesDatasource.saveNote(new Note(title, text, noteId));

        noteView.showNoteList();

    }

    @Override
    public void onNoteLoaded(Note note) {

        if (noteView.isActive()){

            noteView.setTitle(note.getTitle());
            noteView.setText(note.getText());

        }

        isDataMissing = false;

    }

    @Override
    public void onDataNotAvailable() {

        if (noteView.isActive()){
            noteView.showEmptyNoteError();
        }

    }
}
