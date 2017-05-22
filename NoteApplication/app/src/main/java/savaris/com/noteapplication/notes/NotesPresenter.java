package savaris.com.noteapplication.notes;

import android.support.annotation.NonNull;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 19/05/2017.
 */

public class NotesPresenter implements NotesContract.Presenter{

    private final NotesRepository notesRepository;

    private final NotesContract.View notesView;

    private NotesFilterType notesFilterType = NotesFilterType.ALL_NOTES;

    private boolean firstLoad = true;

    public NotesPresenter(@NonNull NotesRepository notesRepository, @NonNull NotesContract.View notesView){
        this.notesRepository = checkNotNull(notesRepository);
        this.notesView = notesView;
        this.notesView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadNotes(boolean forceUpdate) {

    }

    @Override
    public void addNewNote() {

    }

    @Override
    public void openNoteDetails(@NonNull Note requestedNote) {

    }

    @Override
    public void marketNote(@NonNull Note marketNote) {

    }

    @Override
    public void clearMarketNotes() {

    }

    @Override
    public void setFiltering(NotesFilterType requestType) {

    }

    @Override
    public NotesFilterType getFiltering() {
        return null;
    }
}
