package bean;

import com.google.gson.annotations.SerializedName;

public class DeliveryBoyResponse{

	@SerializedName("zcity")
	private String zcity;

	@SerializedName("zcreate_date")
	private String zcreateDate;

	@SerializedName("flag")
	private String flag;

	@SerializedName("zvehical_no")
	private String zvehicalNo;

	@SerializedName("mandt")
	private String mandt;

	@SerializedName("ztrans_no")
	private String ztransNo;

	@SerializedName("zidentity_no")
	private String zidentityNo;

	@SerializedName("zlrno")
	private String zlrno;

	@SerializedName("zcreate_time")
	private String zcreateTime;

	@SerializedName("zmobile_no")
	private String zmobileNo;

	@SerializedName("zboy_name")
	private String zboyName;

	@SerializedName("zvehical_type")
	private String zvehicalType;

	@SerializedName("zidentity_photo")
	private String zidentityPhoto;

	@SerializedName("zph_status")
	private String zphStatus;

	@SerializedName("zstate_name")
	private String zstateName;

	@SerializedName("zaddress")
	private String zaddress;

	@SerializedName("zdriving_licence")
	private String zdrivingLicence;

	public String getZcity(){
		return zcity;
	}

	public String getZcreateDate(){
		return zcreateDate;
	}

	public String getFlag(){
		return flag;
	}

	public String getZvehicalNo(){
		return zvehicalNo;
	}

	public String getMandt(){
		return mandt;
	}

	public String getZtransNo(){
		return ztransNo;
	}

	public String getZidentityNo(){
		return zidentityNo;
	}

	public String getZlrno(){
		return zlrno;
	}

	public String getZcreateTime(){
		return zcreateTime;
	}

	public String getZmobileNo(){
		return zmobileNo;
	}

	public String getZboyName(){
		return zboyName;
	}

	public String getZvehicalType(){
		return zvehicalType;
	}

	public String getZidentityPhoto(){
		return zidentityPhoto;
	}

	public String getZphStatus(){
		return zphStatus;
	}

	public String getZstateName(){
		return zstateName;
	}

	public String getZaddress(){
		return zaddress;
	}

	public String getZdrivingLicence(){
		return zdrivingLicence;
	}
}