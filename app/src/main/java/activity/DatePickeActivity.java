package activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.administrator.shaktiTransportApp.R;


public class DatePickeActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private ViewHolder mViewHolder;
    private Context mContext;

    public static Intent getIntent(Context context, String dd, String mm, String yy, boolean bound_max) {
        Intent intent = new Intent(context, DatePickeActivity.class);
        intent.putExtra("message", "");
        intent.putExtra("dd", dd);
        intent.putExtra("mm", mm);
        intent.putExtra("yy", yy);
        intent.putExtra("bound_max", bound_max);
        return intent;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_view);
        mContext = this;
        mViewHolder = new ViewHolder(findViewById(R.id.main_rl), this, this);

        String message = getIntent().getStringExtra("message");

        boolean max = getIntent().getBooleanExtra("bound_max", true);


        mViewHolder.mDatePicker.setMaxDate(System.currentTimeMillis() - 1000 + (1000 * 60 * 60 * 24 * 14));

        mViewHolder.mDatePicker.setMinDate(System.currentTimeMillis() - 1000);

        setSelectedDate();

        startAnimation();
    }

    void setSelectedDate() {
        try {
            String hey = getIntent().getStringExtra("dd");
            int dd = Integer.parseInt(getIntent().getStringExtra("dd"));
            int mm = Integer.parseInt(getIntent().getStringExtra("mm"));
            int yy = Integer.parseInt(getIntent().getStringExtra("yy"));
            if (dd != 0) {
                mViewHolder.mDatePicker.updateDate(yy, mm - 1, dd);
            }
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        reverseAnimation(null);
    }

    private void startAnimation() {
        // Cancels any animations for this container.
        mViewHolder.mContainerLl.clearAnimation();
        mViewHolder.mContainerRl.clearAnimation();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce_zoom_in_full);
        mViewHolder.mContainerLl.startAnimation(animation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        mViewHolder.mContainerRl.startAnimation(alphaAnimation);
    }

    /**
     * Animation from top to bottom
     */
    private void reverseAnimation(final Intent intent) {
        mViewHolder.mContainerLl.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bounce_zoom_out_full);
        mViewHolder.mContainerLl.startAnimation(animation);
        mViewHolder.mContainerLl.setVisibility(View.GONE);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500);
        mViewHolder.mContainerRl.startAnimation(alphaAnimation);
        mViewHolder.mContainerRl.setVisibility(View.GONE);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mViewHolder.mContainerRl.setVisibility(View.GONE);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("year", "" + mViewHolder.mDatePicker.getYear());
                returnIntent.putExtra("month", "" + (mViewHolder.mDatePicker.getMonth() + 1));
                returnIntent.putExtra("date", "" + mViewHolder.mDatePicker.getDayOfMonth());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                reverseAnimation(null);
                break;
            case R.id.innerrl:
                reverseAnimation(null);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();

        return false;
    }

    public class ViewHolder {
        //    private TextView messageTV;
        private Button mBackButton, mActionButton;
        private RelativeLayout mContainerLl;
        private RelativeLayout mContainerRl;
        private RelativeLayout mDialogBGRL;
        private DatePicker mDatePicker;


        public ViewHolder(View view, View.OnClickListener listener, View.OnTouchListener touchListener) {
            //    messageTV = (TextView) view.findViewById(R.id.header_tv);
            mBackButton = (Button) view.findViewById(R.id.backBtn);
            mContainerRl = (RelativeLayout) view.findViewById(R.id.main_rl);
            mContainerLl = (RelativeLayout) view.findViewById(R.id.innerrl);
            mActionButton = (Button) view.findViewById(R.id.action_btn);
            mDialogBGRL = (RelativeLayout) view.findViewById(R.id.dialog_bg_rl);
            mDatePicker = (DatePicker) view.findViewById(R.id.date_picker);
            mBackButton.setOnClickListener(listener);
            mContainerLl.setOnClickListener(listener);
            mDialogBGRL.setOnClickListener(null);


        }
    }

}
