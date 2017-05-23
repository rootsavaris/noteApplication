package savaris.com.noteapplication.notes.list;

import static com.google.common.base.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import savaris.com.noteapplication.R;
import savaris.com.noteapplication.data.Note;

/**
 * Created by rafael.savaris on 19/05/2017.
 */

public class NotesFragment extends Fragment implements NotesContract.View{

    private NotesContract.Presenter mPresenter;

    private NotesAdapter notesAdapter;

    private TextView filteringLabel;

    private LinearLayout linearLayoutNotes;

    private View noNotesView;

    private ImageView noNotesIcon;

    private TextView noNotesMain;

    private TextView noNotesAdd;

    public NotesFragment() {}

    public static NotesFragment newInstance(){
        return new NotesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesAdapter = new NotesAdapter(new ArrayList<Note>(0), notesItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull NotesContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.notes_list, container, false);

        ListView listView = (ListView) root.findViewById(R.id.notes_list);

        listView.setAdapter(notesAdapter);

        filteringLabel = (TextView) root.findViewById(R.id.filteringLabel);

        linearLayoutNotes = (LinearLayout) root.findViewById(R.id.notesFilterContainer);

        noNotesView = root.findViewById(R.id.noNotes);

        noNotesIcon = (ImageView) root.findViewById(R.id.noNotesIcon);

        noNotesMain = (TextView) root.findViewById(R.id.noNotesMain);

        noNotesAdd = (TextView) root.findViewById(R.id.noNotesAdd);

        noNotesAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showAddNote();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_note);

        fab.setImageResource(R.drawable.ic_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewNote();
            }
        });

        final ScrollChildSwipeRefreshLayout scrollChildSwipeRefreshLayout = (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        scrollChildSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        scrollChildSwipeRefreshLayout.setScrollUpChild(listView);

        scrollChildSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadNotes(false);
            }
        });

        setHasOptionsMenu(true);

        return root;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes_menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_clear:
                mPresenter.clearMarketNotes();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                mPresenter.loadNotes(true);
                break;
        }

        return true;

    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null){
            return;
        }

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(active);
            }
        });

    }

    @Override
    public void showNotes(List<Note> notes) {

        notesAdapter.replaceData(notes);

        linearLayoutNotes.setVisibility(View.VISIBLE);

        noNotesView.setVisibility(View.GONE);

    }

    @Override
    public void showAddNote() {

        Intent intent = new Intent(getContext(), null);
        startActivity(intent, null);

    }

    @Override
    public void showNoteDetailsUi(String noteId) {

        Intent intent = new Intent(getContext(), null);
        intent.putExtra(null, noteId);
        startActivity(intent);

    }

    @Override
    public void showNoteMarked() {
        showMessage(getString(R.string.note_marked));
    }

    @Override
    public void showLoadingNotesError() {
        showMessage(getString(R.string.loading_notes_error));
    }

    @Override
    public void showNoNotes() {
        showNoNotesViews(getResources().getString(R.string.no_notes_all), R.drawable.ic_assignment_turned_in_24dp, false);
    }

    @Override
    public void showMarkedFilterLabel() {
        filteringLabel.setText(getResources().getString(R.string.label_marked));
    }

    @Override
    public void showAllFilterLabel() {
        filteringLabel.setText(getResources().getString(R.string.label_all));
    }

    @Override
    public void showNoMarkedNotes() {
        showNoNotesViews(
                getResources().getString(R.string.no_notes_marked),
                R.drawable.ic_check_circle_24dp,
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_note_message));
    }

    @Override
    public boolean isMarked() {
        return isAdded();
    }

    @Override
    public void showFilteringPopUpMenu() {

        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_notes, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case  R.id.marked:
                        mPresenter.setFiltering(NotesFilterType.MARKED_NOTES);
                        break;
                    default:
                        mPresenter.setFiltering(NotesFilterType.ALL_NOTES);
                        break;
                }

                mPresenter.loadNotes(false);

                return true;

            }
        });

        popup.show();

    }

    NotesItemListener notesItemListener = new NotesItemListener() {

        @Override
        public void onNoteClick(Note clickedNote) {
            mPresenter.openNoteDetails(clickedNote);
        }

        @Override
        public void onMarkedNoteClick(Note markedNote) {
            mPresenter.marketNote(markedNote);
        }
    };

    private void showNoNotesViews(String mainText, int iconRes, boolean showAddView){

        linearLayoutNotes.setVisibility(View.GONE);

        noNotesView.setVisibility(View.VISIBLE);

        noNotesMain.setText(mainText);

        noNotesIcon.setImageDrawable(getResources().getDrawable(iconRes));

        noNotesAdd.setVisibility(showAddView ? View.VISIBLE : View.GONE);

    }

    private void showMessage(String message){
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
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
                    mNotesItemListener.onMarkedNoteClick(note);
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
