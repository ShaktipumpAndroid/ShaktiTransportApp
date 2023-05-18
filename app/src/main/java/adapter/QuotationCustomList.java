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
import database.DatabaseHelper;

/**
 * Created by shakti on 11/8/2016.
 */
public class QuotationCustomList extends ArrayAdapter<QuotationBean> {

    Context context;
    DatabaseHelper db;
    QuotationBean rfqBean;
    ArrayList<QuotationBean> arrayList;
    private List<QuotationBean> SearchesList = null;

    public QuotationCustomList(Context context, ArrayList<QuotationBean> arrayList) {
        super(context, R.layout.rfq_item_view, arrayList);
        this.context = context;
        this.arrayList = arrayList;

        this.SearchesList = new ArrayList<QuotationBean>();
        this.SearchesList.addAll(arrayList);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public QuotationBean getItem(int position) {
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
        QuotationBean quotationBean1 = getItem(position);


        if (view == null) {

            view = LayoutInflater.from(getContext()).inflate(R.layout.rfq_item_view, parent, false);
        }


        TextView txtrfqid = (TextView) view.findViewById(R.id.rfq_id_value);
        TextView textfrom_loc = (TextView) view.findViewById(R.id.from_loc_value);
        TextView textto_loc = (TextView) view.findViewById(R.id.to_loc_value);
        TextView textstatus = (TextView) view.findViewById(R.id.rfq_status_value);


        LoginBean loginBean = new LoginBean();
        String usertype = loginBean.getUsertype();
        usertype = usertype.trim();


        if ("Vendor".equals(usertype)) {

            if (db.isRecordExist(DatabaseHelper.TABLE_QUOTATION, DatabaseHelper.KEY_RFQ_DOC, rfqBean.getRfq_doc())) {

                QuotationBean quotationBean = new QuotationBean();
                quotationBean = db.getQuotationInformation(rfqBean.getRfq_doc());

                textstatus.setText(quotationBean.getQuote_status());
                textstatus.setTextColor(Color.parseColor("#006400"));   //dark green colore
            } else {
                textstatus.setText(String.valueOf(rfqBean.getQuote_status()));
                textstatus.setTextColor(Color.RED);
            }

        } else {
            textstatus.setText(String.valueOf(rfqBean.getQuote_status()));
        }


        txtrfqid.setText(String.valueOf(rfqBean.getRfq_doc()));
        textfrom_loc.setText(String.valueOf(rfqBean.getVehicle_rc()));
        textto_loc.setText(String.valueOf(rfqBean.getQuote_status()));

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(SearchesList);
        } else {
            for (QuotationBean cs : SearchesList) {
                if (cs.getRfq_doc().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getQuote_status().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getVehicle_rc().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }
}