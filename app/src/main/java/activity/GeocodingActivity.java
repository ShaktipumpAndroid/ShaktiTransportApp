package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.administrator.shaktiTransportApp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GeocodingActivity extends Activity {
    ImageView img_add;
    ImageView img_delete;
    Button addressButton1;
    EditText from_address;
    EditText to_address;
    String latitute = null;
    String address = "";
    String address1 = "";
    String address_value = "";
    Context context;
    private LinearLayout moduleOneLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoding);

        context = this;

        moduleOneLL = (LinearLayout) findViewById(R.id.layout_one);
        addressButton1 = (Button) findViewById(R.id.addressButton1);
        img_add = (ImageView) findViewById(R.id.img_add);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        from_address = (EditText) findViewById(R.id.from_addressET);
        to_address = (EditText) findViewById(R.id.to_addressET);

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

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (moduleOneLL.getChildCount() > 0) {
                    moduleOneLL.removeViewAt(moduleOneLL.getChildCount() - 1);
                }

            }
        });

        addressButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                address = from_address.getText().toString();
                address1 = to_address.getText().toString();

                List<String> place_list = new ArrayList<String>();

                if (!TextUtils.isEmpty(address)) {
                    place_list.add(address);
                }

                address_value = GetAddress();

                if (!TextUtils.isEmpty(address_value)) {
                    String[] strings = address_value.split(",");
                    for (int i = 0; i < strings.length; i++) {

                        place_list.add(strings[i]);
                    }


                }
                if (!TextUtils.isEmpty(address1)) {
                    place_list.add(address1);
                }


////              StringTokenizer tokens = new StringTokenizer(address, " / ");
//                StringTokenizer tokens = new StringTokenizer(latitute, " / ");
//                String first = tokens.nextToken();
//                String second = tokens.nextToken();
//
//                StringTokenizer tokens1 = new StringTokenizer(latitute1, " / ");
////                StringTokenizer tokens1 = new StringTokenizer(address1, " / ");
//                String third = tokens1.nextToken();// this will contain "Fruit"
//                String fourth = tokens1.nextToken();// this will contain " they taste good"
//
//                StringTokenizer tokens2 = new StringTokenizer(latitute2, " / ");
////                StringTokenizer tokens2 = new StringTokenizer(address2, " / ");
//                String fifth = tokens2.nextToken();// this will contain "Fruit"
//                String sixth = tokens2.nextToken();// this will contain "they taste good"


                openEventOnMap(place_list);

            }
        });

    }


    public String GetAddress() {
        String finalValueOne = "";
        if (moduleOneLL.getChildCount() > 0) {
            for (int i = 0; i < moduleOneLL.getChildCount(); i++) {
                EditText edit1 = (EditText) moduleOneLL.getChildAt(i).findViewById(R.id.edit_o);
                if (edit1.getVisibility() == View.VISIBLE) {
                    String s1 = edit1.getText().toString().trim();
                    finalValueOne += s1 + ",";
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

//            lat_long = (String) ((location.getLatitude() * 1E6)+"---"+ (double) (location.getLongitude() * 1E6));
            lat_long = (String) (location.getLatitude() + " / " + location.getLongitude());
        } catch (Exception e) {
        }
        return lat_long;
    }
}

