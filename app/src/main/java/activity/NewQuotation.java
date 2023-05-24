package activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.administrator.shaktiTransportApp.R;

import java.util.ArrayList;
import java.util.List;

import activity.languagechange.LocaleHelper;
import backgroundservice.SyncDataService;
import bean.LoginBean;
import bean.QuotationBean;
import bean.RfqBean;
import database.DatabaseHelper;
import utility.CustomUtility;

public class NewQuotation extends AppCompatActivity {

    ImageView img_add, img_delete;
    EditText et_vehicle_type, et_vehicle_weight, et_transit_time, et_delivery_place, et_rfq_date,
            et_rfq_time, et_expiry_date, et_expiry_time, et_route_from, et_route_to, et_distance, et_dala,
            et_height, et_pernr, et_pernrnm, et_mode, et_vehicle_rc, et_remark, et_vehicle_make, et_rate;
    CheckBox c_puc, c_insurence, c_licence;
    String flag_create_rfq = null;
    TextView tv_save, tv_get_route;
    Context context;
    String rfq_docno, fr_route, to_route, via_address_value, vehicle_rc, vehicle_make,
            rate, remark, chk_puc, chk_licence, chk_insurence;
    DatabaseHelper db;
    private Toolbar mToolbar;
    private LinearLayout moduleOneLL;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        Bundle bundle = getIntent().getExtras();
        rfq_docno = bundle.getString("rfq_docno");
        flag_create_rfq = bundle.getString("flag_create_rfq");
        context = this;
        db = new DatabaseHelper(context);

        mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.Quotation_));
        getLayout();
        setData();

        tv_save.setOnClickListener(v -> {
            getData();
            if (!TextUtils.isEmpty(vehicle_rc) && !TextUtils.isEmpty(vehicle_make) && !TextUtils.isEmpty(rate)) {
                SyncDataInBackground();
                onBackPressed();
            } else {
                Toast.makeText(context, getResources().getString(R.string.Please_fill_all), Toast.LENGTH_SHORT).show();
            }
        });

        tv_get_route.setOnClickListener(arg0 -> {
            fr_route = et_route_from.getText().toString();
            to_route = et_route_to.getText().toString();
            List<String> place_list = new ArrayList<>();

            if (!TextUtils.isEmpty(fr_route)) {
                place_list.add(fr_route);
            }
            if (!TextUtils.isEmpty(to_route)) {
                place_list.add(to_route);
            }
            via_address_value = GetAddress();

            if (!TextUtils.isEmpty(via_address_value)) {
                String[] strings = via_address_value.split("#");
                for (int i = 0; i < strings.length; i++) {
                    place_list.add(strings[i]);
                }
            }
            if (place_list.size() > 0) {
                openEventOnMap(place_list);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.enter_Location), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public String GetAddress() {
        String finalValueOne = "";
        if (moduleOneLL.getChildCount() > 0) {
            for (int i = 0; i < moduleOneLL.getChildCount(); i++) {
                EditText edit1 = (EditText) moduleOneLL.getChildAt(i).findViewById(R.id.edit_o);
                if (edit1.getVisibility() == View.VISIBLE) {
                    String s1 = edit1.getText().toString().trim();
                    finalValueOne += s1 + "#";
                }
            }
        } else {
            finalValueOne = "";
        }
        return finalValueOne;
    }

    private void openEventOnMap(List<String> place_list) {
        try {
            String uri = "";
            int i = 0;
            for (i = 0; i < place_list.size(); i++) {
                if (TextUtils.isEmpty(uri)) {
                    uri = "http://maps.google.com/maps?saddr=" + place_list.get(i);
                } else {
                    if (!uri.contains("&daddr")) {
                        uri += "&daddr=" + place_list.get(i);

                    } else {
                        uri += "+to:" + place_list.get(i);
                    }
                }
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("Shakti solar.");
        }
    }

    public void UpdateQuotation() {
        LoginBean loginBean = new LoginBean();
        QuotationBean QuoteBean = new QuotationBean(rfq_docno,
                remark,
                rate,
                vehicle_rc,
                vehicle_make,
                chk_puc,
                chk_insurence,
                chk_licence,
                "X",
                "Quoted",
                loginBean.getUseid(),
                loginBean.getUsertype(),
                "");
        db.updateQuotationData(DatabaseHelper.KEY_INSERT_QUOTATION, QuoteBean);
    }

    public void SyncDataInBackground() {
        UpdateQuotation();
        if (CustomUtility.isInternetOn()) {
            Intent i = new Intent(NewQuotation.this, SyncDataService.class);
            i.putExtra("sync_data", "quote_data");
            i.putExtra("rfq_docno", rfq_docno);
            startService(i);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Quotation_submitted), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Quotation_offline), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        moduleOneLL = (LinearLayout) findViewById(R.id.layout_one);
        et_vehicle_type = (EditText) findViewById(R.id.vehicle_type_et);
        et_vehicle_weight = (EditText) findViewById(R.id.vehicle_weight_et);
        et_transit_time = (EditText) findViewById(R.id.transit_time_et);
        et_delivery_place = (EditText) findViewById(R.id.delivery_place_et);
        et_rfq_date = (EditText) findViewById(R.id.rfq_date);
        et_rfq_time = (EditText) findViewById(R.id.rfq_time);
        et_expiry_date = (EditText) findViewById(R.id.expiry_date);
        et_expiry_time = (EditText) findViewById(R.id.expiry_time);
        et_route_from = (EditText) findViewById(R.id.from_location_et);
        et_route_to = (EditText) findViewById(R.id.to_location_et);
        et_distance = (EditText) findViewById(R.id.distance_et);
        et_mode = (EditText) findViewById(R.id.mode_et);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        img_add = (ImageView) findViewById(R.id.img_add);
        tv_save = (TextView) findViewById(R.id.save);
        tv_get_route = (TextView) findViewById(R.id.get_route);
        et_dala = (EditText) findViewById(R.id.dala_et);
        et_height = (EditText) findViewById(R.id.height_et);
        et_pernr = (EditText) findViewById(R.id.pernr_et);
        et_pernrnm = (EditText) findViewById(R.id.pernrnm_et);
        et_vehicle_rc = (EditText) findViewById(R.id.vehicle_rc);
        et_remark = (EditText) findViewById(R.id.remark);
        et_vehicle_make = (EditText) findViewById(R.id.vehicle_make);
        et_rate = (EditText) findViewById(R.id.rate);
        c_puc = (CheckBox) findViewById(R.id.puc_chk);
        c_insurence = (CheckBox) findViewById(R.id.insurance_chk);
        c_licence = (CheckBox) findViewById(R.id.licence_chk);
    }

    public void setData() {
        RfqBean rfqBean = new RfqBean();
        rfqBean = db.getRfqInformation(rfq_docno);
        et_route_from.setText(rfqBean.getFr_location());
        et_route_to.setText(rfqBean.getTo_location());
        et_distance.setText(rfqBean.getDistance());
        et_mode.setText(rfqBean.getTr_mode());
        et_vehicle_type.setText(rfqBean.getVehicle_type());
        et_vehicle_weight.setText(rfqBean.getVehicle_weight());
        et_rfq_date.setText(rfqBean.getRfq_date());
        et_rfq_time.setText(rfqBean.getRfq_time());
        et_dala.setText(rfqBean.getDala());
        et_height.setText(rfqBean.getHeight());
        et_pernr.setText(rfqBean.getRes_pernr());
        et_pernrnm.setText(rfqBean.getPernr_name());

        if (!TextUtils.isEmpty(rfqBean.getDelivery_place()) || (!rfqBean.getDelivery_place().equals("null"))) {
            et_delivery_place.setText(rfqBean.getDelivery_place());
        }
        if (!TextUtils.isEmpty(rfqBean.getTransit_time()) || (!rfqBean.getTransit_time().equals("null"))) {
            et_transit_time.setText(rfqBean.getTransit_time());
        }
        et_expiry_date.setText(rfqBean.getExpiry_date());
        et_expiry_time.setText(rfqBean.getExpiry_time());
        if (!TextUtils.isEmpty(rfqBean.getVia_address())) {
            if (rfqBean.getVia_address().length() != 0 && !rfqBean.getVia_address().equals("null")) {
                String arr[] = rfqBean.getVia_address().split("#");
                moduleOneLL.removeAllViews();
                for (int i = 0; i < arr.length; i++) {
                    View child_grid = LayoutInflater.from(context).inflate(R.layout.view_for_signature, null);
                    LinearLayout layout_f = (LinearLayout) child_grid.findViewById(R.id.first_layout);
                    EditText edit = (EditText) layout_f.findViewById(R.id.edit_o);
                    try {
                        edit.setText(arr[i]);
                        edit.setFocusable(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    moduleOneLL.setVisibility(View.VISIBLE);
                    moduleOneLL.addView(child_grid);
                }
            }
        }

        if (db.isRecordExist(DatabaseHelper.TABLE_QUOTATION, DatabaseHelper.KEY_RFQ_DOC, rfqBean.getRfq_doc())) {
            QuotationBean quoteBean;
            quoteBean = db.getQuotationInformation(rfqBean.getRfq_doc());
            et_remark.setText(quoteBean.getRemark());
            et_vehicle_make.setText(quoteBean.getVehicle_make());
            et_vehicle_rc.setText(quoteBean.getVehicle_rc());
            et_rate.setText(quoteBean.getRate());
            if (quoteBean.getPuc().equals("yes")) {
                c_puc.setChecked(true);
            }
            if (quoteBean.getInsurance().equals("yes")) {
                c_insurence.setChecked(true);
            }
            if (quoteBean.getLicence().equals("yes")) {
                c_licence.setChecked(true);
            }
            tv_save.setVisibility(View.GONE);
        }

        et_route_from.setFocusable(false);
        et_route_to.setFocusable(false);
        et_distance.setFocusable(false);
        et_mode.setFocusable(false);
        et_vehicle_type.setFocusable(false);
        et_vehicle_weight.setFocusable(false);
        et_rfq_date.setFocusable(false);
        et_rfq_time.setFocusable(false);
        et_delivery_place.setFocusable(false);
        et_transit_time.setFocusable(false);
        et_expiry_date.setFocusable(false);
        et_expiry_time.setFocusable(false);
        et_dala.setFocusable(false);
        et_height.setFocusable(false);
        et_pernr.setFocusable(false);
        et_pernrnm.setFocusable(false);
    }

    public void getData() {
        fr_route = et_route_from.getText().toString();
        vehicle_rc = et_vehicle_rc.getText().toString();
        vehicle_make = et_vehicle_make.getText().toString();
        rate = et_rate.getText().toString();
        remark = et_remark.getText().toString();

        if (c_puc.isChecked()) {
            chk_puc = "yes";
        } else {
            chk_puc = "yes";
        }

        if (c_puc.isChecked()) {
            chk_licence = "yes";
        } else {
            chk_licence = "yes";
        }

        if (c_puc.isChecked()) {
            chk_insurence = "yes";
        } else {
            chk_insurence = "yes";
        }
    }
}

