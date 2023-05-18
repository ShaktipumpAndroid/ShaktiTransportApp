package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.Locale;

import adapter.AssignedDeliveryAdapter;
import adapter.PartialLoadAdapter;
import bean.LoginBean;
import bean.PartialLoadResponse;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class PartialLoadListActivity extends AppCompatActivity {
    Context mContext;
    ListView inst_list;
    PartialLoadAdapter partialLoadAdapter;
    private Toolbar mToolbar;
    private DatabaseHelper db;
    private ProgressDialog progressDialog;
    private ArrayList<PartialLoadResponse> partialLoadResponseArrayList = new ArrayList<>();
    private android.os.Handler mHandler;
    EditText editsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial_load_list);
        inst_list = (ListView) findViewById(R.id.partial_load_list);
        editsearch = (EditText) findViewById(R.id.search);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        db = new DatabaseHelper(mContext);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agreement List ");
        getPartialLoadList();
        mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mString = (String) msg.obj;
                Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
            }
        };

        inst_list.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(mContext, PartialLoadDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable("partialLoadDetail", partialLoadAdapter.arrayList.get(position));
            i.putExtras(extras);
            startActivity(i);
        });

        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                if (null != partialLoadAdapter)
                    partialLoadAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void getPartialLoadList() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        LoginBean loginBean = new LoginBean();
        String username = LoginBean.getUseid();
        param.add(new BasicNameValuePair("trans_no", username));

        progressDialog = ProgressDialog.show(this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.GET_PARTIAL_LOAD_LIST, param);
                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObject = new JSONObject(obj.trim());
                            if (jsonObject.getBoolean("status")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json = jsonArray.getJSONObject(i);
                                    Gson gson = new Gson();
                                    PartialLoadResponse partialLoadResponse = gson.fromJson(String.valueOf(json), PartialLoadResponse.class);
                                    partialLoadResponseArrayList.add(partialLoadResponse);
                                }
                                db.deleteTableData(DatabaseHelper.TABLE_PARTIAL_LOAD);
                                db.insertPartialLoadData(partialLoadResponseArrayList);
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
                    setDataOnAdapter();
                }
            }
        }.start();
    }

    private void setDataOnAdapter() {
        runOnUiThread(() -> {
            try {
                partialLoadResponseArrayList.clear();
                partialLoadResponseArrayList = db.getPartialLoadListData();
                partialLoadAdapter = new PartialLoadAdapter(mContext, partialLoadResponseArrayList);
                inst_list.setAdapter(partialLoadAdapter);
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
