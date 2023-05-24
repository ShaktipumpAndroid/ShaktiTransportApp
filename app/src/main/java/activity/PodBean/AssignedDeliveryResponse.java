package activity.PodBean;

import com.google.gson.annotations.SerializedName;

public class AssignedDeliveryResponse {

	@SerializedName("rfq_doc")
	private String rfqDoc;

	@SerializedName("vehicle_rc")
	private String vehicleRc;

	@SerializedName("conf_date")
	private String confDate;

	@SerializedName("conf_time")
	private String confTime;

	@SerializedName("res_pernr")
	private String transportCode;

	@SerializedName("vend_name")
	private String transporterName;

	@SerializedName("fr_location")
	private String frLocation;

	@SerializedName("to_location")
	private String toLocation;

	public String getRfqDoc(){
		return rfqDoc;
	}

	public String getVehicleRc(){
		return vehicleRc;
	}

	public String getConfDate(){
		return confDate;
	}

	public String getConfTime(){
		return confTime;
	}

	public String getTransportCode(){
		return transportCode;
	}

	public String getTransporterName(){
		return transporterName;
	}

	public String getFrLocation(){
		return frLocation;
	}

	public String getToLocation(){
		return toLocation;
	}
}