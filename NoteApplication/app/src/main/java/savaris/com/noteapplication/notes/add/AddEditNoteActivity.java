package savaris.com.noteapplication.notes.add;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import savaris.com.noteapplication.Injection;
import savaris.com.noteapplication.R;
import savaris.com.noteapplication.utils.ActivityUtils;

public class AddEditNoteActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private AddEditNotePresenter addEditNotePresenter;

    public static final String SHOULD_LOAD_DATA = "SHOULD_LOAD_DATA";

    public static final int REQUEST_ADD_NOTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_note_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);

        AddEditNoteFragment addEditNoteFragment = (AddEditNoteFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String noteId = getIntent().getStringExtra(AddEditNoteFragment.EDIT_NOTE_ID);

        setToolbarTitle(noteId);

        if (addEditNoteFragment == null){

            addEditNoteFragment = AddEditNoteFragment.newInstance();

            if (getIntent().hasExtra(AddEditNoteFragment.EDIT_NOTE_ID)){

                Bundle bundle = new Bundle();

                bundle.putString(AddEditNoteFragment.EDIT_NOTE_ID, noteId);

                addEditNoteFragment.setArguments(bundle);

            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), addEditNoteFragment, R.id.contentFrame);

        }

        boolean shouldLoadData = true;

        if (savedInstanceState != null){
            shouldLoadData = savedInstanceState.getBoolean(AddEditNoteActivity.SHOULD_LOAD_DATA);
        }

        addEditNotePresenter = new AddEditNotePresenter(noteId, Injection.provideNotesRepository(getApplicationContext()), addEditNoteFragment, shouldLoadData);

    }

    private void setToolbarTitle(@Nullable String taskId) {
        if(taskId == null) {
            actionBar.setTitle(R.string.add_note);
        } else {
            actionBar.setTitle(R.string.edit_note);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SHOULD_LOAD_DATA, addEditNotePresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
