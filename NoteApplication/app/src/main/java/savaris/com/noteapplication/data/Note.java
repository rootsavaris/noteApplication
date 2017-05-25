package savaris.com.noteapplication.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Created by rafael.savaris on 09/05/2017.
 */

public class Note {

    @NonNull
    private final String id;

    @NonNull
    private final String title;

    @NonNull
    private final String text;

    @NonNull
    private final boolean marked;

    public Note(@Nullable String title, @Nullable String text,
                @NonNull String id, boolean market) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.marked = market;
    }

    public Note(@Nullable String title, @Nullable String text, boolean market) {
        this(title, text, UUID.randomUUID().toString(), market);
    }

    public Note(@Nullable String title, @Nullable String text, @NonNull String id) {
        this(title, text, id, false);
    }

    public Note(@Nullable String title, @Nullable String text) {
        this(title, text, UUID.randomUUID().toString(), false);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getText() {
        return text;
    }

    @NonNull
    public boolean isMarked() {
        return marked;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(title) &&
                Strings.isNullOrEmpty(text);
    }


}
