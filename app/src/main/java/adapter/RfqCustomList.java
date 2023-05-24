package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.administrator.shaktiTransportApp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bean.LoginBean;
import bean.QuotationBean;
import bean.RfqBean;
import database.DatabaseHelper;

/**
 * Created by shakti on 11/8/2016.
 */
public class RfqCustomList extends ArrayAdapter<RfqBean> {

    Context context;
    DatabaseHelper db;
    RfqBean rfqBean;
    ArrayList<RfqBean> arrayList;
    private List<RfqBean> SearchesList = null;

    public RfqCustomList(Context context, ArrayList<RfqBean> arrayList) {
        super(context, R.layout.rfq_item_view, arrayList);
        this.context = context;
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
        RfqBean rfqBean = getItem(position);


        if (view == null) {

            view = LayoutInflater.from(getContext()).inflate(R.layout.rfq_item_view, parent, false);
        }


        TextView txtrfqid = (TextView) view.findViewById(R.id.rfq_id_value);
        TextView textfrom_loc = (TextView) view.findViewById(R.id.from_loc_value);
        TextView textto_loc = (TextView) view.findViewById(R.id.to_loc_value);
        TextView textstatus = (TextView) view.findViewById(R.id.rfq_status_value);
        TextView textsapno = (TextView) view.findViewById(R.id.sap_id_value);
        TextView textname = (TextView) view.findViewById(R.id.name_value);

        TextView textdetails = (TextView) view.findViewById(R.id.details);
        TextView textconfirm = (TextView) view.findViewById(R.id.confirm);

        TextView textveh_no = (TextView) view.findViewById(R.id.veh_no);
        TextView textveh_nm = (TextView) view.findViewById(R.id.veh_nm);

        textdetails.setVisibility(View.GONE);
        textconfirm.setVisibility(View.GONE);
        textveh_no.setVisibility(View.GONE);
        textveh_nm.setVisibility(View.GONE);


        LoginBean loginBean = new LoginBean();
        String usertype = loginBean.getUsertype().trim();


        if ("Vendor".equals(usertype)) {

            if (db.isRecordExist(DatabaseHelper.TABLE_QUOTATION, DatabaseHelper.KEY_RFQ_DOC, rfqBean.getRfq_doc())) {

                QuotationBean quotationBean;
                quotationBean = db.getQuotationInformation(rfqBean.getRfq_doc());

                textstatus.setText(quotationBean.getQuote_status());
                textstatus.setTextColor(Color.parseColor("#006400"));   //dark green colore
            } else {
                textstatus.setText(String.valueOf(rfqBean.getRfq_status()));
                textstatus.setTextColor(Color.RED);
            }

        } else {
            textstatus.setText(String.valueOf(rfqBean.getRfq_status()));
        }


        txtrfqid.setText(String.valueOf(rfqBean.getRfq_doc()));
        textfrom_loc.setText(String.valueOf(rfqBean.getFr_location()));
        textto_loc.setText(String.valueOf(rfqBean.getTo_location()));
        textsapno.setText(String.valueOf(rfqBean.getRes_pernr()));
        textname.setText(String.valueOf(rfqBean.getPernr_name()));

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