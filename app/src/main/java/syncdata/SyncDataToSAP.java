package syncdata;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bean.QuotationBean;
import bean.RfqBean;
import bean.RouteBean;
import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.WebURL;

/**
 * Created by shakti on 11/21/2016.
 */
public class SyncDataToSAP {

    Context context = null;

    String docno_sap;
    String manual_docno;
    String rfq_done;
    String rfq_docno_sap;
    String quote_done;

    public void SyncRouteDataToSap(Context context, String fr_tehsil, String to_tehsil) {


        this.context = context;


        DatabaseHelper db = new DatabaseHelper(this.context);
        ArrayList<RouteBean> param_route = new ArrayList<RouteBean>();

        param_route = db.getRouteList(DatabaseHelper.KEY_PENDING_ROUTE, fr_tehsil, to_tehsil);

        if (param_route.size() > 0) {

            JSONArray ja_route_data = new JSONArray();

            for (int i = 0; i < param_route.size(); i++) {

                JSONObject jsonObj = new JSONObject();

                try {

                    jsonObj.put(DatabaseHelper.KEY_FR_STATE_TEXT, param_route.get(i).getFr_state());
                    jsonObj.put(DatabaseHelper.KEY_FR_DISTRICT_TEXT, param_route.get(i).getFr_district());
                    jsonObj.put(DatabaseHelper.KEY_FR_TEHSIL_TEXT, param_route.get(i).getFr_tehsil());
                    jsonObj.put(DatabaseHelper.KEY_TO_STATE_TEXT, param_route.get(i).getTo_state());
                    jsonObj.put(DatabaseHelper.KEY_TO_DISTRICT_TEXT, param_route.get(i).getTo_district());
                    jsonObj.put(DatabaseHelper.KEY_TO_TEHSIL_TEXT, param_route.get(i).getTo_tehsil());

                    jsonObj.put(DatabaseHelper.KEY_DISTANCE, param_route.get(i).getDistance());
                    jsonObj.put(DatabaseHelper.KEY_TR_MODE, param_route.get(i).getTr_mode());
                    jsonObj.put(DatabaseHelper.KEY_STATUS, param_route.get(i).getStatus());
                    jsonObj.put(DatabaseHelper.KEY_RES_PERNR, param_route.get(i).getRes_pernr());
                    jsonObj.put(DatabaseHelper.KEY_RES_PERNR_TYPE, param_route.get(i).getRes_pernr_type());


                    ja_route_data.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
            param1.add(new BasicNameValuePair("ROUTE_DATA", String.valueOf(ja_route_data)));

        }
    }


    public void SyncRfqDataToSap(Context context, String rfq_docno) {


        this.context = context;


        DatabaseHelper db = new DatabaseHelper(this.context);
        ArrayList<RfqBean> param_rfq = new ArrayList<RfqBean>();

        param_rfq = db.getRfq(rfq_docno);

        if (param_rfq.size() > 0) {

            JSONArray ja_rfq_data = new JSONArray();

            for (int i = 0; i < param_rfq.size(); i++) {

                JSONObject jsonObj = new JSONObject();

                try {


                    jsonObj.put(DatabaseHelper.KEY_RFQ_DOC, param_rfq.get(i).getRfq_doc());
                    jsonObj.put(DatabaseHelper.KEY_RFQ_DOC_MANUAL, param_rfq.get(i).getRfq_doc_manual());
                    jsonObj.put("pernr", param_rfq.get(i).getPernr());
                    jsonObj.put("name", param_rfq.get(i).getPernr_name());
                    jsonObj.put(DatabaseHelper.KEY_FR_LOCATION, param_rfq.get(i).getFr_location());
                    jsonObj.put(DatabaseHelper.KEY_FR_LATLONG, param_rfq.get(i).getFr_latlong());
                    jsonObj.put(DatabaseHelper.KEY_TO_LOCATION, param_rfq.get(i).getTo_location());
                    jsonObj.put(DatabaseHelper.KEY_TO_LATLONG, param_rfq.get(i).getTo_latlong());
                    jsonObj.put(DatabaseHelper.KEY_DISTANCE, param_rfq.get(i).getDistance());
                    jsonObj.put(DatabaseHelper.KEY_TR_MODE, param_rfq.get(i).getTr_mode());
                    jsonObj.put(DatabaseHelper.KEY_VEHICLE_TYPE, param_rfq.get(i).getVehicle_type());
                    jsonObj.put(DatabaseHelper.KEY_VIA_ADDRESS, param_rfq.get(i).getVia_address());
                    jsonObj.put(DatabaseHelper.KEY_VEHICLE_WEIGHT, param_rfq.get(i).getVehicle_weight());
                    jsonObj.put(DatabaseHelper.KEY_DELIVERY_PLACE, param_rfq.get(i).getDelivery_place());
                    jsonObj.put(DatabaseHelper.KEY_TRANSIT_TIME, param_rfq.get(i).getTransit_time());

                    jsonObj.put(DatabaseHelper.KEY_RFQ_DATE, param_rfq.get(i).getRfq_date());
                    jsonObj.put(DatabaseHelper.KEY_EXPIRY_DATE, param_rfq.get(i).getExpiry_date());
                    jsonObj.put(DatabaseHelper.KEY_EXPIRY_TIME, param_rfq.get(i).getExpiry_time());
                    jsonObj.put(DatabaseHelper.KEY_RFQ_TIME, param_rfq.get(i).getRfq_time());
                    jsonObj.put("dala", param_rfq.get(i).getDala());
                    jsonObj.put("height", param_rfq.get(i).getHeight());
                    jsonObj.put(DatabaseHelper.KEY_RFQ_STATUS, param_rfq.get(i).getRfq_status());
                    jsonObj.put(DatabaseHelper.KEY_RFQ_COMPLETED, param_rfq.get(i).getRfq_completed());
                    jsonObj.put(DatabaseHelper.KEY_RES_PERNR, param_rfq.get(i).getRes_pernr());
                    jsonObj.put(DatabaseHelper.KEY_RES_PERNR_TYPE, param_rfq.get(i).getRes_pernr_type());


                    ja_rfq_data.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            final ArrayList<NameValuePair> param_rfq1 = new ArrayList<NameValuePair>();
            param_rfq1.add(new BasicNameValuePair("RFQ_DATA", String.valueOf(ja_rfq_data)));


            try {
                String obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param_rfq1);

                if (obj2 != "") {

                    JSONArray ja = new JSONArray(obj2);

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);


                        docno_sap = jo.getString("docno");
                        manual_docno = jo.getString("manual_docno");
                        rfq_done = jo.getString("rfq_done");


                        RfqBean rfqBean = new RfqBean();


                        rfqBean = db.getRfqInformation(manual_docno);


                        if ("Y".equals(rfq_done)) {
                            rfqBean.setSync("X");
                            rfqBean.setRfq_completed(" ");
                            rfqBean.setRfq_doc(docno_sap);
                        }

                        db.updateRfqData(DatabaseHelper.KEY_RFQ_SYNC, rfqBean);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void SyncQuotationDataToSap(Context context, String rfq_docno) {

        this.context = context;

        DatabaseHelper db = new DatabaseHelper(this.context);
        ArrayList<QuotationBean> param_quote = new ArrayList<QuotationBean>();

        param_quote = db.getQuote(rfq_docno);

        if (param_quote.size() > 0) {

            JSONArray ja_quote_data = new JSONArray();

            for (int i = 0; i < param_quote.size(); i++) {

                JSONObject jsonObjQuote = new JSONObject();

                try {

                    jsonObjQuote.put(DatabaseHelper.KEY_RFQ_DOC, param_quote.get(i).getRfq_doc());
                    jsonObjQuote.put(DatabaseHelper.KEY_VEHICLE_MAKE, param_quote.get(i).getVehicle_make());
                    jsonObjQuote.put(DatabaseHelper.KEY_VEHICLE_RC, param_quote.get(i).getVehicle_rc());
                    jsonObjQuote.put(DatabaseHelper.KEY_RATE, param_quote.get(i).getRate());
                    jsonObjQuote.put(DatabaseHelper.KEY_REMARK, param_quote.get(i).getRemark());
                    jsonObjQuote.put(DatabaseHelper.KEY_PUC, param_quote.get(i).getPuc());
                    jsonObjQuote.put(DatabaseHelper.KEY_INSURANCE, param_quote.get(i).getInsurance());
                    jsonObjQuote.put(DatabaseHelper.KEY_QUOTE_STATUS, param_quote.get(i).getQuote_status());
                    jsonObjQuote.put(DatabaseHelper.KEY_LICENCE, param_quote.get(i).getLicence());
                    jsonObjQuote.put(DatabaseHelper.KEY_RFQ_COMPLETED, param_quote.get(i).getQuote_completed());
                    jsonObjQuote.put(DatabaseHelper.KEY_RES_PERNR, param_quote.get(i).getRes_pernr());
                    jsonObjQuote.put(DatabaseHelper.KEY_RES_PERNR_TYPE, param_quote.get(i).getRes_pernr_type());


                    ja_quote_data.put(jsonObjQuote);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            final ArrayList<NameValuePair> param1_quote = new ArrayList<NameValuePair>();
            param1_quote.add(new BasicNameValuePair("QUOTE_DATA", String.valueOf(ja_quote_data)));


            try {
                String obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param1_quote);

                if (obj2 != "") {

                    JSONArray ja_quote = new JSONArray(obj2);

                    for (int i = 0; i < ja_quote.length(); i++) {

                        JSONObject jo_quote = ja_quote.getJSONObject(i);


                        rfq_docno_sap = jo_quote.getString("docno");
                        quote_done = jo_quote.getString("quote_done");


                        QuotationBean quoteBean = new QuotationBean();


                        quoteBean = db.getQuotationInformation(rfq_docno_sap);


                        if ("Y".equals(quote_done)) {
                            quoteBean.setSync("X");
                            quoteBean.setQuote_completed(" ");
                        }

                        db.updateQuotationData(DatabaseHelper.KEY_QUOTE_SYNC, quoteBean);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}



