package activity.Pod;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import activity.PodBean.LrInvoiceResponse;
import activity.languagechange.LocaleHelper;
import activity.retrofit.BaseRequest;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class ActivityPODSearchInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Context mContext;

    private RelativeLayout rlvBackViewID;
    private RelativeLayout rlvLRSubmitViewID;
    private EditText edtLrnumberID;
    private String mLRNumberValue;
    private Intent myIntent;
    private String mPlantID;
    private List<LrInvoiceResponse> mLrInvoiceResponse;

    private BaseRequest baseRequest;
    private ProgressDialog progressDialog;

    private DatePickerDialog dpd;
    Calendar now ;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podsearch_info);
        mContext = this;
        now = Calendar.getInstance();
        initView();
    }

    private void initView() {

        baseRequest = new BaseRequest(mContext);
        mLrInvoiceResponse= new ArrayList<>();
        myIntent = getIntent(); // gets the previously created intent
        mPlantID = myIntent.getStringExtra("PlantID"); // will return "FirstKeyValue"

        rlvLRSubmitViewID = findViewById(R.id.rlvLRSubmitViewID);
        edtLrnumberID = findViewById(R.id.edtLrnumberID);

        rlvBackViewID = findViewById(R.id.rlvBackViewID);



        rlvBackViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlvLRSubmitViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(initValidation())
                {
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

    private void showDatePickerDialog() {
//Vihu

       /* String dayNumber  = (String) DateFormat.format("dd",   new Date()); // 06
        String monthNumber  = (String) DateFormat.format("MM",   new Date()); // 06
        //   String year         = (String) DateFormat.format("yyyy", new Date()); // 2013
        String year         = (String) DateFormat.format("yyyy", new Date()); // 13
        System.out.println("mMonthV11==>>"+dayNumber+"/"+monthNumber+"/"+year);
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(year), Integer.parseInt(monthNumber), Integer.parseInt(dayNumber));
        dpd.setMaxDate(c);*/

        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    ActivityPODSearchInfo.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    ActivityPODSearchInfo.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );


        }



        dpd.setAccentColor(Color.parseColor("#034f84"));
        dpd.setTitle(getResources().getString(R.string.LR_Date));
        dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.VERTICAL);

        dpd.setOnCancelListener(dialog -> {
            Log.d("DatePickerDialog", "Dialog was cancelled");
            dpd = null;
        });
        //dpd.show(requireFragmentManager(), "Datepickerdialog");
        //dpd.getShowsDialog();
      //  dpd.show(getFragmentManager(), "DatePickerDialog");
    }

    private boolean initValidation() {
        mLRNumberValue = edtLrnumberID.getText().toString().trim();
      //  mLRMobileValue = edtLrMobileID.getText().toString().trim();

        if(mLRNumberValue.equalsIgnoreCase(""))
        {
            Toast.makeText(mContext, getResources().getString(R.string.enter_Valid_LR), Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
           //
            return true;
        }


       // return true;
    }

 /*   private void callgetLRInvoceListAPI() {

        baseRequest.showLoader();

        if (CustomUtility.isInternetOn()){

            baseRequest.setBaseRequestListner(new RequestReciever() {
                @Override
                public void onSuccess(int APINumber, String Json, Object obj) {
                    //  JSONArray arr = (JSONArray) obj;
                    try {
                        Gson gson = new Gson();
                        //////////////add model class here
                        LrInvoiceModel mLrInvoiceModel = gson.fromJson(Json, LrInvoiceModel.class);
                        getLRInvoceList(mLrInvoiceModel);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int APINumber, String errorCode, String message) {
                    baseRequest.hideLoader();
                    // new HomeFragment.Worker().execute();
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNetworkFailure(int APINumber, String message) {
                    baseRequest.hideLoader();
                    Toast.makeText(mContext, "Please check internet connection!", Toast.LENGTH_LONG).show();
                }
            });

            JsonObject jsonObject = new JsonObject();
            try {
                ////Put input parameter here
                // jsonObject.addProperty("id", mUserID);
                //  jsonObject.addProperty("plantid", "1");
                jsonObject.addProperty("lrno", mLRNumberValue);
                jsonObject.addProperty("mobno", mLRMobileValue);
                //    jsonObject.addProperty("plantid", "1");

            } catch (Exception e) {
                e.printStackTrace();
            }
            //baseRequest.callAPIPost(1, jsonObject, Constant.GET_ALL_NOTIFICATION_LIST_API);/////
            //  baseRequest.callAPIPost(1, jsonObject, NewSolarVFD.GET_PLANT_LIST_CHECK);/////
            baseRequest.callAPIPost(1, jsonObject, WebURL.API_LR_INVOICELIST);/////

        }
        else
        {
            baseRequest.hideLoader();
        }
    }

    private void getLRInvoceList(LrInvoiceModel mLrInvoiceModel) {

        if (mLrInvoiceModel.getStatus()) {

            if(mLrInvoiceResponse != null && mLrInvoiceResponse.size() > 0)
                mLrInvoiceResponse.clear();

            mLrInvoiceResponse = mLrInvoiceModel.getResponse();//setDataAdapter();
            baseRequest.hideLoader();
            Intent mIntent = new Intent(ActivityPODSearchInfo.this, LrtransportList.class);
            mIntent.putExtra("InvoiceList", (Serializable) mLrInvoiceResponse);
            startActivity(mIntent);
           // mDeviceFaultParametrAdapter = new DeviceFaultParametrAdapter(mContext, mFaultRecordResponse);
           // rclyFoultListView.setAdapter(mDeviceFaultParametrAdapter);



        }
        else
        {Toast.makeText(mContext, mLrInvoiceModel.getMessage(), Toast.LENGTH_LONG).show();

            baseRequest.hideLoader();
        }

    }*/


    public void callgetLRInvoceListAPI() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        //   username = inputName.getText().toString();
        //   password = inputPassword.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("lrno", mLRNumberValue));
       // param.add(new BasicNameValuePair("lrdate", mLRMobileValue));
       // param.add(new BasicNameValuePair("mobno", mLRMobileValue));

      //  jsonObject.addProperty("lrno", mLRNumberValue);
       // jsonObject.addProperty("mobno", mLRMobileValue);

        //  param.add(new BasicNameValuePair("pernr", username));
        // param.add(new BasicNameValuePair("pass", password));
        /******************************************************************************************/
/*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.API_LR_INVOICELIST, param);
                    Log.d("check_error", obj);
                    Log.e("check_error", obj);
/******************************************************************************************/
/*                       get JSONwebservice Data
/******************************************************************************************/
                    JSONObject jo = new JSONObject(obj);

                    String mStatus = jo.getString("status");
                   final String mMessage = jo.getString("message");
                    String jo11 = jo.getString("response");
                    if (mStatus.equalsIgnoreCase("true")) {

                        if(mLrInvoiceResponse.size()>0)
                            mLrInvoiceResponse.clear();

                        JSONArray ja = new JSONArray(jo11);
                        // JSONObject jo = ja.getJSONObject(0);

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject join = ja.getJSONObject(i);
                            LrInvoiceResponse mmLrInvoiceResponse = new LrInvoiceResponse();

                            mmLrInvoiceResponse.setLrno(join.getString("lrno"));
                            mmLrInvoiceResponse.setMobno(join.getString("mobno"));
                            mmLrInvoiceResponse.setTransporterName(join.getString("transporter_name"));
                            mmLrInvoiceResponse.setSapBillno(join.getString("sap_billno"));
                            mmLrInvoiceResponse.setBillDate(join.getString("bill_date"));
                            mmLrInvoiceResponse.setGstBillno(join.getString("gst_billno"));
                            mmLrInvoiceResponse.setLrdate(join.getString("lrdate"));


                            mLrInvoiceResponse.add(mmLrInvoiceResponse);

                        }


                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Intent mIntent = new Intent(ActivityPODSearchInfo.this, LrtransportList.class);
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
                    }


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
      /*  DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);*/
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int numDigitsDay = String.valueOf(dayOfMonth).length();
        int numDigitsMonth = String.valueOf(monthOfYear).length();
        //   String substring = str.substring(Math.max(str.length() - 2, 0));
        ++monthOfYear;
        String mDayV = "";
        String mMonthV = "";
        if(numDigitsDay == 1)
        {
            mDayV = "0"+ dayOfMonth;
        }
        else
        {
            mDayV =  ""+dayOfMonth;
        }

        if(numDigitsMonth == 1)
        {
            mMonthV = "0"+ monthOfYear;
        }
        else
        {
            mMonthV =  ""+ monthOfYear;
        }
        // String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        String date = getResources().getString(R.string.picked_date)+mDayV+"/"+mMonthV+"/"+year;
        System.out.println("date==>>"+date);
        System.out.println("mMonthV==>>"+mMonthV+""+year);

       // String oldstring = mMonthV+"-"+mDayV+"-"+year;
        String oldstring = mDayV+"."+mMonthV+"."+year;
        String newstring;
        String str = String.valueOf(year);
      //  edtLrMobileID.setText(oldstring);
        String lstchar = ""+str.charAt(str.length() - 1);
        String lndchar = ""+str.charAt(str.length() - 2);

        System.out.println("splitValue===>>"+lstchar+""+lndchar);
           /* Date dateold = new SimpleDateFormat("yyyy-MM-DD ").parse(oldstring);


             newstring = new SimpleDateFormat("yy-MM-DD").format(dateold);
             String [] splitValue =newstring.split("-");
            System.out.println("splitValue===>>"+splitValue[0]); // 2011-01-18*/





    }
}
