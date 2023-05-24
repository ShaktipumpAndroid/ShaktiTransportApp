package activity;

import android.app.Activity;
import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.administrator.shaktiTransportApp.BuildConfig;
import com.administrator.shaktiTransportApp.R;

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

public class NewRfq extends AppCompatActivity {

    public static Bitmap bitScroll;
    String flag_display = "";

    ImageView img_add;
    ImageView img_delete;

    EditText et_vehicle_type,
            et_vehicle_weight,
            et_transit_time,
            et_tolerance_km,
            et_delivery_place,
            et_rfq_date,
            et_rfq_time,
            et_expiry_date,
            et_expiry_time,
            et_route_from,
            et_route_to,
            et_distance,
            et_dala,
            et_height,
            et_pernr,
            et_pernrnm,
            et_mode;
    String flag_create_rfq = null;
    ScrollView scrollView;
    TextView tv_save;
    TextView tv_get_route;
    Context context;
    String rfq_docno,
            fr_route,
            to_route,
            fr_latlong,
            to_latlong,
            via_address_latlong,
            via_address_value,
            distance,
            mode,
            vehicle_type,
            vehicle_weight,
            final_rate,
            rfq_date,
            rfq_time,
            delivery_place,
            transit_time,
            expiry_date,
            dala,
            height,
            pernr, pernr_name,
            expiry_time, sync;
    DatabaseHelper db;
    private Toolbar mToolbar;
    private TimePicker timePicker1;
    private Calendar calendar;
    private String format = "";
    private int value;
    private LinearLayout moduleOneLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfq);

        Bundle bundle = getIntent().getExtras();
        rfq_docno = bundle.getString("rfq_docno");
        flag_create_rfq = bundle.getString("flag_create_rfq");


        context = this;
        db = new DatabaseHelper(context);


        LoginBean lb = new LoginBean();

        pernr = lb.getUseid();
        pernr_name = lb.getUsername();
//Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Request For Quotation");


        getLayout();


        if ("Y".equals(flag_create_rfq)) {
            setData();
        } else {
            et_pernr.setText(pernr);
            et_pernrnm.setText(pernr_name);
        }

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData();

                if (!TextUtils.isEmpty(fr_route) &&
                        !TextUtils.isEmpty(to_route) &&
                        !TextUtils.isEmpty(mode) &&
                        !TextUtils.isEmpty(vehicle_type) &&
                        !TextUtils.isEmpty(vehicle_weight) &&
                        !TextUtils.isEmpty(rfq_date) &&
                        !TextUtils.isEmpty(rfq_time) &&
                        !TextUtils.isEmpty(expiry_date) &&
                        !TextUtils.isEmpty(expiry_time) &&
                        !TextUtils.isEmpty(distance) &&
                        !TextUtils.isEmpty(dala) &&
                        !TextUtils.isEmpty(height)) {
                    SyncRfqDataInBackground();
                    onBackPressed();

                } else {
                    Toast.makeText(context, "Please fill all the required fields before submit data", Toast.LENGTH_SHORT).show();
                }

            }
        });


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (moduleOneLL.getChildCount() > 0) {
                    moduleOneLL.removeViewAt(moduleOneLL.getChildCount() - 1);
                }

            }
        });


        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                View child_grid = LayoutInflater.from(context).inflate(R.layout.view_for_signature, null);
                LinearLayout layout_f = (LinearLayout) child_grid.findViewById(R.id.first_layout);
                EditText edit = (EditText) layout_f.findViewById(R.id.edit_o);
                moduleOneLL.setVisibility(View.VISIBLE);
                moduleOneLL.addView(child_grid);

            }
        });

        tv_get_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                fr_route = et_route_from.getText().toString();
                to_route = et_route_to.getText().toString();


                List<String> place_list = new ArrayList<String>();

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
                    Toast.makeText(getApplicationContext(), "Please enter From Location, To Location to view Route on Map", Toast.LENGTH_SHORT).show();
                }

            }
        });

        et_rfq_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(DatePickeActivity.getIntent(context, "", "", "", false), 301);

            }
        });


        et_rfq_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
                calendar = Calendar.getInstance();

                startActivityForResult(TimePickeActivity.getIntent(context, "", ""), 303);

            }
        });

        et_expiry_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
                calendar = Calendar.getInstance();
                startActivityForResult(TimePickeActivity.getIntent(context, "", ""), 304);
            }
        });

        et_expiry_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(DatePickeActivity.getIntent(context, "", "", "", false), 302);

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 301) {

                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                String finaldate2 = year + "-" + month + "-" + date;
                finaldate2 = CustomUtility.formateDate(finaldate2);
                et_rfq_date.setText(finaldate2);
            }

            if (requestCode == 302) {

                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                String finaldate3 = year + "-" + month + "-" + date;
                finaldate3 = CustomUtility.formateDate(finaldate3);
                et_expiry_date.setText(finaldate3);
            }

            if (requestCode == 303) {

                String hh = data.getStringExtra("hh");
                String mm = data.getStringExtra("mm");

                String fr = "";
                int int_hh = Integer.parseInt(hh);
                int int_mm = Integer.parseInt(mm);

                if (int_hh == 0) {
                    int_hh += 12;
                    fr = "AM";
                } else if (int_hh == 12) {
                    fr = "PM";
                } else if (int_hh > 12) {
                    int_hh -= 12;
                    fr = "PM";
                } else {
                    fr = "AM";
                }


                if (int_mm < 10) {
                    mm = "" + 0 + mm;
                }


                String str_hh = String.valueOf(int_hh);

                if (int_hh < 10) {
                    str_hh = "" + 0 + str_hh;
                }


                et_rfq_time.setText(str_hh + ":" + mm + ":" + fr);

            }

            if (requestCode == 304) {

                String hh = data.getStringExtra("hh");
                String mm = data.getStringExtra("mm");

                String fr = "";
                int int_hh = Integer.parseInt(hh);
                int int_mm = Integer.parseInt(mm);

                if (int_hh == 0) {
                    int_hh += 12;
                    fr = "AM";
                } else if (int_hh == 12) {
                    fr = "PM";
                } else if (int_hh > 12) {
                    int_hh -= 12;
                    fr = "PM";
                } else {
                    fr = "AM";
                }

                if (int_mm < 10) {
                    mm = "" + 0 + mm;
                }


                String str_hh = String.valueOf(int_hh);

                if (int_hh < 10) {
                    str_hh = "" + 0 + str_hh;
                }


                et_expiry_time.setText(str_hh + ":" + mm + ":" + fr);

            }

        }
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


    public String GetAddressLatlong() {
        String finalValueOne = "";
        if (moduleOneLL.getChildCount() > 0) {
            for (int i = 0; i < moduleOneLL.getChildCount(); i++) {
                EditText edit1 = (EditText) moduleOneLL.getChildAt(i).findViewById(R.id.edit_o);
                if (edit1.getVisibility() == View.VISIBLE) {
                    String s1 = edit1.getText().toString().trim();
                    finalValueOne += getLocationFromAddress(s1) + "#";
                }
            }
        } else {
            finalValueOne = "";
        }
        return finalValueOne;
    }


    //create bitmap from the ScrollView
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    public void saveBitmap(Bitmap bitmap) {


        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + rfq_docno + ".jpeg";
        File imagePath = new File(mPath);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, fos);
            fos.flush();
            fos.close();
            Toast.makeText(getApplicationContext(), "Screenshot Captured", Toast.LENGTH_LONG).show();
            Log.e("ImageSave", "Saveimage");
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
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

    public void UpdateRouteMaster() {
        LoginBean loginBean = new LoginBean();
        rfq_docno = createDocNo();
        RfqBean rfqBean = new RfqBean(rfq_docno,
                rfq_docno,
                pernr_name,
                fr_route,
                to_route,
                fr_latlong,
                to_latlong,
                via_address_value,
                via_address_latlong,
                mode,
                distance,
                vehicle_type,
                vehicle_weight,
                final_rate,
                delivery_place,
                transit_time,
                rfq_date,
                rfq_time,
                expiry_date,
                expiry_time,
                dala,
                height,
                sync,
                "X",
                "New",
                loginBean.getUseid(),
                loginBean.getUsertype(),
                "",
                "");
        db.updateRfqData(DatabaseHelper.KEY_INSERT_RFQ, rfqBean);
    }

    public void SyncRfqDataInBackground() {

        UpdateRouteMaster();

        if (CustomUtility.isInternetOn()) {

            Intent i = new Intent(NewRfq.this, SyncDataService.class);
            i.putExtra("sync_data", "rfq_data");
            i.putExtra("rfq_docno", rfq_docno);
            startService(i);
            Toast.makeText(getApplicationContext(), "Request for Quotation created Successful", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(), "No internet Connection.... Request for Quotation saved offline", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_screenshot, menu);
        return true;
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

            case R.id.action_screen_shot:
                bitScroll = getBitmapFromView(scrollView, scrollView.getChildAt(0).getHeight(), scrollView.getChildAt(0).getWidth());
                saveBitmap(bitScroll);
                /*Intent intent = new Intent(context,PreviewActivity.class);
                startActivity(intent);*/
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void getLayout() {

        scrollView = findViewById(R.id.scrollview);
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
        et_dala = (EditText) findViewById(R.id.dala_et);
        et_height = (EditText) findViewById(R.id.height_et);
        et_pernr = (EditText) findViewById(R.id.pernr_et);
        et_pernrnm = (EditText) findViewById(R.id.pernrnm_et);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        img_add = (ImageView) findViewById(R.id.img_add);
        tv_save = (TextView) findViewById(R.id.save);
        tv_get_route = (TextView) findViewById(R.id.get_route);


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

        if (TextUtils.isEmpty(rfqBean.getDelivery_place()) || rfqBean.getDelivery_place().equals("null")) {
            et_delivery_place.setText(rfqBean.getDelivery_place());
        } else {
            et_delivery_place.setText(rfqBean.getDelivery_place());
        }

        if (TextUtils.isEmpty(rfqBean.getTransit_time()) || rfqBean.getTransit_time().equals("null")) {
            et_transit_time.setText(rfqBean.getTransit_time());
        } else {
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


        img_add.setVisibility(View.GONE);
        img_delete.setVisibility(View.GONE);
        tv_save.setVisibility(View.GONE);
    }

    public void getData() {


        fr_route = et_route_from.getText().toString();
        to_route = et_route_to.getText().toString();
        fr_latlong = getLocationFromAddress(fr_route);
        to_latlong = getLocationFromAddress(to_route);

        via_address_value = GetAddress();
        via_address_latlong = GetAddressLatlong();

        distance = et_distance.getText().toString();
        mode = et_mode.getText().toString();
        vehicle_type = et_vehicle_type.getText().toString();
        vehicle_weight = et_vehicle_weight.getText().toString();
        final_rate = "";
        rfq_date = et_rfq_date.getText().toString();
        rfq_time = et_rfq_time.getText().toString();
        delivery_place = et_delivery_place.getText().toString();
        transit_time = et_transit_time.getText().toString();
        expiry_date = et_expiry_date.getText().toString();
        expiry_time = et_expiry_time.getText().toString();
        dala = et_dala.getText().toString();
        height = et_height.getText().toString();
        pernr = et_pernr.getText().toString();
        pernr_name = et_pernrnm.getText().toString();
    }


    public String getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address = null;
        String lat_long = null;

        try {
            try {
                address = coder.getFromLocationName(strAddress, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            lat_long = (String) (location.getLatitude() + "&" + location.getLongitude());
        } catch (Exception e) {
        }
        return lat_long;
    }


    public String createDocNo() {

        Calendar cal = Calendar.getInstance();
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        String day_str = null;
        String month_str = null;
        String hour_str = null;
        String second_str = null;
        String minute_str = null;


        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();

        String month = dateFormat.format(date);

        LoginBean loginBean = new LoginBean();

        String userid = loginBean.getUseid();
        String usertype = loginBean.getUsertype();

        if (day < 10) {
            day_str = "" + 0 + day;
        } else {
            day_str = "" + day;
        }


        if (minute < 10) {
            minute_str = "" + 0 + minute;
        } else {
            minute_str = "" + minute;
        }


        if (second < 10) {
            second_str = "" + 0 + second;
        } else {
            second_str = "" + second;
        }


        if (hour < 10) {
            hour_str = "" + 0 + hour;
        } else {
            hour_str = "" + hour;
        }


        if (month.length() == 1) {
            month_str = "" + 0 + month;
        } else {
            month_str = month;
        }

        String rfq_docno = "" + userid + day_str + month_str + year + hour_str + minute_str + second_str;
        return rfq_docno;
    }


}

