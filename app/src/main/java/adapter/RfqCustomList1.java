package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administrator.shaktiTransportApp.BuildConfig;
import com.administrator.shaktiTransportApp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.ConfrmVehical;
import activity.NewQuotation;
import bean.LoginBean;
import bean.RfqBean;
import database.DatabaseHelper;

/**
 * Created by shakti on 11/8/2016.
 */
public class RfqCustomList1 extends ArrayAdapter<RfqBean> {

    Context context;
    DatabaseHelper db;
    RfqBean rfqBean;
    ArrayList<RfqBean> arrayList;
    String from;
    private List<RfqBean> SearchesList = null;

    public RfqCustomList1(Context context, String from, ArrayList<RfqBean> arrayList) {
        super(context, R.layout.rfq_item_view, arrayList);
        this.context = context;
        this.from = from;
        this.arrayList = arrayList;
        this.SearchesList = new ArrayList<RfqBean>();
        this.SearchesList.addAll(arrayList);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public RfqBean getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {

        db = new DatabaseHelper(context);
        // Get the data item for this position
        final RfqBean rfqBean = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.rfq_item_view, parent, false);
        }

        TextView txtrfqid = (TextView) view.findViewById(R.id.rfq_id_value);
        TextView textfrom_loc = (TextView) view.findViewById(R.id.from_loc_value);
        TextView textto_loc = (TextView) view.findViewById(R.id.to_loc_value);
        TextView textstatus = (TextView) view.findViewById(R.id.rfq_status_value);
        TextView textsapno = (TextView) view.findViewById(R.id.sap_id_value);
        TextView textname = (TextView) view.findViewById(R.id.name_value);
        TextView textfnalrte = (TextView) view.findViewById(R.id.finl_rate_value);
        TextView textdetails = (TextView) view.findViewById(R.id.details);
        TextView textconfirm = (TextView) view.findViewById(R.id.confirm);
        TextView textveh_no = (TextView) view.findViewById(R.id.veh_no);
        TextView textveh_nm = (TextView) view.findViewById(R.id.veh_nm);

        LinearLayout lin1 = (LinearLayout) view.findViewById(R.id.lin1);
        LinearLayout lin2 = (LinearLayout) view.findViewById(R.id.lin2);

        lin1.setVisibility(View.GONE);
        lin2.setVisibility(View.GONE);

        LoginBean loginBean = new LoginBean();
        String usertype = loginBean.getUsertype().trim();

        if (from.equalsIgnoreCase("A")) {
            textstatus.setText("Approva");
            textveh_nm.setVisibility(View.GONE);
            textveh_no.setVisibility(View.GONE);
            textdetails.setVisibility(View.VISIBLE);
            textconfirm.setVisibility(View.VISIBLE);
        } else {
            textstatus.setText("Confirmed");
            textveh_nm.setVisibility(View.VISIBLE);
            textveh_no.setVisibility(View.VISIBLE);
            textveh_no.setText(rfqBean.getVehicle_rc());
            textdetails.setVisibility(View.GONE);
            textconfirm.setVisibility(View.GONE);
        }
        textstatus.setTextColor(Color.GREEN);

        txtrfqid.setText(String.valueOf(rfqBean.getRfq_doc()));
        textfrom_loc.setText(String.valueOf(rfqBean.getFr_location()));
        textto_loc.setText(String.valueOf(rfqBean.getTo_location()));
        textfnalrte.setText(String.valueOf(rfqBean.getFinal_rate()));

        textdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caseid_text = rfqBean.getRfq_doc();
                Intent i = new Intent(context, NewQuotation.class);
                Bundle extras = new Bundle();
                extras.putString("rfq_docno", caseid_text);
                extras.putString("flag_create_rfq", "Y");
                i.putExtras(extras);
                context.startActivity(i);
            }
        });

        textconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String caseid_text = rfqBean.getRfq_doc();
                String amount_text = rfqBean.getFinal_rate();

                Intent i = new Intent(context, ConfrmVehical.class);
                Bundle extras = new Bundle();
                extras.putString("rfq_docno", caseid_text);
                extras.putString("amount", amount_text);
                i.putExtras(extras);
                context.startActivity(i);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(SearchesList);
        } else {
            for (RfqBean cs : SearchesList) {
                if (cs.getRfq_doc().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getFr_location().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getTo_location().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }
}