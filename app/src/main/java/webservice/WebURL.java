package webservice;


public class WebURL {

    public static final String IMAGE_DIRECTORY_NAME = "shaktitranspotapp";
    public static int UserTypeCheck = 0;
    public static int FORM_CHECK_LENGTH = 0;
    public static String ANDROID_APP_VERSION = "";

    public static String CUSTOMERID_ID= "";
    public static String GALLERY_DIRECTORY_NAME_COMMON = "ShaktiKusum";
    /************* Development server **************/

/* public static final String VERSION_PAGE           = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/version.htm" ;
 public static final String LOGIN_PAGE               = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/login.htm" ;
 public static final String STATE_DATA               = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/state_data.htm" ;
 public static final String ROUTE_DATA               = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/route_data.htm" ;
 public static final String RFQ_DATA                 = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/rfq_data.htm" ;
 public static final String APP_VEHICLE              = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/quotation_apr.htm" ;
 public static final String QUOTATION_DATA           = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/quotation_data.htm" ;
 public static final String SYNC_OFFLINE_DATA_TO_SAP = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/sync_offline_data.htm" ;*/

    /*************production server **************/
    public static String HOST_NAME2 = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/";
    // public static  String HOST_NAME2 = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/";
    public static String COMPELETE_ACCESS_TOKEN_NAME = "";
    public static String mSapBillNumber = "";
    //public static  String API_LR_INVOICELIST = "lr_search.htm";
    public static String API_LR_INVOICELIST = HOST_NAME2 + "lr_search.htm";
    public static String API_LR_PHOTO_SAVE = HOST_NAME2 + "lr_photo.htm";
    public static String API_SNF_DATA_SAVE = HOST_NAME2 + "detail_save.htm";
    public static String API_SNF_GET_INVOICE_DATA = HOST_NAME2 + "ibase.htm";

    // http://spdevsrvr1.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/ibase.htm

    // http://spdevsrvr1.shaktipumps.com:8000/sap/bc/bsp/sap/zmapp_transport/detail_save.htm

    public static final String VERSION_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/version.htm";
    public static final String LOGIN_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/login.htm";
    public static final String STATE_DATA = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/state_data.htm";
    public static final String ROUTE_DATA = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/route_data.htm";
    public static final String RFQ_DATA = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/rfq_data.htm";
    public static final String APP_VEHICLE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/quotation_apr.htm";
    public static final String QUOTATION_DATA = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/quotation_data.htm";
    public static final String SYNC_OFFLINE_DATA_TO_SAP = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/sync_offline_data.htm";
    public static final String STATE_URL = "https://countriesnow.space/api/v0.1/countries/states";
    public static final String CITY_URL = "https://countriesnow.space/api/v0.1/countries/state/cities";
    public static final String CHECK_DELIVERY = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/delivery_boy_login.htm";
    public static final String ADD_DELIVERY_BOY_URL = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/zget_delivery_boy_data.htm";
    public static final String GET_ASSIGNED_DELIVERY_LIST = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/rfq_data_of_delivery_boy.htm?";
    public static final String SUBMIT_ASSIGNED_DELIVERY_DELIVERED = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/delivery_photo_save_new.htm";
    public static final String GET_PARTIAL_LOAD_LIST = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/billing_no_of_vendor_with_agreement.htm";
    public static final String SEND_DELIVERY_ASSIGNED_DATA = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/ztranspoter_bill_assignmenmt.htm";
    public static final String GET_OFFICER_ASSIGNED_DELIVERY_LIST = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_transport/all_delivery_show_trans_officer.htm";

}
