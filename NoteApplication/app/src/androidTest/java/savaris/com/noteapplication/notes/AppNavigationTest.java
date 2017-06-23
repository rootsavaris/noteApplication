package savaris.com.noteapplication.notes;

import android.support.test.espresso.NoActivityResumedException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import savaris.com.noteapplication.R;
import savaris.com.noteapplication.notes.list.NotesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.fail;
import static savaris.com.noteapplication.TestUtils.getToolbarNavigationContentDescription;
import static savaris.com.noteapplication.custom.action.NavigationViewActions.navigateTo;

/**
 * Created by rafael.savaris on 13/06/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppNavigationTest {

    @Rule
    public ActivityTestRule<NotesActivity> notesActivityActivityTestRule = new ActivityTestRule<NotesActivity>(NotesActivity.class);

    @Test
    public void clicOnAndroidHomeIcon_OpensNavigation(){

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)));

        onView(withContentDescription(getToolbarNavigationContentDescription(notesActivityActivityTestRule.getActivity(), R.id.toolbar))).perform(click());

        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.LEFT)));

    }

    @Test
    public void backFromNotesScreen_ExitsApp(){
        assertPressingBackExitsApp();
    }

    @Test
    public void backFromNotesScreenAfterStats_ExitsApp(){

        openNotesScreen();

        assertPressingBackExitsApp();

    }

    private void assertPressingBackExitsApp(){

        try{

            pressBack();

            fail("Should kill the app and throw an exceptuin");

        }catch (NoActivityResumedException ex){

        }

    }

    private void openNotesScreen(){

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(open());

        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.notes_list));

    }





}
