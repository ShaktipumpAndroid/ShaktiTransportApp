package activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.administrator.shaktiTransportApp.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import bean.OfficerAssignedDeliveryResponse;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class OfficerAssignedDeliveryDetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private OfficerAssignedDeliveryResponse officerAssignedDeliveryResponse;
    private TextView tvBillNo, tvZdocdate, tvBillDate, tvWerks, tvZlrno, tvZbookdate,
            tvZtransname, tvTransporter_mob, tvZdelivery, tvZdeliverdTo, tvCustomerCode, tvCustomerName, tvDeliveryAssignMobileNo,tvDeliveredBy;
    private EditText etDeliveryBoyMobileNo;
    private Button btnSubmit;
    private android.os.Handler mHandler;
    private ProgressDialog progressDialog;
    RadioButton rbSelf, rbDeliveryBoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_assigned_delivery_detail);
        Bundle bundle = getIntent().getExtras();
        officerAssignedDeliveryResponse = bundle.getParcelable("partialLoadDetail");
        inItViews();
        setData();
        mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mString = (String) msg.obj;
                Toast.makeText(OfficerAssignedDeliveryDetailActivity.this, mString, Toast.LENGTH_LONG).show();
            }
        };

        btnSubmit.setOnClickListener(v -> {
            if (isValidate()) {
                assignedDelivery();
            }
        });
    }

    private void inItViews() {
        tvBillNo = findViewById(R.id.tvBillNo);
        tvZdocdate = findViewById(R.id.tvZdocdate);
        tvBillDate = findViewById(R.id.tvBillDate);
        tvWerks = findViewById(R.id.tvWerks);
        tvZlrno = findViewById(R.id.tvZlrno);
        tvZbookdate = findViewById(R.id.tvZbookdate);
        tvZtransname = findViewById(R.id.tvZtransname);
        tvTransporter_mob = findViewById(R.id.tvTransporter_mob);
        tvZdelivery = findViewById(R.id.tvZdelivery);
        tvZdeliverdTo = findViewById(R.id.tvZdeliverdTo);
        tvCustomerCode = findViewById(R.id.tvCustomerCode);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvDeliveryAssignMobileNo = findViewById(R.id.tvDeliveryAssignMobileNo);
        tvDeliveredBy = findViewById(R.id.tvDeliveredBy);
        etDeliveryBoyMobileNo = findViewById(R.id.etDeliveryBoyMobileNo);
        rbSelf = findViewById(R.id.rbSelf);
        rbDeliveryBoy = findViewById(R.id.rbDeliveryBoy);
        btnSubmit = findViewById(R.id.btnSubmit);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.assigned_delivery_detail));
    }

    private void setData() {
        tvBillNo.setText(officerAssignedDeliveryResponse.getVbeln());
        tvZdocdate.setText(officerAssignedDeliveryResponse.getZdocdate());
        tvBillDate.setText(officerAssignedDeliveryResponse.getFkdat());
        tvWerks.setText(officerAssignedDeliveryResponse.getWerks());
        tvZlrno.setText(officerAssignedDeliveryResponse.getZlrno());
        tvZbookdate.setText(officerAssignedDeliveryResponse.getZbookdate());
        tvZtransname.setText(officerAssignedDeliveryResponse.getZtransname());
        tvTransporter_mob.setText(officerAssignedDeliveryResponse.getTransporterMob());
        tvZdelivery.setText(officerAssignedDeliveryResponse.getZdelivery());
        tvZdeliverdTo.setText(officerAssignedDeliveryResponse.getZdeliverdTo());
        tvCustomerCode.setText(officerAssignedDeliveryResponse.getKunag());
        tvCustomerName.setText(officerAssignedDeliveryResponse.getCustName());
        tvDeliveryAssignMobileNo.setText(officerAssignedDeliveryResponse.getZassignMobNo());
        tvDeliveredBy.setText(officerAssignedDeliveryResponse.getZdeliveredBy());
    }

    private boolean isValidate() {
        boolean returnValue = false;
        if (etDeliveryBoyMobileNo.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Mobile No", Toast.LENGTH_SHORT).show();
        } else if (etDeliveryBoyMobileNo.getText().toString().trim().length() < 10) {
            Toast.makeText(getApplicationContext(), "Please enter correct Mobile No", Toast.LENGTH_SHORT).show();
        } else {
            returnValue = true;
        }
        return returnValue;
    }

    private void assignedDelivery() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("zdelboy_mob_no", etDeliveryBoyMobileNo.getText().toString().trim()));
        param.add(new BasicNameValuePair("zbill_no", officerAssignedDeliveryResponse.getVbeln()));
        param.add(new BasicNameValuePair("zdelivered_by", "BOY"));

        progressDialog = ProgressDialog.show(OfficerAssignedDeliveryDetailActivity.this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.SEND_DELIVERY_ASSIGNED_DATA, param);
                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObject = new JSONObject(obj);
                            Message msg = new Message();
                            msg.obj = jsonObject.getString("message");
                            mHandler.sendMessage(msg);
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Message msg = new Message();
                        msg.obj = "No Internet Connection";
                        mHandler.sendMessage(msg);
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
