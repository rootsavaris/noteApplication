package savaris.com.noteapplication;

import android.content.Context;
import android.support.annotation.NonNull;

import savaris.com.noteapplication.data.source.NotesRepository;
import savaris.com.noteapplication.data.source.local.NotesLocalDataSource;
import savaris.com.noteapplication.data.source.remote.NotesRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 19/05/2017.
 */

public class Injection {

    public static NotesRepository provideNotesRepository(@NonNull Context context){

        checkNotNull(context);

        return NotesRepository.getInstance(NotesRemoteDataSource.getInstance(), NotesLocalDataSource.getInstance(context));

    }

}
