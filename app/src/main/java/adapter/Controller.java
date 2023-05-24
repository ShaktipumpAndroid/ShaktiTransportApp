package adapter;

import android.app.Application;
import android.content.Context;

import com.administrator.shaktiTransportApp.R;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;


@ReportsCrashes
//        (mailTo = "mayank.singh@shaktipumps.com",
        (mailTo = "vaibhav.patel@shaktipumps.com",

                customReportContent = {ReportField.APP_VERSION_CODE,
                        ReportField.APP_VERSION_NAME,
                        ReportField.ANDROID_VERSION,
                        ReportField.PHONE_MODEL,
                        ReportField.CUSTOM_DATA,
                        ReportField.STACK_TRACE,
                        ReportField.LOGCAT},
                mode = ReportingInteractionMode.TOAST,
                resToastText = R.string.crash_toast_text) //you get to define resToastText


public class Controller extends Application {

    public static final String IMAGE_DIRECTORY_NAME = "shaktitransportapp";
    private static Controller instance;

    public static Controller getInstance() {
        return instance;
    }

    public static void setInstance(Controller instance) {
        Controller.instance = instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        setInstance(this);

        // The following line triggers the initialization of ACRA
    /*    try {
            ACRA.init(this);
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/
    }


}
