
package activity.PodBean;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class LrInvoiceModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("response")
    private List<LrInvoiceResponse> mResponse;
    @SerializedName("status")
    private boolean mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<LrInvoiceResponse> getResponse() {
        return mResponse;
    }

    public void setResponse(List<LrInvoiceResponse> response) {
        mResponse = response;
    }

    public boolean getStatus() {
        return mStatus;
    }

    public void setStatus(boolean status) {
        mStatus = status;
    }

}
