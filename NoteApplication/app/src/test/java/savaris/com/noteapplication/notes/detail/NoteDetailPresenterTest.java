package savaris.com.noteapplication.notes.detail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesDatasource;
import savaris.com.noteapplication.data.source.NotesRepository;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rafael.savaris on 01/06/2017.
 */

public class NoteDetailPresenterTest {

    public static final String TITLE = "title";

    public static final String TEXT = "text";

    public static final Note NOTE = new Note(TITLE, TEXT);

    public static final String INVALID_NOTE_ID = "";

    private NoteDetailPresenter noteDetailPresenter;

    @Mock
    private NoteDetailContract.View view;

    @Mock
    private NotesRepository notesRepository;

    @Captor
    private ArgumentCaptor<NotesDatasource.GetNoteCallback> getNoteCallbackArgumentCaptor;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);

        when(view.isActive()).thenReturn(true);

    }

    @Test
    public void createPresenter_setsPresenterToView(){

        noteDetailPresenter = new NoteDetailPresenter(NOTE.getId(), notesRepository, view);

        verify(view).setPresenter(noteDetailPresenter);

    }

    @Test
    public void getMarkedNoteFromRepositoryAndLoadView(){

        noteDetailPresenter = new NoteDetailPresenter(NOTE.getId(), notesRepository, view);

        noteDetailPresenter.start();

        verify(notesRepository).getNote(eq(NOTE.getId()), getNoteCallbackArgumentCaptor.capture());

        InOrder inOrder = inOrder(view);

        inOrder.verify(view).setLoadingIndicator(true);

        getNoteCallbackArgumentCaptor.getValue().onNoteLoaded(NOTE);

        inOrder.verify(view).setLoadingIndicator(false);

        verify(view).showTitle(TITLE);

        verify(view).showText(TEXT);

        verify(view).showMarkedStatus(false);

    }

    @Test
    public void getUnknownNoteFromRepositoryAndLoadIntoView(){

        noteDetailPresenter = new NoteDetailPresenter(INVALID_NOTE_ID, notesRepository, view);

        noteDetailPresenter.start();

        verify(view).showMissingNote();

    }

    @Test
    public void deleteNote(){

        Note note = new Note(TITLE, TEXT);

        noteDetailPresenter = new NoteDetailPresenter(note.getId(), notesRepository, view);

        noteDetailPresenter.deleteNote();

        verify(notesRepository).deleteNote(note.getId());

        verify(view).showNoteDeleted();

    }

    @Test
    public void markNote(){

        Note note = new Note(TITLE, TEXT);

        noteDetailPresenter = new NoteDetailPresenter(note.getId(), notesRepository, view);

        noteDetailPresenter.markedNote();

        verify(notesRepository).markNote(note.getId());

        verify(view).showNoteMarked();

    }

    @Test
    public void markNoteIsShowWhenEditing(){

        noteDetailPresenter = new NoteDetailPresenter(NOTE.getId(), notesRepository, view);

        noteDetailPresenter.editNote();

        verify(view).showEditNote(NOTE.getId());

    }

    @Test
    public void invalidNoteIsNotShowWhenEditing(){

        noteDetailPresenter = new NoteDetailPresenter(INVALID_NOTE_ID, notesRepository, view);

        noteDetailPresenter.editNote();

        verify(view, never()).showEditNote(INVALID_NOTE_ID);

        verify(view).showMissingNote();

    }


}
