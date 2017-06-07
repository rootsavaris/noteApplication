package savaris.com.noteapplication.data.source;

import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.local.NotesLocalDataSource;
import savaris.com.noteapplication.data.source.remote.NotesRemoteDataSource;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.booleanThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by rafael.savaris on 06/06/2017.
 */

public class NotesRepositoryTest {

    @Mock
    NotesRemoteDataSource notesRemoteDataSource;

    @Mock
    NotesLocalDataSource notesLocalDataSource;

    @Mock
    NotesDatasource.LoadNotesCallback loadNotesCallback;

    @Mock
    NotesDatasource.GetNoteCallback getNoteCallback;

    @Captor
    private ArgumentCaptor<NotesDatasource.LoadNotesCallback> loadNotesCallbackArgumentCaptor;

    private NotesRepository notesRepository;

    private static List<Note> NOTES = Lists.newArrayList(new Note("Title1", "Text"), new Note("Title2", "Text"));

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

        twoNotesLoadCallsToRepository(loadNotesCallback);

        verify(notesRemoteDataSource).getNotes(any(NotesDatasource.LoadNotesCallback.class));

    }

    @Test
    public void getNotes_requestAllNotesFromLocalDatasource() {

        notesRepository.getNotes(loadNotesCallback);

        verify(notesLocalDataSource).getNotes(any(NotesDatasource.LoadNotesCallback.class));

    }

    @Test
    public void saveNote_savesNoteToServiceAPI(){

        Note note = new Note("Title", "Text");

        notesRepository.saveNote(note);

        verify(notesRemoteDataSource).saveNote(note);
        verify(notesLocalDataSource).saveNote(note);

        assertThat(notesRepository.cachedNotes.size(), is(1));

    }

    @Test
    public void markNote_marksNoteToServiceApiUpdateCache(){

        Note note = new Note("Title", "Text");

        notesRepository.saveNote(note);

        notesRepository.markNote(note);

        verify(notesRemoteDataSource).markNote(note);

        verify(notesLocalDataSource).markNote(note);

        assertThat(notesRepository.cachedNotes.size(), is(1));

        assertThat(notesRepository.cachedNotes.get(note.getId()).isMarked(), is(true));

    }

    @Test
    public void markNoteId_marksNoteToServiceApiUpdateCache(){

        Note note = new Note("Title", "Text");

        notesRepository.saveNote(note);

        notesRepository.markNote(note.getId());

        verify(notesRemoteDataSource).markNote(note);

        verify(notesLocalDataSource).markNote(note);

        assertThat(notesRepository.cachedNotes.size(), is(1));

        assertThat(notesRepository.cachedNotes.get(note.getId()).isMarked(), is(true));

    }

    @Test
    public void getNote_requestSingleNoteFromLocalDataSource(){

        notesRepository.getNote("title", getNoteCallback);

        verify(notesLocalDataSource).getNote(eq("title"), any(NotesDatasource.GetNoteCallback.class));

    }

    @Test
    public void deleteMarkedNotes_deleteMarkedNotesToServiceAPIUpdatesCache(){

        Note note = new Note("Title", "Text", true);

        notesRepository.saveNote(note);

        Note note2 = new Note("Title2", "Text");

        notesRepository.saveNote(note2);

        Note note3 = new Note("Title", "Text", true);

        notesRepository.saveNote(note3);

        notesRepository.clearMarkedNotes();

        verify(notesRemoteDataSource).clearMarkedNotes();

        verify(notesLocalDataSource).clearMarkedNotes();

        assertThat(notesRepository.cachedNotes.size(), is(1));

        assertTrue(!notesRepository.cachedNotes.get(note2.getId()).isMarked());

        assertThat(notesRepository.cachedNotes.get(note2.getId()).getTitle(), is("Title2"));

    }

    @Test
    public void deleteAllNotes_deleteNotesToServiceAPIUpdatesCache(){

        Note note = new Note("Title", "Text", true);

        notesRepository.saveNote(note);

        Note note2 = new Note("Title2", "Text");

        notesRepository.saveNote(note2);

        Note note3 = new Note("Title", "Text", true);

        notesRepository.saveNote(note3);

        notesRepository.deleteAllNotes();

        verify(notesRemoteDataSource).deleteAllNotes();

        verify(notesLocalDataSource).deleteAllNotes();

        assertThat(notesRepository.cachedNotes.size(), is(0));

    }

    @Test
    public void deleteNote_deleteNoteToServiceAPIRemovedFromCache(){

        Note note = new Note("Title", "Text", true);

        notesRepository.saveNote(note);

        assertThat(notesRepository.cachedNotes.containsKey(note.getId()), is(true));

        notesRepository.deleteNote(note.getId());

        verify(notesRemoteDataSource).deleteNote(note.getId());

        verify(notesLocalDataSource).deleteNote(note.getId());

        assertThat(notesRepository.cachedNotes.containsKey(note.getId()), is(false));

    }

    @Test
    public void getNotesWithDirtyCache_notesAreRetrievedFromRemote(){

        notesRepository.refreshNotes();

        notesRepository.getNotes(loadNotesCallback);

        setNotesAvailable(notesRemoteDataSource, NOTES);

        verify(notesLocalDataSource, never()).getNotes(loadNotesCallback);

        verify(loadNotesCallback).onNotesLoaded(NOTES);

    }

    private void setNotesAvailable(NotesDatasource datasource, List<Note> notes){

        verify(datasource).getNotes(loadNotesCallbackArgumentCaptor.capture());

        loadNotesCallbackArgumentCaptor.getValue().onNotesLoaded(notes);

    }


    private void twoNotesLoadCallsToRepository(NotesDatasource.LoadNotesCallback  callback){

        notesRepository.getNotes(callback);

        verify(notesLocalDataSource).getNotes(loadNotesCallbackArgumentCaptor.capture());

        loadNotesCallbackArgumentCaptor.getValue().onDataNotAvailable();

        verify(notesRemoteDataSource).getNotes(loadNotesCallbackArgumentCaptor.capture());

        loadNotesCallbackArgumentCaptor.getValue().onDataNotAvailable();

        notesRepository.getNotes(callback);

    }


}
