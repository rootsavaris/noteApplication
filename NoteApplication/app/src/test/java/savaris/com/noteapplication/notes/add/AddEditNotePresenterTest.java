package savaris.com.noteapplication.notes.add;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesDatasource;
import savaris.com.noteapplication.data.source.NotesRepository;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rafael.savaris on 05/06/2017.
 */

public class AddEditNotePresenterTest {

    @Mock
    private NotesRepository notesRepository;

    @Mock
    private AddEditNoteContract.View view;

    @Captor
    private ArgumentCaptor<NotesDatasource.GetNoteCallback> getNoteCallbackArgumentCaptor;

    private AddEditNotePresenter addEditNotePresenter;

    @Before
    public void setupMocksAbdView(){

        MockitoAnnotations.initMocks(this);

        when(view.isActive()).thenReturn(true);

    }

    @Test
    public void createPresenter_setsThePresenterToView(){

        addEditNotePresenter = new AddEditNotePresenter(null, notesRepository, view, true);

        verify(view).setPresenter(addEditNotePresenter);

    }

    @Test
    public void saveNewNoteToRepository_showSuccessMessageUi(){

        addEditNotePresenter = new AddEditNotePresenter(null, notesRepository, view, true);

        addEditNotePresenter.saveNote("Test", "Test");

        verify(notesRepository).saveNote(any(Note.class));

        verify(view).showNoteList();

    }

    @Test
    public void saveEmptyNote_ShowErrorUi(){

        addEditNotePresenter = new AddEditNotePresenter(null, notesRepository, view, true);

        addEditNotePresenter.saveNote("", "");

        verify(view).showEmptyNoteError();

    }

    @Test
    public void saveExistingNoteToRepository_showSuccessMessageUi() {

        addEditNotePresenter = new AddEditNotePresenter(null, notesRepository, view, true);

        addEditNotePresenter.saveNote("TITLE", "TEXT");

        verify(notesRepository).saveNote(any(Note.class));

        verify(view).showNoteList();

    }

    @Test
    public void populateNote_callsRepoAndUpdateView(){

        Note note = new Note("TITLE", "TEXT");

        addEditNotePresenter = new AddEditNotePresenter(note.getId(), notesRepository, view, true);

        addEditNotePresenter.populateNote();

        verify(notesRepository).getNote(eq(note.getId()), getNoteCallbackArgumentCaptor.capture());

        assertThat(addEditNotePresenter.isDataMissing(), is(true));

        getNoteCallbackArgumentCaptor.getValue().onNoteLoaded(note);

        verify(view).setTitle(note.getTitle());

        verify(view).setText(note.getText());

        assertThat(addEditNotePresenter.isDataMissing(), is(false));

    }

}
