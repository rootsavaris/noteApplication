package savaris.com.noteapplication.notes;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesDatasource;
import savaris.com.noteapplication.data.source.NotesRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 19/05/2017.
 */

public class NotesPresenter implements NotesContract.Presenter{

    private final NotesRepository notesRepository;

    private final NotesContract.View notesView;

    private NotesFilterType notesFilterType = NotesFilterType.ALL_NOTES;

    private boolean firstLoad = true;

    public NotesPresenter(@NonNull NotesRepository notesRepository, @NonNull NotesContract.View notesView){
        this.notesRepository = checkNotNull(notesRepository, "Repository can not be null");
        this.notesView = checkNotNull(notesView, "View can not be null");
        this.notesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadNotes(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {



    }

    @Override
    public void loadNotes(boolean forceUpdate) {

        loadNotes(forceUpdate || firstLoad, true);

        firstLoad = false;

    }

    private void loadNotes(boolean forceUpdate, final boolean showLoading){

        if (showLoading){
            notesView.setLoadingIndicator(true);
        }

        if (forceUpdate){
            notesRepository.refreshNotes();
        }

        notesRepository.getNotes(new NotesDatasource.LoadNotesCallback() {

            @Override
            public void onNotesLoaded(List<Note> notes) {

                List<Note> notesList = new ArrayList<>();

                for(Note note : notes){

                    switch (notesFilterType){

                        case ALL_NOTES:
                            notesList.add(note);
                            break;
                        case MARKED_NOTES:
                            if (note.isMarked()){
                                notesList.add(note);
                            }
                            break;
                        default:
                            notesList.add(note);
                            break;
                    }

                }

                if (showLoading){
                    notesView.setLoadingIndicator(false);
                }

                processNotes(notesList);

            }

            @Override
            public void onDataNotAvailable() {
                notesView.showLoadingNotesError();
            }

        });

    }

    @Override
    public void addNewNote() {
        notesView.showAddNote();
    }

    @Override
    public void openNoteDetails(@NonNull Note requestedNote) {
        checkNotNull(requestedNote, "note can not be null");
        notesView.showNoteDetailsUi(requestedNote.getId());
    }

    @Override
    public void marketNote(@NonNull Note markedNote) {
        checkNotNull(markedNote, "markedNote can not be null");
        notesRepository.markNote(markedNote);
        notesView.showNoteMarked();
        loadNotes(false, false);
    }

    @Override
    public void clearMarketNotes() {
        notesRepository.clearMarkedNotes();
        loadNotes(false, false);
    }

    @Override
    public void setFiltering(NotesFilterType requestType) {
        notesFilterType = requestType;
    }

    @Override
    public NotesFilterType getFiltering() {
        return notesFilterType;
    }

    private void processNotes(List<Note> notes){

        if (notes.isEmpty()){
            processEmptyNotes();
        } else {
            notesView.showNotes(notes);
            showFilterLabel();
        }

    }

    private void processEmptyNotes(){

        switch (notesFilterType){

            case MARKED_NOTES:
                notesView.showNoMarkedNotes();
                break;
            default:
                notesView.showNoNotes();
                break;
        }

    }

    private void showFilterLabel(){

        switch (notesFilterType){

            case MARKED_NOTES:
                notesView.showMarkedFilterLabel();
                break;
            default:
                notesView.showAllFilterLabel();
                break;
        }

    }

}
