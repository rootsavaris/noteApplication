package savaris.com.noteapplication.notes.detail;

import savaris.com.noteapplication.BasePresenter;
import savaris.com.noteapplication.BaseView;

/**
 * Created by rafael.savaris on 24/05/2017.
 */

public interface NoteDetailContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingNote();

        void hideTitle();

        void showTitle(String title);

        void hideText();

        void showText(String text);

        void showMarkedStatus(boolean marked);

        void showEditNote(String noteId);

        void showNoteDeleted();

        void showNoteMarked();

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void editNote();

        void deleteNote();

        void markedNote();

    }

}
