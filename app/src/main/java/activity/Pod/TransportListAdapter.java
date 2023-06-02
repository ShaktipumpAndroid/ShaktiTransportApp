package activity.Pod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.administrator.shaktiTransportApp.R;

import java.util.List;

import activity.PodBean.LrInvoiceResponse;
import webservice.WebURL;


public class TransportListAdapter extends RecyclerView.Adapter<TransportListAdapter.ViewHolder> {

    private Context mContext;
    private List<LrInvoiceResponse> mLrInvoiceResponse;
    private List<Boolean> mLrInvoiceSelectionCheck;

/*private List<FaultRecordResponse> mFaultRecordResponse;

    public TransportListAdapter(Context mContext, List<FaultRecordResponse> mFaultRecordResponse) {
        // this.galleryModelsList = galleryModelsList;
        this.mContext = mContext;
        this.mFaultRecordResponse = mFaultRecordResponse;

    }*/

    public TransportListAdapter(Context mContext, List<LrInvoiceResponse> mLrInvoiceResponse, List<Boolean> mLrInvoiceSelectionCheck) {
        this.mContext = mContext;
        this.mLrInvoiceResponse = mLrInvoiceResponse;
        this.mLrInvoiceSelectionCheck = mLrInvoiceSelectionCheck;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.foult_item_row, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtTRLRValueID.setText(mLrInvoiceResponse.get(position).getLrno());
        holder.txtTRNameValueID.setText(mLrInvoiceResponse.get(position).getTransporterName());
        holder.txtMOBNumberValueID.setText(mLrInvoiceResponse.get(position).getMobno());
        holder.txtSAPBillValueID.setText(mLrInvoiceResponse.get(position).getSapBillno());
        holder.txtTRBLDValueID.setText(mLrInvoiceResponse.get(position).getBillDate());
        holder.txtLRDateValueID.setText(mLrInvoiceResponse.get(position).getBillDate());
        holder.txtGSTBillValueID.setText(mLrInvoiceResponse.get(position).getGstBillno());

        if (mLrInvoiceSelectionCheck.get(position)) {
            ///true section
            holder.imgCheckBoxImageID.setImageResource(R.drawable.ic_select_icon);
        } else {
            holder.imgCheckBoxImageID.setImageResource(R.drawable.ic_unselect_icon);
            //else sections
        }

        holder.imgCheckBoxImageID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int kk = 0; kk < mLrInvoiceResponse.size(); kk++) {

                    if (position == kk) {
                        mLrInvoiceSelectionCheck.set(kk, true);
                    } else {
                        mLrInvoiceSelectionCheck.set(kk, false);
                    }
                }
                WebURL.mSapBillNumber = mLrInvoiceResponse.get(position).getSapBillno();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (mLrInvoiceResponse != null && mLrInvoiceResponse.size() > 0)
            return mLrInvoiceResponse.size();
        else
            return 0;
        //  return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCheckBoxImageID;
        public TextView txtTRLRValueID, txtTRNameValueID, txtMOBNumberValueID, txtLRDateValueID, txtSAPBillValueID, txtTRBLDValueID, txtGSTBillValueID;
        public RelativeLayout rlvNotifyItemMainViewID;

        public ViewHolder(View v) {
            super(v);
            imgCheckBoxImageID = (ImageView) v.findViewById(R.id.imgCheckBoxImageID);
            txtTRLRValueID = (TextView) v.findViewById(R.id.txtTRLRValueID);
            txtTRNameValueID = (TextView) v.findViewById(R.id.txtTRNameValueID);
            txtMOBNumberValueID = (TextView) v.findViewById(R.id.txtMOBNumberValueID);
            txtSAPBillValueID = (TextView) v.findViewById(R.id.txtSAPBillValueID);
            txtTRBLDValueID = (TextView) v.findViewById(R.id.txtTRBLDValueID);
            txtLRDateValueID = (TextView) v.findViewById(R.id.txtLRDateValueID);
            txtGSTBillValueID = (TextView) v.findViewById(R.id.txtGSTBillValueID);
        }
    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
}


