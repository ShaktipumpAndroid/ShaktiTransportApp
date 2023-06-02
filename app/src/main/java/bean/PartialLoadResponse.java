package bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PartialLoadResponse implements Parcelable {

	@SerializedName("zdoc_no")
	private String zdocNo;

	@SerializedName("zdocdate")
	private String zdocdate;

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

	@SerializedName("vbeln")
	private String vbeln;

	@SerializedName("zmobileno")
	private String zmobileno;

	@SerializedName("ztransname")
	private String ztransname;

	@SerializedName("cust_name")
	private String custName;

	@SerializedName("zdelivery")
	private String zdelivery;

	@SerializedName("zdeliverd_to")
	private String zdeliverdTo;

	@SerializedName("fkdat")
	private String fkdat;

	@SerializedName("vkorg")
	private String vkorg;

	@SerializedName("status")
	private String status;

	public PartialLoadResponse(Parcel in) {
		zdocNo = in.readString();
		zdocdate = in.readString();
		zlrno = in.readString();
		transporterMob = in.readString();
		werks = in.readString();
		kunag = in.readString();
		zbookdate = in.readString();
		vbeln = in.readString();
		zmobileno = in.readString();
		ztransname = in.readString();
		custName = in.readString();
		zdelivery = in.readString();
		zdeliverdTo = in.readString();
		fkdat = in.readString();
		vkorg = in.readString();
		status = in.readString();
	}

	public static final Creator<PartialLoadResponse> CREATOR = new Creator<PartialLoadResponse>() {
		@Override
		public PartialLoadResponse createFromParcel(Parcel in) {
			return new PartialLoadResponse(in);
		}

		@Override
		public PartialLoadResponse[] newArray(int size) {
			return new PartialLoadResponse[size];
		}
	};

	public PartialLoadResponse() {

	}

	public String getZdocNo(){
		return zdocNo;
	}

	public String getZdocdate(){
		return zdocdate;
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

	public String getVbeln(){
		return vbeln;
	}

	public String getZmobileno(){
		return zmobileno;
	}

	public String getZtransname(){
		return ztransname;
	}

	public String getCustName(){
		return custName;
	}

	public String getZdelivery(){
		return zdelivery;
	}

	public String getZdeliverdTo() {
		return zdeliverdTo;
	}

	public String getFkdat(){
		return fkdat;
	}

	public String getVkorg(){
		return vkorg;
	}

	public String getStatus(){
		return status;
	}

	public void setZdocNo(String zdocNo) {
		this.zdocNo = zdocNo;
	}

	public void setZdocdate(String zdocdate) {
		this.zdocdate = zdocdate;
	}

	public void setZlrno(String zlrno) {
		this.zlrno = zlrno;
	}

	public void setTransporterMob(String transporterMob) {
		this.transporterMob = transporterMob;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public void setKunag(String kunag) {
		this.kunag = kunag;
	}

	public void setZbookdate(String zbookdate) {
		this.zbookdate = zbookdate;
	}

	public void setVbeln(String vbeln) {
		this.vbeln = vbeln;
	}

	public void setZmobileno(String zmobileno) {
		this.zmobileno = zmobileno;
	}

	public void setZtransname(String ztransname) {
		this.ztransname = ztransname;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public void setZdelivery(String zdelivery) {
		this.zdelivery = zdelivery;
	}

	public void setZdeliverdTo(String zdeliverdTo) {
		this.zdeliverdTo = zdeliverdTo;
	}

	public void setFkdat(String fkdat) {
		this.fkdat = fkdat;
	}

	public void setVkorg(String vkorg) {
		this.vkorg = vkorg;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(zdocNo);
		dest.writeString(zdocdate);
		dest.writeString(zlrno);
		dest.writeString(transporterMob);
		dest.writeString(werks);
		dest.writeString(kunag);
		dest.writeString(zbookdate);
		dest.writeString(vbeln);
		dest.writeString(zmobileno);
		dest.writeString(ztransname);
		dest.writeString(custName);
		dest.writeString(zdelivery);
		dest.writeString(zdeliverdTo);
		dest.writeString(fkdat);
		dest.writeString(vkorg);
		dest.writeString(status);
	}

	@Override
	public String toString() {
		return "PartialLoadResponse{" +
				"zdocNo='" + zdocNo + '\'' +
				", zdocdate='" + zdocdate + '\'' +
				", zlrno='" + zlrno + '\'' +
				", transporterMob='" + transporterMob + '\'' +
				", werks='" + werks + '\'' +
				", kunag='" + kunag + '\'' +
				", zbookdate='" + zbookdate + '\'' +
				", vbeln='" + vbeln + '\'' +
				", zmobileno='" + zmobileno + '\'' +
				", ztransname='" + ztransname + '\'' +
				", custName='" + custName + '\'' +
				", zdelivery='" + zdelivery + '\'' +
				", zdeliverdTo='" + zdeliverdTo + '\'' +
				", fkdat='" + fkdat + '\'' +
				", vkorg='" + vkorg + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}