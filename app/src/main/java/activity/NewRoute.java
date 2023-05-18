package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.administrator.shaktiTransportApp.BuildConfig;
import com.administrator.shaktiTransportApp.R;

import java.util.ArrayList;
import java.util.List;

import backgroundservice.SyncDataService;
import bean.LoginBean;
import bean.RouteBean;
import database.DatabaseHelper;
import utility.CustomUtility;

public class NewRoute extends AppCompatActivity {

    String flag_display = "";
    Spinner spinner_fr_state,
            spinner_fr_district,
            spinner_fr_tehsil,
            spinner_to_state,
            spinner_to_district,
            spinner_to_tehsil,
            spinner_tr_mode;
    int index_fr_state,
            index_fr_district,
            index_fr_tehsil,
            index_to_state,
            index_to_district,
            index_to_tehsil,
            index_tr_mode;
    ArrayAdapter<String>
            dataAdapter_fr_district,
            dataAdapter_fr_state,
            dataAdapter_fr_tehsil,
            dataAdapter_to_district,
            dataAdapter_to_state,
            dataAdapter_to_tehsil,
            dataAdapter_tr_mode;
    int index_scheme;
    int index_category;
    List<String> list_fr_state = null;
    List<String> list_fr_district = null;
    List<String> list_fr_tehsil = null;
    List<String> list_to_state = null;
    List<String> list_to_district = null;
    List<String> list_to_tehsil = null;
    List<String> list_tr_mode = null;
    EditText et_distance;
    TextView tv_save;
    Context context;
    String state_fr_text,
            district_fr_text,
            tehsil_fr_text,
            state_to_text,
            district_to_text,
            tehsil_to_text,
            tr_mode_text,
            distance_text,
            status;
    String fr_tehsil = "",
            to_tehsil = "";
    DatabaseHelper db;
    RouteBean routeBean1 = new RouteBean();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);

        Bundle bundle = getIntent().getExtras();

        flag_display = bundle.getString("flag_display");
        fr_tehsil = bundle.getString("from_loc");
        to_tehsil = bundle.getString("to_loc");


        context = this;
        db = new DatabaseHelper(context);

        list_fr_state = new ArrayList<String>();
        list_fr_district = new ArrayList<String>();
        list_fr_tehsil = new ArrayList<String>();
        list_to_state = new ArrayList<String>();
        list_to_district = new ArrayList<String>();
        list_to_tehsil = new ArrayList<String>();
        list_tr_mode = new ArrayList<String>();


//Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Route Detail");

        getLayout();

        if ("X".equals(flag_display)) {

            routeBean1 = db.getRoute(fr_tehsil, to_tehsil);
            et_distance.setText(routeBean1.getDistance());

            isFocusOff();
        }

        spinner_fr_state.setPrompt("Select From State");
        spinner_fr_district.setPrompt("Select From District");
        spinner_fr_tehsil.setPrompt("Select From Tehsil");
        spinner_to_state.setPrompt("Select To State");
        spinner_to_district.setPrompt("Select To District");
        spinner_to_tehsil.setPrompt("Select To Tehsil");
        spinner_tr_mode.setPrompt("Select Mode of Transport");


        getTrModeValue();

        // Creating adapter for spinner
        dataAdapter_tr_mode = new ArrayAdapter<String>(this, R.layout.spinner_item_left, list_tr_mode);
        // Drop down layout style - list view with radio button
        dataAdapter_tr_mode.setDropDownViewResource(R.layout.spinner_item_center);
        // attaching data adapter to spinner
        spinner_tr_mode.setAdapter(dataAdapter_tr_mode);

        if ("X".equals(flag_display)) {

            if (!TextUtils.isEmpty((routeBean1.getTr_mode()))) {
                spinner_tr_mode.setSelection(db.getPosition(spinner_tr_mode, routeBean1.getTr_mode()));
            }
        }

        spinner_tr_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index_tr_mode = arg0.getSelectedItemPosition();
                tr_mode_text = spinner_tr_mode.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        list_fr_state.clear();
        list_fr_state = db.getList(DatabaseHelper.KEY_STATE, null, "Select From State");

        dataAdapter_fr_state = new ArrayAdapter<String>(this, R.layout.spinner_item_left, list_fr_state);
        dataAdapter_fr_state.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_fr_state.setAdapter(dataAdapter_fr_state);

        if ("X".equals(flag_display)) {
            if (!TextUtils.isEmpty(routeBean1.getFr_state())) {
                spinner_fr_state.setSelection(db.getPosition(spinner_fr_state, routeBean1.getFr_state()));
            }
        }


        spinner_fr_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index_fr_state = arg0.getSelectedItemPosition();
                state_fr_text = spinner_fr_state.getSelectedItem().toString().trim();

                list_fr_district.clear();
                list_fr_district = db.getList(DatabaseHelper.KEY_DISTRICT, state_fr_text, "Select From District");
                dataAdapter_fr_district = new ArrayAdapter<String>(context, R.layout.spinner_item_left, list_fr_district);
                dataAdapter_fr_district.setDropDownViewResource(R.layout.spinner_item_center);
                spinner_fr_district.setAdapter(dataAdapter_fr_district);

                if ("X".equals(flag_display)) {
                    if (!TextUtils.isEmpty(routeBean1.getFr_district())) {
                        spinner_fr_district.setSelection(db.getPosition(spinner_fr_district, routeBean1.getFr_district()));

                    }
                }

                spinner_fr_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                        index_fr_district = arg0.getSelectedItemPosition();
                        district_fr_text = spinner_fr_district.getSelectedItem().toString();

                        list_fr_tehsil.clear();
                        list_fr_tehsil = db.getList(DatabaseHelper.KEY_TEHSIL, district_fr_text, "Select From Tehsil");
                        dataAdapter_fr_tehsil = new ArrayAdapter<String>(context, R.layout.spinner_item_left, list_fr_tehsil);
                        dataAdapter_fr_tehsil.setDropDownViewResource(R.layout.spinner_item_center);
                        spinner_fr_tehsil.setAdapter(dataAdapter_fr_tehsil);

                        if ("X".equals(flag_display)) {
                            if (!TextUtils.isEmpty(routeBean1.getFr_tehsil())) {
                                spinner_fr_tehsil.setSelection(db.getPosition(spinner_fr_tehsil, routeBean1.getFr_tehsil()));

                            }
                        }
                        spinner_fr_tehsil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                                index_fr_tehsil = arg0.getSelectedItemPosition();
                                tehsil_fr_text = spinner_fr_tehsil.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        list_to_state.clear();
        list_to_state = db.getList(DatabaseHelper.KEY_STATE, null, "Select To State");


        dataAdapter_to_state = new ArrayAdapter<String>(this, R.layout.spinner_item_left, list_to_state);
        dataAdapter_to_state.setDropDownViewResource(R.layout.spinner_item_center);
        spinner_to_state.setAdapter(dataAdapter_to_state);

        if ("X".equals(flag_display)) {
            if (!TextUtils.isEmpty(routeBean1.getTo_state())) {
                spinner_to_state.setSelection(db.getPosition(spinner_to_state, routeBean1.getTo_state()));
            }
        }


        spinner_to_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index_to_state = arg0.getSelectedItemPosition();
                state_to_text = spinner_to_state.getSelectedItem().toString().trim();

                list_to_district.clear();
                list_to_district = db.getList(DatabaseHelper.KEY_DISTRICT, state_to_text, "Select To District");
                dataAdapter_to_district = new ArrayAdapter<String>(context, R.layout.spinner_item_left, list_to_district);
                dataAdapter_to_district.setDropDownViewResource(R.layout.spinner_item_center);
                spinner_to_district.setAdapter(dataAdapter_to_district);

                if ("X".equals(flag_display)) {
                    if (!TextUtils.isEmpty(routeBean1.getTo_district())) {
                        spinner_to_district.setSelection(db.getPosition(spinner_to_district, routeBean1.getTo_district()));

                    }
                }

                spinner_to_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                        index_to_district = arg0.getSelectedItemPosition();
                        district_to_text = spinner_to_district.getSelectedItem().toString();

                        list_to_tehsil.clear();
                        list_to_tehsil = db.getList(DatabaseHelper.KEY_TEHSIL, district_to_text, "Select To Tehsil");
                        dataAdapter_to_tehsil = new ArrayAdapter<String>(context, R.layout.spinner_item_left, list_to_tehsil);
                        dataAdapter_to_tehsil.setDropDownViewResource(R.layout.spinner_item_center);
                        spinner_to_tehsil.setAdapter(dataAdapter_to_tehsil);

                        if ("X".equals(flag_display)) {
                            if (!TextUtils.isEmpty(routeBean1.getTo_tehsil())) {
                                spinner_to_tehsil.setSelection(db.getPosition(spinner_to_tehsil, routeBean1.getTo_tehsil()));
                            }
                        }

                        spinner_to_tehsil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                                index_to_tehsil = arg0.getSelectedItemPosition();
                                tehsil_to_text = spinner_to_tehsil.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                distance_text = et_distance.getText().toString().trim();

                if (!db.isSpinnerNotSelected(DatabaseHelper.KEY_FR_TEHSIL_TEXT, tehsil_fr_text) &&
                        !db.isSpinnerNotSelected(DatabaseHelper.KEY_TO_TEHSIL_TEXT, tehsil_to_text) &&
                        !db.isSpinnerNotSelected(DatabaseHelper.KEY_TR_MODE, tr_mode_text) &&
                        !TextUtils.isEmpty(distance_text)) {
                    SyncRouteDataInBackground();

                } else {
                    Toast.makeText(context, "Please fill all the required fields before submit data", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void UpdateRouteMaster() {

        status = "P";

        LoginBean loginBean = new LoginBean();

        RouteBean routeBean = new RouteBean(state_fr_text,
                district_fr_text,
                tehsil_fr_text,
                state_to_text,
                district_to_text,
                tehsil_to_text,
                tr_mode_text,
                distance_text,
                status,
                loginBean.getUseid(),
                loginBean.getUsertype());

        if (!db.checkRecord(DatabaseHelper.TABLE_ROUTE, DatabaseHelper.KEY_FR_TEHSIL_TEXT, tehsil_fr_text, DatabaseHelper.KEY_TO_TEHSIL_TEXT, tehsil_to_text)) {
            db.insertRouteData(DatabaseHelper.KEY_INSERT_ROUTE, routeBean);
        } else {
            Toast.makeText(getApplicationContext(), "Route already exist for above location", Toast.LENGTH_SHORT).show();
        }


    }


    public void SyncRouteDataInBackground() {
        if (CustomUtility.isInternetOn()) {


            UpdateRouteMaster();

            if (CustomUtility.isInternetOn()) {

                Intent i = new Intent(NewRoute.this, SyncDataService.class);
                i.putExtra("to_tehsil", tehsil_to_text);
                i.putExtra("from_tehsil", tehsil_fr_text);
                i.putExtra("sync_data", "route_data");
                startService(i);
                Toast.makeText(getApplicationContext(), "Route created Successful, pending for Approval", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(getApplicationContext(), "No internet Connection.... Route Data saved offline", Toast.LENGTH_SHORT).show();
            }


        } else {

            Toast.makeText(getApplicationContext(), "No internet Connection.... Please try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_signout:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getLayout() {
        spinner_fr_state = (Spinner) findViewById(R.id.spinner_fr_state);
        spinner_fr_district = (Spinner) findViewById(R.id.spinner_fr_district);
        spinner_fr_tehsil = (Spinner) findViewById(R.id.spinner_fr_tehsil);
        spinner_to_state = (Spinner) findViewById(R.id.spinner_to_state);
        spinner_to_district = (Spinner) findViewById(R.id.spinner_to_district);
        spinner_to_tehsil = (Spinner) findViewById(R.id.spinner_to_tehsil);
        spinner_tr_mode = (Spinner) findViewById(R.id.spinner_tr_mode);
        spinner_tr_mode = (Spinner) findViewById(R.id.spinner_tr_mode);
        et_distance = (EditText) findViewById(R.id.distance_et);
        tv_save = (TextView) findViewById(R.id.save);
    }

    public void getTrModeValue() {
        list_tr_mode.add("Mode of Transport");
        list_tr_mode.add("By Road");
        list_tr_mode.add("By Air");
        list_tr_mode.add("By Sea");
        list_tr_mode.add("By Train");
        list_tr_mode.add("Other");
    }


    public void isFocusOff() {

        et_distance.setFocusable(false);

        spinner_tr_mode.setEnabled(false);
        spinner_fr_state.setEnabled(false);
        spinner_fr_district.setEnabled(false);
        spinner_fr_tehsil.setEnabled(false);
        spinner_to_state.setEnabled(false);
        spinner_to_district.setEnabled(false);
        spinner_to_tehsil.setEnabled(false);

        tv_save.setVisibility(View.GONE);

    }


}
