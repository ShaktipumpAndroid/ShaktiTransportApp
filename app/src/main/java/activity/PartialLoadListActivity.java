package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.net.ParseException;

import com.administrator.shaktiTransportApp.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

import activity.languagechange.LocaleHelper;
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
    private DatabaseHelper db;
    private ProgressDialog progressDialog;
    private final ArrayList<PartialLoadResponse> partialLoadResponseArrayList = new ArrayList<>();
    private android.os.Handler mHandler;
    EditText editsearch;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial_load_list);
        inst_list =  findViewById(R.id.partial_load_list);
        editsearch =  findViewById(R.id.search);
        mContext = this;
        Toolbar mToolbar = findViewById(R.id.toolbar);
        db = new DatabaseHelper(mContext);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.Agreement_List));
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
        final ArrayList<NameValuePair> param = new ArrayList<>();
        String username = LoginBean.getUseid();
        param.add(new BasicNameValuePair("trans_no", username));

        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.Connecting));

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
                               // db.deleteTableData(DatabaseHelper.TABLE_PARTIAL_LOAD);
                                Log.e("length===>",""+partialLoadResponseArrayList.size());
                                db.insertPartialLoadData(partialLoadResponseArrayList);
                                progressDialog.dismiss();
                            } else {
                                Message msg = new Message();
                                msg.obj = jsonObject.getString("message");
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connecting_failed), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Message msg = new Message();
                        msg.obj = getResources().getString(R.string.No_Internet);
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

    @SuppressLint("SimpleDateFormat")
    private void setDataOnAdapter() {
        runOnUiThread(() -> {
            try {
                Log.e("PartialLoadList===>",""+db.getPartialLoadListData().size());
                ArrayList<PartialLoadResponse> partialLoadResponses = db.getPartialLoadListData();


                Collections.sort(partialLoadResponses, (o1, o2) -> {
                    try {
                        return new SimpleDateFormat("dd.MM.yyyy").parse(o1.getFkdat()).compareTo(new SimpleDateFormat("dd.MM.yyyy").parse(o2.getFkdat()));
                    } catch (ParseException e) {
                        return 0;
                    } catch (java.text.ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                System.out.println(partialLoadResponses);
                partialLoadAdapter = new PartialLoadAdapter(mContext, partialLoadResponses);
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
