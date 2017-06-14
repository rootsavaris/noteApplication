package savaris.com.noteapplication;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

/**
 * Created by rafael.savaris on 13/06/2017.
 */

public class TestUtils {

    public static String getToolbarNavigationContentDescription(@NonNull Activity activity, @IdRes int toolbar1){

        Toolbar toolbar = (Toolbar) activity.findViewById(toolbar1);

        if (toolbar != null){
            return (String) toolbar.getNavigationContentDescription();
        } else {
            throw new RuntimeException();
        }

    }

}
