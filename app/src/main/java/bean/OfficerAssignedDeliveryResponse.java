package bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class OfficerAssignedDeliveryResponse implements Parcelable {

	@SerializedName("zdeliverd_to")
	private String zdeliverdTo;

//	@SerializedName("zdoc_no")
//	private String zdocNo;

	@SerializedName("zdocdate")
	private String zdocdate;

	@SerializedName("zdelivered_by")
	private String zdeliveredBy;

	@SerializedName("zlrno")
	private String zlrno;

	@SerializedName("transporter_mob")
	private String transporterMob;

	@SerializedName("werks")
	private String werks;

	@SerializedName("kunag")
	private String kunag;

	@SerializedName("zbookdate")
	private String zbookdate;

	@SerializedName("zassign_mob_no")
	private String zassignMobNo;

	@SerializedName("vbeln")
	private String vbeln;

//	@SerializedName("zmobileno")
//	private String zmobileno;

	@SerializedName("ztransname")
	private String ztransname;

	@SerializedName("cust_name")
	private String custName;

	@SerializedName("zdelivery")
	private String zdelivery;

	@SerializedName("fkdat")
	private String fkdat;

//	@SerializedName("vkorg")
//	private String vkorg;

//	@SerializedName("status")
//	private String status;

	protected OfficerAssignedDeliveryResponse(Parcel in) {
		zdeliverdTo = in.readString();
//		zdocNo = in.readString();
		zdocdate = in.readString();
		zdeliveredBy = in.readString();
		zlrno = in.readString();
		transporterMob = in.readString();
		werks = in.readString();
		kunag = in.readString();
		zbookdate = in.readString();
		zassignMobNo = in.readString();
		vbeln = in.readString();
//		zmobileno = in.readString();
		ztransname = in.readString();
		custName = in.readString();
		zdelivery = in.readString();
		fkdat = in.readString();
//		vkorg = in.readString();
//		status = in.readString();
	}

	public static final Creator<OfficerAssignedDeliveryResponse> CREATOR = new Creator<OfficerAssignedDeliveryResponse>() {
		@Override
		public OfficerAssignedDeliveryResponse createFromParcel(Parcel in) {
			return new OfficerAssignedDeliveryResponse(in);
		}

		@Override
		public OfficerAssignedDeliveryResponse[] newArray(int size) {
			return new OfficerAssignedDeliveryResponse[size];
		}
	};

	public String getZdeliverdTo(){
		return zdeliverdTo;
	}

//	public String getZdocNo(){
//		return zdocNo;
//	}

	public String getZdocdate(){
		return zdocdate;
	}

	public String getZdeliveredBy(){
		return zdeliveredBy;
	}

	public String getZlrno(){
		return zlrno;
	}

	public String getTransporterMob(){
		return transporterMob;
	}

	public String getWerks(){
		return werks;
	}

	public String getKunag(){
		return kunag;
	}

	public String getZbookdate(){
		return zbookdate;
	}

	public String getZassignMobNo(){
		return zassignMobNo;
	}

	public String getVbeln(){
		return vbeln;
	}

//	public String getZmobileno(){
//		return zmobileno;
//	}

	public String getZtransname(){
		return ztransname;
	}

	public String getCustName(){
		return custName;
	}

	public String getZdelivery(){
		return zdelivery;
	}

	public String getFkdat(){
		return fkdat;
	}

//	public String getVkorg(){
//		return vkorg;
//	}
//
//	public String getStatus(){
//		return status;
//	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(zdeliverdTo);
//		dest.writeString(zdocNo);
		dest.writeString(zdocdate);
		dest.writeString(zdeliveredBy);
		dest.writeString(zlrno);
		dest.writeString(transporterMob);
		dest.writeString(werks);
		dest.writeString(kunag);
		dest.writeString(zbookdate);
		dest.writeString(zassignMobNo);
		dest.writeString(vbeln);
//		dest.writeString(zmobileno);
		dest.writeString(ztransname);
		dest.writeString(custName);
		dest.writeString(zdelivery);
		dest.writeString(fkdat);
//		dest.writeString(vkorg);
//		dest.writeString(status);
	}
}