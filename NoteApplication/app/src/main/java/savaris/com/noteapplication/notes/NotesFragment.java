package savaris.com.noteapplication.notes;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import savaris.com.noteapplication.R;
import savaris.com.noteapplication.data.Note;

/**
 * Created by rafael.savaris on 19/05/2017.
 */

public class NotesFragment extends Fragment implements NotesContract.View{

    private NotesContract.Presenter mPresenter;

    public NotesFragment() {}

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
    public boolean isMarked() {
        return false;
    }

    @Override
    public void showFilteringPopUpMenu() {

    }

    private static class NotesAdapter extends BaseAdapter{

        private List<Note> mNotes;

        private NotesItemListener mNotesItemListener;

        public NotesAdapter(List<Note> notes, NotesItemListener notesItemListener){
            setList(notes);
            mNotesItemListener = notesItemListener;
        }

        public void replaceData(List<Note> notes){
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<Note> notes){
            mNotes = checkNotNull(notes);
        }

        @Override
        public int getCount() {
            return mNotes.size();
        }

        @Override
        public Note getItem(int i) {
            return mNotes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View viewRoot = view;

            if (viewRoot == null){

                LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

                viewRoot = layoutInflater.inflate(R.layout.note_item, viewGroup, false);

            }

            final Note note = getItem(i);

            TextView title = (TextView) viewRoot.findViewById(R.id.title);

            title.setText(note.getTitle());

            CheckBox checkBox = (CheckBox) viewRoot.findViewById(R.id.mark);

            checkBox.setChecked(note.isMarked());

            if (note.isMarked()){
                viewRoot.setBackgroundDrawable(viewGroup.getContext().getResources().getDrawable(R.drawable.list_completed_touch_feedback));
            } else {
                viewRoot.setBackgroundDrawable(viewGroup.getContext().getResources().getDrawable(R.drawable.touch_feedback));
            }

            checkBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (!note.isMarked()){
                        mNotesItemListener.onMarkedNoteClick(note);
                    }

                }
            });

            viewRoot.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    mNotesItemListener.onNoteClick(note);

                }
            });

            return viewRoot;

        }
    }

    public interface NotesItemListener {

        void onNoteClick(Note clickedNote);

        void onMarkedNoteClick(Note markedNote);

    }

}
