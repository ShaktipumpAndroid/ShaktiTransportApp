package activity;

/**
 * Created by shakti on 10/3/2016.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.administrator.shaktiTransportApp.R;

import activity.Pod.ActivityPODSearchInfo;
import activity.languagechange.LocaleHelper;
import bean.LoginBean;
import database.DatabaseHelper;


public class HomeFragment extends Fragment {

    Context context;
    TextView textview_cnfrm_vehicle;
    TextView textview_rfq, textview_app_vehicle, textView_pod;
    String usertype;
    RelativeLayout approve_vehicle, cnfrm_vehicle, rfq, rlAssignedDeliveryList, rlAgreement;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        DatabaseHelper db = new DatabaseHelper(getActivity());


        textview_rfq = (TextView) rootView.findViewById(R.id.textview_rfq);
        textview_app_vehicle = (TextView) rootView.findViewById(R.id.textview_app_vehicle);
        textview_cnfrm_vehicle = (TextView) rootView.findViewById(R.id.textview_cnfrm_vehicle);
        textView_pod = (TextView) rootView.findViewById(R.id.text_pod);
        rfq = (RelativeLayout) rootView.findViewById(R.id.rfq);
        rlAssignedDeliveryList = (RelativeLayout) rootView.findViewById(R.id.rlAssignedDeliveryList);
        rlAgreement = rootView.findViewById(R.id.rlAgreement);
        approve_vehicle = (RelativeLayout) rootView.findViewById(R.id.approve_vehicle);
        cnfrm_vehicle = (RelativeLayout) rootView.findViewById(R.id.cnfrm_vehicle);

        LoginBean loginBean = new LoginBean();

        usertype = loginBean.getUsertype().trim();

        rlAssignedDeliveryList.setVisibility(View.GONE);
        if ("Driver".equals(usertype)) {
            textview_rfq.setText(getResources().getString(R.string.Customer_Data));
            approve_vehicle.setVisibility(View.GONE);
            cnfrm_vehicle.setVisibility(View.GONE);
        } else if ("Vendor".equals(usertype)) {
            textview_rfq.setText(getResources().getString(R.string.RFQ_Plan));
            approve_vehicle.setVisibility(View.VISIBLE);
            cnfrm_vehicle.setVisibility(View.VISIBLE);
            rlAgreement.setVisibility(View.VISIBLE);
            textView_pod.setText(getResources().getString(R.string.pod_data));
            rlAssignedDeliveryList.setVisibility(View.VISIBLE);
        } else if("Transport Officer".equals(usertype)){
            rlAssignedDeliveryList.setVisibility(View.VISIBLE);
            rlAgreement.setVisibility(View.GONE);
            textview_rfq.setText(getResources().getString(R.string.Create_Request));
            approve_vehicle.setVisibility(View.GONE);
            cnfrm_vehicle.setVisibility(View.GONE);
        } else {
            textview_rfq.setText(getResources().getString(R.string.Create_Request));
            approve_vehicle.setVisibility(View.GONE);
            cnfrm_vehicle.setVisibility(View.GONE);
        }


        textview_rfq.setOnClickListener(view -> {
            if ("Vendor".equals(usertype)) {
                Intent intent = new Intent(context, AssignedRfqVendor.class);
                startActivity(intent);
            } else if ("Driver".equals(usertype)) {
                Intent intent = new Intent(context, CustomerDetails.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(context, AssignedRfq.class);
                startActivity(intent);
            }
        });

        rlAssignedDeliveryList.setOnClickListener(view -> {

            if ("Vendor".equals(usertype)) {
                Intent mIntent = new Intent(context, ActivityPODSearchInfo.class);
                startActivity(mIntent);
            }else {
                Intent intent = new Intent(context, OfficerAssignedDeliveryListActivity.class);
                startActivity(intent);
            }
        });

        rlAgreement.setOnClickListener(view -> {
            Intent intent = new Intent(context, PartialLoadListActivity.class);
            startActivity(intent);
        });

        textview_app_vehicle.setOnClickListener(view -> {
            if ("Vendor".equals(usertype)) {
                Intent intent = new Intent(context, VehicleApprovedActivity.class);
                intent.putExtra("from", "A");
                startActivity(intent);
            }
        });

        textview_cnfrm_vehicle.setOnClickListener(view -> {
            if ("Vendor".equals(usertype)) {
                Intent intent = new Intent(context, VehicleApprovedActivity.class);
                intent.putExtra("from", "C");
                startActivity(intent);
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}