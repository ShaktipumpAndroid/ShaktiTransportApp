package activity.PodBean;

import com.google.gson.annotations.SerializedName;

public class DeliveryDeliveredInput{

	@SerializedName("“photo2”")
	private String photo2;

	@SerializedName("“photo3”")
	private String photo3;

	@SerializedName("“photo1”")
	private String photo1;

	@SerializedName("driver_mob")
	private String driverMob;

	@SerializedName("“photo6”")
	private String photo6;

	@SerializedName("“res_pernr”")
	private String resPernr;

	@SerializedName("“photo4”")
	private String photo4;

	@SerializedName("rfq_doc")
	private String rfqDoc;

	@SerializedName("“photo5”")
	private String photo5;

	@SerializedName("“bill_no”")
	private String billNo;

	@SerializedName("trans_no")
	private String transNo;

	@SerializedName("“assign_mob”")
	private String assignMob;

	public String getPhoto2(){
		return photo2;
	}

	public String getPhoto3(){
		return photo3;
	}

	public String getPhoto1(){
		return photo1;
	}

	public String getDriverMob(){
		return driverMob;
	}

	public String getPhoto6(){
		return photo6;
	}

	public String getResPernr(){
		return resPernr;
	}

	public String getPhoto4(){
		return photo4;
	}

	public String getRfqDoc(){
		return rfqDoc;
	}

	public String getPhoto5(){
		return photo5;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public void setDriverMob(String driverMob) {
		this.driverMob = driverMob;
	}

	public void setPhoto6(String photo6) {
		this.photo6 = photo6;
	}

	public void setResPernr(String resPernr) {
		this.resPernr = resPernr;
	}

	public void setPhoto4(String photo4) {
		this.photo4 = photo4;
	}

	public void setRfqDoc(String rfqDoc) {
		this.rfqDoc = rfqDoc;
	}

	public void setPhoto5(String photo5) {
		this.photo5 = photo5;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getAssignMob() {
		return assignMob;
	}

	public void setAssignMob(String assignMob) {
		this.assignMob = assignMob;
	}
}