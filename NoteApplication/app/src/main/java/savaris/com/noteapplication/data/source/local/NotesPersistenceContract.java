package savaris.com.noteapplication.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by rafael.savaris on 16/05/2017.
 */

public final class NotesPersistenceContract {

    private NotesPersistenceContract(){}

    public static abstract class NotesEntry implements BaseColumns {

        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_ENTRY_ID = "entryId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_MARKED = "marked";

    }

}
