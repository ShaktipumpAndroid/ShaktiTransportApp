package activity.PodBean;

import com.google.gson.annotations.SerializedName;

public class AssignedDeliveryDetailResponse{

	@SerializedName("fr_location")
	private String frLocation;

	@SerializedName("delivery_place")
	private String deliveryPlace;

	@SerializedName("distance")
	private String distance;

	@SerializedName("vehicle_type")
	private String vehicleType;

	@SerializedName("rfq_time")
	private String rfqTime;

	@SerializedName("confirmed")
	private String confirmed;

	@SerializedName("conf_time")
	private String confTime;

	@SerializedName("rfq_date")
	private String rfqDate;

	@SerializedName("rfq_doc")
	private String rfqDoc;

	@SerializedName("res_pernr")
	private String transportCode;

	@SerializedName("transit_time")
	private String transitTime;

	@SerializedName("to_location")
	private String toLocation;

	@SerializedName("vehicle_rc")
	private String vehicleRc;

	@SerializedName("conf_date")
	private String confDate;

	@SerializedName("via_address")
	private String viaAddress;

	@SerializedName("name1")
	private String transporterName;

	@SerializedName("bill_no")
	private String billNo;

	@SerializedName("trans_no")
	private String transNo;

	@SerializedName("assign_mob")
	private String assignMob;

	@SerializedName("zlrno")
	private String zLRno;

	@SerializedName("zbookdate")
	private String zBookDate;

	@SerializedName("zmobileno")
	private String zMobile;

	public String getzLRno() {
		return zLRno;
	}

	public void setzLRno(String zLRno) {
		this.zLRno = zLRno;
	}

	public String getzBookDate() {
		return zBookDate;
	}

	public void setzBookDate(String zBookDate) {
		this.zBookDate = zBookDate;
	}

	public String getzMobile() {
		return zMobile;
	}

	public void setzMobile(String zMobile) {
		this.zMobile = zMobile;
	}

	public String getFrLocation(){
		return frLocation;
	}

	public String getDeliveryPlace(){
		return deliveryPlace;
	}

	public String getDistance(){
		return distance;
	}

	public String getVehicleType(){
		return vehicleType;
	}

	public String getRfqTime(){
		return rfqTime;
	}

	public String getConfirmed(){
		return confirmed;
	}

	public String getConfTime(){
		return confTime;
	}

	public String getRfqDate(){
		return rfqDate;
	}

	public String getRfqDoc(){
		return rfqDoc;
	}

	public String getTransportCode(){
		return transportCode;
	}

	public String getTransitTime(){
		return transitTime;
	}

	public String getToLocation(){
		return toLocation;
	}

	public String getVehicleRc(){
		return vehicleRc;
	}

	public String getConfDate(){
		return confDate;
	}

	public String getViaAddress(){
		return viaAddress;
	}

	public String getTransporterName(){
		return transporterName;
	}

	public void setFrLocation(String frLocation) {
		this.frLocation = frLocation;
	}

	public void setDeliveryPlace(String deliveryPlace) {
		this.deliveryPlace = deliveryPlace;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public void setRfqTime(String rfqTime) {
		this.rfqTime = rfqTime;
	}

	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}

	public void setConfTime(String confTime) {
		this.confTime = confTime;
	}

	public void setRfqDate(String rfqDate) {
		this.rfqDate = rfqDate;
	}

	public void setRfqDoc(String rfqDoc) {
		this.rfqDoc = rfqDoc;
	}

	public void setTransportCode(String transportCode) {
		this.transportCode = transportCode;
	}

	public void setTransitTime(String transitTime) {
		this.transitTime = transitTime;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public void setVehicleRc(String vehicleRc) {
		this.vehicleRc = vehicleRc;
	}

	public void setConfDate(String confDate) {
		this.confDate = confDate;
	}

	public void setViaAddress(String viaAddress) {
		this.viaAddress = viaAddress;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public void setAssignMob(String assignMob) {
		this.assignMob = assignMob;
	}

	public String getBillNo() {
		return billNo;
	}

	public String getTransNo() {
		return transNo;
	}

	public String getAssignMob() {
		return assignMob;
	}
}