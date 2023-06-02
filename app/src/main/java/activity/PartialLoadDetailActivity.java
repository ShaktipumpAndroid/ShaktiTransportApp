package activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.administrator.shaktiTransportApp.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import activity.languagechange.LocaleHelper;
import activity.retrofit.BaseRequest;
import activity.retrofit.RequestReciever;
import bean.PartialLoadResponse;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class PartialLoadDetailActivity extends AppCompatActivity {

    private PartialLoadResponse partialLoadResponse;
    Context mContext;
    private DatabaseHelper db;
    String value = "Self";
    private TextView tvZdoc_no, tvBillNo, tvZdocdate, tvVkorg, tvBillDate, tvWerks, tvZlrno,
            tvZbookdate, tvZmobileno, tvZtransname, tvTransporter_mob, tvZdelivery, tvZdeliverdTo,
            tvStatus, tvCustomerCode, tvCustomerName;
    private EditText etDistance, etDeliveryBoyMobileNo;
    private ImageView mobile_image;
    private LinearLayout llDeliveryBoy;
    private Button btnSubmit;
    RadioButton rb;
    private RadioGroup rgDeliveryAssigned;
    private android.os.Handler mHandler;
    private ProgressDialog progressDialog;
    RadioButton rbSelf, rbDeliveryBoy;
    private BaseRequest baseRequest;
//    ArrayList deliveryBoyList = new ArrayList<DeliveryBoyResponse>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    @SuppressLint("HandlerLeak")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial_load_detail);
        Bundle bundle = getIntent().getExtras();
        partialLoadResponse = bundle.getParcelable("partialLoadDetail");
        inItViews();
        setData();
        mContext = this;
        baseRequest = new BaseRequest(this);
        db = new DatabaseHelper(mContext);
//        getDeliveryBoysList();
        mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mString = (String) msg.obj;
                Toast.makeText(PartialLoadDetailActivity.this, mString, Toast.LENGTH_LONG).show();
            }
        };

        mobile_image.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(partialLoadResponse.getZmobileno())) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 222);
                    } else {
                        Make_Call(partialLoadResponse.getZmobileno());
                    }

                } else {
                    Make_Call(partialLoadResponse.getZmobileno());
                }
            }
            Toast.makeText(mContext, "Calling....", Toast.LENGTH_SHORT).show();
        });

        rgDeliveryAssigned.setOnCheckedChangeListener((group, checkedId) -> {
            rb =  findViewById(checkedId);

            value = rb.getText().toString();
            if (rb.getText().equals("Self")) {

                llDeliveryBoy.setVisibility(View.GONE);
            } else {
                llDeliveryBoy.setVisibility(View.VISIBLE);
            }
        });

        btnSubmit.setOnClickListener(v -> {

                if(!value.equalsIgnoreCase("Self")){
                    if (!etDistance.getText().toString().isEmpty()) {
                        if(!etDeliveryBoyMobileNo.getText().toString().isEmpty() && etDeliveryBoyMobileNo.length() == 10){
                            callInsertAndUpdateDebugDataAPI();
                            assignedDelivery();
                        }
                        else{
                            Toast.makeText(mContext, "Enter Mobile no", Toast.LENGTH_SHORT).show();
                        }
                         }else {
                          Toast.makeText(mContext, "Enter all fields no", Toast.LENGTH_SHORT).show();
                        }
                }else {
                    if(!etDistance.getText().toString().isEmpty()){
                        assignedDelivery();
                    }else {
                        Toast.makeText(mContext, "Enter all fields no", Toast.LENGTH_SHORT).show();
                    }

                }


        });
    }
    private void Make_Call(String mobile) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            // new Capture_employee_gps_location(context, "12", mobile);
            intent.setData(Uri.parse("tel:" + mobile));
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callInsertAndUpdateDebugDataAPI() {
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int APINumber, String Json, Object obj) {
                try {
                    if (!obj.toString().isEmpty()) {
                        Toast.makeText(mContext, "OTP send successfully.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, "OTP send failed please try again.", Toast.LENGTH_LONG).show();
                    }
                    baseRequest.hideLoader();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int APINumber, String errorCode, String message) {
                baseRequest.hideLoader();
                //  Toast.makeText(mContext, "OTP send failed please try again On Failure.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkFailure(int APINumber, String message) {
                baseRequest.hideLoader();
                Toast.makeText(mContext, "Please check internet connection!", Toast.LENGTH_LONG).show();
            }
        });

        Map<String, String> wordsByKey = new HashMap<>();
        System.out.println("jsonObject==>>" + wordsByKey);
        //baseRequest.callAPIGET(1, wordsByKey, NewSolarVFD.GET_DEVICE_SIM_NUMBER_API);/////
        // baseRequest.callAPIGETDirectURL(1, "http://login.yourbulksms.com/api/sendhttp.php?authkey=8716AQbKpjEHR5b4479de&mobiles=" + edtINSTNumberIDSTR + "&message=Enter The Following OTP To Verify Your Account " + mORG_OTP_VALUE + " SHAKTI&sender=SHAKTl&route=4&country=91&DLT_TE_ID=1707161675029844457");/////
        baseRequest.callAPIGETDirectURL(1,   "http://control.yourbulksms.com/api/sendhttp.php?authkey=393770756d707334373701&mobiles="+etDeliveryBoyMobileNo.getText().toString()+"&message=Dear%20User%20New%20Delivery%20is%20Assigned%20to%20you%20Please%20Use%20the%20below%20link%20to%20install%20the%20App%20https%3A%2F%2Fplay.google.com%2Fstore%2Fsearch%3Fq%3Dshakti%2520Transport%2520App%26c%3Dapps%20SHAKTIPUMPS&sender=SHAKTl&route=2&unicode=0&country=91&DLT_TE_ID=1707168475371194441");
    }

   /* public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String str = "";
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rbSelf:
                if (checked)
                    str = "Self";
                break;
            case R.id.rbDeliveryBoy:
                if (checked)
                    str = "Delivery Boy";
                break;
        }
//        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }*/


    private void inItViews() {
        tvZdoc_no = findViewById(R.id.tvZdoc_no);
        tvBillNo = findViewById(R.id.tvBillNo);
        tvZdocdate = findViewById(R.id.tvZdocdate);
        tvVkorg = findViewById(R.id.tvVkorg);
        tvBillDate = findViewById(R.id.tvBillDate);
        tvWerks = findViewById(R.id.tvWerks);
        tvZlrno = findViewById(R.id.tvZlrno);
        tvZbookdate = findViewById(R.id.tvZbookdate);
        tvZmobileno = findViewById(R.id.tvZmobileno);
        tvZtransname = findViewById(R.id.tvZtransname);
        tvTransporter_mob = findViewById(R.id.tvTransporter_mob);
        tvZdelivery = findViewById(R.id.tvZdelivery);
        tvZdeliverdTo = findViewById(R.id.tvZdeliverdTo);
        tvStatus = findViewById(R.id.tvStatus);
        tvCustomerCode = findViewById(R.id.tvCustomerCode);
        tvCustomerName = findViewById(R.id.tvCustomerName);
//        spinner_delivery_boy = (Spinner) findViewById(R.id.spinner_delivery_boy);
//        spinner_delivery_boy.setPrompt("Select Delivery Boy");
        llDeliveryBoy = findViewById(R.id.llDeliveryBoy);
        rgDeliveryAssigned =  findViewById(R.id.rgDeliveryAssigned);
        etDistance = findViewById(R.id.etDistance);
        etDeliveryBoyMobileNo = findViewById(R.id.etDeliveryBoyMobileNo);
        mobile_image = findViewById(R.id.mobile_image);
//        etVehicleType = findViewById(R.id.etVehicleType);
//        etVehicleNo = findViewById(R.id.etVehicleNo);
//        etLicence = findViewById(R.id.etLicence);
//        llVehicleType = findViewById(R.id.llVehicleType);
//        llVehicleNo = findViewById(R.id.llVehicleNo);
//        llLicence = findViewById(R.id.llLicence);
        rbSelf = findViewById(R.id.rbSelf);
        rbDeliveryBoy = findViewById(R.id.rbDeliveryBoy);
        btnSubmit = findViewById(R.id.btnSubmit);
        Toolbar mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.Agreement_Detail));
    }

    private void setData() {
        tvZdoc_no.setText(partialLoadResponse.getZdocNo());
        tvBillNo.setText(partialLoadResponse.getVbeln());
        tvZdocdate.setText(partialLoadResponse.getZdocdate());
        tvVkorg.setText(partialLoadResponse.getVkorg());
        tvBillDate.setText(partialLoadResponse.getFkdat());
        tvWerks.setText(partialLoadResponse.getWerks());
        tvZlrno.setText(partialLoadResponse.getZlrno());
        tvZbookdate.setText(partialLoadResponse.getZbookdate());
        tvZmobileno.setText(partialLoadResponse.getZmobileno());
        tvZtransname.setText(partialLoadResponse.getZtransname());
        tvTransporter_mob.setText(partialLoadResponse.getTransporterMob());
        tvZdelivery.setText(partialLoadResponse.getZdelivery());
        tvZdeliverdTo.setText(partialLoadResponse.getZdeliverdTo());
        tvStatus.setText(partialLoadResponse.getStatus());
        tvCustomerCode.setText(partialLoadResponse.getKunag());
        tvCustomerName.setText(partialLoadResponse.getCustName());
    }

    private boolean isValidate() {
        boolean returnValue = false;
//        if (etDistance.getText().toString().trim().equals("")) {
//            Toast.makeText(getApplicationContext(), "Please enter Distance", Toast.LENGTH_SHORT).show();
//        } else
        if (llDeliveryBoy.getVisibility() == View.VISIBLE && etDeliveryBoyMobileNo.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Mobile_No), Toast.LENGTH_SHORT).show();
        } else if (llDeliveryBoy.getVisibility() == View.VISIBLE && etDeliveryBoyMobileNo.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Mobile_Nocorrect), Toast.LENGTH_SHORT).show();
        } else {
            returnValue = true;
        }
        return returnValue;
    }

    private void assignedDelivery() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        int selectedId = rgDeliveryAssigned.getCheckedRadioButtonId();
        RadioButton radioSexButton =  findViewById(selectedId);
        final ArrayList<NameValuePair> param = new ArrayList<>();
        String zdeliveredBy = "BOY";
        if (!radioSexButton.getText().equals("Self")) {
            param.add(new BasicNameValuePair("zdelboy_mob_no", etDeliveryBoyMobileNo.getText().toString().trim()));
        } else {
            zdeliveredBy = "SELF";
        }
        param.add(new BasicNameValuePair("zbill_no", partialLoadResponse.getVbeln()));
        param.add(new BasicNameValuePair("zdelivered_by", zdeliveredBy));
        param.add(new BasicNameValuePair("zdistance", etDistance.getText().toString().trim()));

        progressDialog = ProgressDialog.show(PartialLoadDetailActivity.this, "", "Connecting to server..please wait !");

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
                            //Delete
                            db.deleteTableData(DatabaseHelper.TABLE_PARTIAL_LOAD);
                            finish();
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
