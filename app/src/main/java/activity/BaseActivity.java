package activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Base activity class. This may be useful in
 * Implementing google analytics or
 * Any app wise implementation
 **/

public abstract class BaseActivity extends FragmentActivity {
    private static List<Activity> sActivities = new ArrayList<Activity>();
	/*private static TextView actionBarTitle;
	private static ImageView rightButton ;
	private static ImageView leftButton;*/

    public static void finishAllActivities() {
        for (int i = 0; i < sActivities.size(); i++) {
            sActivities.get(i).finish();
        }
    }

    public static void setHeader(View view, int leftButtonId, int rightButtonId, String title, OnClickListener leftButtonListener, OnClickListener rightButtonListener) {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        sActivities.remove(this);
        super.onDestroy();
    }
}