package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.administrator.shaktiTransportApp.R;
import java.util.Collections;
import java.util.List;

import activity.PodBean.AssignedDeliveryDetailResponse;
import activity.PodBean.AssignedDeliveryResponse;
import utility.ClickListiner;

public class AssignedDeliveryAdapter extends RecyclerView.Adapter<AssignedDeliveryAdapter.ViewHolder> {

    List<AssignedDeliveryDetailResponse> list = Collections.emptyList();

    Context mContext;
    ClickListiner listiner;

    public AssignedDeliveryAdapter(List<AssignedDeliveryDetailResponse> list, Context context, ClickListiner listiner) {
        this.list = list;
        this.mContext = context;
        this.listiner = listiner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.assiged_delivery_list_item, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rfqNo, vehNo, confirmationDate, confirmationTime, transporterName, transporterCode, fromLocValue, toLocValue;

        public ViewHolder(View v) {
            super(v);
            rfqNo = (TextView) v.findViewById(R.id.rfq_no);
            vehNo = (TextView) v.findViewById(R.id.veh_no);
            confirmationDate = (TextView) v.findViewById(R.id.confirmation_date);
            confirmationTime = (TextView) v.findViewById(R.id.confirmation_time);
            transporterName = (TextView) v.findViewById(R.id.transporter_name);
            transporterCode = (TextView) v.findViewById(R.id.transporter_code);
            fromLocValue = (TextView) v.findViewById(R.id.from_loc_value);
            toLocValue = (TextView) v.findViewById(R.id.to_loc_value);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.rfqNo.setText(list.get(position).getRfqDoc());
        holder.vehNo.setText(list.get(position).getVehicleRc());
        holder.confirmationDate.setText(list.get(position).getConfDate());
        holder.confirmationTime.setText(list.get(position).getConfTime());
        holder.transporterCode.setText(list.get(position).getTransportCode());
        holder.transporterName.setText(list.get(position).getTransporterName());
        holder.fromLocValue.setText(list.get(position).getFrLocation());
        holder.toLocValue.setText(list.get(position).getToLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listiner.click(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
