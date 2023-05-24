package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.administrator.shaktiTransportApp.R;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import activity.Pod.ActivityInvoiceGetInfo;
import activity.languagechange.LocaleHelper;
import activity.retrofit.BaseRequest;
import activity.retrofit.RequestReciever;
import bean.LoginBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.Constants;
import webservice.CustomHttpClient;
import webservice.WebURL;

@SuppressWarnings("deprecation")
public class Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner_type;
    int index;
    String username,
            password,
            login, name,
            usertype,
            rfqnum,loginUser,
            spinner_type_text;
    List<String> list = null;
    Context mContext;
    @SuppressLint("HandlerLeak")
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(Login.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    String[] language;
    private ProgressDialog progressDialog;
    private EditText inputName, inputPassword, edt_verifyOtp;
    private TextInputLayout inputLayoutName,
            inputLayoutPassword;
    private LinearLayout ll_UserNamePassword;
    private RelativeLayout rl_Otp, lvlOTPMianID;
    private EditText edtINSTNumberID;
    private BaseRequest baseRequest;
    private String edtINSTNumberIDSTR, mORG_OTP_VALUE = "";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        baseRequest = new BaseRequest(this);
        list = new ArrayList<>();

        Spinner language_spinner = findViewById(R.id.spinnerLanguage);
        language = new String[]{getResources().getString(R.string.select_language), getResources().getString(R.string.english), getResources().getString(R.string.hindi)};

        getUserTypeValue();

        inputLayoutName =  findViewById(R.id.input_layout_name);
        inputLayoutPassword =  findViewById(R.id.input_layout_password);
        inputName =  findViewById(R.id.login_Et);
        inputPassword =  findViewById(R.id.password);
        ImageView btnSignUp =  findViewById(R.id.btn_signup);
        ll_UserNamePassword = findViewById(R.id.ll_UserNamePassword);
        rl_Otp = findViewById(R.id.rl_Otp);
        edtINSTNumberID = findViewById(R.id.edtINSTNumberID);
        TextView txt_GetOtp = findViewById(R.id.txt_GetOtp);
        TextView txtVerifyOtp = findViewById(R.id.txtVerifyOtp);
        lvlOTPMianID = findViewById(R.id.lvlOTPMianID);
        edt_verifyOtp = findViewById(R.id.edt_verifyOtp);
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        spinner_type =  findViewById(R.id.spinner_type);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                if (index == 4) {
                    ll_UserNamePassword.setVisibility(View.GONE);
                    rl_Otp.setVisibility(View.VISIBLE);
                } else {
                    ll_UserNamePassword.setVisibility(View.VISIBLE);
                    rl_Otp.setVisibility(View.GONE);
                }
                spinner_type_text = spinner_type.getSelectedItem().toString();

                if (spinner_type_text.equals(getResources().getString(R.string.Transport_Officer))) {
                    loginUser = "Transport Officer";

                } else if (spinner_type_text.equals(getResources().getString(R.string.Vendor))) {
                    loginUser = "Vendor";

                } else if (spinner_type_text.equals(getResources().getString(R.string.Driver))) {
                    loginUser = "Driver";

                } else if (spinner_type_text.equals(getResources().getString(R.string.Delivery_Boy))) {
                    loginUser = "Delivery Boy";

                }

                Log.e("Type===>",""+loginUser);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_type.setPrompt(getResources().getString(R.string.select_type));
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_center, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        spinner_type.setAdapter(dataAdapter);

        btnSignUp.setOnClickListener(view -> {

            if (validateType()) {
                    submitForm();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_center, language);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language_spinner.setSelection(0, false);
        language_spinner.setAdapter(adapter);

        language_spinner.setOnItemSelectedListener(this);


        txt_GetOtp.setOnClickListener(view -> {
            edtINSTNumberIDSTR = edtINSTNumberID.getText().toString().trim();
            if (edtINSTNumberIDSTR.equalsIgnoreCase("")) {
                Toast.makeText(mContext, getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();
            } else if (edtINSTNumberIDSTR.length() < 10 && android.util.Patterns.PHONE.matcher(edtINSTNumberIDSTR).matches()) {
                Toast.makeText(mContext, getResources().getString(R.string.enter_vaild_mobile), Toast.LENGTH_SHORT).show();
            } else {
                checkMobileNumber();
            }
        });

        txtVerifyOtp.setOnClickListener(view -> {
            if (mORG_OTP_VALUE.equalsIgnoreCase(edt_verifyOtp.getText().toString().trim())) {
                DatabaseHelper dataHelper = new DatabaseHelper(Login.this);
                dataHelper.insertLoginData(edtINSTNumberIDSTR, "", "Delivery Boy", "");

                LoginBean.setLogin(edtINSTNumberIDSTR, "", "Delivery Boy", "");
                CustomUtility.setSharedPreference(mContext, "UserTypeSNF", "4");
                Intent intent = new Intent(mContext, AssignedDeliveryListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(mContext, "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        // this will convert any number sequence into 6 character.
        return String.format("%04d", number);
    }

    private void callInsertAndUpdateDebugDataAPI() {
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int APINumber, String Json, Object obj) {
                try {
                    if (!obj.toString().isEmpty()) {
                        Toast.makeText(mContext, getResources().getString(R.string.otp_successfully), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, getResources().getString(R.string.otp_failed), Toast.LENGTH_LONG).show();
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
                Toast.makeText(mContext, getResources().getString(R.string.please_check), Toast.LENGTH_LONG).show();
            }
        });

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") Map<String, String> wordsByKey = new HashMap<>();
        System.out.println("jsonObject==>>" + wordsByKey);
        //baseRequest.callAPIGET(1, wordsByKey, NewSolarVFD.GET_DEVICE_SIM_NUMBER_API);/////
       // baseRequest.callAPIGETDirectURL(1, "http://login.yourbulksms.com/api/sendhttp.php?authkey=8716AQbKpjEHR5b4479de&mobiles=" + edtINSTNumberIDSTR + "&message=Enter The Following OTP To Verify Your Account " + mORG_OTP_VALUE + " SHAKTI&sender=SHAKTl&route=4&country=91&DLT_TE_ID=1707161675029844457");/////
        baseRequest.callAPIGETDirectURL(1,   "http://control.yourbulksms.com/api/sendhttp.php?authkey=393770756d707334373701&" + "mobiles="+edtINSTNumberIDSTR+"&message=Enter The Following OTP To Verify Your Account " + mORG_OTP_VALUE + " SHAKTI&sender=SHAKTl&route=2&unicode=0&country=91&DLT_TE_ID=1707161675029844457");
    }

    private void serverLogin() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        username = inputName.getText().toString();
        password = inputPassword.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", username));
        param.add(new BasicNameValuePair("PASS", password));
        param.add(new BasicNameValuePair("OBJS", loginUser));
        param.add(new BasicNameValuePair("DEVICE_NAME", CustomUtility.getDeviceName()));
        param.add(new BasicNameValuePair("APP_VERSION", WebURL.ANDROID_APP_VERSION));
        param.add(new BasicNameValuePair("API", String.valueOf(Build.VERSION.SDK_INT)));
        param.add(new BasicNameValuePair("API_VERSION", Build.VERSION.RELEASE));
        param.add(new BasicNameValuePair("FCM_CODE", CustomUtility.getDeviceTokenSharedPreferences(mContext, "refreshedToken")));

        progressDialog = ProgressDialog.show(Login.this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.LOGIN_PAGE, param);
                        if (!obj.equalsIgnoreCase("")) {
                            JSONArray ja = new JSONArray(obj);

                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jo = ja.getJSONObject(i);
                                login = jo.getString("LOGIN").trim();
                                name = jo.getString("NAME").trim();
                                usertype = jo.getString("OBJS").trim();
                                if (usertype.contains("Driver")) {
                                    rfqnum = jo.getString("RFQ_DOC");
                                } else {
                                    rfqnum = "";
                                }
                            }
                            Log.e("LOG", "IN" + login);

                            if ("Y".equals(login)) {
                                @SuppressWarnings("resource") DatabaseHelper dataHelper = new DatabaseHelper(Login.this);
                                dataHelper.insertLoginData(username, name, usertype, rfqnum);
                                LoginBean.setLogin(username, name, usertype, rfqnum);

                                progressDialog.dismiss();

                                CustomUtility.setSharedPreference(mContext, "capture", "0");

                                if (WebURL.UserTypeCheck == 1) {
                                    CustomUtility.setSharedPreference(mContext, "UserTypeSNF", "1");/// CNF= 1, Transport = 2;
                                    Intent intent = new Intent(Login.this, ActivityInvoiceGetInfo.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("login_flag", "X");
                                    startActivity(intent);
                                } else if (WebURL.UserTypeCheck == 2) {
                                    CustomUtility.setSharedPreference(mContext, "UserTypeSNF", "2");/// CNF= 1, Transport = 2;
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("login_flag", "X");
                                    startActivity(intent);
                                } else {
                                    CustomUtility.setSharedPreference(mContext, "UserTypeSNF", "0");/// CNF= 1, Transport = 2;
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("login_flag", "X");
                                    startActivity(intent);
                                }
                                finish();
                            } else if ("X".equals(login)) {
                                progressDialog.dismiss();
                                Message msg = new Message();
                                msg.obj = "Already Submitted!";
                                mHandler.sendMessage(msg);
                            } else {
                                progressDialog.dismiss();
                                Message msg = new Message();
                                msg.obj = "Invalid username or password";
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Message msg = new Message();
                        msg.obj = "No Internet Connection";
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        serverLogin();
    }

    private void checkMobileNumber() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("driver_mob", edtINSTNumberIDSTR));

        progressDialog = ProgressDialog.show(Login.this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.CHECK_DELIVERY, param);
                        obj = obj.trim();
                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObject = new JSONObject(obj);
                            if (jsonObject.getBoolean("status")) {
                                runOnUiThread(() -> {
                                    try {
                                        mORG_OTP_VALUE = getRandomNumberString();
//                                        Toast.makeText(Login.this, mORG_OTP_VALUE, Toast.LENGTH_LONG).show();
                                        lvlOTPMianID.setVisibility(View.VISIBLE);
                                        callInsertAndUpdateDebugDataAPI();
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

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void getUserTypeValue() {
        list.add(getResources().getString(R.string.select_type));
        list.add(getResources().getString(R.string.Transport_Officer));
        list.add(getResources().getString(R.string.Vendor));
        list.add(getResources().getString(R.string.Driver));
        list.add(getResources().getString(R.string.Delivery_Boy));
    }

    private boolean validateType() {
        boolean value;
        if (index == 0) {
            Toast.makeText(this, getResources().getString(R.string.Please_Select_Type), Toast.LENGTH_LONG).show();
            value = false;
        } else {
            value = true;
        }
        return value;
    }

    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.login_Et) {
                validateName();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getSelectedItem().toString().equals(getResources().getString(R.string.hindi))) {
            CustomUtility.setSharedPreference(getApplicationContext(), Constants.selectedLanguage,"Hindi");
            updateView("hi");
        } else if (parent.getSelectedItem().toString().equals(getResources().getString(R.string.english))) {
            CustomUtility.setSharedPreference(getApplicationContext(), Constants.selectedLanguage,"English");
            updateView("en");
        }
    }

    private void updateView(String languageCode) {
        Context context = LocaleHelper.setLocale(Login.this, languageCode);
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.setLocale(new Locale(languageCode)); // API 17+ only.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(conf);
        } else {
            resources.updateConfiguration(conf, dm);
        }
        Intent refresh = new Intent(this, Login.class);
        finish();
        startActivity(refresh);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
