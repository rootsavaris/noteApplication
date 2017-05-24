package savaris.com.noteapplication.notes.detail;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import savaris.com.noteapplication.Injection;
import savaris.com.noteapplication.R;
import savaris.com.noteapplication.utils.ActivityUtils;

public class NoteDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "NOTE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_detail_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        ab.setDisplayShowHomeEnabled(true);

        String noteId = getIntent().getStringExtra(EXTRA_NOTE_ID);

        NoteDetailFragment noteDetailFragment = (NoteDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (noteDetailFragment == null){

            noteDetailFragment = NoteDetailFragment.newInstance(noteId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), noteDetailFragment, R.id.contentFrame);

        }

        new NoteDetailPresenter(noteId, Injection.provideNotesRepository(getApplicationContext()), noteDetailFragment);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
