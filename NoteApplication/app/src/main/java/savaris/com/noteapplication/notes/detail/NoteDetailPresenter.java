package savaris.com.noteapplication.notes.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import savaris.com.noteapplication.data.Note;
import savaris.com.noteapplication.data.source.NotesDatasource;
import savaris.com.noteapplication.data.source.NotesRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 24/05/2017.
 */

public class NoteDetailPresenter implements NoteDetailContract.Presenter {

    @Nullable
    private String noteId;

    private final NotesRepository notesRepository;

    private final NoteDetailContract.View noteView;

    public NoteDetailPresenter(String noteId, @NonNull NotesRepository notesRepository, @NonNull NoteDetailContract.View noteView) {
        this.noteId = noteId;
        this.notesRepository = checkNotNull(notesRepository);
        this.noteView = checkNotNull(noteView);
        this.noteView.setPresenter(this);
    }

    @Override
    public void start() {
        openNote();
    }

    @Override
    public void editNote() {

        if (Strings.isNullOrEmpty(noteId)){
            noteView.showMissingNote();
            return;
        }

        noteView.showEditNote(noteId);

    }

    @Override
    public void deleteNote() {

        if (Strings.isNullOrEmpty(noteId)){
            noteView.showMissingNote();
            return;
        }

        notesRepository.deleteNote(noteId);
        noteView.showNoteDeleted();

    }

    @Override
    public void markedNote() {

        if (Strings.isNullOrEmpty(noteId)){
            noteView.showMissingNote();
            return;
        }

        notesRepository.markNote(noteId);
        noteView.showNoteMarked();

    }

    private void openNote(){

        if (Strings.isNullOrEmpty(noteId)){
            noteView.showMissingNote();
            return;
        }

        noteView.setLoadingIndicator(true);

        notesRepository.getNote(noteId, new NotesDatasource.GetNoteCallback() {

            @Override
            public void onNoteLoaded(Note note) {

                if (!noteView.isActive()){
                    return;
                }

                noteView.setLoadingIndicator(false);

                if (note == null){
                    noteView.showMissingNote();
                } else {
                    showNote(note);
                }

            }

            @Override
            public void onDataNotAvailable() {

                if (!noteView.isActive()){
                    return;
                }

                noteView.showMissingNote();

            }

        });

    }

    private void showNote(@NonNull Note note){

        String title = note.getTitle();
        String text = note.getText();

        if (Strings.isNullOrEmpty(title)){
            noteView.hideTitle();
        } else {
            noteView.showTitle(title);
        }

        if (Strings.isNullOrEmpty(text)){
            noteView.hideText();;
        } else {
            noteView.showText(text);
        }

        noteView.showMarkedStatus(note.isMarked());

    }

}
