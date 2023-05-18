package activity;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.administrator.shaktiTransportApp.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import backgroundservice.SyncDataService;
import bean.LoginBean;
import bean.RfqBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class ConfrmVehical extends AppCompatActivity {

    String rfq_docno,amount,pernr,pernr_name,objs,veh_no,veh_typ,veh_id,drv_nam,drv_mob,lr11,lr12,lr13,lr14,lr15,lr16,lr17,lr18,lr19,lr20;
    ScrollView scrollView;
    TextView tv_save;
    Context context;
    DatabaseHelper db;
    private Toolbar mToolbar;
    String docno_sap;
    AlertDialog dialog;
    String rfq_update;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ConfrmVehical.this, mString, Toast.LENGTH_LONG).show();
        }
    };

    EditText rfq_no,amt,vehcl_no,vehcl_type,drv_nm,drv_mb,lr1,lr2,lr3,lr4,lr5,lr6,lr7,lr8,lr9,lr10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnfrm_vehical);

        Bundle bundle = getIntent().getExtras();
        rfq_docno = bundle.getString("rfq_docno");
        amount = bundle.getString("amount");

        context = this;
        db = new DatabaseHelper(context);


        LoginBean lb = new LoginBean();

        pernr = lb.getUseid();
        pernr_name = lb.getUsername();
         objs = lb.getUsertype();
//Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Confirm Vehical");

        getLayout();


        vehcl_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicaltypDialog();
            }
        });


        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rfq_docno = rfq_no.getText().toString();
                amount = amt.getText().toString();
                veh_no = vehcl_no.getText().toString();
                veh_typ = vehcl_type.getText().toString();
                drv_nam = drv_nm.getText().toString();
                drv_mob = drv_mb.getText().toString();

                lr11 = lr1.getText().toString();
                lr12 = lr2.getText().toString();
                lr13 = lr3.getText().toString();
                lr14 = lr4.getText().toString();
                lr15 = lr5.getText().toString();
                lr16 = lr6.getText().toString();
                lr17 = lr7.getText().toString();
                lr18 = lr8.getText().toString();
                lr19 = lr9.getText().toString();
                lr20 = lr10.getText().toString();

                if(!TextUtils.isEmpty(vehcl_type.getText()))
                {

                    String name = String.valueOf(vehcl_type.getText());
                    if(name.equals("Full Load (FTL)"))
                    {

                        veh_id = "01";
                    }
                    else{

                        veh_id = "02";
                    }
                }

                if (!TextUtils.isEmpty(veh_no) &&
                        !TextUtils.isEmpty(veh_typ) &&
                        !TextUtils.isEmpty(drv_nam) &&
                        !TextUtils.isEmpty(drv_mob) &&
                        !TextUtils.isEmpty(lr11)) {

                    if (CustomUtility.isInternetOn()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false); // if you want user to wait for some process to finish,
                        builder.setView(R.layout.layout_loading);
                        dialog = builder.create();
                        SyncCnfVehDataToSap(context,rfq_docno,pernr,objs,veh_no,veh_id,drv_nam,drv_mob,lr11,lr12,lr13,lr14,lr15,lr16,lr17,lr18,lr19,lr20);

                    } else {

                        Toast.makeText(getApplicationContext(), "No internet Connection...,Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                    }
                    onBackPressed();

                } else {
                    Toast.makeText(context, "Please fill all the required fields before submit data", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void vehicaltypDialog() {

        int checkedItem ;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Vehical Type");
        final String[] items = {"Full Load (FTL)","Part Load & Local"};

        final String[] usb = new String[1];

        if(!TextUtils.isEmpty(vehcl_type.getText()))
        {

            String name = String.valueOf(vehcl_type.getText());
            if(name.equals("Full Load (FTL)"))
            {
                checkedItem = 0;
                veh_id = "01";
            }
            else{
                checkedItem = 1;
                veh_id = "02";
            }
        }
        else{

            checkedItem = -1;
        }


        alertDialog.setSingleChoiceItems(items, checkedItem,  new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                usb[0] = items[arg1];

                if(usb[0].equals("Full Load (FTL)"))
                {
                    veh_id = "01";
                }
                else{
                    veh_id = "02";
                }



            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                vehcl_type.setText(usb[0]);

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void getLayout() {

        scrollView = findViewById(R.id.scrollview);

        rfq_no = (EditText) findViewById(R.id.rfq);
        amt = (EditText) findViewById(R.id.amt);
        vehcl_no = (EditText) findViewById(R.id.vehical_no);
        vehcl_type = (EditText) findViewById(R.id.vehical_typ);
        drv_mb = (EditText) findViewById(R.id.driver_mb);
        drv_nm = (EditText) findViewById(R.id.driver_nm);
        lr1 = (EditText) findViewById(R.id.lr1);
        lr2 = (EditText) findViewById(R.id.lr2);
        lr3 = (EditText) findViewById(R.id.lr3);
        lr4 = (EditText) findViewById(R.id.lr4);
        lr5  = (EditText) findViewById(R.id.lr5);
        lr6 = (EditText) findViewById(R.id.lr6);
        lr7 = (EditText) findViewById(R.id.lr7);
        lr8 = (EditText) findViewById(R.id.lr8);
        lr9 = (EditText) findViewById(R.id.lr9);
        lr10 = (EditText) findViewById(R.id.lr10);

        tv_save = (TextView) findViewById(R.id.save);

        rfq_no.setText(rfq_docno);
        amt.setText(amount);





    }

    public void SyncCnfVehDataToSap(Context context,String rfq_docno, String pernr, String objs, String veh_no, String veh_typ, String drv_nam, String drv_mob, String lr11, String lr12,String lr13,String lr14,String lr15,String lr16,String lr17,String lr18,String lr19,String lr20) {

        this.context = context;

            JSONArray ja_rfq_data = new JSONArray();

                JSONObject jsonObj = new JSONObject();

                try {


                    jsonObj.put("rfq_doc",rfq_docno);
                    jsonObj.put("res_pernr",pernr);
                    jsonObj.put("res_pernr_type",objs);
                    jsonObj.put("vehicle_rc",veh_no);
                    jsonObj.put("vehicle_type",veh_typ);
                    jsonObj.put("driver_name",drv_nam);
                    jsonObj.put("driver_mob",drv_mob);
                    jsonObj.put("lr_no1",lr11);
                    jsonObj.put("lr_no2",lr12);
                    jsonObj.put("lr_no3",lr13);
                    jsonObj.put("lr_no4",lr14);
                    jsonObj.put("lr_no5",lr15);
                    jsonObj.put("lr_no6",lr16);
                    jsonObj.put("lr_no7",lr17);
                    jsonObj.put("lr_no8",lr18);
                    jsonObj.put("lr_no9",lr19);
                    jsonObj.put("lr_no10",lr20);

                    ja_rfq_data.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();

                    if(dialog!=null)
                    {
                        dialog.dismiss();
                    }

                }

            final ArrayList<NameValuePair> param_rfq1 = new ArrayList<NameValuePair>();
            param_rfq1.add(new BasicNameValuePair("UPDATE_DATA", String.valueOf(ja_rfq_data)));


            try {
                String obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param_rfq1);

                if (obj2 != "") {

                    JSONArray ja = new JSONArray(obj2);

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);


                        docno_sap = jo.getString("docno");
                        rfq_update = jo.getString("rfq_update");

                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }

                        if ("Y".equals(rfq_update)) {

                            Message msg = new Message();
                            msg.obj = "Vehical Confirm Successfully...";
                            mHandler.sendMessage(msg);

                        }
                        else{

                            Message msg = new Message();
                            msg.obj = "Please try again...";
                            mHandler.sendMessage(msg);

                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                if(dialog!=null)
                {
                    dialog.dismiss();
                }
            }

    }



}

