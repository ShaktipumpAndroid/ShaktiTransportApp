package activity;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.administrator.shaktiTransportApp.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import activity.PodBean.AssignedDeliveryDetailResponse;
import activity.PodBean.DeliveryDeliveredInput;
import adapter.AssignedDeliveryAdapter;
import bean.LoginBean;
import database.DatabaseHelper;
import utility.ClickListiner;
import utility.CustomUtility;
import webservice.CameraUtils;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class AssignedDeliveryListActivity extends AppCompatActivity implements ClickListiner {

    private RecyclerView recyclerView;
    private AssignedDeliveryAdapter assignedDeliveryAdapter;
    private ProgressDialog progressDialog;
    private android.os.Handler mHandler;
    private ArrayList<AssignedDeliveryDetailResponse> assignedDeliveryResponseArrayList = new ArrayList<>();
    DatabaseHelper db;
    Context context;
    private Toolbar mToolbar;
    String mImageFolderName = "/SKAPP/UNLOAD/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_delivery_list);
        context = this;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = new DatabaseHelper(context);
        db.getLogin();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Assigned Delivery List");
        submitAssignedDeliveryDeliveredData();
//        getAssignedDeliveryList();
        mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mString = (String) msg.obj;
                Toast.makeText(AssignedDeliveryListActivity.this, mString, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void getAssignedDeliveryList() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        LoginBean loginBean = new LoginBean();
        String username = LoginBean.getUseid();
        param.add(new BasicNameValuePair("driver_mob", username));

        progressDialog = ProgressDialog.show(AssignedDeliveryListActivity.this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.GET_ASSIGNED_DELIVERY_LIST, param);
                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObject = new JSONObject(obj.trim());
                            if (jsonObject.getBoolean("status")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json = jsonArray.getJSONObject(i);
                                    Gson gson = new Gson();
                                    AssignedDeliveryDetailResponse assignedDeliveryResponse = gson.fromJson(String.valueOf(json), AssignedDeliveryDetailResponse.class);
                                    assignedDeliveryResponseArrayList.add(assignedDeliveryResponse);
                                }
                                db.deleteTableData(DatabaseHelper.TABLE_ASSIGNED_DELIVERIES);
                                db.insertAssignedDeliveriesData(assignedDeliveryResponseArrayList);
                            } else {
                                Message msg = new Message();
                                msg.obj = jsonObject.getString("message");
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
//                        setDataOnAdapter();
                        Message msg = new Message();
                        msg.obj = "No Internet Connection";
                        mHandler.sendMessage(msg);
                    }
                    progressDialog.dismiss();
                    runOnUiThread(() -> {
                        setDataOnAdapter();
                    });
                } catch (Exception e) {
                    progressDialog.dismiss();
                    runOnUiThread(() -> {
                        setDataOnAdapter();
                    });
                }
            }
        }.start();
    }

    private void setDataOnAdapter() {
        try {
            assignedDeliveryResponseArrayList.clear();
            assignedDeliveryResponseArrayList = db.getAssignedDeliveryListData();
            assignedDeliveryAdapter = new AssignedDeliveryAdapter(assignedDeliveryResponseArrayList, this, this::click);
            recyclerView.setAdapter(assignedDeliveryAdapter);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void click(int index) {
        Intent intentNewDeliveryRequestActivity = new Intent(AssignedDeliveryListActivity.this, AssignedDeliveryDetailActivity.class);
        intentNewDeliveryRequestActivity.putExtra("rfqNoValue", assignedDeliveryResponseArrayList.get(index).getRfqDoc());
        startActivityForResult(intentNewDeliveryRequestActivity, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sync_state:
                return true;
            case R.id.action_pod_state:
                return true;
            case R.id.action_signout:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Are you sure you wish to Sign Out ?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        db.deleteTableData(DatabaseHelper.TABLE_LOGIN);
        CustomUtility.setSharedPreference(context, "capture", "0");
        String selectedFilePath = "/storage/emulated/0/signdemo/signature.jpg";
        File file = new File(selectedFilePath);
        boolean deleted = file.delete();
        OnBackPressed();
    }

    private void OnBackPressed() {
        System.exit(0);
    }

    private void submitAssignedDeliveryDeliveredData() {
        LoginBean loginBean = new LoginBean();
        String driverMob = loginBean.getUseid();
        WebURL.CUSTOMERID_ID = driverMob;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        DatabaseHelper dataHelper = new DatabaseHelper(context);
//        int count = 0;
        ArrayList<DeliveryDeliveredInput> deliveryDeliveredInputList = dataHelper.getDeliveredData();

        progressDialog = ProgressDialog.show(this, "", "Sync Data on Server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        for (int i = 0; i < deliveryDeliveredInputList.size(); i++) {
                            JSONArray ja_invc_data = new JSONArray();
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("rfq_doc", deliveryDeliveredInputList.get(i).getRfqDoc());
                                jsonObj.put("driver_mob", deliveryDeliveredInputList.get(i).getDriverMob());
                                jsonObj.put("res_pernr", deliveryDeliveredInputList.get(i).getResPernr());
                                jsonObj.put("left_photo", deliveryDeliveredInputList.get(i).getPhoto1());
                                jsonObj.put("right_photo", deliveryDeliveredInputList.get(i).getPhoto2());
                                jsonObj.put("top_photo", deliveryDeliveredInputList.get(i).getPhoto3());
                                jsonObj.put("front_photo", deliveryDeliveredInputList.get(i).getPhoto4());
                                jsonObj.put("lrcopy_photo", deliveryDeliveredInputList.get(i).getPhoto5());
                                jsonObj.put("matrecp_photo", deliveryDeliveredInputList.get(i).getPhoto6());
                                jsonObj.put("bill_no", deliveryDeliveredInputList.get(i).getBillNo());
                                jsonObj.put("trans_no", deliveryDeliveredInputList.get(i).getTransNo());
                                jsonObj.put("assign_mob", deliveryDeliveredInputList.get(i).getAssignMob());
                                ja_invc_data.put(jsonObj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
                            param1_invc.add(new BasicNameValuePair("final", String.valueOf(ja_invc_data)));
                            String obj = CustomHttpClient.executeHttpPost1(WebURL.SUBMIT_ASSIGNED_DELIVERY_DELIVERED, param1_invc);

                            if (!obj.equalsIgnoreCase("")) {
                                JSONObject jsonObject = new JSONObject(obj.trim());
                                boolean status = jsonObject.getBoolean("status");
                                if (status || !status) {
                                    db.deleteDeliveredData(deliveryDeliveredInputList.get(i).getRfqDoc());
                                    CameraUtils.deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + CameraUtils.DELIVERED_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID));
                                }
                            }
                        }
                    }
                    callGetAssignedDeliveryListApi();
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    callGetAssignedDeliveryListApi();
                }
            }
        }.start();
    }

    private void callGetAssignedDeliveryListApi() {
        runOnUiThread(() -> {
            try {
                getAssignedDeliveryList();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callGetAssignedDeliveryListApi();
    }
}
