package activity;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.shaktiTransportApp.BuildConfig;
import com.administrator.shaktiTransportApp.R;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapter.RfqCustomList1;
import bean.LoginBean;
import bean.RfqBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;


/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleApprovedActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    RecyclerView recyclerView;
    View.OnClickListener onclick;
    LinearLayout lin1, lin2;
    RfqCustomList1 adapter;
    DatabaseHelper db;
    int count, count1;
    ListView inst_list;
    TextView textview_rfq_id;
    String caseid_text = "";
    AlertDialog dialog;
    private LinearLayoutManager layoutManagerSubCategory;
    private EditText start_date, end_date;
    private TextView save;
    private Toolbar mToolbar;
    private TextInputLayout start, end;
    private String mUserID, type, mStart, mEnd, obj, from;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approved_vehicle);
        context = this;

        Bundle bundle = getIntent().getExtras();
        from = bundle.getString("from");

        db = new DatabaseHelper(context);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (from.equalsIgnoreCase("A")) {
            getSupportActionBar().setTitle("Approve Vehical");
        } else {
            getSupportActionBar().setTitle("Confirm Vehical");
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);

        inst_list = (ListView) findViewById(R.id.rfq_list);

        start = (TextInputLayout) findViewById(R.id.start);
        end = (TextInputLayout) findViewById(R.id.end);

        start_date = (EditText) findViewById(R.id.start_date);
        end_date = (EditText) findViewById(R.id.end_date);
        save = (TextView) findViewById(R.id.save);
        start_date.setFocusable(false);
        end_date.setFocusable(false);

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        start_date.setText(i2 + "/" + i1 + "/" + i);
                        mStart = start_date.getText().toString().trim();
                        parseDateToddMMyyyy1(mStart);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Start Date");
                datePickerDialog.show();
            }
        });

        // Date help for leave to
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        end_date.setText(i2 + "/" + i1 + "/" + i);
                        mEnd = end_date.getText().toString().trim();
                        parseDateToddMMyyyy2(mEnd);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("End Date");
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(view -> {
            if (CustomUtility.isInternetOn()) {
                // Write Your Code What you want to do
                LoginBean lb = new LoginBean();
                mUserID = lb.getUseid();
                type = lb.getUsertype();
                db.deleteEmployeeData(db.TABLE_RFQ);
                checkDataValtidation();
                ArrayList<RfqBean> quotationBeanArrayList = new ArrayList<RfqBean>();
                if (from.equalsIgnoreCase("A")) {
                    quotationBeanArrayList = db.getRfqappList();
                } else {
                    quotationBeanArrayList = db.getRfqcnfrmList();
                }
                adapter = new RfqCustomList1(context, from, quotationBeanArrayList);
                inst_list.setAdapter(adapter);
            } else {
                Toast.makeText(context, "No internet Connection ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
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


    @Override
    public void onClick(View view) {

    }

    public String parseDateToddMMyyyy1(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            mStart = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parseDateToddMMyyyy2(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            mEnd = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void checkDataValtidation() {
        try {
            if (mStart == null || mStart.equalsIgnoreCase("") || mStart.equalsIgnoreCase(null)) {
                start_date.setFocusable(true);
                start_date.requestFocus();
                Toast.makeText(context, getResources().getString(R.string.Please_select_start), Toast.LENGTH_SHORT).show();
                if (dialog != null)
                    dialog.dismiss();
            } else if (mEnd == null || mEnd.equalsIgnoreCase("") || mEnd.equalsIgnoreCase(null)) {
                end_date.setFocusable(true);
                end_date.requestFocus();
                Toast.makeText(context, getResources().getString(R.string.Please_select_end), Toast.LENGTH_SHORT).show();
                if (dialog != null)
                    dialog.dismiss();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading);
                dialog = builder.create();
                getApprovaData(context);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public void getApprovaData(Context context) {
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("pernr", mUserID));
        param.add(new BasicNameValuePair("begda", mStart));
        param.add(new BasicNameValuePair("endda", mEnd));
        param.add(new BasicNameValuePair("objs", type));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.APP_VEHICLE, param);
            Log.d("DATA", obj);
            JSONArray ja_rfq = new JSONArray(obj);
            for (int i = 0; i < ja_rfq.length(); i++) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                JSONObject jo_rfq = ja_rfq.getJSONObject(i);
                RfqBean rfqBean = new RfqBean(
                        jo_rfq.optString("rfq_doc"),
                        jo_rfq.optString("rfq_doc_manual"),
                        jo_rfq.optString("res_name"),
                        jo_rfq.optString("fr_location"),
                        jo_rfq.optString("to_location"),
                        jo_rfq.optString("fr_latlong"),
                        jo_rfq.optString("to_latlong"),
                        jo_rfq.optString("via_address"),
                        jo_rfq.optString("via_address_latlong"),
                        jo_rfq.optString("tr_mode"),
                        jo_rfq.optString("distance"),
                        jo_rfq.optString("vehicle_type"),
                        jo_rfq.optString("vehicle_weight"),
                        jo_rfq.optString("final_rate"),
                        jo_rfq.optString("delivery_place"),
                        jo_rfq.optString("transit_time"),
                        jo_rfq.optString("rfq_date"),
                        jo_rfq.optString("rfq_time"),
                        jo_rfq.optString("expiry_date"),
                        jo_rfq.optString("expiry_time"),
                        jo_rfq.optString("dala"),
                        jo_rfq.optString("height"),
                        jo_rfq.optString("sync"),
                        jo_rfq.optString("rfq_completed"),
                        jo_rfq.optString("rfq_status"),
                        jo_rfq.optString("res_pernr"),
                        jo_rfq.optString("res_pernr_type"),
                        jo_rfq.optString("vehicle_rc"),
                        jo_rfq.optString("confirmed"));

                if (dataHelper.isRecordExist(DatabaseHelper.TABLE_RFQ, DatabaseHelper.KEY_RFQ_DOC, rfqBean.getRfq_doc())) {
                    dataHelper.updateRfqData(dataHelper.KEY_RFQ_UPDATE_FROM_SAP, rfqBean);
                } else {
                    dataHelper.updateRfqData(dataHelper.KEY_INSERT_RFQ, rfqBean);
                }
                if (dialog != null)
                    dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (dialog != null)
                dialog.dismiss();
            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        if (mStart != null && mEnd != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false); // if you want user to wait for some process to finish,
            builder.setView(R.layout.layout_loading);
            dialog = builder.create();
            getApprovaData(context);
        }
    }

}
