package savaris.com.noteapplication.notes;

import android.support.annotation.NonNull;

import java.util.List;

import savaris.com.noteapplication.BasePresenter;
import savaris.com.noteapplication.BaseView;
import savaris.com.noteapplication.data.Note;

/**
 * Created by rafael.savaris on 19/05/2017.
 */

public interface NotesContract {

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadNotes(boolean forceUpdate);

        void addNewNote();

        void openNoteDetails(@NonNull Note requestedNote);

        void marketNote(@NonNull Note marketNote);

        void clearMarketNotes();

        void setFiltering(NotesFilterType requestType);

        NotesFilterType getFiltering();

    }

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showNotes(List<Note> notes);

        void showAddNote();

        void showNoteDetailsUi(String noteId);

        void showNoteMarked();

        void showLoadingNotesError();

        void showNoNotes();

        void showActiveFilterLabel();

        void showAllFilterLabel();

        void showNoMarketNotes();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();

    }

}
