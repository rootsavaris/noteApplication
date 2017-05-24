package savaris.com.noteapplication.notes.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.common.base.Preconditions;

import savaris.com.noteapplication.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 24/05/2017.
 */

public class NoteDetailFragment extends Fragment implements NoteDetailContract.View {

    private static final int REQUEST_EDIT_NOTE = 1;

    private NoteDetailContract.Presenter presenter;

    private TextView textTitle;

    private TextView textText;

    private CheckBox checkMarked;

    public static NoteDetailFragment newInstance(@Nullable String noteId){

        Bundle bundle = new Bundle();

        bundle.putString(NoteDetailActivity.EXTRA_NOTE_ID, noteId);

        NoteDetailFragment fragment = new NoteDetailFragment();

        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.notes_detail_fragment, container, false);

        setHasOptionsMenu(true);

        textTitle = (TextView) root.findViewById(R.id.note_detail_title);

        textText = (TextView) root.findViewById(R.id.note_detail_text);

        checkMarked = (CheckBox) root.findViewById(R.id.note_detail_marked);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_note);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.editNote();
            }
        });

        return root;
    }

    @Override
    public void setPresenter(@NonNull NoteDetailContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes_detail_menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.deleteNote();
                return true;
        }
        return false;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            textTitle.setText("");
            textText.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showMissingNote() {
        textTitle.setText("");
        textText.setText("No Information");
    }

    @Override
    public void hideTitle() {
        textTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {
        textTitle.setVisibility(View.VISIBLE);
        textTitle.setText(title);
    }

    @Override
    public void hideText() {
        textText.setVisibility(View.GONE);
    }

    @Override
    public void showText(String text) {
        textText.setVisibility(View.VISIBLE);
        textText.setText(text);
    }

    @Override
    public void showMarkedStatus(boolean marked) {

        Preconditions.checkNotNull(checkMarked);

        checkMarked.setChecked(marked);

        checkMarked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.markedNote();
            }
        });


    }

    @Override
    public void showEditNote(String noteId) {

        Intent intent = new Intent(getContext(), null);

        intent.putExtra(null, noteId);

        startActivity(intent);

    }

    @Override
    public void showNoteDeleted() {
        getActivity().finish();
    }

    @Override
    public void showNoteMarked() {
        Snackbar.make(getView(), "Note marked", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_EDIT_NOTE){

            if (resultCode == Activity.RESULT_OK){
                getActivity().finish();
            }

        }

    }
}
