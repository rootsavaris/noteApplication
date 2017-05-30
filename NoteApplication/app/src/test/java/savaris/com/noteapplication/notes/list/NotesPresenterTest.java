package savaris.com.noteapplication.notes.list;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesDatasource;
import savaris.com.noteapplication.data.source.NotesRepository;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rafael.savaris on 29/05/2017.
 */

public class NotesPresenterTest {

    private static List<Note> notes;

    private NotesPresenter notesPresenter;

    @Mock
    private NotesRepository notesRepository;

    @Mock
    private NotesContract.View view;

    @Captor
    private ArgumentCaptor<NotesDatasource.LoadNotesCallback> loadNotesCallbackArgumentCaptor;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);

        notesPresenter = new NotesPresenter(notesRepository, view);

        when(view.isActive()).thenReturn(true);

        notes = Lists.newArrayList(new Note("Title1", "Description1"), new Note("Title2", "Description2", true), new Note("Title3", "Description3", true));

    }

    @Test
    public void createPresenter(){

        notesPresenter = new NotesPresenter(notesRepository, view);

        verify(view).setPresenter(notesPresenter);

    }

    @Test
    public void loadAllNotes(){

        notesPresenter.setFiltering(NotesFilterType.ALL_NOTES);

        notesPresenter.loadNotes(true);

        verify(notesRepository).getNotes(loadNotesCallbackArgumentCaptor.capture());

        loadNotesCallbackArgumentCaptor.getValue().onNotesLoaded(notes);

        InOrder inOrder = inOrder(view);

        inOrder.verify(view).setLoadingIndicator(true);

        inOrder.verify(view).setLoadingIndicator(false);

        ArgumentCaptor<List> showNotesArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(view).showNotes(showNotesArgumentCaptor.capture());

        assertTrue(showNotesArgumentCaptor.getValue().size() == 3);

    }

}
