package activity.Pod;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.administrator.shaktiTransportApp.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import activity.PodBean.InvoicelistBeanResponse;
import activity.languagechange.LocaleHelper;
import activity.retrofit.BaseRequest;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class ActivityUpdateSandFData extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Context mContext;

    private RelativeLayout rlvBackViewID;

    private RelativeLayout rlvLRSubmitViewID;
    private EditText edtVehicleNoID,edtDispachDateID, edtDeliveryDateID, edtTransportNameID, edtDriverNoID, edtRFQNoID;

    private EditText edtBillNo1ID,edtBillNo2ID, edtBillNo3ID, edtBillNo4ID, edtBillNo5ID, edtBillNo6ID;
    private EditText edtLRNo1ID,edtLRNo2ID, edtLRNo3ID, edtLRNo4ID, edtLRNo5ID, edtLRNo6ID;
    private EditText edtPumpSer1ID,edtPumpSer2ID, edtPumpSer3ID, edtPumpSer4ID, edtPumpSer5ID, edtPumpSer6ID;
    private EditText edtMotorSer1ID,edtMotorSer2ID, edtMotorSer3ID, edtMotorSer4ID, edtMotorSer5ID, edtMotorSer6ID;
    private EditText edtControllerSer1ID,edtControllerSer2ID, edtControllerSer3ID, edtControllerSer4ID, edtControllerSer5ID, edtControllerSer6ID;


    private String mStrVehicleNoID,mStrDispachDateID,mStrDispachDateID1, mStrDeliveryDateID,mStrDeliveryDateID1, mStrTransportNameID,mStrDriverNoID,mStrRFQNoID;
    private String mStrBillNo1ID,mStrBillNo2ID,mStrBillNo3ID,mStrBillNo4ID,mStrBillNo5ID, mStrBillNo6ID;
    private String mStrLRNo1ID,mStrLRNo2ID,mStrLRNo3ID,mStrLRNo4ID,mStrLRNo5ID, mStrLRNo6ID;
    private String mStrPumpSer1ID,mStrPumpSer2ID,mStrPumpSer3ID,mStrPumpSer4ID,mStrPumpSer5ID, mStrPumpSer6ID;
    private String mStrPumpSer1ID_Srv,mStrPumpSer2ID_Srv,mStrPumpSer3ID_Srv,mStrPumpSer4ID_Srv,mStrPumpSer5ID_Srv, mStrPumpSer6ID_Srv;
    private String mStrMotorSer1ID,mStrMotorSer2ID,mStrMotorSer3ID,mStrMotorSer4ID,mStrMotorSer5ID, mStrMotorSer6ID;
    private String mStrMotorSer1ID_Srv,mStrMotorSer2ID_Srv,mStrMotorSer3ID_Srv,mStrMotorSer4ID_Srv,mStrMotorSer5ID_Srv, mStrMotorSer6ID_Srv;
    private String mStrControllerSer1ID,mStrControllerSer2ID,mStrControllerSer3ID,mStrControllerSer4ID,mStrControllerSer5ID, mStrControllerSer6ID;
    private String mStrControllerSer1ID_Srv,mStrControllerSer2ID_Srv,mStrControllerSer3ID_Srv,mStrControllerSer4ID_Srv,mStrControllerSer5ID_Srv, mStrControllerSer6ID_Srv;

    private ImageView imgDelivertDateID, imgDispachDateID;

    private TextView txtSaveAllID;

    int checkDateClick = 0;
    int checkScannerClick = 0;
    //1ID
    private Intent myIntent;


    private BaseRequest baseRequest;
    private ProgressDialog progressDialog;

    private DatePickerDialog dpd;
    Calendar now ;

    private ImageView imgScannerSX1ID,imgScannerSX2ID,imgScannerSX3ID;
    private ImageView imgScannerFv1ID,imgScannerFv2ID,imgScannerFv3ID;
    private ImageView imgScannerFr1ID,imgScannerFr2ID,imgScannerFr3ID;
    private ImageView imgScannerT1ID,imgScannerT2ID,imgScannerT3ID;
    private ImageView imgScannerS1ID,imgScannerS2ID,imgScannerS3ID;
    private ImageView imgScannerF1ID,imgScannerF2ID,imgScannerF3ID;

    private CardView cradViewF1ID,cradViewF2ID,cradViewF3ID,cradViewF4ID,cradViewF5ID,cradViewF6ID;
    private  RelativeLayout rlvVarified1ID, rlvVarified2ID, rlvVarified3ID, rlvVarified4ID, rlvVarified5ID, rlvVarified6ID;
    private ImageView imgVarifed1ID, imgVarifed2ID , imgVarifed3ID, imgVarifed4ID, imgVarifed5ID, imgVarifed6ID;
private int intVarified1ID=0,intVarified2ID=0,intVarified3ID=0,intVarified4ID=0,intVarified5ID=0,intVarified6ID=0;

    private List<InvoicelistBeanResponse> mInvoicelistBeanResponse;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sandf_data);
        mContext = this;
        now = Calendar.getInstance();



       // dpd = (DatePickerDialog) this.dpd.getParentFragmentManager().findFragmentByTag("Datepickerdialog");

        initView();
        initHeaderView();
        initScannerView();
    }

    private void initScannerView() {

        imgScannerF1ID = findViewById(R.id.imgScannerF1ID);
        imgScannerF2ID = findViewById(R.id.imgScannerF2ID);
        imgScannerF3ID = findViewById(R.id.imgScannerF3ID);

        imgScannerS1ID = findViewById(R.id.imgScannerS1ID);
        imgScannerS2ID = findViewById(R.id.imgScannerS2ID);
        imgScannerS3ID = findViewById(R.id.imgScannerS3ID);

        imgScannerT1ID = findViewById(R.id.imgScannerT1ID);
        imgScannerT2ID = findViewById(R.id.imgScannerT2ID);
        imgScannerT3ID = findViewById(R.id.imgScannerT3ID);

        imgScannerFr1ID = findViewById(R.id.imgScannerFr1ID);
        imgScannerFr2ID = findViewById(R.id.imgScannerFr2ID);
        imgScannerFr3ID = findViewById(R.id.imgScannerFr3ID);

        imgScannerFv1ID = findViewById(R.id.imgScannerFv1ID);
        imgScannerFv2ID = findViewById(R.id.imgScannerFv2ID);
        imgScannerFv3ID = findViewById(R.id.imgScannerFv3ID);

        imgScannerSX1ID = findViewById(R.id.imgScannerSX1ID);
        imgScannerSX2ID = findViewById(R.id.imgScannerSX2ID);
        imgScannerSX3ID = findViewById(R.id.imgScannerSX3ID);



        imgScannerF1ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=11;
                openScannerCode();

            }
        });

        imgScannerF2ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=12;
                openScannerCode();
            }
        });

        imgScannerF3ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=13;
                openScannerCode();
            }
        });

        imgScannerS1ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=21;
                openScannerCode();
            }
        });

        imgScannerS2ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=22;
                openScannerCode();
            }
        });

        imgScannerS3ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=23;
                openScannerCode();
            }
        });

        imgScannerT1ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=31;
                openScannerCode();
            }
        });

        imgScannerT2ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=32;
                openScannerCode();
            }
        });

        imgScannerT3ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=33;
                openScannerCode();
            }
        });

        imgScannerFr1ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=41;
                openScannerCode();
            }
        });

        imgScannerFr2ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=42;
                openScannerCode();
            }
        });


        imgScannerFr3ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=43;
                openScannerCode();
            }
        });

        imgScannerFv1ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=51;
                openScannerCode();
            }
        });

        imgScannerFv2ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=52;
                openScannerCode();
            }
        });

        imgScannerFv3ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=53;
                openScannerCode();
            }
        });


        imgScannerSX1ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=61;
                openScannerCode();
            }
        });


        imgScannerSX2ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=62;
                openScannerCode();
            }
        });

        imgScannerSX3ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkScannerClick=63;
                openScannerCode();
            }
        });


    }

    private void openScannerCode() {

        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "BAR_CODE_MODE"); // "PRODUCT_MODE for bar codes//QR_CODE_MODE

            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    private void initHeaderView() {


        cradViewF1ID = findViewById(R.id.cradViewF1ID);
        cradViewF2ID = findViewById(R.id.cradViewF2ID);
        cradViewF3ID = findViewById(R.id.cradViewF3ID);
        cradViewF4ID = findViewById(R.id.cradViewF4ID);
        cradViewF5ID = findViewById(R.id.cradViewF5ID);
        cradViewF6ID = findViewById(R.id.cradViewF6ID);


        rlvVarified1ID = findViewById(R.id.rlvVarified1ID);
        rlvVarified2ID = findViewById(R.id.rlvVarified2ID);
        rlvVarified3ID = findViewById(R.id.rlvVarified3ID);
        rlvVarified4ID = findViewById(R.id.rlvVarified4ID);
        rlvVarified5ID = findViewById(R.id.rlvVarified5ID);
        rlvVarified6ID = findViewById(R.id.rlvVarified6ID);


        imgVarifed1ID = findViewById(R.id.imgVarifed1ID);
        imgVarifed2ID = findViewById(R.id.imgVarifed2ID);
        imgVarifed3ID = findViewById(R.id.imgVarifed3ID);
        imgVarifed4ID = findViewById(R.id.imgVarifed4ID);
        imgVarifed5ID = findViewById(R.id.imgVarifed5ID);
        imgVarifed6ID = findViewById(R.id.imgVarifed6ID);


        txtSaveAllID = findViewById(R.id.txtSaveAllID);
        imgDelivertDateID = findViewById(R.id.imgDelivertDateID);
        imgDispachDateID = findViewById(R.id.imgDispachDateID);

        edtVehicleNoID = findViewById(R.id.edtVehicleNoID);
        edtDispachDateID = findViewById(R.id.edtDispachDateID);
        edtDeliveryDateID = findViewById(R.id.edtDeliveryDateID);
        edtTransportNameID = findViewById(R.id.edtTransportNameID);
        edtDriverNoID = findViewById(R.id.edtDriverNoID);
        edtRFQNoID = findViewById(R.id.edtRFQNoID);

        txtSaveAllID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDataAllEDT();


            }
        });


        imgDispachDateID.setOnClickListener(view -> {
            Calendar currentDate;
            int mDay, mMonth, mYear;
            currentDate = Calendar.getInstance();

            mDay = currentDate.get(Calendar.DAY_OF_MONTH);
            mMonth = currentDate.get(Calendar.MONTH);
            mYear = currentDate.get(Calendar.YEAR);
            android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(mContext, (datePicker, i, i1, i2) -> {
                i1 = i1 + 1;
                edtDispachDateID.setText(i2 + "/" + i1 + "/" + i);
              /*  mStart = edtDispachDateID.getText().toString().trim();
                parseDateToddMMyyyy1(mStart);*/
            }, mYear, mMonth, mDay);
            datePickerDialog.setTitle("Start Date");
            datePickerDialog.show();
        });

        imgDelivertDateID.setOnClickListener(view -> {
            Calendar currentDate;
            int mDay, mMonth, mYear;
            currentDate = Calendar.getInstance();

            mDay = currentDate.get(Calendar.DAY_OF_MONTH);
            mMonth = currentDate.get(Calendar.MONTH);
            mYear = currentDate.get(Calendar.YEAR);
            android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(mContext, (datePicker, i, i1, i2) -> {
                i1 = i1 + 1;
                edtDeliveryDateID.setText(i2 + "/" + i1 + "/" + i);
              /*  mStart = edtDispachDateID.getText().toString().trim();
                parseDateToddMMyyyy1(mStart);*/
            }, mYear, mMonth, mDay);
            datePickerDialog.setTitle("Start Date");
            datePickerDialog.show();
        });


        edtBillNo1ID = findViewById(R.id.edtBillNo1ID);
        edtBillNo2ID = findViewById(R.id.edtBillNo2ID);
        edtBillNo3ID = findViewById(R.id.edtBillNo3ID);
        edtBillNo4ID = findViewById(R.id.edtBillNo4ID);
        edtBillNo5ID = findViewById(R.id.edtBillNo5ID);
        edtBillNo6ID = findViewById(R.id.edtBillNo6ID);
        //
        //
        edtLRNo1ID = findViewById(R.id.edtLRNo1ID);
        edtLRNo2ID = findViewById(R.id.edtLRNo2ID);
        edtLRNo3ID = findViewById(R.id.edtLRNo3ID);
        edtLRNo4ID = findViewById(R.id.edtLRNo4ID);
        edtLRNo5ID = findViewById(R.id.edtLRNo5ID);
        edtLRNo6ID = findViewById(R.id.edtLRNo6ID);
        //
        edtPumpSer1ID = findViewById(R.id.edtPumpSer1ID);
        edtPumpSer2ID = findViewById(R.id.edtPumpSer2ID);
        edtPumpSer3ID = findViewById(R.id.edtPumpSer3ID);
        edtPumpSer4ID = findViewById(R.id.edtPumpSer4ID);
        edtPumpSer5ID = findViewById(R.id.edtPumpSer5ID);
        edtPumpSer6ID = findViewById(R.id.edtPumpSer6ID);
        //
        //
        edtMotorSer1ID = findViewById(R.id.edtMotorSer1ID);
        edtMotorSer2ID = findViewById(R.id.edtMotorSer2ID);
        edtMotorSer3ID = findViewById(R.id.edtMotorSer3ID);
        edtMotorSer4ID = findViewById(R.id.edtMotorSer4ID);
        edtMotorSer5ID = findViewById(R.id.edtMotorSer5ID);
        edtMotorSer6ID = findViewById(R.id.edtMotorSer6ID);
        //

        edtControllerSer1ID = findViewById(R.id.edtControllerSer1ID);
        edtControllerSer2ID = findViewById(R.id.edtControllerSer2ID);
        edtControllerSer3ID = findViewById(R.id.edtControllerSer3ID);
        edtControllerSer4ID = findViewById(R.id.edtControllerSer4ID);
        edtControllerSer5ID = findViewById(R.id.edtControllerSer5ID);
        edtControllerSer6ID = findViewById(R.id.edtControllerSer6ID);


        for (int i = 0; i < mInvoicelistBeanResponse.size(); i++) {

            if(i == 0)
            {
                edtBillNo1ID.setText(mInvoicelistBeanResponse.get(i).getBill().trim());
             //   edtPumpSer1ID.setText(mInvoicelistBeanResponse.get(i).getSetSerno().trim());
              //  edtMotorSer1ID.setText(mInvoicelistBeanResponse.get(i).getMatorSerno().trim());
              //  edtControllerSer1ID.setText(mInvoicelistBeanResponse.get(i).getControllerSerno().trim());


                mStrPumpSer1ID_Srv = mInvoicelistBeanResponse.get(i).getSetSerno().trim();
                mStrMotorSer1ID_Srv = mInvoicelistBeanResponse.get(i).getMatorSerno().trim();
                mStrControllerSer1ID_Srv = mInvoicelistBeanResponse.get(i).getControllerSerno().trim();
               // edtLRNo1ID.setText(mInvoicelistBeanResponse.get(i).get.trim());
            }
            else  if(i == 1)
            {
                edtBillNo2ID.setText(mInvoicelistBeanResponse.get(i).getBill().trim());
               // edtPumpSer2ID.setText(mInvoicelistBeanResponse.get(i).getSetSerno().trim());
              //  edtMotorSer2ID.setText(mInvoicelistBeanResponse.get(i).getMatorSerno().trim());
              //  edtControllerSer2ID.setText(mInvoicelistBeanResponse.get(i).getControllerSerno().trim());


                mStrPumpSer2ID_Srv = mInvoicelistBeanResponse.get(i).getSetSerno().trim();
                mStrMotorSer2ID_Srv = mInvoicelistBeanResponse.get(i).getMatorSerno().trim();
                mStrControllerSer2ID_Srv = mInvoicelistBeanResponse.get(i).getControllerSerno().trim();
                // edtLRNo1ID.setText(mInvoicelistBeanResponse.get(i).get.trim());
            }
            else  if(i == 2)
            {
                edtBillNo3ID.setText(mInvoicelistBeanResponse.get(i).getBill().trim());
              //  edtPumpSer3ID.setText(mInvoicelistBeanResponse.get(i).getSetSerno().trim());
              //  edtMotorSer3ID.setText(mInvoicelistBeanResponse.get(i).getMatorSerno().trim());
               // edtControllerSer3ID.setText(mInvoicelistBeanResponse.get(i).getControllerSerno().trim());

                mStrPumpSer3ID_Srv = mInvoicelistBeanResponse.get(i).getSetSerno().trim();
                mStrMotorSer3ID_Srv = mInvoicelistBeanResponse.get(i).getMatorSerno().trim();
                mStrControllerSer3ID_Srv = mInvoicelistBeanResponse.get(i).getControllerSerno().trim();
                // edtLRNo1ID.setText(mInvoicelistBeanResponse.get(i).get.trim());
            }
            else  if(i == 3)
            {
                edtBillNo4ID.setText(mInvoicelistBeanResponse.get(i).getBill().trim());
              //  edtPumpSer4ID.setText(mInvoicelistBeanResponse.get(i).getSetSerno().trim());
              //  edtMotorSer4ID.setText(mInvoicelistBeanResponse.get(i).getMatorSerno().trim());
              //  edtControllerSer4ID.setText(mInvoicelistBeanResponse.get(i).getControllerSerno().trim());


                mStrPumpSer4ID_Srv = mInvoicelistBeanResponse.get(i).getSetSerno().trim();
                mStrMotorSer4ID_Srv = mInvoicelistBeanResponse.get(i).getMatorSerno().trim();
                mStrControllerSer4ID_Srv = mInvoicelistBeanResponse.get(i).getControllerSerno().trim();
                // edtLRNo1ID.setText(mInvoicelistBeanResponse.get(i).get.trim());
            }
            else  if(i == 4)
            {
                edtBillNo5ID.setText(mInvoicelistBeanResponse.get(i).getBill().trim());
              //  edtPumpSer5ID.setText(mInvoicelistBeanResponse.get(i).getSetSerno().trim());
             //   edtMotorSer5ID.setText(mInvoicelistBeanResponse.get(i).getMatorSerno().trim());
             //   edtControllerSer5ID.setText(mInvoicelistBeanResponse.get(i).getControllerSerno().trim());


                mStrPumpSer5ID_Srv = mInvoicelistBeanResponse.get(i).getSetSerno().trim();
                mStrMotorSer5ID_Srv = mInvoicelistBeanResponse.get(i).getMatorSerno().trim();
                mStrControllerSer5ID_Srv = mInvoicelistBeanResponse.get(i).getControllerSerno().trim();
                // edtLRNo1ID.setText(mInvoicelistBeanResponse.get(i).get.trim());
            }
            else  if(i == 5)
            {
                edtBillNo6ID.setText(mInvoicelistBeanResponse.get(i).getBill().trim());
               // edtPumpSer6ID.setText(mInvoicelistBeanResponse.get(i).getSetSerno().trim());
              //  edtMotorSer6ID.setText(mInvoicelistBeanResponse.get(i).getMatorSerno().trim());
              //  edtControllerSer6ID.setText(mInvoicelistBeanResponse.get(i).getControllerSerno().trim());


                mStrPumpSer6ID_Srv = mInvoicelistBeanResponse.get(i).getSetSerno().trim();
                mStrMotorSer6ID_Srv = mInvoicelistBeanResponse.get(i).getMatorSerno().trim();
                mStrControllerSer6ID_Srv = mInvoicelistBeanResponse.get(i).getControllerSerno().trim();
                // edtLRNo1ID.setText(mInvoicelistBeanResponse.get(i).get.trim());
            }

        }


        if(WebURL.FORM_CHECK_LENGTH == 1)
        {

            cradViewF1ID.setVisibility(View.VISIBLE);
            cradViewF2ID.setVisibility(View.GONE);
            cradViewF3ID.setVisibility(View.GONE);
            cradViewF4ID.setVisibility(View.GONE);
            cradViewF5ID.setVisibility(View.GONE);
            cradViewF6ID.setVisibility(View.GONE);
        }
        else  if(WebURL.FORM_CHECK_LENGTH == 2)
        {
            cradViewF1ID.setVisibility(View.VISIBLE);
            cradViewF2ID.setVisibility(View.VISIBLE);
            cradViewF3ID.setVisibility(View.GONE);
            cradViewF4ID.setVisibility(View.GONE);
            cradViewF5ID.setVisibility(View.GONE);
            cradViewF6ID.setVisibility(View.GONE);
        }
        else  if(WebURL.FORM_CHECK_LENGTH == 3)
        {
            cradViewF1ID.setVisibility(View.VISIBLE);
            cradViewF2ID.setVisibility(View.VISIBLE);
            cradViewF3ID.setVisibility(View.VISIBLE);
            cradViewF4ID.setVisibility(View.GONE);
            cradViewF5ID.setVisibility(View.GONE);
            cradViewF6ID.setVisibility(View.GONE);
        }
        else  if(WebURL.FORM_CHECK_LENGTH == 4)
        {
            cradViewF1ID.setVisibility(View.VISIBLE);
            cradViewF2ID.setVisibility(View.VISIBLE);
            cradViewF3ID.setVisibility(View.VISIBLE);
            cradViewF4ID.setVisibility(View.VISIBLE);
            cradViewF5ID.setVisibility(View.GONE);
            cradViewF6ID.setVisibility(View.GONE);
        }
        else  if(WebURL.FORM_CHECK_LENGTH == 5)
        {
            cradViewF1ID.setVisibility(View.VISIBLE);
            cradViewF2ID.setVisibility(View.VISIBLE);
            cradViewF3ID.setVisibility(View.VISIBLE);
            cradViewF4ID.setVisibility(View.VISIBLE);
            cradViewF5ID.setVisibility(View.VISIBLE);
            cradViewF6ID.setVisibility(View.GONE);
        }
        else  if(WebURL.FORM_CHECK_LENGTH == 6)
        {
            cradViewF1ID.setVisibility(View.VISIBLE);
            cradViewF2ID.setVisibility(View.VISIBLE);
            cradViewF3ID.setVisibility(View.VISIBLE);
            cradViewF4ID.setVisibility(View.VISIBLE);
            cradViewF5ID.setVisibility(View.VISIBLE);
            cradViewF6ID.setVisibility(View.VISIBLE);
        }


        initVarifedFormDataClick();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                if(checkScannerClick == 11)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtPumpSer1ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 12)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtMotorSer1ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 13)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtControllerSer1ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 21)
                {

                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtPumpSer2ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 22)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtMotorSer2ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 23)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtControllerSer2ID.setText(contents);
                  //  Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 31)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtPumpSer3ID.setText(contents);
                  //  Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 32)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtMotorSer3ID.setText(contents);
                  //  Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 33)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtControllerSer3ID.setText(contents);
                  //  Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 41)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtPumpSer4ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 42)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtMotorSer4ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 43)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtControllerSer4ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 51)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtPumpSer5ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 52)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtMotorSer5ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 53)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtControllerSer5ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 61)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtPumpSer6ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 62)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtMotorSer6ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }
                else  if(checkScannerClick == 63)
                {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    edtControllerSer6ID.setText(contents);
                   // Toast.makeText(mContext, contents, Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(mContext, "Please try again!", Toast.LENGTH_SHORT).show();
            }
          /*  if(resultCode == RESULT_CANCELLED){
                //handle cancel
            }*/
        }
    }

    private void initVarifedFormDataClick() {

        rlvVarified1ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mStrControllerSer1ID = edtControllerSer1ID.getText().toString().trim();
                mStrMotorSer1ID = edtMotorSer1ID.getText().toString().trim();
                mStrPumpSer1ID = edtPumpSer1ID.getText().toString().trim();

                if(!mStrPumpSer1ID_Srv.equalsIgnoreCase(mStrPumpSer1ID))
                {
                    Toast.makeText(mContext, "Pump serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed1ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrMotorSer1ID_Srv.equalsIgnoreCase(mStrMotorSer1ID))
                {
                    Toast.makeText(mContext, "Motor serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed1ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrControllerSer1ID_Srv.equalsIgnoreCase(mStrControllerSer1ID))
                {
                    Toast.makeText(mContext, "Controller serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed1ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else
                {
                    intVarified1ID=2;
                    imgVarifed1ID.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));
                }

            }
        });



        ///222

        rlvVarified2ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mStrControllerSer2ID = edtControllerSer2ID.getText().toString().trim();



                mStrMotorSer2ID = edtMotorSer2ID.getText().toString().trim();


                mStrPumpSer2ID = edtPumpSer2ID.getText().toString().trim();

                if(!mStrPumpSer2ID_Srv.equalsIgnoreCase(mStrPumpSer2ID))
                {
                    Toast.makeText(mContext, "Pump serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed2ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrMotorSer2ID_Srv.equalsIgnoreCase(mStrMotorSer2ID))
                {
                    Toast.makeText(mContext, "Motor serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed2ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrControllerSer2ID_Srv.equalsIgnoreCase(mStrControllerSer2ID))
                {
                    Toast.makeText(mContext, "Controller serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed2ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else
                {
                    intVarified2ID=2;
                    imgVarifed2ID.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));
                }



            }
        });

        ///33

        rlvVarified3ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mStrMotorSer3ID = edtMotorSer3ID.getText().toString().trim();
                mStrControllerSer3ID = edtControllerSer3ID.getText().toString().trim();
                mStrPumpSer3ID = edtPumpSer3ID.getText().toString().trim();

                if(!mStrPumpSer3ID_Srv.equalsIgnoreCase(mStrPumpSer3ID))
                {
                    Toast.makeText(mContext, "Pump serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed3ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrMotorSer3ID_Srv.equalsIgnoreCase(mStrMotorSer3ID))
                {
                    Toast.makeText(mContext, "Motor serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed3ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrControllerSer3ID_Srv.equalsIgnoreCase(mStrControllerSer3ID))
                {
                    Toast.makeText(mContext, "Controller serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed3ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else
                {
                    intVarified3ID=2;
                    imgVarifed3ID.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));
                }



            }
        });

        ///44

        rlvVarified4ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                mStrControllerSer4ID = edtControllerSer4ID.getText().toString().trim();
                mStrMotorSer4ID = edtMotorSer4ID.getText().toString().trim();
                mStrPumpSer4ID = edtPumpSer4ID.getText().toString().trim();

                if(!mStrPumpSer4ID_Srv.equalsIgnoreCase(mStrPumpSer4ID))
                {
                    Toast.makeText(mContext, "Pump serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed4ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrMotorSer4ID_Srv.equalsIgnoreCase(mStrMotorSer4ID))
                {
                    Toast.makeText(mContext, "Motor serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed4ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrControllerSer4ID_Srv.equalsIgnoreCase(mStrControllerSer4ID))
                {
                    Toast.makeText(mContext, "Controller serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed4ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else
                {
                    intVarified4ID=2;
                    imgVarifed4ID.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));
                }



            }
        });


 ///55

        rlvVarified5ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mStrControllerSer5ID = edtControllerSer5ID.getText().toString().trim();

                mStrMotorSer5ID = edtMotorSer5ID.getText().toString().trim();
                mStrPumpSer5ID = edtPumpSer5ID.getText().toString().trim();

                if(!mStrPumpSer5ID_Srv.equalsIgnoreCase(mStrPumpSer5ID))
                {
                    Toast.makeText(mContext, "Pump serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed5ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrMotorSer5ID_Srv.equalsIgnoreCase(mStrMotorSer5ID))
                {
                    Toast.makeText(mContext, "Motor serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed5ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrControllerSer5ID_Srv.equalsIgnoreCase(mStrControllerSer5ID))
                {
                    Toast.makeText(mContext, "Controller serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed5ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else
                {
                    intVarified5ID=2;
                    imgVarifed5ID.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));
                }



            }
        });


///55

        rlvVarified6ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mStrPumpSer6ID = edtPumpSer6ID.getText().toString().trim();

                mStrMotorSer6ID = edtMotorSer6ID.getText().toString().trim();

                mStrControllerSer6ID = edtControllerSer6ID.getText().toString().trim();


                if(!mStrPumpSer6ID_Srv.equalsIgnoreCase(mStrPumpSer6ID))
                {
                    Toast.makeText(mContext, "Pump serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed6ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrMotorSer6ID_Srv.equalsIgnoreCase(mStrMotorSer6ID))
                {
                    Toast.makeText(mContext, "Motor serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed6ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else  if(!mStrControllerSer6ID_Srv.equalsIgnoreCase(mStrControllerSer6ID))
                {
                    Toast.makeText(mContext, "Controller serial number not matched.", Toast.LENGTH_SHORT).show();
                    imgVarifed6ID.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
                }
                else
                {
                    intVarified6ID=2;
                    imgVarifed6ID.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));
                }



            }
        });



    }

    private void getDataAllEDT() {


        mStrVehicleNoID = edtVehicleNoID.getText().toString().trim();
      //  mStrDispachDateID1 = edtDispachDateID.getText().toString().trim();


       // mStrDispachDateID=mStrDispachDateID1.replace(".","");

       // mStrDeliveryDateID1 = edtDeliveryDateID.getText().toString().trim();

       // mStrDeliveryDateID=mStrDeliveryDateID1.replace(".","");

        mStrTransportNameID = edtTransportNameID.getText().toString().trim();
        mStrDriverNoID = edtDriverNoID.getText().toString().trim();
        mStrRFQNoID = edtRFQNoID.getText().toString().trim();


        mStrBillNo1ID = edtBillNo1ID.getText().toString().trim();
        mStrBillNo2ID = edtBillNo2ID.getText().toString().trim();
        mStrBillNo3ID = edtBillNo3ID.getText().toString().trim();
        mStrBillNo4ID = edtBillNo4ID.getText().toString().trim();
        mStrBillNo5ID = edtBillNo5ID.getText().toString().trim();
        mStrBillNo6ID = edtBillNo6ID.getText().toString().trim();


        mStrLRNo1ID = edtLRNo1ID.getText().toString().trim();
        mStrLRNo2ID = edtLRNo2ID.getText().toString().trim();
        mStrLRNo3ID = edtLRNo3ID.getText().toString().trim();
        mStrLRNo4ID = edtLRNo4ID.getText().toString().trim();
        mStrLRNo5ID = edtLRNo5ID.getText().toString().trim();
        mStrLRNo6ID = edtLRNo6ID.getText().toString().trim();


        mStrPumpSer1ID = edtPumpSer1ID.getText().toString().trim();
        mStrPumpSer2ID = edtPumpSer2ID.getText().toString().trim();
        mStrPumpSer3ID = edtPumpSer3ID.getText().toString().trim();
        mStrPumpSer4ID = edtPumpSer4ID.getText().toString().trim();
        mStrPumpSer5ID = edtPumpSer5ID.getText().toString().trim();
        mStrPumpSer6ID = edtPumpSer6ID.getText().toString().trim();


        mStrMotorSer1ID = edtMotorSer1ID.getText().toString().trim();
        mStrMotorSer2ID = edtMotorSer2ID.getText().toString().trim();
        mStrMotorSer3ID = edtMotorSer3ID.getText().toString().trim();
        mStrMotorSer4ID = edtMotorSer4ID.getText().toString().trim();
        mStrMotorSer5ID = edtMotorSer5ID.getText().toString().trim();
        mStrMotorSer6ID = edtMotorSer6ID.getText().toString().trim();


        mStrControllerSer1ID = edtControllerSer1ID.getText().toString().trim();
        mStrControllerSer2ID = edtControllerSer2ID.getText().toString().trim();
        mStrControllerSer3ID = edtControllerSer3ID.getText().toString().trim();
        mStrControllerSer4ID = edtControllerSer4ID.getText().toString().trim();
        mStrControllerSer5ID = edtControllerSer5ID.getText().toString().trim();
        mStrControllerSer6ID = edtControllerSer6ID.getText().toString().trim();


        ititValidationHeader();


    }

    private void ititValidationHeader() {


        if(mStrVehicleNoID.equalsIgnoreCase(""))
        {
            Toast.makeText(mContext, "Please eneter vehicle number", Toast.LENGTH_SHORT).show();
        }
        else if(mStrDispachDateID.equalsIgnoreCase(""))
        {
            Toast.makeText(mContext, "Please select Dispach Date", Toast.LENGTH_SHORT).show();
        }
        else if(mStrDeliveryDateID.equalsIgnoreCase(""))
        {
            Toast.makeText(mContext, "Please select Delivery Date", Toast.LENGTH_SHORT).show();
        }
        else if(mStrTransportNameID.equalsIgnoreCase(""))
        {
            Toast.makeText(mContext, "Please enter transport name", Toast.LENGTH_SHORT).show();
        }
        else if(mStrDriverNoID.equalsIgnoreCase(""))
        {
            Toast.makeText(mContext, "Please enter driver number", Toast.LENGTH_SHORT).show();
        }
        else if(mStrRFQNoID.equalsIgnoreCase(""))
        {
            Toast.makeText(mContext, "Please enter RFQ number", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(WebURL.FORM_CHECK_LENGTH == 1)
            {

                if(intVarified1ID == 2)
                {
                    new SaveAllSANDFData().execute();
                }


            }
            else  if(WebURL.FORM_CHECK_LENGTH == 2)
            {

                if(intVarified1ID == 2)
                {
                    if(intVarified2ID == 2)
                    {
                        new SaveAllSANDFData().execute();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                }

            }
            else  if(WebURL.FORM_CHECK_LENGTH == 3)
            {
                if(intVarified1ID == 2)
                {
                    if(intVarified2ID == 2)
                    {
                        if(intVarified3ID == 2)
                        {
                            new SaveAllSANDFData().execute();
                        }
                        else
                        {
                            Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                }

            }
            else  if(WebURL.FORM_CHECK_LENGTH == 4)
            {
                if(intVarified1ID == 2)
                {
                    if(intVarified2ID == 2)
                    {
                        if(intVarified3ID == 2)
                        {
                            if(intVarified4ID == 2)
                            {
                                new SaveAllSANDFData().execute();
                            }
                            else
                            {
                                Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                }

            }
            else  if(WebURL.FORM_CHECK_LENGTH == 5)
            {
                if(intVarified1ID == 2)
                {
                    if(intVarified2ID == 2)
                    {
                        if(intVarified3ID == 2)
                        {
                            if(intVarified4ID == 2)
                            {
                                if(intVarified5ID == 2)
                                {
                                    new SaveAllSANDFData().execute();
                                }
                                else
                                {
                                    Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                }
            }
            else  if(WebURL.FORM_CHECK_LENGTH == 6)
            {
                if(intVarified1ID == 2)
                {
                    if(intVarified2ID == 2)
                    {
                        if(intVarified3ID == 2)
                        {
                            if(intVarified4ID == 2)
                            {
                                if(intVarified5ID == 2)
                                {
                                    if(intVarified6ID == 2)
                                    {
                                        new SaveAllSANDFData().execute();
                                    }
                                    else
                                    {
                                        Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Please varify serial number", Toast.LENGTH_SHORT).show();
                }

            }

          //  new SaveAllSANDFData().execute();
           // Toast.makeText(mContext, "Ready for Go....", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {

        baseRequest = new BaseRequest(mContext);


        mInvoicelistBeanResponse = (List<InvoicelistBeanResponse>) getIntent().getSerializableExtra("InvoiceList");

        System.out.println("mLrInvoiceResponse.size()2==>>"+mInvoicelistBeanResponse.size());

     //   baseRequest = new BaseRequest(this);
      //  mFaultRecordResponse = new ArrayList<>();

     //   edtLrMobileID = findViewById(R.id.edtLrMobileID);
       // txtOpenCalenderID = findViewById(R.id.txtOpenCalenderID);
        rlvBackViewID = findViewById(R.id.rlvBackViewID);

        rlvBackViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        String dayNumber  = (String) DateFormat.format("dd",   new Date()); // 06
        String monthNumber  = (String) DateFormat.format("MM",   new Date()); // 06
        //   String year         = (String) DateFormat.format("yyyy", new Date()); // 2013
        String year         = (String) DateFormat.format("yyyy", new Date()); // 13
        System.out.println("mMonthV11==>>"+dayNumber+"/"+monthNumber+"/"+year);
        /*Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(year), Integer.parseInt(monthNumber), Integer.parseInt(dayNumber));
        dpd.setMaxDate(c);*/


        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    ActivityUpdateSandFData.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    ActivityUpdateSandFData.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );


        }



        dpd.setAccentColor(Color.parseColor("#034f84"));
        dpd.setTitle("Pic LR Date");
        dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.VERTICAL);
      //  dpd.setMinDate(now);
        dpd.setMaxDate(now);
       // dpd.set(new Date().getTime());
        dpd.setOnCancelListener(dialog -> {
            Log.d("DatePickerDialog", "Dialog was cancelled");
            dpd = null;
        });
        //dpd.show(requireFragmentManager(), "Datepickerdialog");
        //dpd.getShowsDialog();



        dpd.show(this.dpd.getParentFragmentManager(), "DatePickerDialog");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dpd = null;
    }

  /*  @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) this.dpd.getParentFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }*/

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int numDigitsDay = String.valueOf(dayOfMonth).length();
        int numDigitsMonth = String.valueOf(monthOfYear).length();

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
        String date = "You picked the following date: "+mDayV+"/"+mMonthV+"/"+year;
        System.out.println("date==>>"+date);
        System.out.println("mMonthV==>>"+mMonthV+""+year);

       // String oldstring = mMonthV+"-"+mDayV+"-"+year;
        String oldstring = mDayV+"."+mMonthV+"."+year;
       // String oldstring = year+""+mMonthV+""+mDayV;
        String oldstring1 = year+""+mMonthV+""+mDayV;
        String newstring;
        String str = String.valueOf(year);
      //  edtLrMobileID.setText(oldstring);
        String lstchar = ""+str.charAt(str.length() - 1);
        String lndchar = ""+str.charAt(str.length() - 2);

    //    mStrDispachDateID
             //   mStrDeliveryDateID
        if(checkDateClick == 1)
        {
            edtDispachDateID.setText(oldstring);
            mStrDispachDateID = oldstring1;
        }
        else if(checkDateClick == 2)
        {
            edtDeliveryDateID.setText(oldstring);
            mStrDeliveryDateID = oldstring1;
        }

        System.out.println("splitValue===>>"+lstchar+""+lndchar);
           /* Date dateold = new SimpleDateFormat("yyyy-MM-DD ").parse(oldstring);


             newstring = new SimpleDateFormat("yy-MM-DD").format(dateold);
             String [] splitValue =newstring.split("-");
            System.out.println("splitValue===>>"+splitValue[0]); // 2011-01-18*/





    }




    private class SaveAllSANDFData extends AsyncTask<String, String, String> {

       // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;

            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                // jsonObj.put("project_no", projno);
                jsonObj.put("WERKS",mInvoicelistBeanResponse.get(0).getWERKS());
                jsonObj.put("RFQ_DOC",mStrRFQNoID);
                jsonObj.put("ZDOCDATE",mStrDispachDateID);
                //jsonObj.put("ZBOOKDATE",mStrDispachDateID);
                jsonObj.put("ZTRANSNAME",mStrTransportNameID);
                jsonObj.put("TRANSPORTER_MOB",mStrDriverNoID);
                jsonObj.put("DEL_DATE",mStrDeliveryDateID);
                jsonObj.put("FREIGHT_TYPE","01");
                jsonObj.put("VEHICLE_NO",mStrVehicleNoID);

                // jsonObj.put("beneficiary",ben);
                //  jsonObj.put("mobno",mLrInvoiceResponse.get(0).getMobno());
                //jsonObj.put("kunnr", mUserID);

                jsonObj.put("ZLRNO1", mStrLRNo1ID);
                jsonObj.put("ZLRNO2", mStrLRNo2ID);
                jsonObj.put("ZLRNO3", mStrLRNo3ID);
                jsonObj.put("ZLRNO4", mStrLRNo4ID);
                jsonObj.put("ZLRNO5", mStrLRNo5ID);
                jsonObj.put("ZLRNO6", mStrLRNo6ID);

                jsonObj.put("VBELN1", mStrBillNo1ID);
                jsonObj.put("VBELN2", mStrBillNo2ID);
                jsonObj.put("VBELN3", mStrBillNo3ID);
                jsonObj.put("VBELN4", mStrBillNo4ID);
                jsonObj.put("VBELN5", mStrBillNo5ID);
                jsonObj.put("VBELN6", mStrBillNo6ID);


               /* String MOB_NAME = CustomUtility.getSharedPreferences(mContext, "MOBName");
                String MOB_API_NAME = CustomUtility.getSharedPreferences(mContext,"MOBversionAPI");
                String MOB_VERSION_NAME = CustomUtility.getSharedPreferences(mContext,"MOBversionRelease");

                jsonObj.put("MOB_NAME", MOB_NAME);
                jsonObj.put("MOB_API_NAME", MOB_API_NAME);
                jsonObj.put("MOB_VERSION_NAME", MOB_VERSION_NAME);*/



                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "No internet connection!!\n\nData save in local storage", Toast.LENGTH_SHORT).show();
                // mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO,SIGNL_STREN,SIM,NET_REG,SER_CONNECT,CAB_CONNECT,LATITUDE,LANGITUDE,MOBILE,IMEI,DONGAL_ID,MUserId,true);
                //  mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO,SIGNL_STREN,SIM,NET_REG,SER_CONNECT,CAB_CONNECT,LATITUDE,LANGITUDE,MOBILE,IMEI,DONGAL_ID,MUserId,RMS_STATUS,RMS_CURRENT_ONLINE_STATUS,RMS_LAST_ONLINE_DATE, true);
                // long iiii = mDatabaseHelperTeacher.insertDeviceDebugInforData(DEVICE_NO,SIGNL_STREN,SIM,NET_REG,SER_CONNECT,CAB_CONNECT,LATITUDE,LANGITUDE,MOBILE,IMEI,DONGAL_ID,MUserId,RMS_STATUS, RMS_CURRENT_ONLINE_STATUS,RMS_LAST_ONLINE_DATE, mInstallerName, mInstallerMOB, true);
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("SAVE", String.valueOf(ja_invc_data)));///array name lr_save
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                //obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVE_INSTALLATION_DATA, param1_invc);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.API_SNF_DATA_SAVE, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (!obj2.equalsIgnoreCase("")) {
                    JSONObject object = new JSONObject(obj2);
                    String mStatus = object.getString("status");
                    final String mMessage = object.getString("message");
                    String jo11 = object.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, "Data Submitted Successfully...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }

                        });


                        //changeButtonVisibilityRLV(true, 1.0f, rlvBT_7_ID_save);

                       // finish();
                        //finish();
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(mContext, "Data Not Submitted, Please try After Sometime", Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();

                            }

                        });

                        //  finish();
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            onResume();

            progressDialog.dismiss();  // dismiss dialog


        }
    }





}
