
package activity.PodBean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class InvoiceBeanModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("response")
    private List<InvoicelistBeanResponse> mResponse;
    @SerializedName("status")
    private Boolean mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<InvoicelistBeanResponse> getResponse() {
        return mResponse;
    }

    public void setResponse(List<InvoicelistBeanResponse> response) {
        mResponse = response;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

}
