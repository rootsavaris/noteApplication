package savaris.com.noteapplication.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Not;

import java.util.List;

import savaris.com.noteapplication.data.source.NotesDatasource;
import savaris.com.noteapplication.data.source.local.NotesLocalDataSource;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by rafael.savaris on 12/06/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotesLocalDataSourceTest {

    private NotesLocalDataSource notesLocalDataSource;

    private String TITLE1 = "title1";

    private String TITLE2 = "title2";

    private String TITLE3 = "title3";

    @Before
    public void setup(){

        notesLocalDataSource = NotesLocalDataSource.getInstance(InstrumentationRegistry.getTargetContext());

    }

    @After
    public void cleanUp(){
        notesLocalDataSource.deleteAllNotes();
    }

    @Test
    public void testPreConditions(){
        Assert.assertNotNull(notesLocalDataSource);
    }

    @Test
    public void saveNote_retrieveNote(){

        final Note note = new Note(TITLE1, "");

        notesLocalDataSource.saveNote(note);

        notesLocalDataSource.getNote(note.getId(), new NotesDatasource.GetNoteCallback() {

            @Override
            public void onNoteLoaded(Note note) {
                Assert.assertThat(note, is(note));
            }

            @Override
            public void onDataNotAvailable() {
                fail("CallBackError");
            }
        });

    }

    @Test
    public void markNote_retrieveNoteIsMarked(){

        final Note note = new Note(TITLE1, "");

        notesLocalDataSource.saveNote(note);

        notesLocalDataSource.markNote(note);

        notesLocalDataSource.getNote(note.getId(), new NotesDatasource.GetNoteCallback() {

            @Override
            public void onNoteLoaded(Note note) {

                assertThat(note, is(note));
                assertThat(note.isMarked(), is(true));

            }

            @Override
            public void onDataNotAvailable() {
                fail("callBack Error");
            }
        });

    }

    @Test
    public void clearMarkedNote_noteNotretrievable(){

        NotesDatasource.GetNoteCallback callback1 = mock(NotesDatasource.GetNoteCallback.class);
        NotesDatasource.GetNoteCallback callback2 = mock(NotesDatasource.GetNoteCallback.class);
        NotesDatasource.GetNoteCallback callback3 = mock(NotesDatasource.GetNoteCallback.class);

        final Note note1 = new Note(TITLE1, "");
        notesLocalDataSource.saveNote(note1);
        notesLocalDataSource.markNote(note1);

        final Note note2 = new Note(TITLE2, "");
        notesLocalDataSource.saveNote(note2);
        notesLocalDataSource.markNote(note2);

        final Note note3 = new Note(TITLE3, "");
        notesLocalDataSource.saveNote(note3);

        notesLocalDataSource.clearMarkedNotes();

        notesLocalDataSource.getNote(note1.getId(), callback1);

        verify(callback1).onDataNotAvailable();

        verify(callback1, never()).onNoteLoaded(note1);


        notesLocalDataSource.getNote(note2.getId(), callback2);

        verify(callback2).onDataNotAvailable();

        verify(callback2, never()).onNoteLoaded(note1);

        notesLocalDataSource.getNote(note3.getId(), callback3);

        verify(callback3, never()).onDataNotAvailable();

        verify(callback3).onNoteLoaded(note3);

    }

    @Test
    public void deleteAllNotes_emptyListOfretrieveNote(){

        Note note = new Note(TITLE1, "");

        notesLocalDataSource.saveNote(note);

        NotesDatasource.LoadNotesCallback callback = mock(NotesDatasource.LoadNotesCallback.class);

        notesLocalDataSource.deleteAllNotes();

        notesLocalDataSource.getNotes(callback);

        verify(callback).onDataNotAvailable();

        verify(callback, never()).onNotesLoaded(anyList());

    }

    @Test
    public void getNotes_retrieveSavedNotes(){

        final Note note1 = new Note(TITLE1, "");

        notesLocalDataSource.saveNote(note1);

        final Note note2 = new Note(TITLE2, "");

        notesLocalDataSource.saveNote(note2);

        notesLocalDataSource.getNotes(new NotesDatasource.LoadNotesCallback() {

            @Override
            public void onNotesLoaded(List<Note> notes) {

                assertNotNull(notes);

                assertTrue(notes.size() >= 2);

                boolean newNote1Found = false;

                boolean newNote2Found = false;

                for(Note note : notes){

                    if (note.getId().equals(note1.getId())){
                        newNote1Found = true;
                    }

                    if (note.getId().equals(note2.getId())){
                        newNote2Found = true;
                    }

                }

                assertTrue(newNote1Found);
                assertTrue(newNote2Found);

            }

            @Override
            public void onDataNotAvailable() {
                fail();
            }
        });

    }

}
