package activity.Pod;
import static android.app.PendingIntent.getActivity;

import activity.PodBean.LrInvoiceResponse;
import activity.retrofit.BaseRequest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.administrator.shaktiTransportApp.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import activity.PodBean.InvoicelistBeanResponse;
import activity.retrofit.BaseRequest;
import bean.LoginBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class ActivityInvoiceGetInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Context mContext;

    private RelativeLayout rlvBackViewID;
    private RelativeLayout rlvLRSubmitViewID;
    private DatePickerDialog dpd;
    private EditText edtInvoice1ID,edtInvoice2ID,edtInvoice3ID,edtInvoice4ID,edtInvoice5ID,edtInvoice6ID;

    private String mStrInvoice1ID,mStrInvoice2ID,mStrInvoice3ID,mStrInvoice4ID,mStrInvoice5ID,mStrInvoice6ID;

    private String mLRNumberValue = "";

    DatabaseHelper db;
    private Intent myIntent;
    private List<InvoicelistBeanResponse> mInvoicelistBeanResponse;
    private List<LrInvoiceResponse> mLrInvoiceResponse;
    private String mPlantID;
    private BaseRequest baseRequest;
    private ProgressDialog progressDialog;

    Calendar now;
    String usertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_get_info);
        mContext = this;
        now = Calendar.getInstance();

        dpd = (DatePickerDialog) this.dpd.getFragmentManager().findFragmentByTag("Datepickerdialog");

        initView();
    }

    private void initView() {

        WebURL.FORM_CHECK_LENGTH = 0;

        LoginBean loginBean = new LoginBean();

        usertype = LoginBean.getUsertype();

        baseRequest = new BaseRequest(mContext);
        mLrInvoiceResponse = new ArrayList<>();
        mInvoicelistBeanResponse = new ArrayList<>();
        myIntent = getIntent(); // gets the previously created intent
        mPlantID = myIntent.getStringExtra("PlantID"); // will return "FirstKeyValue"

        //   baseRequest = new BaseRequest(this);
        //  mFaultRecordResponse = new ArrayList<>();
        db = new DatabaseHelper(mContext);
        rlvLRSubmitViewID = findViewById(R.id.rlvLRSubmitViewID);

        edtInvoice1ID = findViewById(R.id.edtInvoice1ID);
        edtInvoice2ID = findViewById(R.id.edtInvoice2ID);
        edtInvoice3ID = findViewById(R.id.edtInvoice3ID);
        edtInvoice4ID = findViewById(R.id.edtInvoice4ID);
        edtInvoice5ID = findViewById(R.id.edtInvoice5ID);
        edtInvoice6ID = findViewById(R.id.edtInvoice6ID);
        //   edtLrMobileID = findViewById(R.id.edtLrMobileID);
        // txtOpenCalenderID = findViewById(R.id.txtOpenCalenderID);
        rlvBackViewID = findViewById(R.id.rlvBackViewID);


        rlvBackViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                // Setting Dialog Title
                alertDialog.setTitle("Confirmation");
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you wish to Sign Out ?");
                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
                finish();
            }
        });

        rlvLRSubmitViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initValidation()) {
                    String [] mStrLen = mLRNumberValue.split(",");

                    WebURL.FORM_CHECK_LENGTH = mStrLen.length;

                    callgetLRInvoceListAPI();
                }

            }
        });

     /*   txtOpenCalenderID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    showDatePickerDialog();


            }
        });*/


        //  callGetFaultListAPI();
    }

    public void logout() {


        db.deleteTableData(DatabaseHelper.TABLE_LOGIN);
//      db.deleteTableData(DatabaseHelper.TABLE_SYNC_DAILY);
        if (usertype.equalsIgnoreCase("Vendor")) {
            db.deleteTableData(DatabaseHelper.TABLE_QUOTATION);
        } else {
            db.deleteTableData(DatabaseHelper.TABLE_RFQ);
        }
        CustomUtility.setSharedPreference(mContext, "capture", "0");
        String selectedFilePath = "/storage/emulated/0/signdemo/signature.jpg";
        File file = new File(selectedFilePath);
        boolean deleted = file.delete();
        finish();

    }

    private boolean initValidation() {

        mLRNumberValue = "";

        mStrInvoice1ID = edtInvoice1ID.getText().toString().trim();
        mStrInvoice2ID = edtInvoice2ID.getText().toString().trim();
        mStrInvoice3ID = edtInvoice3ID.getText().toString().trim();
        mStrInvoice4ID = edtInvoice4ID.getText().toString().trim();
        mStrInvoice5ID = edtInvoice5ID.getText().toString().trim();
        mStrInvoice6ID = edtInvoice6ID.getText().toString().trim();
        //  mLRMobileValue = edtLrMobileID.getText().toString().trim();

        if (mStrInvoice1ID.equalsIgnoreCase("") && mStrInvoice2ID.equalsIgnoreCase("") && mStrInvoice3ID.equalsIgnoreCase("") && mStrInvoice4ID.equalsIgnoreCase("") && mStrInvoice5ID.equalsIgnoreCase("") && mStrInvoice6ID.equalsIgnoreCase("")) {

            mLRNumberValue = "";
            Toast.makeText(mContext, "Please enter Valid Invoice Number", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!mStrInvoice1ID.equalsIgnoreCase("") ) {

            if(!mLRNumberValue.equalsIgnoreCase(""))
                mLRNumberValue = mLRNumberValue+","+mStrInvoice1ID;
            else
                mLRNumberValue = mStrInvoice1ID;
        }


         if (!mStrInvoice2ID.equalsIgnoreCase(""))
        {
            if(!mLRNumberValue.equalsIgnoreCase(""))
                 mLRNumberValue = mLRNumberValue+","+mStrInvoice2ID;
            else
                mLRNumberValue = mStrInvoice2ID;
        }


        if (!mStrInvoice3ID.equalsIgnoreCase(""))
        {
            if(!mLRNumberValue.equalsIgnoreCase(""))
                mLRNumberValue = mLRNumberValue+","+mStrInvoice3ID;
            else
                mLRNumberValue = mStrInvoice3ID;
        }

        if (!mStrInvoice4ID.equalsIgnoreCase(""))
        {
            if(!mLRNumberValue.equalsIgnoreCase(""))
                mLRNumberValue = mLRNumberValue+","+mStrInvoice4ID;
            else
                mLRNumberValue = mStrInvoice4ID;
        }

        if (!mStrInvoice5ID.equalsIgnoreCase(""))
        {
            if(!mLRNumberValue.equalsIgnoreCase(""))
                mLRNumberValue = mLRNumberValue+","+mStrInvoice5ID;
            else
                mLRNumberValue = mStrInvoice5ID;
        }

        if (!mStrInvoice6ID.equalsIgnoreCase(""))
        {
            if(!mLRNumberValue.equalsIgnoreCase(""))
                mLRNumberValue = mLRNumberValue+","+mStrInvoice6ID;
            else
                mLRNumberValue = mStrInvoice6ID;
        }


        if(!mLRNumberValue.equalsIgnoreCase(""))
        {

            return true;
        }
        else
        {
            return false;
        }

        /*else if(mLRNumberValue.equalsIgnoreCase(""))
        {
            Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_SHORT).show();
            return false;
        }*/


        // return true;
    }




    public void callgetLRInvoceListAPI() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        //   username = inputName.getText().toString();
        //   password = inputPassword.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        //  param.add(new BasicNameValuePair("lrno", mLRNumberValue));
        param.add(new BasicNameValuePair("bill", mLRNumberValue));

        // param.add(new BasicNameValuePair("lrdate", mLRMobileValue));
        // param.add(new BasicNameValuePair("mobno", mLRMobileValue));

        //  jsonObject.addProperty("lrno", mLRNumberValue);
        // jsonObject.addProperty("mobno", mLRMobileValue);

        //  param.add(new BasicNameValuePair("pernr", username));
        // param.add(new BasicNameValuePair("pass", password));
        //******************************************************************************************/
/*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.API_SNF_GET_INVOICE_DATA, param);
                    Log.d("check_error", obj);
                    Log.e("check_error", obj);
//******************************************************************************************/
/*                       get JSONwebservice Data
/******************************************************************************************/
//8109816953
                    ///7219015180
                    if (!obj.equalsIgnoreCase("")) {

                        JSONArray ja = new JSONArray(obj);
                      //  JSONObject jo = new JSONObject(obj);

                       /* String mStatus = jo.getString("status");
                        final String mMessage = jo.getString("message");*/
                     //   String jo11 = jo.getString("response");

                        if (mInvoicelistBeanResponse.size() > 0)
                            mInvoicelistBeanResponse.clear();

                       // JSONArray ja = new JSONArray(jo11);
                        // JSONObject jo = ja.getJSONObject(0);

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject join = ja.getJSONObject(i);
                            InvoicelistBeanResponse mmLrInvoiceResponse = new InvoicelistBeanResponse();

                            mmLrInvoiceResponse.setBill(join.getString("bill"));
                            mmLrInvoiceResponse.setControllerSerno(join.getString("controllerSerno"));
                            mmLrInvoiceResponse.setMatorSerno(join.getString("matorSerno"));
                            mmLrInvoiceResponse.setSetSerno(join.getString("setSerno"));
                            mmLrInvoiceResponse.setWERKS(join.getString("werks"));


                            mInvoicelistBeanResponse.add(mmLrInvoiceResponse);

                        }


                        runOnUiThread(() -> {
                            try {
                                Intent mIntent = new Intent(ActivityInvoiceGetInfo.this, ActivityUpdateSandFData.class);
                                mIntent.putExtra("InvoiceList", (Serializable) mInvoicelistBeanResponse);
                                startActivity(mIntent);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            progressDialog.dismiss();
                        });
                    }

                 /*   if (mStatus.equalsIgnoreCase("true")) {

                        if(mInvoicelistBeanResponse.size()>0)
                            mInvoicelistBeanResponse.clear();

                        JSONArray ja = new JSONArray(jo11);
                        // JSONObject jo = ja.getJSONObject(0);

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject join = ja.getJSONObject(i);
                            InvoicelistBeanResponse mmLrInvoiceResponse = new InvoicelistBeanResponse();

                            mmLrInvoiceResponse.setBill(join.getString("bill"));
                            mmLrInvoiceResponse.setControllerSerno(join.getString("controllerSerno"));
                            mmLrInvoiceResponse.setMatorSerno(join.getString("matorSerno"));
                            mmLrInvoiceResponse.setSetSerno(join.getString("setSerno"));


                            mInvoicelistBeanResponse.add(mmLrInvoiceResponse);

                        }


                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Intent mIntent = new Intent(ActivityInvoiceGetInfo.this, ActivityUpdateSandFData.class);
                                    mIntent.putExtra("InvoiceList", (Serializable) mLrInvoiceResponse);
                                    startActivity(mIntent);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }


                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        });
                        //   Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }*/


                } catch (Exception ex) {
                    ex.printStackTrace();
                    // dismiss the progress dialog
                    progressDialog.dismiss();
                }

            }

        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dpd = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) this.dpd.getParentFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int numDigitsDay = String.valueOf(dayOfMonth).length();
        int numDigitsMonth = String.valueOf(monthOfYear).length();
        //   String substring = str.substring(Math.max(str.length() - 2, 0));
        ++monthOfYear;
        String mDayV = "";
        String mMonthV = "";
        if (numDigitsDay == 1) {
            mDayV = "0" + dayOfMonth;
        } else {
            mDayV = "" + dayOfMonth;
        }

        if (numDigitsMonth == 1) {
            mMonthV = "0" + monthOfYear;
        } else {
            mMonthV = "" + monthOfYear;
        }
        // String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        String date = "You picked the following date: " + mDayV + "/" + mMonthV + "/" + year;
        System.out.println("date==>>" + date);
        System.out.println("mMonthV==>>" + mMonthV + "" + year);

        // String oldstring = mMonthV+"-"+mDayV+"-"+year;
        String oldstring = mDayV + "." + mMonthV + "." + year;
        String newstring;
        String str = String.valueOf(year);
        //  edtLrMobileID.setText(oldstring);
        String lstchar = "" + str.charAt(str.length() - 1);
        String lndchar = "" + str.charAt(str.length() - 2);

        System.out.println("splitValue===>>" + lstchar + "" + lndchar);
           /* Date dateold = new SimpleDateFormat("yyyy-MM-DD ").parse(oldstring);


             newstring = new SimpleDateFormat("yy-MM-DD").format(dateold);
             String [] splitValue =newstring.split("-");
            System.out.println("splitValue===>>"+splitValue[0]); // 2011-01-18*/


    }
}
