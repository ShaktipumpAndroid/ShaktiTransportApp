package activity.Pod;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.administrator.shaktiTransportApp.R;
import activity.Login;
import activity.languagechange.LocaleHelper;
import webservice.WebURL;

public class NavigateOptionActivity extends AppCompatActivity {
    Context context;
    private TextView txtTransportID, txtSNDaftID;
    private TextView txtPODID;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_option);
        context = this;

        txtSNDaftID = (TextView) findViewById(R.id.txtSNDaftID);
        txtTransportID = (TextView) findViewById(R.id.txtTransportID);
        txtPODID = (TextView) findViewById(R.id.txtPODID);

        txtSNDaftID.setOnClickListener(v -> {
            WebURL.UserTypeCheck = 1;
            Intent mIntent = new Intent(NavigateOptionActivity.this, Login.class);
            startActivity(mIntent);
        });

        txtTransportID.setOnClickListener(v -> {
            WebURL.UserTypeCheck = 2;
            Intent mIntent = new Intent(NavigateOptionActivity.this, Login.class);
            startActivity(mIntent);
        });

        txtPODID.setOnClickListener(v -> {
            Intent mIntent = new Intent(NavigateOptionActivity.this, ActivityPODSearchInfo.class);
            startActivity(mIntent);
        });
    }
}