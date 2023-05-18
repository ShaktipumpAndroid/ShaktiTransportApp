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

import bean.RouteBean;
import database.DatabaseHelper;

/**
 * Created by shakti on 11/8/2016.
 */
public class RouteCustomList extends ArrayAdapter<RouteBean> {

    Context context;
    DatabaseHelper db;


    ArrayList<RouteBean> arrayList;
    private List<RouteBean> SearchesList = null;

    public RouteCustomList(Context context, ArrayList<RouteBean> arrayList) {
        super(context, R.layout.activity_route_list_item_view, arrayList);
        this.context = context;
        this.arrayList = arrayList;

        this.SearchesList = new ArrayList<RouteBean>();
        this.SearchesList.addAll(arrayList);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public RouteBean getItem(int position) {
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
        RouteBean routeBean = getItem(position);


        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_route_list_item_view, parent, false);
        }


        TextView fr_loc = (TextView) view.findViewById(R.id.fr_loc_value);
        TextView to_loc = (TextView) view.findViewById(R.id.to_loc_value);
        TextView distance = (TextView) view.findViewById(R.id.distance_value);
        TextView mode = (TextView) view.findViewById(R.id.mode_value);


        String fr_location = String.valueOf(routeBean.getFr_tehsil()) + " , " +
                String.valueOf(routeBean.getFr_district()) + " , " +
                String.valueOf(routeBean.getFr_state());

//        fr_loc.setText(String.valueOf(routeBean.getFr_tehsil()));
        fr_loc.setText(fr_location);


        String to_location = String.valueOf(routeBean.getTo_tehsil()) + " , " +
                String.valueOf(routeBean.getTo_district()) + " , " +
                String.valueOf(routeBean.getTo_state());

        to_loc.setText(to_location);
//        to_loc.setText(String.valueOf(routeBean.getTo_tehsil()));
        distance.setText(String.valueOf(routeBean.getDistance()));
        mode.setText(String.valueOf(routeBean.getTr_mode()));


        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(SearchesList);
        } else {
            for (RouteBean cs : SearchesList) {
                if (cs.getFr_tehsil().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getTo_tehsil().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }
}