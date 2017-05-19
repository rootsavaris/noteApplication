package savaris.com.noteapplication.notes;

import android.support.v4.app.Fragment;

import java.util.List;

import savaris.com.noteapplication.data.Note;

/**
 * Created by rafael.savaris on 19/05/2017.
 */

public class NotesFragment extends Fragment implements NotesContract.View{

    public static NotesFragment newInstance(){
        return new NotesFragment();
    }

    @Override
    public void setPresenter(NotesContract.Presenter presenter) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showNotes(List<Note> notes) {

    }

    @Override
    public void showAddNote() {

    }

    @Override
    public void showNoteDetailsUi(String noteId) {

    }

    @Override
    public void showNoteMarked() {

    }

    @Override
    public void showLoadingNotesError() {

    }

    @Override
    public void showNoNotes() {

    }

    @Override
    public void showActiveFilterLabel() {

    }

    @Override
    public void showAllFilterLabel() {

    }

    @Override
    public void showNoMarketNotes() {

    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showFilteringPopUpMenu() {

    }
}
