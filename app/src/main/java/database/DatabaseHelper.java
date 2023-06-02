package database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import activity.PodBean.AssignedDeliveryDetailResponse;
import activity.PodBean.DeliveryDeliveredInput;
import activity.PodBean.ImageModel;
import bean.DocumentBean;
import bean.LoginBean;
import bean.PartialLoadResponse;
import bean.QuotationBean;
import bean.RfqBean;
import bean.RouteBean;
import utility.CustomUtility;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "db_shakti_transport";

    // Database Version
    public static final int DATABASE_VERSION = 4;

    // Table Names
    public static final String TABLE_LOGIN = "tbl_login";
    public static final String TABLE_STATE_SEARCH = "tbl_state_detail";
    public static final String TABLE_SYNC_DAILY = "tbl_sync_daily";
    public static final String TABLE_ROUTE = "tbl_route";
    public static final String TABLE_RFQ = "tbl_rfq";
    public static final String TABLE_QUOTATION = "tbl_quotation";
    public static final String TABLE_VERIFICATION = "tbl_verification";
    public static final String TABLE_ASSIGNED_DELIVERIES = "tbl_assigned_deliveries";
    public static final String TABLE_ASSIGNED_DELIVERY_DELIVERED = "tbl_assigned_delivery_delivered";
    public static final String TABLE_PARTIAL_LOAD = "tbl_partial_load";
    public static final String TABLE_LR_IMAGES = "tbl_lr_transport_images";

    //fields name
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LONG = "longitued";
    public static final String KEY_INST_DOC = "rfq_no";
    public static final String KEY_DOC_DATE = "doc_date";

    // TABLE_ASSIGNED_DELIVERY_DELIVERED field names
    public static final String KEY_DELIVERY_RFQ_DOC = "rfq_doc";
    public static final String KEY_DRIVER_MOB = "driver_mob";
    public static final String KEY_TRANSPORT_COD = "transport_cod";
    public static final String KEY_BILL_NO = "bill_no";
    public static final String KEY_TRANS_NO = "trans_no";
    public static final String KEY_ASSIGN_NO = "assign_no";
    public static final String KEY_BOOK_DATE = "book_date";
    public static final String KEY_LR_NO = "lr_no";
    public static final String KEY_MOBILE = "mobile_no";
    public static final String KEY_DELIVERY_PHOTO1 = "delivery_photo_1";
    public static final String KEY_DELIVERY_PHOTO2 = "delivery_photo_2";
    public static final String KEY_DELIVERY_PHOTO3 = "delivery_photo_3";
    public static final String KEY_DELIVERY_PHOTO4 = "delivery_photo_4";
    public static final String KEY_DELIVERY_PHOTO5 = "delivery_photo_5";
    public static final String KEY_DELIVERY_PHOTO6 = "delivery_photo_6";
    /*End*/
    // TABLE_ASSIGNED_DELIVERIES field names
    public static final String KEY_ASSIGNED_DELIVERY_RFQ_DOC = "rfq_doc";
    public static final String KEY_ASSIGNED_DELIVERY_VEHICLE_RC = "vehicle_rc";
    public static final String KEY_ASSIGNED_DELIVERY_CONF_DATE = "conf_date";
    public static final String KEY_ASSIGNED_DELIVERY_CONF_TIME = "conf_time";
    public static final String KEY_ASSIGNED_DELIVERY_CONFIRMED = "confirmed";
    public static final String KEY_ASSIGNED_DELIVERY_RES_PERNR = "res_pernr";
    public static final String KEY_ASSIGNED_DELIVERY_TRANSPORTER_NAME = "transporter_name";
    public static final String KEY_ASSIGNED_DELIVERY_FR_LOCATION = "fr_location";
    public static final String KEY_ASSIGNED_DELIVERY_TO_LOCATION = "to_location";
    public static final String KEY_ASSIGNED_DELIVERY_VIA_ADDRESS = "via_address";
    public static final String KEY_ASSIGNED_DELIVERY_VEHICLE_TYPE = "vehicle_type";
    public static final String KEY_ASSIGNED_DELIVERY_DISTANCE = "distance";
    public static final String KEY_ASSIGNED_DELIVERY_DELIVERY_PLACE = "delivery_place";
    public static final String KEY_ASSIGNED_DELIVERY_TRANSIT_TIME = "transit_time";
    public static final String KEY_ASSIGNED_DELIVERY_RFQ_DATE = "rfq_date";
    public static final String KEY_ASSIGNED_DELIVERY_RFQ_TIME = "rfq_time";
    /* END */
    // TABLE_PARTIAL_LOAD field names
    public static final String KEY_PARTIAL_LOAD_ZDOC_DOC = "zdoc_no";
    public static final String KEY_PARTIAL_LOAD_ZDOC_DATE = "zdocdate";
    public static final String KEY_PARTIAL_LOAD_ZLRNO = "zlrno";
    public static final String KEY_PARTIAL_LOAD_TRANSPORTER_MOB = "transporter_mob";
    public static final String KEY_PARTIAL_LOAD_WERKS = "werks";
    public static final String KEY_PARTIAL_LOAD_KUNAG = "kunag";
    public static final String KEY_PARTIAL_LOAD_ZBOOK_DATE = "zbookdate";
    public static final String KEY_PARTIAL_LOAD_VBELN = "vbeln";
    public static final String KEY_PARTIAL_LOAD_ZMOBILENO = "zmobileno";
    public static final String KEY_PARTIAL_LOAD_ZTRANSNAME = "ztransname";
    public static final String KEY_PARTIAL_LOAD_CUST_NAME = "cust_name";
    public static final String KEY_PARTIAL_LOAD_ZDELIVERY = "zdelivery";
    public static final String KEY_PARTIAL_LOAD_ZDELIVERY_TO = "zdeliverd_to";
    public static final String KEY_PARTIAL_LOAD_FKDATE = "fkdat";
    public static final String KEY_PARTIAL_LOAD_VKORG = "vkorg";
    public static final String KEY_PARTIAL_LOAD_STATUS = "status";
    /* END */
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_COUNTRY_TEXT = "country_text";
    public static final String KEY_STATE = "state";
    public static final String KEY_STATE_TEXT = "state_text";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_DISTRICT_TEXT = "district_text";
    public static final String KEY_TEHSIL = "tehsil";
    public static final String KEY_TEHSIL_TEXT = "tehsil_text";
    public static final String KEY_USERID = "userid";
    public static final String KEY_DATE = "date";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERTYPE = "usertype";

    public static final String KEY_IMAGES_ID = "imagesId",KEY_IMAGES_NAME = "imagesName",KEY_IMAGES_PATH = "imagesPath",KEY_IMAGES_SELECTED = "imagesSelected",KEY_IMAGES_BILL_NO= "imagesBillNo";

    public static final String KEY_ID = "id";
    public static final String KEY_RFQ_DOC = "rfq_doc";
    public static final String KEY_RFQ_DOC_MANUAL = "rfq_doc_manual";
    public static final String KEY_FR_STATE_TEXT = "fr_state_text";
    public static final String KEY_FR_DISTRICT_TEXT = "fr_district_text";
    public static final String KEY_FR_TEHSIL_TEXT = "fr_tehsil_text";
    public static final String KEY_TO_STATE_TEXT = "to_state_text";
    public static final String KEY_TO_DISTRICT_TEXT = "to_district_text";
    public static final String KEY_TO_TEHSIL_TEXT = "to_tehsil_text";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_TR_MODE = "tr_mode";
    public static final String KEY_UPDATE_ROUTE_FROM_SAP = "update_route_sap";
    public static final String KEY_INSERT_ROUTE = "insert_route";
    public static final String KEY_INSERT_RFQ = "insert_rfq";
    public static final String KEY_INSERT_QUOTATION = "insert_quotation";
    public static final String KEY_RFQ_SYNC = "rfq_sync";
    public static final String KEY_RFQ_UPDATE_FROM_SAP = "rfq_update_from_sap";
    public static final String KEY_QUOTATION_UPDATE_FROM_SAP = "quotation_update_from_sap";
    public static final String KEY_QUOTE_SYNC = "quote_sync";
    public static final String KEY_STATUS = "status";
    public static final String KEY_PENDING_ROUTE = "pending_route";
    public static final String KEY_APPROVED_ROUTE = "approved_route";
    public static final String KEY_RES_PERNR = "res_pernr";
    public static final String KEY_RES_PERNR_TYPE = "res_pernr_type";

    public static final String KEY_FR_LOCATION = "fr_location";
    public static final String KEY_TO_LOCATION = "to_location";
    public static final String KEY_FR_LATLONG = "fr_latlong";
    public static final String KEY_TO_LATLONG = "to_latlong";
    public static final String KEY_VIA_ADDRESS = "via_address";
    public static final String KEY_VIA_ADDRESS_LATLONG = "via_address_latlong";
    public static final String KEY_VEHICLE_TYPE = "vehicle_type";
    public static final String KEY_VEHICLE_WEIGHT = "vehicle_weight";
    public static final String KEY_DELIVERY_PLACE = "delivery_place";
    public static final String KEY_TRANSIT_TIME = "transit_time";
    public static final String KEY_RFQ_DATE = "rfq_date";
    public static final String KEY_RFQ_TIME = "rfq_time";
    public static final String KEY_EXPIRY_DATE = "expiry_date";
    public static final String KEY_EXPIRY_TIME = "expiry_time";
    public static final String KEY_SYNC = "sync";
    public static final String KEY_CONFIRMED = "confirmed";
    public static final String KEY_VEH_RC = "vehicle_rc";
    public static final String KEY_RFQ_COMPLETED = "rfq_completed";
    public static final String KEY_RFQ_PENDING = "rfq_pending";
    public static final String KEY_RFQ_STATUS = "rfq_status";

    public static final String KEY_QUOTE_STATUS = "quote_status";
    public static final String KEY_VEHICLE_RC = "vehicle_rc";
    public static final String KEY_VEHICLE_MAKE = "vehicle_make";
    public static final String KEY_REMARK = "remark";
    public static final String KEY_RATE = "rate";
    public static final String KEY_PUC = "puc";
    public static final String KEY_INSURANCE = "insurance";
    public static final String KEY_LICENCE = "licence";
    public static final String KEY_QUOTE_COMPLETED = "quote_completed";
    public static final String KEY_QUOTE_PENDING = "quote_pending";

    public static final String KEY_ADD1 = "add1";
    public static final String KEY_ADD2 = "add2";
    public static final String KEY_ADD3 = "add3";
    public static final String KEY_ADD4 = "add4";
    public static final String KEY_ADD5 = "add5";
    public static final String KEY_ADD6 = "add6";
    public static final String KEY_ADD7 = "add7";
    public static final String KEY_ADD8 = "add8";
    public static final String KEY_ADD9 = "add9";
    public static final String KEY_ADD10 = "add10";
    public static final String KEY_ADD11 = "add11";
    public static final String KEY_ADD12 = "add12";
    public static final String KEY_ADD13 = "add13";
    public static final String KEY_ADD14 = "add14";
    public static final String KEY_ADD15 = "add15";
    public static final String KEY_ADD16 = "add16";
    public static final String KEY_ADD17 = "add17";
    public static final String KEY_ADD18 = "add18";
    public static final String KEY_ADD19 = "add19";
    public static final String KEY_ADD20 = "add20";

    public static final String KEY_PHOTO_1 = "photo1";
    public static final String KEY_PHOTO_2 = "photo2";
    public static final String KEY_PHOTO_3 = "photo3";
    public static final String KEY_PHOTO_4 = "photo4";
    public static final String KEY_PHOTO_5 = "photo5";

    public static final String KEY_PHOTO_6 = "photo6";
    public static final String KEY_PHOTO_7 = "photo7";
    public static final String KEY_PHOTO_8 = "photo8";
    public static final String KEY_PHOTO_9 = "photo9";
    public static final String KEY_PHOTO_10 = "photo10";

// Table Create Statements

    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOGIN + "("
            + KEY_USERID + " PRIMARY KEY ,"
            + KEY_USERNAME + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_USERTYPE + " TEXT)";


    private static final String CREATE_TABLE_STATE_SEARCH = "CREATE TABLE "
            + TABLE_STATE_SEARCH + "("
            + KEY_COUNTRY + " TEXT,"
            + KEY_COUNTRY_TEXT + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_TEHSIL + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT)";

    private static final String CREATE_TABLE_LR_IMAGES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LR_IMAGES + "("
            + KEY_IMAGES_ID + " TEXT,"
            + KEY_IMAGES_NAME + " TEXT,"
            + KEY_IMAGES_BILL_NO + " TEXT,"
            + KEY_IMAGES_PATH + " TEXT,"
            + KEY_IMAGES_SELECTED + " TEXT)";



    private static final String CREATE_TABLE_SYNC_DAILY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SYNC_DAILY + "("
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_DATE + " TEXT)";


    private static final String CREATE_TABLE_ROUTE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ROUTE + "("
            + KEY_ID + " INTEGER        PRIMARY KEY AUTOINCREMENT,"
            + KEY_FR_STATE_TEXT + " TEXT,"
            + KEY_FR_DISTRICT_TEXT + " TEXT,"
            + KEY_FR_TEHSIL_TEXT + " TEXT,"
            + KEY_TO_STATE_TEXT + " TEXT,"
            + KEY_TO_DISTRICT_TEXT + " TEXT,"
            + KEY_TO_TEHSIL_TEXT + " TEXT,"
            + KEY_DISTANCE + " TEXT,"
            + KEY_TR_MODE + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_RES_PERNR + " TEXT,"
            + KEY_RES_PERNR_TYPE + " TEXT,"


            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT,"
            + KEY_ADD17 + " TEXT,"
            + KEY_ADD18 + " TEXT,"
            + KEY_ADD19 + " TEXT,"
            + KEY_ADD20 + " TEXT)";


    private static final String CREATE_TABLE_RFQ = "CREATE TABLE IF NOT EXISTS "
            + TABLE_RFQ + "("
            + KEY_RFQ_DOC + " TEXT,"
            + KEY_RFQ_DOC_MANUAL + " TEXT,"
            + KEY_FR_LOCATION + " TEXT,"
            + KEY_TO_LOCATION + " TEXT,"
            + KEY_FR_LATLONG + " TEXT,"
            + KEY_TO_LATLONG + " TEXT,"
            + KEY_VIA_ADDRESS + " TEXT,"
            + KEY_VIA_ADDRESS_LATLONG + " TEXT,"
            + KEY_TR_MODE + " TEXT,"
            + KEY_VEHICLE_TYPE + " TEXT,"
            + KEY_VEHICLE_WEIGHT + " TEXT,"
            + KEY_DISTANCE + " TEXT,"
            + KEY_DELIVERY_PLACE + " TEXT,"
            + KEY_TRANSIT_TIME + " TEXT,"
            + KEY_RFQ_DATE + " TEXT,"
            + KEY_RFQ_TIME + " TEXT,"
            + KEY_EXPIRY_DATE + " TEXT,"
            + KEY_EXPIRY_TIME + " TEXT,"

            + KEY_SYNC + " TEXT,"
            + KEY_CONFIRMED + " TEXT,"
            + KEY_VEH_RC + " TEXT,"
            + KEY_RFQ_COMPLETED + " TEXT,"
            + KEY_RFQ_STATUS + " TEXT,"
            + KEY_RES_PERNR + " TEXT,"
            + KEY_RES_PERNR_TYPE + " TEXT,"


            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT,"
            + KEY_ADD17 + " TEXT,"
            + KEY_ADD18 + " TEXT,"
            + KEY_ADD19 + " TEXT,"
            + KEY_ADD20 + " TEXT)";

    private static final String CREATE_TABLE_QUOTATION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_QUOTATION + "("
            + KEY_RFQ_DOC + " TEXT,"
            + KEY_RFQ_DOC_MANUAL + " TEXT,"
            + KEY_VEHICLE_RC + " TEXT,"
            + KEY_VEHICLE_MAKE + " TEXT,"
            + KEY_REMARK + " TEXT,"
            + KEY_RATE + " TEXT,"
            + KEY_PUC + " TEXT,"
            + KEY_INSURANCE + " TEXT,"
            + KEY_LICENCE + " TEXT,"
            + KEY_QUOTE_STATUS + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_QUOTE_COMPLETED + " TEXT,"
            + KEY_RES_PERNR + " TEXT,"
            + KEY_RES_PERNR_TYPE + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_ADD11 + " TEXT,"
            + KEY_ADD12 + " TEXT,"
            + KEY_ADD13 + " TEXT,"
            + KEY_ADD14 + " TEXT,"
            + KEY_ADD15 + " TEXT,"
            + KEY_ADD16 + " TEXT,"
            + KEY_ADD17 + " TEXT,"
            + KEY_ADD18 + " TEXT,"
            + KEY_ADD19 + " TEXT,"
            + KEY_ADD20 + " TEXT)";

    private static final String CREATE_TABLE_DOC_VERIFICATION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_VERIFICATION + "("
            + KEY_INST_DOC + " PRIMARY KEY ,"
            + KEY_DOC_DATE + " TEXT,"
            + KEY_LAT + " TEXT,"
            + KEY_LONG + " TEXT,"
            + KEY_PHOTO_1 + " TEXT,"
            + KEY_PHOTO_2 + " TEXT,"
            + KEY_PHOTO_3 + " TEXT,"
            + KEY_PHOTO_4 + " TEXT,"
            + KEY_PHOTO_5 + " TEXT,"
            + KEY_PHOTO_6 + " TEXT,"
            + KEY_PHOTO_7 + " TEXT,"
            + KEY_PHOTO_8 + " TEXT,"
            + KEY_PHOTO_9 + " TEXT,"
            + KEY_PHOTO_10 + " TEXT,"
            + KEY_USERTYPE + " TEXT)";

    private static final String CREATE_TABLE_ASSIGNED_DELIVERIES = "CREATE TABLE IF NOT EXISTS " +
            TABLE_ASSIGNED_DELIVERIES + "(" +
            KEY_ASSIGNED_DELIVERY_RFQ_DOC + " TEXT," +
            KEY_ASSIGNED_DELIVERY_VEHICLE_RC + " TEXT," +
            KEY_BILL_NO + " TEXT," +
            KEY_TRANS_NO + " TEXT," +
            KEY_ASSIGN_NO + " TEXT," +
            KEY_BOOK_DATE + " TEXT," +
            KEY_LR_NO + " TEXT," +
            KEY_MOBILE+ " TEXT," +
            KEY_ASSIGNED_DELIVERY_CONF_DATE + " TEXT," +
            KEY_ASSIGNED_DELIVERY_CONF_TIME + " TEXT," +
            KEY_ASSIGNED_DELIVERY_CONFIRMED + " TEXT," +
            KEY_ASSIGNED_DELIVERY_RES_PERNR + " TEXT," +
            KEY_ASSIGNED_DELIVERY_TRANSPORTER_NAME + " TEXT," +
            KEY_ASSIGNED_DELIVERY_FR_LOCATION + " TEXT," +
            KEY_ASSIGNED_DELIVERY_TO_LOCATION + " TEXT," +
            KEY_ASSIGNED_DELIVERY_VIA_ADDRESS + " TEXT," +
            KEY_ASSIGNED_DELIVERY_VEHICLE_TYPE + " TEXT," +
            KEY_ASSIGNED_DELIVERY_DISTANCE + " TEXT," +
            KEY_ASSIGNED_DELIVERY_DELIVERY_PLACE + " TEXT," +
            KEY_ASSIGNED_DELIVERY_TRANSIT_TIME + " TEXT," +
            KEY_ASSIGNED_DELIVERY_RFQ_DATE + " TEXT," +
            KEY_ASSIGNED_DELIVERY_RFQ_TIME + " TEXT)";

    private static final String CREATE_TABLE_PARTIAL_LOAD = "CREATE TABLE IF NOT EXISTS " +
            TABLE_PARTIAL_LOAD + "(" +
            KEY_PARTIAL_LOAD_ZDOC_DOC + " TEXT," +
            KEY_PARTIAL_LOAD_ZDOC_DATE + " TEXT," +
            KEY_PARTIAL_LOAD_ZLRNO + " TEXT," +
            KEY_PARTIAL_LOAD_TRANSPORTER_MOB + " TEXT," +
            KEY_PARTIAL_LOAD_WERKS + " TEXT," +
            KEY_PARTIAL_LOAD_KUNAG + " TEXT," +
            KEY_PARTIAL_LOAD_ZBOOK_DATE + " TEXT," +
            KEY_PARTIAL_LOAD_VBELN + " TEXT," +
            KEY_PARTIAL_LOAD_ZMOBILENO + " TEXT," +
            KEY_PARTIAL_LOAD_ZTRANSNAME + " TEXT," +
            KEY_PARTIAL_LOAD_CUST_NAME + " TEXT," +
            KEY_PARTIAL_LOAD_ZDELIVERY + " TEXT," +
            KEY_PARTIAL_LOAD_ZDELIVERY_TO + " TEXT," +
            KEY_PARTIAL_LOAD_FKDATE + " TEXT," +
            KEY_PARTIAL_LOAD_VKORG + " TEXT," +
            KEY_PARTIAL_LOAD_STATUS + " TEXT)";

    private static final String CREATE_TABLE_ASSIGNED_DELIVERY_DELIVERED = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ASSIGNED_DELIVERY_DELIVERED + "("
            + KEY_DELIVERY_RFQ_DOC + " TEXT,"
            + KEY_DRIVER_MOB + " TEXT,"
            + KEY_TRANSPORT_COD + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_TRANS_NO + " TEXT,"
            + KEY_ASSIGN_NO + " TEXT,"
            + KEY_DELIVERY_PHOTO1 + " BLOB,"
            + KEY_DELIVERY_PHOTO2 + " BLOB,"
            + KEY_DELIVERY_PHOTO3 + " BLOB,"
            + KEY_DELIVERY_PHOTO4 + " BLOB,"
            + KEY_DELIVERY_PHOTO5 + " BLOB,"
            + KEY_DELIVERY_PHOTO6 + " BLOB)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_STATE_SEARCH);
        db.execSQL(CREATE_TABLE_ROUTE);
        db.execSQL(CREATE_TABLE_RFQ);
        db.execSQL(CREATE_TABLE_QUOTATION);
        db.execSQL(CREATE_TABLE_DOC_VERIFICATION);
        db.execSQL(CREATE_TABLE_ASSIGNED_DELIVERIES);
        db.execSQL(CREATE_TABLE_ASSIGNED_DELIVERY_DELIVERED);
        db.execSQL(CREATE_TABLE_PARTIAL_LOAD);
        db.execSQL(CREATE_TABLE_LR_IMAGES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNC_DAILY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE_SEARCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RFQ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LR_IMAGES);

        // create new tables
        onCreate(db);
    }

    /****************************** Delete  data **********************************************/
    public void deleteTableData(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, null, null);
    }

    public void deleteDeliveredData(String rfqNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where;
        where = KEY_DELIVERY_RFQ_DOC + "='" + rfqNo + "'";
        int value = db.delete(TABLE_ASSIGNED_DELIVERY_DELIVERED, where, null);
        Log.i("DeleteValue", value + "");
    }

    public void deleteDeliveryData(String rfqNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where;
        where = KEY_ASSIGNED_DELIVERY_RFQ_DOC + "='" + rfqNo + "'";
        int value = db.delete(TABLE_ASSIGNED_DELIVERIES, where, null);
        Log.i("DeleteValue", value + "");
    }

    /****************************** insert  data **********************************************/

    public void insertSyncDailyData(String date) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_DATE, date);

            // Insert Row
            db.insert(TABLE_SYNC_DAILY, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    public boolean isRecordExist(String tablename, String field, String fieldvalue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor c;

        String Query = "SELECT * FROM " + tablename + " WHERE " + field + " = '" + fieldvalue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    @SuppressLint("Range")
    public boolean isImageSaved(String tablename, String where, String docno, String field) {
        String result = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor c;

        String Query = "SELECT * FROM " + tablename + " WHERE " + where + " = " + docno;

        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    result = cursor.getString(cursor.getColumnIndex(field));
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();

        if (result != null && !result.isEmpty()) {

            return true;
        } else {
            return false;
        }


    }

    public boolean checkRecord(String tablename, String field1, String value1,
                               String field2, String value2) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor c;


        String Query = "SELECT * FROM " + tablename + " WHERE " + field1 + " = '" + value1 + "'"
                + " AND " + field2 + " = '" + value2 + "'";


        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    @SuppressLint("Range")
    public String getSyncDailyDate() {
        String date = null;

        SQLiteDatabase db = null;
        String selectQuery = null;
        Cursor c = null;

        try {
            db = this.getReadableDatabase();
            selectQuery = "SELECT  * FROM " + TABLE_SYNC_DAILY;

            c = db.rawQuery(selectQuery, null);

            if (c.getCount() > 0) {

                if (c.moveToFirst()) {
                    date = c.getString(c.getColumnIndex(KEY_DATE));
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        return date;
    }

    public void insertStateData(String country, String country_text, String state, String state_text, String district, String district_text, String tehsil, String tehsil_text) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_COUNTRY, country);
            values.put(KEY_COUNTRY_TEXT, country_text);
            values.put(KEY_STATE, state);
            values.put(KEY_STATE_TEXT, state_text);
            values.put(KEY_DISTRICT, district);
            values.put(KEY_DISTRICT_TEXT, district_text);
            values.put(KEY_TEHSIL, tehsil);
            values.put(KEY_TEHSIL_TEXT, tehsil_text);


            // Insert Row
            long i = db.insert(TABLE_STATE_SEARCH, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> getList(String key, String text, String title) {

        ArrayList<String> list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();


        db.beginTransaction();
        try {

            String selectQuery = null;

            switch (key) {

                case KEY_STATE:
                    selectQuery = "SELECT  DISTINCT " + KEY_STATE_TEXT + " FROM " + TABLE_STATE_SEARCH;
                    list.add(title);
                    break;
                case KEY_DISTRICT:

                    selectQuery = "SELECT  DISTINCT " + KEY_DISTRICT_TEXT + " FROM " + TABLE_STATE_SEARCH
                            + " WHERE " + KEY_STATE_TEXT + " = '" + text + "'";

                    list.add(title);
                    break;

                case KEY_TEHSIL:
                    selectQuery = "SELECT  DISTINCT " + KEY_TEHSIL_TEXT + " FROM " + TABLE_STATE_SEARCH
                            + " WHERE " + KEY_DISTRICT_TEXT + " = '" + text + "'";
                    list.add(title);
                    break;

            }


            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        switch (key) {

                            case KEY_STATE:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                                break;
                            case KEY_DISTRICT:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                                break;
                            case KEY_TEHSIL:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_TEHSIL_TEXT)));
                                break;


                        }
                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return list;
    }

    public int getPosition(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    public void insertLoginData(String userid, String username, String usertype, String userrfq) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_USERID, userid);
            values.put(KEY_USERNAME, username);
            values.put(KEY_USERTYPE, usertype);
            values.put(KEY_ADD1, userrfq);
            // Insert Row
            db.insert(TABLE_LOGIN, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    @SuppressLint("Range")
    public boolean getLogin() {
        SQLiteDatabase db = null;
        String selectQuery;
        Cursor c = null;

        try {
            db = this.getReadableDatabase();
            selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
            c = db.rawQuery(selectQuery, null);
            LoginBean lb = new LoginBean();
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    LoginBean.setLogin(c.getString(c.getColumnIndex(KEY_USERID)),
                            c.getString(c.getColumnIndex(KEY_USERNAME)),
                            c.getString(c.getColumnIndex(KEY_USERTYPE)),
                            c.getString(c.getColumnIndex(KEY_ADD1)));
                }
                return true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            c.close();
            assert db != null;
            db.close();
        }
        return false;
    }


    @SuppressLint("Range")
    public ArrayList<RouteBean> getRouteList(String key, String fr_tehsil, String to_tehsil) {

        ArrayList<RouteBean> routelist = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String lv_status = null;

        db.beginTransaction();
        try {

            switch (key) {
                case KEY_PENDING_ROUTE:
                    lv_status = "P";
                    break;
                case KEY_APPROVED_ROUTE:
                    lv_status = "A";
                    break;
            }

            String selectQuery = "";
            if (fr_tehsil.equals("null")) {


                selectQuery = "SELECT  *  FROM " + DatabaseHelper.TABLE_ROUTE
                        + " WHERE " + DatabaseHelper.KEY_STATUS + " = '" + lv_status + "'";


            } else {

                selectQuery = "SELECT  *  FROM " + DatabaseHelper.TABLE_ROUTE
                        + " WHERE " + DatabaseHelper.KEY_FR_TEHSIL_TEXT + " = '" + fr_tehsil + "'"
                        + " AND " + DatabaseHelper.KEY_TO_TEHSIL_TEXT + " = '" + to_tehsil + "'"
                        + " AND " + DatabaseHelper.KEY_STATUS + " = '" + lv_status + "'";
            }


            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        RouteBean routeBean = new RouteBean();
                        routeBean.setFr_state(cursor.getString(cursor.getColumnIndex(KEY_FR_STATE_TEXT)));
                        routeBean.setFr_district(cursor.getString(cursor.getColumnIndex(KEY_FR_DISTRICT_TEXT)));
                        routeBean.setFr_tehsil(cursor.getString(cursor.getColumnIndex(KEY_FR_TEHSIL_TEXT)));
                        routeBean.setTo_state(cursor.getString(cursor.getColumnIndex(KEY_TO_STATE_TEXT)));
                        routeBean.setTo_district(cursor.getString(cursor.getColumnIndex(KEY_TO_DISTRICT_TEXT)));
                        routeBean.setTo_tehsil(cursor.getString(cursor.getColumnIndex(KEY_TO_TEHSIL_TEXT)));
                        routeBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        routeBean.setTr_mode(cursor.getString(cursor.getColumnIndex(KEY_TR_MODE)));
                        routeBean.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                        routeBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        routeBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));


                        routelist.add(routeBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return routelist;
    }

    @SuppressLint("Range")
    public ArrayList<RfqBean> getRfq(String rfq_docno) {

        ArrayList<RfqBean> rfqlist = new ArrayList<RfqBean>();

        SQLiteDatabase db = this.getReadableDatabase();

        String lv_status = "";

        db.beginTransaction();
        try {
            String selectQuery;
            selectQuery = "SELECT  *  FROM " + TABLE_RFQ + " WHERE " + KEY_RFQ_DOC + " = '" + rfq_docno + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        RfqBean rfqBean = new RfqBean();

                        rfqBean.setRfq_doc(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC)));
                        rfqBean.setRfq_doc_manual(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC_MANUAL)));

                        rfqBean.setFr_location(cursor.getString(cursor.getColumnIndex(KEY_FR_LOCATION)));
                        rfqBean.setFr_latlong(cursor.getString(cursor.getColumnIndex(KEY_FR_LATLONG)));
                        rfqBean.setTo_location(cursor.getString(cursor.getColumnIndex(KEY_TO_LOCATION)));
                        rfqBean.setTo_latlong(cursor.getString(cursor.getColumnIndex(KEY_TO_LATLONG)));

                        rfqBean.setVia_address(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS)));
                        rfqBean.setVia_address_latlong(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS_LATLONG)));

                        rfqBean.setRfq_date(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DATE)));
                        rfqBean.setRfq_time(cursor.getString(cursor.getColumnIndex(KEY_RFQ_TIME)));

                        rfqBean.setDelivery_place(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PLACE)));
                        rfqBean.setTransit_time(cursor.getString(cursor.getColumnIndex(KEY_TRANSIT_TIME)));

                        rfqBean.setExpiry_date(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_DATE)));
                        rfqBean.setExpiry_time(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_TIME)));

                        rfqBean.setDala(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        rfqBean.setHeight(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));

                        rfqBean.setPernr_name(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));


                        rfqBean.setVehicle_type(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_TYPE)));
                        rfqBean.setVehicle_weight(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_WEIGHT)));
                        rfqBean.setFinal_rate(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));

                        rfqBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        rfqBean.setTr_mode(cursor.getString(cursor.getColumnIndex(KEY_TR_MODE)));

                        rfqBean.setRfq_status(cursor.getString(cursor.getColumnIndex(KEY_RFQ_STATUS)));
                        rfqBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        rfqBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));
                        rfqBean.setConfirmed(cursor.getString(cursor.getColumnIndex(KEY_CONFIRMED)));
                        rfqBean.setVehicle_rc(cursor.getString(cursor.getColumnIndex(KEY_VEH_RC)));
                        rfqlist.add(rfqBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return rfqlist;
    }

    @SuppressLint("Range")
    public ArrayList<QuotationBean> getQuote(String rfq_docno) {
        ArrayList<QuotationBean> quotelist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String lv_status = "";
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery = "SELECT  *  FROM " + TABLE_QUOTATION + " WHERE " + KEY_RFQ_DOC + " = '" + rfq_docno + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        QuotationBean quoteBean = new QuotationBean();
                        quoteBean.setRfq_doc(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC)));
                        quoteBean.setVehicle_make(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_MAKE)));
                        quoteBean.setVehicle_rc(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_RC)));
                        quoteBean.setRemark(cursor.getString(cursor.getColumnIndex(KEY_REMARK)));
                        quoteBean.setRate(cursor.getString(cursor.getColumnIndex(KEY_RATE)));
                        quoteBean.setPuc(cursor.getString(cursor.getColumnIndex(KEY_PUC)));
                        quoteBean.setInsurance(cursor.getString(cursor.getColumnIndex(KEY_INSURANCE)));
                        quoteBean.setQuote_status(cursor.getString(cursor.getColumnIndex(KEY_QUOTE_STATUS)));
                        quoteBean.setLicence(cursor.getString(cursor.getColumnIndex(KEY_LICENCE)));
                        quoteBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        quoteBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));
                        quotelist.add(quoteBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return quotelist;
    }

    public void insertRouteData(String key, RouteBean routeBean) {
        long i = 0;
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            String where = " ";
            switch (key) {
                case KEY_INSERT_ROUTE:

                    values.put(KEY_FR_STATE_TEXT, routeBean.getFr_state());
                    values.put(KEY_FR_DISTRICT_TEXT, routeBean.getFr_district());
                    values.put(KEY_FR_TEHSIL_TEXT, routeBean.getFr_tehsil());
                    values.put(KEY_TO_STATE_TEXT, routeBean.getTo_state());
                    values.put(KEY_TO_DISTRICT_TEXT, routeBean.getTo_district());
                    values.put(KEY_TO_TEHSIL_TEXT, routeBean.getTo_tehsil());
                    values.put(KEY_TR_MODE, routeBean.getTr_mode());
                    values.put(KEY_DISTANCE, routeBean.getDistance());
                    values.put(KEY_STATUS, routeBean.getStatus());
                    values.put(KEY_RES_PERNR, routeBean.getRes_pernr());
                    values.put(KEY_RES_PERNR_TYPE, routeBean.getRes_pernr_type());

                    i = db.insert(TABLE_ROUTE, null, values);
                    break;

                case KEY_UPDATE_ROUTE_FROM_SAP:

                    values.put(KEY_FR_STATE_TEXT, routeBean.getFr_state());
                    values.put(KEY_FR_DISTRICT_TEXT, routeBean.getFr_district());
                    values.put(KEY_FR_TEHSIL_TEXT, routeBean.getFr_tehsil());
                    values.put(KEY_TO_STATE_TEXT, routeBean.getTo_state());
                    values.put(KEY_TO_DISTRICT_TEXT, routeBean.getTo_district());
                    values.put(KEY_TO_TEHSIL_TEXT, routeBean.getTo_tehsil());
                    values.put(KEY_TR_MODE, routeBean.getTr_mode());
                    values.put(KEY_DISTANCE, routeBean.getDistance());
                    values.put(KEY_STATUS, routeBean.getStatus());

                    where = KEY_FR_TEHSIL_TEXT + " = '" + routeBean.getFr_tehsil() + "'" + " AND "
                            + KEY_TO_TEHSIL_TEXT + " = '" + routeBean.getTo_tehsil() + "'";

                    i = db.update(TABLE_ROUTE, values, where, null);
                    break;
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertDeliveryDeliveredData(DeliveryDeliveredInput deliveryDeliveredInput) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_DELIVERY_RFQ_DOC, deliveryDeliveredInput.getRfqDoc());
            values.put(KEY_DRIVER_MOB, deliveryDeliveredInput.getDriverMob());
            values.put(KEY_TRANSPORT_COD, deliveryDeliveredInput.getResPernr());
            values.put(KEY_BILL_NO, deliveryDeliveredInput.getBillNo());
            values.put(KEY_TRANS_NO, deliveryDeliveredInput.getTransNo());
            values.put(KEY_ASSIGN_NO, deliveryDeliveredInput.getAssignMob());
            values.put(KEY_DELIVERY_PHOTO1, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO2, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO3, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO4, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO5, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO6, deliveryDeliveredInput.getPhoto1());

            i = db.insert(TABLE_ASSIGNED_DELIVERY_DELIVERED, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateDeliveryDeliveredData(DeliveryDeliveredInput deliveryDeliveredInput) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        try {
            String where = " ";
            values = new ContentValues();
            values.put(KEY_DELIVERY_RFQ_DOC, deliveryDeliveredInput.getRfqDoc());
            values.put(KEY_DRIVER_MOB, deliveryDeliveredInput.getDriverMob());
            values.put(KEY_TRANSPORT_COD, deliveryDeliveredInput.getResPernr());
            values.put(KEY_BILL_NO, deliveryDeliveredInput.getBillNo());
            values.put(KEY_TRANS_NO, deliveryDeliveredInput.getTransNo());
            values.put(KEY_ASSIGN_NO, deliveryDeliveredInput.getAssignMob());
            values.put(KEY_DELIVERY_PHOTO1, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO2, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO3, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO4, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO5, deliveryDeliveredInput.getPhoto1());
            values.put(KEY_DELIVERY_PHOTO6, deliveryDeliveredInput.getPhoto1());

            where = KEY_DELIVERY_RFQ_DOC + "='" + deliveryDeliveredInput.getRfqDoc() + "'";
            i = db.update(TABLE_ASSIGNED_DELIVERY_DELIVERED, values, where, null);

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @SuppressLint("Range")
    public ArrayList<DeliveryDeliveredInput> getDeliveredData() {
        ArrayList<DeliveryDeliveredInput> deliveryDeliveredInputArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_ASSIGNED_DELIVERY_DELIVERED;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        DeliveryDeliveredInput deliveryDeliveredInput = new DeliveryDeliveredInput();
                        deliveryDeliveredInput.setRfqDoc(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_RFQ_DOC)));
                        deliveryDeliveredInput.setResPernr(cursor.getString(cursor.getColumnIndex(KEY_TRANSPORT_COD)));
                        deliveryDeliveredInput.setDriverMob(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_MOB)));
                        deliveryDeliveredInput.setBillNo(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        deliveryDeliveredInput.setTransNo(cursor.getString(cursor.getColumnIndex(KEY_TRANS_NO)));
                        deliveryDeliveredInput.setAssignMob(cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_NO)));
                        deliveryDeliveredInput.setPhoto1(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO1)));
                        deliveryDeliveredInput.setPhoto2(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO2)));
                        deliveryDeliveredInput.setPhoto3(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO3)));
                        deliveryDeliveredInput.setPhoto4(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO4)));
                        deliveryDeliveredInput.setPhoto5(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO5)));
                        deliveryDeliveredInput.setPhoto6(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO6)));

                        deliveryDeliveredInputArrayList.add(deliveryDeliveredInput);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return deliveryDeliveredInputArrayList;
    }

    @SuppressLint("Range")
    public DeliveryDeliveredInput getDeliveredDetail(String rfqCode) {
        DeliveryDeliveredInput deliveryDeliveredInput = new DeliveryDeliveredInput();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_ASSIGNED_DELIVERY_DELIVERED + " WHERE " + KEY_ASSIGNED_DELIVERY_RFQ_DOC + " = '" + rfqCode + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    deliveryDeliveredInput.setRfqDoc(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_RFQ_DOC)));
                    deliveryDeliveredInput.setResPernr(cursor.getString(cursor.getColumnIndex(KEY_TRANSPORT_COD)));
                    deliveryDeliveredInput.setDriverMob(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_MOB)));
                    deliveryDeliveredInput.setBillNo(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                    deliveryDeliveredInput.setTransNo(cursor.getString(cursor.getColumnIndex(KEY_TRANS_NO)));
                    deliveryDeliveredInput.setAssignMob(cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_NO)));
                    deliveryDeliveredInput.setPhoto1(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO1)));
                    deliveryDeliveredInput.setPhoto2(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO2)));
                    deliveryDeliveredInput.setPhoto3(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO3)));
                    deliveryDeliveredInput.setPhoto4(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO4)));
                    deliveryDeliveredInput.setPhoto5(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO5)));
                    deliveryDeliveredInput.setPhoto6(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PHOTO6)));
                    cursor.moveToNext();
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return deliveryDeliveredInput;
    }

    public void insertAssignedDeliveriesData(ArrayList<AssignedDeliveryDetailResponse> assignedDeliveryDetailResponse) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        CustomUtility customUtility = new CustomUtility();

        try {
            for (int p = 0; p < assignedDeliveryDetailResponse.size(); p++) {
                try {
                    values = new ContentValues();
                    values.put(KEY_ASSIGNED_DELIVERY_RFQ_DOC, assignedDeliveryDetailResponse.get(p).getRfqDoc());
                    values.put(KEY_ASSIGNED_DELIVERY_VEHICLE_RC, assignedDeliveryDetailResponse.get(p).getVehicleRc());
                    values.put(KEY_ASSIGNED_DELIVERY_CONF_DATE, assignedDeliveryDetailResponse.get(p).getConfDate());
                    values.put(KEY_ASSIGNED_DELIVERY_CONF_TIME, assignedDeliveryDetailResponse.get(p).getConfTime());
                    values.put(KEY_ASSIGNED_DELIVERY_CONFIRMED, assignedDeliveryDetailResponse.get(p).getConfirmed());
                    values.put(KEY_ASSIGNED_DELIVERY_RES_PERNR, assignedDeliveryDetailResponse.get(p).getTransportCode());
                    values.put(KEY_ASSIGNED_DELIVERY_TRANSPORTER_NAME, assignedDeliveryDetailResponse.get(p).getTransporterName());
                    values.put(KEY_ASSIGNED_DELIVERY_FR_LOCATION, assignedDeliveryDetailResponse.get(p).getFrLocation());
                    values.put(KEY_ASSIGNED_DELIVERY_TO_LOCATION, assignedDeliveryDetailResponse.get(p).getToLocation());
                    values.put(KEY_ASSIGNED_DELIVERY_VIA_ADDRESS, assignedDeliveryDetailResponse.get(p).getViaAddress());
                    values.put(KEY_ASSIGNED_DELIVERY_VEHICLE_TYPE, assignedDeliveryDetailResponse.get(p).getVehicleType());
                    values.put(KEY_ASSIGNED_DELIVERY_DISTANCE, assignedDeliveryDetailResponse.get(p).getDistance());
                    values.put(KEY_ASSIGNED_DELIVERY_DELIVERY_PLACE, assignedDeliveryDetailResponse.get(p).getDeliveryPlace());
                    values.put(KEY_ASSIGNED_DELIVERY_TRANSIT_TIME, assignedDeliveryDetailResponse.get(p).getTransitTime());
                    values.put(KEY_ASSIGNED_DELIVERY_RFQ_DATE, assignedDeliveryDetailResponse.get(p).getRfqDate());
                    values.put(KEY_ASSIGNED_DELIVERY_RFQ_TIME, assignedDeliveryDetailResponse.get(p).getRfqTime());
                    values.put(KEY_BILL_NO, assignedDeliveryDetailResponse.get(p).getBillNo());
                    values.put(KEY_TRANS_NO, assignedDeliveryDetailResponse.get(p).getTransNo());
                    values.put(KEY_ASSIGN_NO, assignedDeliveryDetailResponse.get(p).getAssignMob());
                    values.put(KEY_BOOK_DATE,assignedDeliveryDetailResponse.get(p).getzBookDate());
                    values.put(KEY_LR_NO,assignedDeliveryDetailResponse.get(p).getzLRno());
                    values.put(KEY_MOBILE,assignedDeliveryDetailResponse.get(p).getzMobile());


                    db.insert(TABLE_ASSIGNED_DELIVERIES, null, values);


                } catch (SQLiteException e) {
                    e.printStackTrace();
                }

            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertPartialLoadData(ArrayList<PartialLoadResponse> partialLoadResponseArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;

        try {
            for (int p = 0; p < partialLoadResponseArrayList.size(); p++) {
                    values = new ContentValues();
                    values.put(KEY_PARTIAL_LOAD_ZDOC_DOC, partialLoadResponseArrayList.get(p).getZdocNo());
                    values.put(KEY_PARTIAL_LOAD_ZDOC_DATE, partialLoadResponseArrayList.get(p).getZdocdate());
                    values.put(KEY_PARTIAL_LOAD_ZLRNO, partialLoadResponseArrayList.get(p).getZlrno());
                    values.put(KEY_PARTIAL_LOAD_TRANSPORTER_MOB, partialLoadResponseArrayList.get(p).getTransporterMob());
                    values.put(KEY_PARTIAL_LOAD_WERKS, partialLoadResponseArrayList.get(p).getWerks());
                    values.put(KEY_PARTIAL_LOAD_KUNAG, partialLoadResponseArrayList.get(p).getKunag());
                    values.put(KEY_PARTIAL_LOAD_ZBOOK_DATE, partialLoadResponseArrayList.get(p).getZbookdate());
                    values.put(KEY_PARTIAL_LOAD_VBELN, partialLoadResponseArrayList.get(p).getVbeln());
                    values.put(KEY_PARTIAL_LOAD_ZMOBILENO, partialLoadResponseArrayList.get(p).getZmobileno());
                    values.put(KEY_PARTIAL_LOAD_ZTRANSNAME, partialLoadResponseArrayList.get(p).getZtransname());
                    values.put(KEY_PARTIAL_LOAD_CUST_NAME, partialLoadResponseArrayList.get(p).getCustName());
                    values.put(KEY_PARTIAL_LOAD_ZDELIVERY, partialLoadResponseArrayList.get(p).getZdelivery());
                    values.put(KEY_PARTIAL_LOAD_ZDELIVERY_TO, partialLoadResponseArrayList.get(p).getZdeliverdTo());
                    values.put(KEY_PARTIAL_LOAD_FKDATE, partialLoadResponseArrayList.get(p).getFkdat());
                    values.put(KEY_PARTIAL_LOAD_VKORG, partialLoadResponseArrayList.get(p).getVkorg());
                    values.put(KEY_PARTIAL_LOAD_STATUS, partialLoadResponseArrayList.get(p).getStatus());

                db.insert(TABLE_PARTIAL_LOAD, null, values);


            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("Error====>", e.toString());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateRfqData(String key, RfqBean rfqBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        CustomUtility customUtility = new CustomUtility();
        LoginBean loginBean = new LoginBean();
        try {
            values = new ContentValues();
            String where = " ";
            switch (key) {
                case KEY_INSERT_RFQ:
                    values.put(KEY_RFQ_DOC, rfqBean.getRfq_doc());
                    values.put(KEY_RFQ_DOC_MANUAL, rfqBean.getRfq_doc_manual());
                    values.put(KEY_FR_LATLONG, rfqBean.getFr_latlong());
                    values.put(KEY_FR_LOCATION, rfqBean.getFr_location());
                    values.put(KEY_TO_LOCATION, rfqBean.getTo_location());
                    values.put(KEY_TO_LATLONG, rfqBean.getTo_latlong());
                    values.put(KEY_VIA_ADDRESS, rfqBean.getVia_address());
                    values.put(KEY_VIA_ADDRESS_LATLONG, rfqBean.getVia_address_latlong());
                    values.put(KEY_DISTANCE, rfqBean.getDistance());
                    values.put(KEY_TR_MODE, rfqBean.getTr_mode());
                    values.put(KEY_DELIVERY_PLACE, rfqBean.getDelivery_place());
                    values.put(KEY_TRANSIT_TIME, rfqBean.getTransit_time());
                    values.put(KEY_VEHICLE_TYPE, rfqBean.getVehicle_type());
                    values.put(KEY_VEHICLE_WEIGHT, rfqBean.getVehicle_weight());
                    values.put(KEY_ADD4, rfqBean.getFinal_rate());
                    values.put(KEY_RFQ_DATE, rfqBean.getRfq_date());
                    values.put(KEY_RFQ_TIME, rfqBean.getRfq_time());
                    values.put(KEY_EXPIRY_DATE, rfqBean.getExpiry_date());
                    values.put(KEY_EXPIRY_TIME, rfqBean.getExpiry_time());
                    values.put(KEY_ADD1, rfqBean.getDala());
                    values.put(KEY_ADD2, rfqBean.getHeight());
                    values.put(KEY_ADD3, rfqBean.getPernr_name());
                    values.put(KEY_RFQ_COMPLETED, rfqBean.getRfq_completed());
                    values.put(KEY_RFQ_STATUS, rfqBean.getRfq_status());
                    values.put(KEY_RES_PERNR, rfqBean.getRes_pernr());
                    values.put(KEY_RES_PERNR_TYPE, rfqBean.getRes_pernr_type());
                    values.put(KEY_SYNC, rfqBean.getSync());
                    values.put(KEY_CONFIRMED, rfqBean.getConfirmed());
                    values.put(KEY_VEH_RC, rfqBean.getVehicle_rc());

                    i = db.insert(TABLE_RFQ, null, values);
                    break;

                case KEY_RFQ_UPDATE_FROM_SAP:

                    values.put(KEY_RFQ_DOC, rfqBean.getRfq_doc());
                    values.put(KEY_RFQ_DOC_MANUAL, rfqBean.getRfq_doc_manual());
                    values.put(KEY_FR_LATLONG, rfqBean.getFr_latlong());
                    values.put(KEY_FR_LOCATION, rfqBean.getFr_location());
                    values.put(KEY_TO_LOCATION, rfqBean.getTo_location());
                    values.put(KEY_TO_LATLONG, rfqBean.getTo_latlong());
                    values.put(KEY_VIA_ADDRESS, rfqBean.getVia_address());
                    values.put(KEY_VIA_ADDRESS_LATLONG, rfqBean.getVia_address_latlong());
                    values.put(KEY_DISTANCE, rfqBean.getDistance());
                    values.put(KEY_TR_MODE, rfqBean.getTr_mode());
                    values.put(KEY_DELIVERY_PLACE, rfqBean.getDelivery_place());
                    values.put(KEY_TRANSIT_TIME, rfqBean.getTransit_time());
                    values.put(KEY_VEHICLE_TYPE, rfqBean.getVehicle_type());
                    values.put(KEY_VEHICLE_WEIGHT, rfqBean.getVehicle_weight());
                    values.put(KEY_ADD4, rfqBean.getFinal_rate());
                    values.put(KEY_RFQ_DATE, rfqBean.getRfq_date());
                    values.put(KEY_RFQ_TIME, rfqBean.getRfq_time());
                    values.put(KEY_EXPIRY_DATE, rfqBean.getExpiry_date());
                    values.put(KEY_EXPIRY_TIME, rfqBean.getExpiry_time());
                    values.put(KEY_ADD1, rfqBean.getDala());
                    values.put(KEY_ADD2, rfqBean.getHeight());
                    values.put(KEY_ADD3, rfqBean.getPernr_name());
                    values.put(KEY_RFQ_COMPLETED, rfqBean.getRfq_completed());
                    values.put(KEY_RFQ_STATUS, rfqBean.getRfq_status());
                    values.put(KEY_RES_PERNR, rfqBean.getRes_pernr());
                    values.put(KEY_RES_PERNR_TYPE, rfqBean.getRes_pernr_type());
                    values.put(KEY_RFQ_COMPLETED, rfqBean.getRfq_completed());
                    values.put(KEY_SYNC, rfqBean.getSync());
                    values.put(KEY_CONFIRMED, rfqBean.getConfirmed());
                    values.put(KEY_VEH_RC, rfqBean.getVehicle_rc());

                    where = KEY_RFQ_DOC_MANUAL + "='" + rfqBean.getRfq_doc_manual() + "'";

                    i = db.update(TABLE_RFQ, values, where, null);

                case KEY_RFQ_SYNC:
                    values.put(KEY_RFQ_DOC, rfqBean.getRfq_doc());
                    values.put(KEY_RFQ_COMPLETED, rfqBean.getRfq_completed());
                    values.put(KEY_SYNC, rfqBean.getSync());
                    values.put(KEY_CONFIRMED, rfqBean.getConfirmed());
                    values.put(KEY_VEH_RC, rfqBean.getVehicle_rc());
                    where = KEY_RFQ_DOC_MANUAL + "='" + rfqBean.getRfq_doc_manual() + "'";
                    i = db.update(TABLE_RFQ, values, where, null);
            }
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateQuotationData(String key, QuotationBean quoteBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        CustomUtility customUtility = new CustomUtility();
        LoginBean loginBean = new LoginBean();

        try {
            values = new ContentValues();
            String where = " ";
            switch (key) {
                case KEY_INSERT_QUOTATION:
                    values.put(KEY_RFQ_DOC, quoteBean.getRfq_doc());
                    values.put(KEY_RFQ_DOC_MANUAL, quoteBean.getRfq_doc());
                    values.put(KEY_RATE, quoteBean.getRate());
                    values.put(KEY_REMARK, quoteBean.getRemark());
                    values.put(KEY_VEHICLE_MAKE, quoteBean.getVehicle_make());
                    values.put(KEY_VEHICLE_RC, quoteBean.getVehicle_rc());
                    values.put(KEY_PUC, quoteBean.getPuc());
                    values.put(KEY_INSURANCE, quoteBean.getInsurance());
                    values.put(KEY_LICENCE, quoteBean.getLicence());
                    values.put(KEY_QUOTE_STATUS, quoteBean.getQuote_status());
                    values.put(KEY_QUOTE_COMPLETED, quoteBean.getQuote_completed());
                    values.put(KEY_RES_PERNR, quoteBean.getRes_pernr());
                    values.put(KEY_RES_PERNR_TYPE, quoteBean.getRes_pernr_type());
                    values.put(KEY_SYNC, quoteBean.getSync());

                    i = db.insert(TABLE_QUOTATION, null, values);
                    break;

                case KEY_QUOTE_SYNC:
                    values.put(KEY_RFQ_DOC, quoteBean.getRfq_doc());
                    values.put(KEY_RFQ_COMPLETED, quoteBean.getQuote_completed());
                    values.put(KEY_SYNC, quoteBean.getSync());

                    where = KEY_RFQ_DOC + "='" + quoteBean.getRfq_doc() + "'";

                    i = db.update(TABLE_QUOTATION, values, where, null);

                case KEY_QUOTATION_UPDATE_FROM_SAP:
                    values.put(KEY_RFQ_DOC, quoteBean.getRfq_doc());
                    values.put(KEY_RFQ_DOC_MANUAL, quoteBean.getRfq_doc());
                    values.put(KEY_RATE, quoteBean.getRate());
                    values.put(KEY_REMARK, quoteBean.getRemark());
                    values.put(KEY_VEHICLE_MAKE, quoteBean.getVehicle_make());
                    values.put(KEY_VEHICLE_RC, quoteBean.getVehicle_rc());
                    values.put(KEY_PUC, quoteBean.getPuc());
                    values.put(KEY_INSURANCE, quoteBean.getInsurance());
                    values.put(KEY_LICENCE, quoteBean.getLicence());
                    values.put(KEY_QUOTE_STATUS, quoteBean.getQuote_status());
                    values.put(KEY_QUOTE_COMPLETED, quoteBean.getQuote_completed());
                    values.put(KEY_RES_PERNR, quoteBean.getRes_pernr());
                    values.put(KEY_RES_PERNR_TYPE, quoteBean.getRes_pernr_type());
                    values.put(KEY_SYNC, quoteBean.getSync());

                    where = KEY_RFQ_DOC + "='" + quoteBean.getRfq_doc() + "'";

                    i = db.update(TABLE_QUOTATION, values, where, null);
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public Boolean isSpinnerNotSelected(String key, String itemstring) {
        Boolean flag = false;
        if (!TextUtils.isEmpty(itemstring)) {
            switch (key) {
                case DatabaseHelper.KEY_FR_TEHSIL_TEXT:
                    if (itemstring.equals("Select From Tehsil")) {
                        flag = true;
                    }
                    break;

                case DatabaseHelper.KEY_TO_TEHSIL_TEXT:
                    if (itemstring.equals("Select To Tehsil")) {
                        flag = true;
                    }
                    break;

                case DatabaseHelper.KEY_TR_MODE:
                    if (itemstring.equals("Mode of Transport")) {
                        flag = true;
                    }
                    break;

            }
        } else {
            flag = true;
        }
        return flag;
    }

    @SuppressLint("Range")
    public RouteBean getRoute(String fr_tehsil, String to_tehsil) {

        RouteBean routeBean = new RouteBean();
        SQLiteDatabase db = this.getReadableDatabase();


        db.beginTransaction();
        try {


            String selectQuery = "SELECT  *  FROM " + DatabaseHelper.TABLE_ROUTE
                    + " WHERE " + DatabaseHelper.KEY_FR_TEHSIL_TEXT + " = '" + fr_tehsil + "'"
                    + " AND " + DatabaseHelper.KEY_TO_TEHSIL_TEXT + " = '" + to_tehsil + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {

                        routeBean.setFr_state(cursor.getString(cursor.getColumnIndex(KEY_FR_STATE_TEXT)));
                        routeBean.setFr_district(cursor.getString(cursor.getColumnIndex(KEY_FR_DISTRICT_TEXT)));
                        routeBean.setFr_tehsil(cursor.getString(cursor.getColumnIndex(KEY_FR_TEHSIL_TEXT)));
                        routeBean.setTo_state(cursor.getString(cursor.getColumnIndex(KEY_TO_STATE_TEXT)));
                        routeBean.setTo_district(cursor.getString(cursor.getColumnIndex(KEY_TO_DISTRICT_TEXT)));
                        routeBean.setTo_tehsil(cursor.getString(cursor.getColumnIndex(KEY_TO_TEHSIL_TEXT)));
                        routeBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        routeBean.setTr_mode(cursor.getString(cursor.getColumnIndex(KEY_TR_MODE)));

                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return routeBean;
    }

    @SuppressLint("Range")
    public RfqBean getRfqInformation(String rfq_docno) {
        RfqBean rfqBean = new RfqBean();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String selectQuery = null;
        db.beginTransaction();
        try {
            selectQuery = "SELECT  *  FROM " + TABLE_RFQ + " WHERE " + KEY_RFQ_DOC + " = '" + rfq_docno + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        rfqBean = new RfqBean();

                        rfqBean.setRfq_doc(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC)));
                        rfqBean.setRfq_doc_manual(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC_MANUAL)));
                        rfqBean.setFr_location(cursor.getString(cursor.getColumnIndex(KEY_FR_LOCATION)));
                        rfqBean.setFr_latlong(cursor.getString(cursor.getColumnIndex(KEY_FR_LATLONG)));
                        rfqBean.setTo_location(cursor.getString(cursor.getColumnIndex(KEY_TO_LOCATION)));
                        rfqBean.setTo_latlong(cursor.getString(cursor.getColumnIndex(KEY_TO_LATLONG)));
                        rfqBean.setVia_address(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS)));
                        rfqBean.setVia_address_latlong(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS_LATLONG)));
                        rfqBean.setRfq_date(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DATE)));
                        rfqBean.setRfq_time(cursor.getString(cursor.getColumnIndex(KEY_RFQ_TIME)));
                        rfqBean.setDelivery_place(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PLACE)));
                        rfqBean.setTransit_time(cursor.getString(cursor.getColumnIndex(KEY_TRANSIT_TIME)));
                        rfqBean.setExpiry_date(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_DATE)));
                        rfqBean.setExpiry_time(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_TIME)));
                        rfqBean.setDala(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        rfqBean.setHeight(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        rfqBean.setPernr_name(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));
                        rfqBean.setVehicle_type(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_TYPE)));
                        rfqBean.setVehicle_weight(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_WEIGHT)));
                        rfqBean.setFinal_rate(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));
                        rfqBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        rfqBean.setTr_mode(cursor.getString(cursor.getColumnIndex(KEY_TR_MODE)));
                        rfqBean.setRfq_status(cursor.getString(cursor.getColumnIndex(KEY_RFQ_STATUS)));
                        rfqBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        rfqBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));
                        rfqBean.setConfirmed(cursor.getString(cursor.getColumnIndex(KEY_CONFIRMED)));
                        rfqBean.setVehicle_rc(cursor.getString(cursor.getColumnIndex(KEY_VEH_RC)));
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return rfqBean;
    }

    @SuppressLint("Range")
    public QuotationBean getQuotationInformation(String rfq_docno) {
        QuotationBean quoteBean = new QuotationBean();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String selectQuery = null;
        db.beginTransaction();

        try {
            selectQuery = "SELECT * FROM " + TABLE_QUOTATION + " WHERE " + KEY_RFQ_DOC + " = '" + rfq_docno + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        quoteBean = new QuotationBean();
                        quoteBean.setRfq_doc(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC)));
                        quoteBean.setVehicle_rc(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_RC)));
                        quoteBean.setVehicle_make(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_MAKE)));
                        quoteBean.setRemark(cursor.getString(cursor.getColumnIndex(KEY_REMARK)));
                        quoteBean.setRate(cursor.getString(cursor.getColumnIndex(KEY_RATE)));
                        quoteBean.setPuc(cursor.getString(cursor.getColumnIndex(KEY_PUC)));
                        quoteBean.setInsurance(cursor.getString(cursor.getColumnIndex(KEY_INSURANCE)));
                        quoteBean.setLicence(cursor.getString(cursor.getColumnIndex(KEY_LICENCE)));
                        quoteBean.setQuote_status(cursor.getString(cursor.getColumnIndex(KEY_QUOTE_STATUS)));
                        quoteBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        quoteBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return quoteBean;
    }

    @SuppressLint("Range")
    public ArrayList<AssignedDeliveryDetailResponse> getAssignedDeliveryListData() {
        ArrayList<AssignedDeliveryDetailResponse> assignedDeliveryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_ASSIGNED_DELIVERIES;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        AssignedDeliveryDetailResponse assignedDeliveryResponse = new AssignedDeliveryDetailResponse();
                        assignedDeliveryResponse.setRfqDoc(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_RFQ_DOC)));
                        assignedDeliveryResponse.setVehicleRc(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_VEHICLE_RC)));
                        assignedDeliveryResponse.setConfDate(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_CONF_DATE)));
                        assignedDeliveryResponse.setConfTime(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_CONF_TIME)));
                        assignedDeliveryResponse.setConfirmed(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_CONFIRMED)));
                        assignedDeliveryResponse.setTransportCode(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_RES_PERNR)));
                        assignedDeliveryResponse.setTransporterName(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_TRANSPORTER_NAME)));
                        assignedDeliveryResponse.setFrLocation(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_FR_LOCATION)));
                        assignedDeliveryResponse.setToLocation(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_TO_LOCATION)));
                        assignedDeliveryResponse.setViaAddress(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_VIA_ADDRESS)));
                        assignedDeliveryResponse.setVehicleType(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_VEHICLE_TYPE)));
                        assignedDeliveryResponse.setDistance(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_DISTANCE)));
                        assignedDeliveryResponse.setDeliveryPlace(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_DELIVERY_PLACE)));
                        assignedDeliveryResponse.setTransitTime(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_TRANSIT_TIME)));
                        assignedDeliveryResponse.setRfqDate(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_RFQ_DATE)));
                        assignedDeliveryResponse.setRfqTime(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_RFQ_TIME)));
                        assignedDeliveryResponse.setBillNo(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        assignedDeliveryResponse.setTransNo(cursor.getString(cursor.getColumnIndex(KEY_TRANS_NO)));
                        assignedDeliveryResponse.setAssignMob(cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_NO)));
                        assignedDeliveryResponse.setzLRno(cursor.getString(cursor.getColumnIndex(KEY_LR_NO)));
                        assignedDeliveryResponse.setzBookDate(cursor.getString(cursor.getColumnIndex(KEY_BOOK_DATE)));
                        assignedDeliveryResponse.setzMobile(cursor.getString(cursor.getColumnIndex(KEY_MOBILE)));
                        assignedDeliveryResponse.setViaAddress(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_VIA_ADDRESS)));

                        assignedDeliveryList.add(assignedDeliveryResponse);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return assignedDeliveryList;
    }

    @SuppressLint("Range")
    public ArrayList<PartialLoadResponse> getPartialLoadListData() {
        ArrayList<PartialLoadResponse> assignedDeliveryList = new ArrayList<PartialLoadResponse>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery =  "SELECT * FROM " + TABLE_PARTIAL_LOAD;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        PartialLoadResponse partialLoadResponse = new PartialLoadResponse();
                        partialLoadResponse.setZdocNo(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_ZDOC_DOC)));
                        partialLoadResponse.setZdocdate(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_ZDOC_DATE)));
                        partialLoadResponse.setZlrno(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_ZLRNO)));
                        partialLoadResponse.setTransporterMob(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_TRANSPORTER_MOB)));
                        partialLoadResponse.setWerks(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_WERKS)));
                        partialLoadResponse.setKunag(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_KUNAG)));
                        partialLoadResponse.setZbookdate(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_ZBOOK_DATE)));
                        partialLoadResponse.setVbeln(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_VBELN)));
                        partialLoadResponse.setZmobileno(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_ZMOBILENO)));
                        partialLoadResponse.setZtransname(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_ZTRANSNAME)));
                        partialLoadResponse.setCustName(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_CUST_NAME)));
                        partialLoadResponse.setZdelivery(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_ZDELIVERY)));
                        partialLoadResponse.setZdeliverdTo(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_ZDELIVERY_TO)));
                        partialLoadResponse.setFkdat(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_FKDATE)));
                        partialLoadResponse.setVkorg(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_VKORG)));
                        partialLoadResponse.setStatus(cursor.getString(cursor.getColumnIndex(KEY_PARTIAL_LOAD_STATUS)));
                        assignedDeliveryList.add(partialLoadResponse);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return assignedDeliveryList;
    }

    @SuppressLint("Range")
    public AssignedDeliveryDetailResponse getAssignedDeliveryDetail(String rfqCode) {
        AssignedDeliveryDetailResponse assignedDeliveryDetailResponse = null;
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_ASSIGNED_DELIVERIES + " WHERE " + KEY_ASSIGNED_DELIVERY_RFQ_DOC + " = '" + rfqCode + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
//                    while (!cursor.isAfterLast()) {
                    assignedDeliveryDetailResponse = new AssignedDeliveryDetailResponse();
                    assignedDeliveryDetailResponse.setRfqDoc(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_RFQ_DOC)));
                    assignedDeliveryDetailResponse.setVehicleRc(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_VEHICLE_RC)));
                    assignedDeliveryDetailResponse.setConfDate(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_CONF_DATE)));
                    assignedDeliveryDetailResponse.setConfTime(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_CONF_TIME)));
                    assignedDeliveryDetailResponse.setConfirmed(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_CONFIRMED)));
                    assignedDeliveryDetailResponse.setTransportCode(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_RES_PERNR)));
                    assignedDeliveryDetailResponse.setTransporterName(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_TRANSPORTER_NAME)));
                    assignedDeliveryDetailResponse.setFrLocation(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_FR_LOCATION)));
                    assignedDeliveryDetailResponse.setToLocation(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_TO_LOCATION)));
                    assignedDeliveryDetailResponse.setViaAddress(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_VIA_ADDRESS)));
                    assignedDeliveryDetailResponse.setVehicleType(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_VEHICLE_TYPE)));
                    assignedDeliveryDetailResponse.setDistance(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_DISTANCE)));
                    assignedDeliveryDetailResponse.setDeliveryPlace(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_DELIVERY_PLACE)));
                    assignedDeliveryDetailResponse.setTransitTime(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_TRANSIT_TIME)));
                    assignedDeliveryDetailResponse.setRfqDate(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_RFQ_DATE)));
                    assignedDeliveryDetailResponse.setRfqTime(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_RFQ_TIME)));
                    assignedDeliveryDetailResponse.setBillNo(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                    assignedDeliveryDetailResponse.setTransNo(cursor.getString(cursor.getColumnIndex(KEY_TRANS_NO)));
                    assignedDeliveryDetailResponse.setAssignMob(cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_NO)));
                    cursor.moveToNext();
//                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return assignedDeliveryDetailResponse;
    }

    @SuppressLint("Range")
    public ArrayList<RfqBean> getRfqList() {
        ArrayList<RfqBean> rfqlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_RFQ;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        RfqBean rfqBean = new RfqBean();
                        rfqBean.setRfq_doc(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC)));
                        rfqBean.setRfq_doc_manual(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC_MANUAL)));
                        rfqBean.setFr_location(cursor.getString(cursor.getColumnIndex(KEY_FR_LOCATION)));
                        rfqBean.setFr_latlong(cursor.getString(cursor.getColumnIndex(KEY_FR_LATLONG)));
                        rfqBean.setTo_location(cursor.getString(cursor.getColumnIndex(KEY_TO_LOCATION)));
                        rfqBean.setTo_latlong(cursor.getString(cursor.getColumnIndex(KEY_TO_LATLONG)));
                        rfqBean.setVia_address(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS)));
                        rfqBean.setVia_address_latlong(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS_LATLONG)));
                        rfqBean.setRfq_date(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DATE)));
                        rfqBean.setRfq_time(cursor.getString(cursor.getColumnIndex(KEY_RFQ_TIME)));
                        rfqBean.setDelivery_place(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PLACE)));
                        rfqBean.setTransit_time(cursor.getString(cursor.getColumnIndex(KEY_TRANSIT_TIME)));
                        rfqBean.setExpiry_date(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_DATE)));
                        rfqBean.setExpiry_time(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_TIME)));
                        rfqBean.setDala(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        rfqBean.setHeight(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        rfqBean.setPernr_name(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));
                        rfqBean.setVehicle_type(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_TYPE)));
                        rfqBean.setVehicle_weight(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_WEIGHT)));
                        rfqBean.setFinal_rate(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));
                        rfqBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        rfqBean.setTr_mode(cursor.getString(cursor.getColumnIndex(KEY_TR_MODE)));
                        rfqBean.setRfq_status(cursor.getString(cursor.getColumnIndex(KEY_RFQ_STATUS)));
                        rfqBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        rfqBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));
                        rfqBean.setConfirmed(cursor.getString(cursor.getColumnIndex(KEY_CONFIRMED)));
                        rfqBean.setVehicle_rc(cursor.getString(cursor.getColumnIndex(KEY_VEH_RC)));
                        rfqlist.add(rfqBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return rfqlist;
    }

    @SuppressLint("Range")
    public ArrayList<RfqBean> getRfqcnfrmList() {
        ArrayList<RfqBean> rfqlist = new ArrayList<RfqBean>();
        rfqlist.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        String pending = "";
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery = "SELECT  *  FROM " + TABLE_RFQ + " WHERE " + KEY_CONFIRMED + " = '" + 'X' + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        RfqBean rfqBean = new RfqBean();
                        rfqBean.setRfq_doc(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC)));
                        rfqBean.setRfq_doc_manual(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC_MANUAL)));
                        rfqBean.setFr_location(cursor.getString(cursor.getColumnIndex(KEY_FR_LOCATION)));
                        rfqBean.setFr_latlong(cursor.getString(cursor.getColumnIndex(KEY_FR_LATLONG)));
                        rfqBean.setTo_location(cursor.getString(cursor.getColumnIndex(KEY_TO_LOCATION)));
                        rfqBean.setTo_latlong(cursor.getString(cursor.getColumnIndex(KEY_TO_LATLONG)));
                        rfqBean.setVia_address(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS)));
                        rfqBean.setVia_address_latlong(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS_LATLONG)));
                        rfqBean.setRfq_date(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DATE)));
                        rfqBean.setRfq_time(cursor.getString(cursor.getColumnIndex(KEY_RFQ_TIME)));
                        rfqBean.setDelivery_place(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PLACE)));
                        rfqBean.setTransit_time(cursor.getString(cursor.getColumnIndex(KEY_TRANSIT_TIME)));
                        rfqBean.setExpiry_date(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_DATE)));
                        rfqBean.setExpiry_time(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_TIME)));
                        rfqBean.setDala(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        rfqBean.setHeight(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        rfqBean.setPernr_name(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));
                        rfqBean.setVehicle_type(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_TYPE)));
                        rfqBean.setVehicle_weight(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_WEIGHT)));
                        rfqBean.setFinal_rate(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));
                        rfqBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        rfqBean.setTr_mode(cursor.getString(cursor.getColumnIndex(KEY_TR_MODE)));
                        rfqBean.setRfq_status(cursor.getString(cursor.getColumnIndex(KEY_RFQ_STATUS)));
                        rfqBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        rfqBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));
                        rfqBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_CONFIRMED)));
                        rfqBean.setVehicle_rc(cursor.getString(cursor.getColumnIndex(KEY_VEH_RC)));
                        rfqlist.add(rfqBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return rfqlist;
    }

    @SuppressLint("Range")
    public ArrayList<RfqBean> getRfqappList() {
        ArrayList<RfqBean> rfqlist = new ArrayList<RfqBean>();
        rfqlist.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        String pending = "";
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery = "SELECT  *  FROM " + TABLE_RFQ + " WHERE " + KEY_CONFIRMED + " != '" + 'X' + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        RfqBean rfqBean = new RfqBean();
                        rfqBean.setRfq_doc(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC)));
                        rfqBean.setRfq_doc_manual(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC_MANUAL)));
                        rfqBean.setFr_location(cursor.getString(cursor.getColumnIndex(KEY_FR_LOCATION)));
                        rfqBean.setFr_latlong(cursor.getString(cursor.getColumnIndex(KEY_FR_LATLONG)));
                        rfqBean.setTo_location(cursor.getString(cursor.getColumnIndex(KEY_TO_LOCATION)));
                        rfqBean.setTo_latlong(cursor.getString(cursor.getColumnIndex(KEY_TO_LATLONG)));
                        rfqBean.setVia_address(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS)));
                        rfqBean.setVia_address_latlong(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS_LATLONG)));
                        rfqBean.setRfq_date(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DATE)));
                        rfqBean.setRfq_time(cursor.getString(cursor.getColumnIndex(KEY_RFQ_TIME)));
                        rfqBean.setDelivery_place(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PLACE)));
                        rfqBean.setTransit_time(cursor.getString(cursor.getColumnIndex(KEY_TRANSIT_TIME)));
                        rfqBean.setExpiry_date(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_DATE)));
                        rfqBean.setExpiry_time(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_TIME)));

                        rfqBean.setDala(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        rfqBean.setHeight(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));

                        rfqBean.setPernr_name(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));

                        rfqBean.setVehicle_type(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_TYPE)));
                        rfqBean.setVehicle_weight(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_WEIGHT)));
                        rfqBean.setFinal_rate(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));

                        rfqBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        rfqBean.setTr_mode(cursor.getString(cursor.getColumnIndex(KEY_TR_MODE)));

                        rfqBean.setRfq_status(cursor.getString(cursor.getColumnIndex(KEY_RFQ_STATUS)));
                        rfqBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        rfqBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));
                        rfqBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_CONFIRMED)));
                        rfqBean.setVehicle_rc(cursor.getString(cursor.getColumnIndex(KEY_VEH_RC)));


                        rfqlist.add(rfqBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return rfqlist;


    }


    @SuppressLint("Range")
    public ArrayList<RfqBean> getRfqList(String key) {

        String complete = "X";
        String pending = "";

        ArrayList<RfqBean> list_rfq = new ArrayList<>();

        list_rfq.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;

        db.beginTransaction();
        try {

            switch (key) {
                case KEY_RFQ_COMPLETED:
                    selectQuery = "SELECT  *  FROM " + TABLE_RFQ + " WHERE " + KEY_RFQ_COMPLETED + " = '" + complete + "'";
                    break;
                case KEY_RFQ_PENDING:

                    selectQuery = "SELECT  *  FROM " + TABLE_RFQ + " WHERE " + KEY_SYNC + " IS NULL OR "
                            + KEY_SYNC + " = '" + pending + "'";

                    break;
            }


            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        RfqBean rfqBean = new RfqBean();


                        //general info data
                        rfqBean.setRfq_doc(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC)));
                        rfqBean.setRfq_doc_manual(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC_MANUAL)));
                        rfqBean.setFr_location(cursor.getString(cursor.getColumnIndex(KEY_FR_LOCATION)));
                        rfqBean.setFr_latlong(cursor.getString(cursor.getColumnIndex(KEY_FR_LATLONG)));
                        rfqBean.setTo_location(cursor.getString(cursor.getColumnIndex(KEY_TO_LOCATION)));
                        rfqBean.setTo_latlong(cursor.getString(cursor.getColumnIndex(KEY_TO_LATLONG)));
                        rfqBean.setVia_address(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS)));
                        rfqBean.setVia_address_latlong(cursor.getString(cursor.getColumnIndex(KEY_VIA_ADDRESS_LATLONG)));
                        rfqBean.setRfq_date(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DATE)));
                        rfqBean.setRfq_time(cursor.getString(cursor.getColumnIndex(KEY_RFQ_TIME)));
                        rfqBean.setDelivery_place(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PLACE)));
                        rfqBean.setTransit_time(cursor.getString(cursor.getColumnIndex(KEY_TRANSIT_TIME)));
                        rfqBean.setExpiry_date(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_DATE)));
                        rfqBean.setExpiry_time(cursor.getString(cursor.getColumnIndex(KEY_EXPIRY_TIME)));
                        rfqBean.setDala(cursor.getString(cursor.getColumnIndex(KEY_ADD1)));
                        rfqBean.setHeight(cursor.getString(cursor.getColumnIndex(KEY_ADD2)));
                        rfqBean.setPernr_name(cursor.getString(cursor.getColumnIndex(KEY_ADD3)));
                        rfqBean.setVehicle_type(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_TYPE)));
                        rfqBean.setVehicle_weight(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_WEIGHT)));
                        rfqBean.setFinal_rate(cursor.getString(cursor.getColumnIndex(KEY_ADD4)));
                        rfqBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        rfqBean.setTr_mode(cursor.getString(cursor.getColumnIndex(KEY_TR_MODE)));
                        rfqBean.setRfq_status(cursor.getString(cursor.getColumnIndex(KEY_RFQ_STATUS)));
                        rfqBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        rfqBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));
                        rfqBean.setConfirmed(cursor.getString(cursor.getColumnIndex(KEY_CONFIRMED)));
                        rfqBean.setVehicle_rc(cursor.getString(cursor.getColumnIndex(KEY_VEH_RC)));

                        list_rfq.add(rfqBean);
                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return list_rfq;
    }


    @SuppressLint("Range")
    public ArrayList<QuotationBean> getQuoteList(String key) {

        String complete = "X";
        String pending = "";

        ArrayList<QuotationBean> list_quote = new ArrayList<>();

        list_quote.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;

        db.beginTransaction();
        try {

            switch (key) {
                case KEY_QUOTE_COMPLETED:
                    selectQuery = "SELECT  *  FROM " + TABLE_QUOTATION + " WHERE " + KEY_QUOTE_COMPLETED + " = '" + complete + "'";
                    break;
                case KEY_QUOTE_PENDING:

                    selectQuery = "SELECT  *  FROM " + TABLE_QUOTATION + " WHERE " + KEY_SYNC + " IS NULL OR "
                            + KEY_SYNC + " = '" + pending + "'";

                    break;
            }


            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        QuotationBean quoteBean = new QuotationBean();

                        quoteBean.setRfq_doc(cursor.getString(cursor.getColumnIndex(KEY_RFQ_DOC)));
                        quoteBean.setVehicle_make(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_MAKE)));
                        quoteBean.setVehicle_rc(cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_RC)));
                        quoteBean.setRemark(cursor.getString(cursor.getColumnIndex(KEY_REMARK)));
                        quoteBean.setRate(cursor.getString(cursor.getColumnIndex(KEY_RATE)));
                        quoteBean.setPuc(cursor.getString(cursor.getColumnIndex(KEY_PUC)));
                        quoteBean.setQuote_completed(cursor.getString(cursor.getColumnIndex(KEY_QUOTE_COMPLETED)));
                        quoteBean.setQuote_status(cursor.getString(cursor.getColumnIndex(KEY_QUOTE_STATUS)));
                        quoteBean.setInsurance(cursor.getString(cursor.getColumnIndex(KEY_INSURANCE)));
                        quoteBean.setLicence(cursor.getString(cursor.getColumnIndex(KEY_LICENCE)));
                        quoteBean.setRes_pernr(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR)));
                        quoteBean.setRes_pernr_type(cursor.getString(cursor.getColumnIndex(KEY_RES_PERNR_TYPE)));


                        list_quote.add(quoteBean);
                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return list_quote;
    }

    public void insertDocumentData(DocumentBean documentBean) {

        long i;

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();

            values.put(KEY_INST_DOC, documentBean.getInst_docno());
            values.put(KEY_DOC_DATE, documentBean.getDoc_date());
            values.put(KEY_LAT, documentBean.getLatitude());
            values.put(KEY_LONG, documentBean.getLongitude());
            values.put(KEY_PHOTO_1, documentBean.getPhoto_1());
            values.put(KEY_PHOTO_2, documentBean.getPhoto_2());
            values.put(KEY_PHOTO_3, documentBean.getPhoto_3());
            values.put(KEY_PHOTO_4, documentBean.getPhoto_4());
            values.put(KEY_PHOTO_5, documentBean.getPhoto_5());
            values.put(KEY_PHOTO_6, documentBean.getPhoto_6());
            values.put(KEY_PHOTO_7, documentBean.getPhoto_7());
            values.put(KEY_PHOTO_8, documentBean.getPhoto_8());
            values.put(KEY_PHOTO_9, documentBean.getPhoto_9());
            values.put(KEY_PHOTO_10, documentBean.getPhoto_10());


            // Insert Row
            i = db.insert(TABLE_VERIFICATION, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    public void updateDocumentData(DocumentBean documentBean) {

        long i;

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();

            values.put(KEY_INST_DOC, documentBean.getInst_docno());
            values.put(KEY_DOC_DATE, documentBean.getDoc_date());
            values.put(KEY_LAT, documentBean.getLatitude());
            values.put(KEY_LONG, documentBean.getLongitude());
            values.put(KEY_PHOTO_1, documentBean.getPhoto_1());
            values.put(KEY_PHOTO_2, documentBean.getPhoto_2());
            values.put(KEY_PHOTO_3, documentBean.getPhoto_3());
            values.put(KEY_PHOTO_4, documentBean.getPhoto_4());
            values.put(KEY_PHOTO_5, documentBean.getPhoto_5());
            values.put(KEY_PHOTO_6, documentBean.getPhoto_6());
            values.put(KEY_PHOTO_7, documentBean.getPhoto_7());
            values.put(KEY_PHOTO_8, documentBean.getPhoto_8());
            values.put(KEY_PHOTO_9, documentBean.getPhoto_9());
            values.put(KEY_PHOTO_10, documentBean.getPhoto_10());


            where = KEY_INST_DOC + "='" + documentBean.getInst_docno() + "'";

            i = db.update(TABLE_VERIFICATION, values, where, null);

            Log.e("DATA", "&&&&" + db.update(TABLE_VERIFICATION, values, where, null));

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    @SuppressLint("Range")
    public ArrayList<DocumentBean> getDocumentInformation1(String enq_docno) {

        DocumentBean documentBean = new DocumentBean();
        ArrayList<DocumentBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {

            String selectQuery = "SELECT  *  FROM " + TABLE_VERIFICATION + " WHERE " + KEY_INST_DOC + " = '" + enq_docno + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        documentBean = new DocumentBean();

                        documentBean.setInst_docno(cursor.getString(cursor.getColumnIndex(KEY_INST_DOC)));
                        documentBean.setDoc_date(cursor.getString(cursor.getColumnIndex(KEY_DOC_DATE)));
                        documentBean.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LAT)));
                        documentBean.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONG)));
                        documentBean.setPhoto_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                        documentBean.setPhoto_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                        documentBean.setPhoto_3(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_3)));
                        documentBean.setPhoto_4(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_4)));
                        documentBean.setPhoto_5(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_5)));
                        documentBean.setPhoto_6(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_6)));
                        documentBean.setPhoto_7(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_7)));
                        documentBean.setPhoto_8(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_8)));
                        documentBean.setPhoto_9(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_9)));
                        documentBean.setPhoto_10(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_10)));
                        list_document.add(documentBean);


                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return list_document;
    }

    @SuppressLint("Range")
    public DocumentBean getDocumentInformation(String enq_docno) {

        DocumentBean documentBean = new DocumentBean();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {

            String selectQuery = "SELECT  *  FROM " + TABLE_VERIFICATION + " WHERE " + KEY_INST_DOC + "='" + enq_docno + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        documentBean = new DocumentBean();

                        documentBean.setInst_docno(cursor.getString(cursor.getColumnIndex(KEY_INST_DOC)));
                        documentBean.setDoc_date(cursor.getString(cursor.getColumnIndex(KEY_DOC_DATE)));
                        documentBean.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LAT)));
                        documentBean.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONG)));
                        documentBean.setPhoto_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                        documentBean.setPhoto_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                        documentBean.setPhoto_3(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_3)));
                        documentBean.setPhoto_4(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_4)));
                        documentBean.setPhoto_5(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_5)));
                        documentBean.setPhoto_6(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_6)));
                        documentBean.setPhoto_7(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_7)));
                        documentBean.setPhoto_8(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_8)));
                        documentBean.setPhoto_9(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_9)));
                        documentBean.setPhoto_10(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_10)));


                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return documentBean;
    }

    @SuppressLint("Range")
    public String getImage(String table, String where, String docno, String field, String where1, String key_flag) {

        String result = null;
        String lv_type = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String selectQuery = null;
        db.beginTransaction();


        switch (table) {
            case TABLE_VERIFICATION:

                switch (field) {
                    case KEY_PHOTO_1:
                        field = KEY_PHOTO_1;
                        break;
                    case KEY_PHOTO_2:
                        field = KEY_PHOTO_2;
                        break;
                    case KEY_PHOTO_3:
                        field = KEY_PHOTO_3;
                        break;

                    case KEY_PHOTO_4:
                        field = KEY_PHOTO_4;
                        break;

                    case KEY_PHOTO_5:
                        field = KEY_PHOTO_5;
                        break;

                    case KEY_PHOTO_6:
                        field = KEY_PHOTO_6;
                        break;
                    case KEY_PHOTO_7:
                        field = KEY_PHOTO_7;
                        break;
                    case KEY_PHOTO_8:
                        field = KEY_PHOTO_8;
                        break;

                    case KEY_PHOTO_9:
                        field = KEY_PHOTO_9;
                        break;

                    case KEY_PHOTO_10:
                        field = KEY_PHOTO_10;
                        break;
                }

                break;

        }

        try {

            selectQuery = " SELECT *  FROM " + table + " WHERE " + where + " = '" + docno + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {

                        result = cursor.getString(cursor.getColumnIndex(field));
                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return result;
    }

    public void deleteEmployeeData(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablename, null, null);
    }

    public List<ImageModel> getAllLrTransportImages() {
        ArrayList<ImageModel> lrtransportImages = new ArrayList<>();
        SQLiteDatabase  database = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(database,TABLE_LR_IMAGES)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_LR_IMAGES, null);
            ImageModel imageModel;

            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();

                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setBillNo(mcursor.getString(2));
                    imageModel.setImagePath(mcursor.getString(3));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(4)));
                    lrtransportImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return lrtransportImages;
    }

    public List<ImageModel> getAssignedDeliveryImages() {
        ArrayList<ImageModel> Images = new ArrayList<>();
        SQLiteDatabase  database = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(database,TABLE_LR_IMAGES)) {
            Cursor cursor = database.rawQuery(" SELECT * FROM " + TABLE_LR_IMAGES, null);
            ImageModel imageModel;

            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();

                    imageModel = new ImageModel();
                    imageModel.setID(cursor.getString(0));
                    imageModel.setName(cursor.getString(1));
                    imageModel.setBillNo(cursor.getString(2));
                    imageModel.setImagePath(cursor.getString(3));
                    imageModel.setImageSelected(Boolean.parseBoolean(cursor.getString(4)));
                    Images.add(imageModel);
                }
            }
            cursor.close();
            database.close();
        }
        return Images;
    }

    public void updateLrImagesRecord(String name, String path, boolean isSelected, String sapbillno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGES_NAME, name);
        values.put(KEY_IMAGES_PATH, path);
        values.put(KEY_IMAGES_SELECTED, isSelected);
        values.put(KEY_IMAGES_BILL_NO, sapbillno);
        db.update(TABLE_LR_IMAGES,values,"imagesName = '"+name+"'",null);
        db.close();
    }

    public void insertLrImage(String name, String path, boolean isSelected, String sapbillno) {

        SQLiteDatabase  database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_IMAGES_NAME, name);
        contentValues.put(KEY_IMAGES_PATH, path);
        contentValues.put(KEY_IMAGES_SELECTED, isSelected);
        contentValues.put(KEY_IMAGES_BILL_NO, sapbillno);
        database.insert(TABLE_LR_IMAGES, null, contentValues);
        database.close();
    }

    @SuppressLint("Range")
    public AssignedDeliveryDetailResponse getAssignedDeliveryListValue(String billNo) {
        AssignedDeliveryDetailResponse assignedDeliveryResponse = null;

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_ASSIGNED_DELIVERIES + " WHERE " + KEY_BILL_NO + " = '" + billNo + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        assignedDeliveryResponse = new AssignedDeliveryDetailResponse();
                        assignedDeliveryResponse.setBillNo(cursor.getString(cursor.getColumnIndex(KEY_BILL_NO)));
                        assignedDeliveryResponse.setzLRno(cursor.getString(cursor.getColumnIndex(KEY_LR_NO)));
                        assignedDeliveryResponse.setzBookDate(cursor.getString(cursor.getColumnIndex(KEY_BOOK_DATE)));
                        assignedDeliveryResponse.setzMobile(cursor.getString(cursor.getColumnIndex(KEY_MOBILE)));
                        assignedDeliveryResponse.setViaAddress(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_VIA_ADDRESS)));
                        assignedDeliveryResponse.setTransporterName(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_DELIVERY_TRANSPORTER_NAME)));
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return assignedDeliveryResponse;
    }
}
