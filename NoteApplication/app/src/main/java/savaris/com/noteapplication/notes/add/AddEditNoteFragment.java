package savaris.com.noteapplication.notes.add;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import savaris.com.noteapplication.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 24/05/2017.
 */

public class AddEditNoteFragment extends Fragment implements AddEditNoteContract.View{

    public static final String EDIT_NOTE_ID = "EDIT_NOTE_ID";

    private AddEditNoteContract.Presenter presenter;

    private TextView textTitle;

    private TextView textText;

    public static AddEditNoteFragment newInstance(){
        return new AddEditNoteFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_note_done);

        fab.setImageResource(R.drawable.ic_done);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveNote(textTitle.getText().toString(), textText.getText().toString());
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.add_note_fragment, container, false);
        textTitle = (TextView) root.findViewById(R.id.add_note_title);
        textText = (TextView) root.findViewById(R.id.add_note_text);
        setHasOptionsMenu(true);
        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    public AddEditNoteFragment(){}

    @Override
    public void setPresenter(@NonNull AddEditNoteContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void showEmptyNoteError() {
        Snackbar.make(textTitle, getString(R.string.empty_note_message), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void showNoteList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        textTitle.setText(title);
    }

    @Override
    public void setText(String text) {
        textText.setText(text);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
