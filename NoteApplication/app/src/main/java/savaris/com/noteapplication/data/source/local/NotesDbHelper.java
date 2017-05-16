package savaris.com.noteapplication.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rafael.savaris on 16/05/2017.
 */

public class NotesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Notes.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NotesPersistenceContract.NotesEntry.TABLE_NAME + "(" +
                    NotesPersistenceContract.NotesEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    NotesPersistenceContract.NotesEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    NotesPersistenceContract.NotesEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    NotesPersistenceContract.NotesEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    NotesPersistenceContract.NotesEntry.COLUMN_NAME_MARKED + BOOLEAN_TYPE +
                    " )";

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
