package savaris.com.noteapplication.notes.list;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesRepository;

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




    }

}
