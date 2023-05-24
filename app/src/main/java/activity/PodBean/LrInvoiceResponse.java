
package activity.PodBean;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class LrInvoiceResponse implements Serializable {

    @SerializedName("bill_date")
    private String mBillDate;
    @SerializedName("gst_billno")
    private String mGstBillno;
    @SerializedName("lrno")
    private String mLrno;
    @SerializedName("mobno")
    private String mMobno;
    @SerializedName("sap_billno")
    private String mSapBillno;
    @SerializedName("transporter_name")
    private String mTransporterName;

    @SerializedName("lrdate")
    private String mLrdate;

    public String getBillDate() {
        return mBillDate;
    }

    public void setBillDate(String billDate) {
        mBillDate = billDate;
    }

    public String getGstBillno() {
        return mGstBillno;
    }

    public void setGstBillno(String gstBillno) {
        mGstBillno = gstBillno;
    }

    public String getLrno() {
        return mLrno;
    }

    public void setLrno(String lrno) {
        mLrno = lrno;
    }

    public String getMobno() {
        return mMobno;
    }

    public void setMobno(String mobno) {
        mMobno = mobno;
    }

    public String getSapBillno() {
        return mSapBillno;
    }

    public void setSapBillno(String sapBillno) {
        mSapBillno = sapBillno;
    }

    public String getTransporterName() {
        return mTransporterName;
    }

    public void setTransporterName(String transporterName) {
        mTransporterName = transporterName;
    }

    public String getLrdate() {
        return mLrdate;
    }

    public void setLrdate(String lrdate) {
        mLrdate = lrdate;
    }

}
