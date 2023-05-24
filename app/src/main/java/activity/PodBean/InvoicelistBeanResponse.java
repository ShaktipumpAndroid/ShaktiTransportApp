
package activity.PodBean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class InvoicelistBeanResponse implements Serializable {

    @SerializedName("bill")
    private String mBill;
    @SerializedName("controllerSerno")
    private String mControllerSerno;
    @SerializedName("matorSerno")
    private String mMatorSerno;
    @SerializedName("setSerno")
    private String mSetSerno;
    @SerializedName("WERKS")
    private String mWERKS;

    public String getBill() {
        return mBill;
    }

    public void setBill(String bill) {
        mBill = bill;
    }

    public String getControllerSerno() {
        return mControllerSerno;
    }

    public void setControllerSerno(String controllerSerno) {
        mControllerSerno = controllerSerno;
    }

    public String getMatorSerno() {
        return mMatorSerno;
    }

    public void setMatorSerno(String matorSerno) {
        mMatorSerno = matorSerno;
    }

    public String getSetSerno() {
        return mSetSerno;
    }

    public void setSetSerno(String setSerno) {
        mSetSerno = setSerno;
    }

    public String getWERKS() {
        return mWERKS;
    }

    public void setWERKS(String WERKS) {
        mWERKS = WERKS;
    }

}
