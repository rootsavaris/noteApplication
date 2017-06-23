package savaris.com.noteapplication.notes;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;

import android.text.TextUtils;
import android.view.View;
import savaris.com.noteapplication.Injection;
import savaris.com.noteapplication.TestUtils;
import savaris.com.noteapplication.notes.list.NotesActivity;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkArgument;

/**
 * Created by rafael.savaris on 22/06/2017.
 */

public class NotesScreenTest {

    @Rule
    public ActivityTestRule<NotesActivity> notesActivityActivityTestRule = new ActivityTestRule<NotesActivity>(NotesActivity.class){

        @Override
        protected void beforeActivityLaunched() {

            super.beforeActivityLaunched();

            Injection.provideNotesRepository(InstrumentationRegistry.getTargetContext()).deleteAllNotes();

        }
    };

    private Matcher<View> withItemText(final String itemText){

        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");

        return new Typr



    }

}
