package activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.administrator.shaktiTransportApp.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import adapter.OfficerAssignedDeliveryAdapter;
import bean.LoginBean;
import bean.OfficerAssignedDeliveryResponse;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class OfficerAssignedDeliveryListActivity extends AppCompatActivity {
    Context mContext;
    ListView inst_list;
    OfficerAssignedDeliveryAdapter officerAssignedDeliveryAdapter;
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
    private ArrayList<OfficerAssignedDeliveryResponse> officerAssignedDeliveryResponseArrayList = new ArrayList<>();
    private android.os.Handler mHandler;
    EditText editsearch, etTransportNo;
    TextView tvStartDate, tvStartDateValue, tvEndDate, tvEndDateValue;
    Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial_load_list);
        inst_list = (ListView) findViewById(R.id.partial_load_list);
        editsearch = (EditText) findViewById(R.id.search);
        etTransportNo = (EditText) findViewById(R.id.etTransportNo);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvStartDateValue = (TextView) findViewById(R.id.tvStartDateValue);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvEndDateValue = (TextView) findViewById(R.id.tvEndDateValue);
        btnGo = (Button) findViewById(R.id.btnGo);

        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(this.getString(R.string.assigned_delivery_list));

        mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mString = (String) msg.obj;
                Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
            }
        };

        btnGo.setOnClickListener(v -> {
            if (isInputValid()) {
                getOfficerAssignedDeliveryList();
            }
        });

        inst_list.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(mContext, OfficerAssignedDeliveryDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable("partialLoadDetail", officerAssignedDeliveryAdapter.arrayList.get(position));
            i.putExtras(extras);
            startActivity(i);
        });

        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                if (null != officerAssignedDeliveryAdapter)
                    officerAssignedDeliveryAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

        tvStartDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    OfficerAssignedDeliveryListActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> tvStartDateValue.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),
                    year, month, day);
            datePickerDialog.show();
        });

        tvEndDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    OfficerAssignedDeliveryListActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> tvEndDateValue.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private boolean isInputValid() {
        boolean result = false;
//        if (etTransportNo.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "Please Enter Transporter No", Toast.LENGTH_SHORT).show();
//        }
        if (tvStartDateValue.getText().equals("")) {
            Toast.makeText(this, "Please select Start Date", Toast.LENGTH_SHORT).show();
        } else if (tvEndDateValue.getText().equals("")) {
            Toast.makeText(this, "Please select End Date", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }

    private void getOfficerAssignedDeliveryList() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        final ArrayList<NameValuePair> param = new ArrayList<>();
        LoginBean loginBean = new LoginBean();
        String username = LoginBean.getUseid();
        param.add(new BasicNameValuePair("trans_no", etTransportNo.getText().toString().trim()));
        param.add(new BasicNameValuePair("billdt_low", tvStartDateValue.getText().toString()));
        param.add(new BasicNameValuePair("billdt_high", tvEndDateValue.getText().toString()));

        progressDialog = ProgressDialog.show(this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.GET_OFFICER_ASSIGNED_DELIVERY_LIST, param);
                        if (!obj.equalsIgnoreCase("")) {
                            officerAssignedDeliveryResponseArrayList.clear();
                            JSONObject jsonObject = new JSONObject(obj.trim());
                            if (jsonObject.getBoolean("status")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json = jsonArray.getJSONObject(i);
                                    Gson gson = new Gson();
                                    OfficerAssignedDeliveryResponse officerAssignedDeliveryResponse = gson.fromJson(String.valueOf(json), OfficerAssignedDeliveryResponse.class);
                                    officerAssignedDeliveryResponseArrayList.add(officerAssignedDeliveryResponse);
                                }
                                progressDialog.dismiss();
                            } else {
                                Message msg = new Message();
                                msg.obj = jsonObject.getString("message");
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
                    setDataOnAdapter();
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    private void setDataOnAdapter() {
        runOnUiThread(() -> {
            try {
                officerAssignedDeliveryAdapter = new OfficerAssignedDeliveryAdapter(mContext, officerAssignedDeliveryResponseArrayList);
                inst_list.setAdapter(officerAssignedDeliveryAdapter);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
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
