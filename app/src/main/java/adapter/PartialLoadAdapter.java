package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.administrator.shaktiTransportApp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bean.PartialLoadResponse;

public class PartialLoadAdapter extends ArrayAdapter<PartialLoadResponse> {

    Context context;
    public ArrayList<PartialLoadResponse> arrayList;
    private List<PartialLoadResponse> SearchesList = null;

    public PartialLoadAdapter(Context context, ArrayList<PartialLoadResponse> arrayList) {
        super(context, R.layout.partial_load_item_view, arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.SearchesList = new ArrayList<>();
        this.SearchesList.addAll(arrayList);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public PartialLoadResponse getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        PartialLoadResponse partialLoadResponse = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.partial_load_item_view, parent, false);
        }
        TextView tvBillNo = (TextView) view.findViewById(R.id.tvBillNo);
        TextView tvBillDate = (TextView) view.findViewById(R.id.tvBillDate);
        TextView tvLrNo = (TextView) view.findViewById(R.id.tvLrNo);
        TextView tvCustomerCode = (TextView) view.findViewById(R.id.tvCustomerCode);
        TextView tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);

        tvBillNo.setText(String.valueOf(partialLoadResponse.getVbeln()));
        tvBillDate.setText(String.valueOf(partialLoadResponse.getFkdat()))  ;
        tvLrNo.setText(String.valueOf(partialLoadResponse.getZlrno()));
        tvCustomerCode.setText(String.valueOf(partialLoadResponse.getKunag()));
        tvCustomerName.setText(String.valueOf(partialLoadResponse.getCustName()));
        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(SearchesList);
        } else {
            for (PartialLoadResponse cs : SearchesList) {
                if (cs.getCustName().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getVbeln().toLowerCase(Locale.getDefault()).contains(charText) || cs.getZlrno().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }
}
