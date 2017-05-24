package savaris.com.noteapplication.notes.add;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import savaris.com.noteapplication.data.source.NotesDatasource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 24/05/2017.
 */

public class AddEditNotePresenter implements AddEditNoteContract.Presenter {

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

    }

    @Override
    public void saveNote(String title, String text) {

    }

    @Override
    public void populateNote() {

    }

    @Override
    public boolean isDataMissing() {
        return false;
    }
}
