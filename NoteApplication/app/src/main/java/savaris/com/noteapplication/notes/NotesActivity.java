package savaris.com.noteapplication.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import savaris.com.noteapplication.R;

/**
 * Created by rafael.savaris on 18/05/2017.
 */

public class NotesActivity extends AppCompatActivity{

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_activity);

    }
}
