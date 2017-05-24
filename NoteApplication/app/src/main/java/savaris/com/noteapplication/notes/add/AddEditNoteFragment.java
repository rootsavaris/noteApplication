package savaris.com.noteapplication.notes.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
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

    }

    @Override
    public void showNoteList() {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setText(String text) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
