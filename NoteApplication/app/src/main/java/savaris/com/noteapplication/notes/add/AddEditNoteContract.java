package savaris.com.noteapplication.notes.add;

import savaris.com.noteapplication.BasePresenter;
import savaris.com.noteapplication.BaseView;

/**
 * Created by rafael.savaris on 24/05/2017.
 */

public interface AddEditNoteContract {

    interface View extends BaseView<Presenter> {

        void showEmptyNoteError();

        void showNoteList();

        void setTitle(String title);

        void setText(String text);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveNote(String title, String text);

        void populateNote();

        boolean isDataMissing();

    }

}
