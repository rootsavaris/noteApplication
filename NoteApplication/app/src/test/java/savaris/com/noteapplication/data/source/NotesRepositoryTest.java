package savaris.com.noteapplication.data.source;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import savaris.com.noteapplication.data.source.local.NotesLocalDataSource;
import savaris.com.noteapplication.data.source.remote.NotesRemoteDataSource;

/**
 * Created by rafael.savaris on 06/06/2017.
 */

public class NotesRepositoryTest {

    @Mock
    NotesRemoteDataSource notesRemoteDataSource;

    @Mock
    NotesLocalDataSource notesLocalDataSource;

    @Captor
    private ArgumentCaptor<NotesDatasource.LoadNotesCallback> loadNotesCallbackArgumentCaptor;

    private NotesRepository notesRepository;

    @Before
    public void setupNotesRepository(){

        MockitoAnnotations.initMocks(this);

        notesRepository = NotesRepository.getInstance(notesRemoteDataSource, notesLocalDataSource);

    }

    @After
    public void detroyNotesRepository(){
        NotesRepository.destroyInstance();
    }

    @Test
    public void getNotes_repositoryCacheAfterFirstApiCall(){




    }

    private void

}
