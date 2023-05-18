package activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.administrator.shaktiTransportApp.R;
import com.google.gson.JsonObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import activity.Pod.ActivityInvoiceGetInfo;
import activity.Pod.ActivityUpdateSandFData;
import bean.LoginBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class AddDeliveryBoyFormActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
    private android.os.Handler mHandler;
    private Spinner spinner_state, spinner_city;
    private Button btnSubmit;
    private EditText et_name, et_mobile_number, et_address, et_driving_licence, et_vehicleType,
            et_vehicle_registration_number, et_user_aadhar_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_boy_form);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSubmit = findViewById(R.id.btnSubmit);
        et_name = findViewById(R.id.et_name);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_address = findViewById(R.id.et_address);
        et_driving_licence = findViewById(R.id.et_driving_licence);
        et_vehicleType = findViewById(R.id.et_vehicleType);
        et_vehicle_registration_number = findViewById(R.id.et_vehicle_registration_number);
        et_user_aadhar_no = findViewById(R.id.et_user_aadhar_no);

        setToolBar();
        getStateName();
        mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mString = (String) msg.obj;
                Toast.makeText(AddDeliveryBoyFormActivity.this, mString, Toast.LENGTH_LONG).show();
            }
        };

        spinner_state = (Spinner) findViewById(R.id.spinner_state);
//        spinner_state.setPrompt("Select State");
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
//        spinner_city.setPrompt("Select City");

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                getCityName(spinner_state.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btnSubmit.setOnClickListener(v -> {
            if (isValidate()) {
                addDeliveryBoy(et_mobile_number.toString().trim(), et_name.toString().trim(), spinner_city.getSelectedItem().toString(),
                        spinner_state.getSelectedItem().toString(), et_address.toString().trim(), et_driving_licence.toString().trim(),
                        et_user_aadhar_no.toString().trim(), et_vehicleType.toString().trim(), et_vehicle_registration_number.toString().trim());
            }
        });
    }

    private boolean isValidate() {
        boolean returnValue = false;
        if (et_name.getText().toString().trim().isEmpty()) {
            showToast("Please enter name.");
        }else if (et_mobile_number.getText().toString().trim().isEmpty()) {
            showToast("Please enter Mobile no.");
        }else if (et_address.getText().toString().trim().isEmpty()) {
            showToast("Please enter Address.");
        }else if (et_driving_licence.getText().toString().trim().isEmpty()) {
            showToast("Please enter Driving Licence.");
        }else if (et_user_aadhar_no.getText().toString().trim().isEmpty()) {
            showToast("Please enter Aadhar no.");
        }else if (et_vehicleType.getText().toString().trim().isEmpty()) {
            showToast("Please enter Vehicle Type.");
        }else if (et_vehicle_registration_number.getText().toString().trim().isEmpty()) {
            showToast("Please enter Vehicle Registration no.");
        }else {
            returnValue = true;
        }
        return returnValue;
    }

    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delivery Boy Form");
    }

    private void getStateName() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("country", "India"));

        progressDialog = ProgressDialog.show(AddDeliveryBoyFormActivity.this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.STATE_URL, param);
                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObjectMain = new JSONObject(obj);
                            if (!jsonObjectMain.getBoolean("error")) {
                                JSONObject data = jsonObjectMain.getJSONObject("data");
                                JSONArray stateArray = data.getJSONArray("states");
                                ArrayList arrayListState = new ArrayList<String>();
                                for (int i = 0; i < stateArray.length(); i++) {
                                    JSONObject stateName = (JSONObject) stateArray.get(i);
                                    arrayListState.add(stateName.getString("name"));
                                }
                                runOnUiThread(() -> {
                                    try {
                                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddDeliveryBoyFormActivity.this, android.R.layout.simple_spinner_item, arrayListState);
                                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner_state.setAdapter(dataAdapter);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                });
                            } else {
                                Message msg = new Message();
                                msg.obj = jsonObjectMain.getString("msg");
                                mHandler.sendMessage(msg);
                            }
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

    private void getCityName(String stateName) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("country", "India"));
        param.add(new BasicNameValuePair("state", stateName));

        progressDialog = ProgressDialog.show(AddDeliveryBoyFormActivity.this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.CITY_URL, param);
                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObject = new JSONObject(obj);
                            if (!jsonObject.getBoolean("error")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                ArrayList arrayListState = new ArrayList<String>();
                                for (int i = 0; i < data.length(); i++) {
                                    arrayListState.add(data.get(i));
                                }
                                runOnUiThread(() -> {
                                    try {
                                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddDeliveryBoyFormActivity.this, android.R.layout.simple_spinner_item, arrayListState);
                                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                        spinner_city.setAdapter(dataAdapter);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                });
                            } else {
                                Message msg = new Message();
                                msg.obj = jsonObject.getString("msg");
                                mHandler.sendMessage(msg);
                            }
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

    private void addDeliveryBoy(String boyMobileNo, String boyName,
                                String cityName, String stateName, String address, String drivingLicence,
                                String identityNo, String vehicleType, String vehicleNo) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("delivey_mob_no", boyMobileNo));
        param.add(new BasicNameValuePair("trans_no", ""));
        param.add(new BasicNameValuePair("boy_name", boyName));
        param.add(new BasicNameValuePair("city_name", cityName));
        param.add(new BasicNameValuePair("state_name", stateName));
        param.add(new BasicNameValuePair("address", address));
        param.add(new BasicNameValuePair("driving_licence", drivingLicence));
        param.add(new BasicNameValuePair("identity_no", identityNo));
        param.add(new BasicNameValuePair("vehical_type", vehicleType));
        param.add(new BasicNameValuePair("vehical_no", vehicleNo));

        progressDialog = ProgressDialog.show(AddDeliveryBoyFormActivity.this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.ADD_DELIVERY_BOY_URL, param);
                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObject = new JSONObject(obj);
//                            if (!jsonObject.getBoolean("status")) {
//                            } else {
                            Message msg = new Message();
                            msg.obj = jsonObject.getString("message");
                            mHandler.sendMessage(msg);
//                            }
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
        int id_temp = item.getItemId();
        switch (id_temp) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
