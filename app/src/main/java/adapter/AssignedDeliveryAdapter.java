package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        LinearLayout layout, partialLayout;
        TextView rfqNo, vehNo, confirmationDate, confirmationTime, transporterName, transporterCode, fromLocValue, toLocValue;
        TextView bill,lr,book,address,contact,transportName;

        public ViewHolder(View v) {
            super(v);
            rfqNo =  v.findViewById(R.id.rfq_no);
            vehNo =  v.findViewById(R.id.veh_no);
            confirmationDate =  v.findViewById(R.id.confirmation_date);
            confirmationTime =  v.findViewById(R.id.confirmation_time);
            transporterName =  v.findViewById(R.id.transporter_name);
            transporterCode =  v.findViewById(R.id.transporter_code);
            fromLocValue =  v.findViewById(R.id.from_loc_value);
            toLocValue =  v.findViewById(R.id.to_loc_value);

            layout = v.findViewById(R.id.llMainLayout);
            partialLayout = v.findViewById(R.id.partialLayout);

            bill = v.findViewById(R.id.bill_no_value);
            lr = v.findViewById(R.id.Lr_no_value);
            book = v.findViewById(R.id.booking_value);
            address = v.findViewById(R.id.address_value);
            contact = v.findViewById(R.id.contact_value);
            transportName = v.findViewById(R.id.transport_name_value);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(!list.get(position).getRfqDoc().equalsIgnoreCase("")){

            holder.partialLayout.setVisibility(View.GONE);
            holder.layout.setVisibility(View.VISIBLE);

            holder.rfqNo.setText(list.get(position).getRfqDoc());
            holder.vehNo.setText(list.get(position).getVehicleRc());
            holder.confirmationDate.setText(list.get(position).getConfDate());
            holder.confirmationTime.setText(list.get(position).getConfTime());
            holder.transporterCode.setText(list.get(position).getTransportCode());
            holder.transporterName.setText(list.get(position).getTransporterName());
            holder.fromLocValue.setText(list.get(position).getFrLocation());
            holder.toLocValue.setText(list.get(position).getToLocation());

            holder.itemView.setOnClickListener(v -> listiner.click(position));


        }
        else {
            holder.partialLayout.setVisibility(View.VISIBLE);
            holder.layout.setVisibility(View.GONE);

            holder.contact.setText(list.get(position).getzMobile());
            holder.transportName.setText(list.get(position).getTransporterName());
            holder.bill.setText(list.get(position).getBillNo());
            holder.book.setText(list.get(position).getzBookDate());
            holder.lr.setText(list.get(position).getzLRno());
            holder.address.setText(list.get(position).getViaAddress());

            holder.itemView.setOnClickListener(v -> listiner.click(position));
        }


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
