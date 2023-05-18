package webservice;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bean.LoginBean;
import bean.QuotationBean;
import bean.RfqBean;
import bean.RouteBean;
import database.DatabaseHelper;


public class SAPWebService {

    //get user id of person
    String userid = LoginBean.getUseid();
    String usertype = LoginBean.getUsertype();


    RouteBean routeBean;


    String country,
            country_text,
            state,
            state_text,
            district,
            district_text,
            tehsil,
            tehsil_text;

    String docno_sap;
    String manual_docno;
    String rfq_done;
    String rfq_docno_sap;
    String quote_done;

    public int getStateData(Context context) {

        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        try {

            String obj = CustomHttpClient.executeHttpPost1(WebURL.STATE_DATA, param);

            JSONArray ja_state = new JSONArray(obj);

            dataHelper.deleteTableData(DatabaseHelper.TABLE_STATE_SEARCH);

            for (int i = 0; i < ja_state.length(); i++) {

                JSONObject jo_state = ja_state.getJSONObject(i);

                country = jo_state.optString("country");
                country_text = jo_state.optString("countrytext");
                state = jo_state.optString("state");
                state_text = jo_state.optString("statetext");
                district = jo_state.optString("district");
                district_text = jo_state.optString("districttext");
                tehsil = jo_state.optString("tehsil");
                tehsil_text = jo_state.optString("tehsiltext");

                dataHelper.insertStateData(country, country_text, state, state_text, district, district_text, tehsil, tehsil_text);
            }

        } catch (Exception e) {
            Log.d("msg", "" + e);
        }
        return progressBarStatus;
    }

    public int getRouteData(Context context) {

        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);


        final ArrayList<NameValuePair> param_route = new ArrayList<NameValuePair>();


        param_route.add(new BasicNameValuePair("pernr", userid));
        param_route.add(new BasicNameValuePair("objs", usertype));

        try {

            String obj = CustomHttpClient.executeHttpPost1(WebURL.ROUTE_DATA, param_route);

            JSONArray ja_route = new JSONArray(obj);


            for (int i = 0; i < ja_route.length(); i++) {


                JSONObject jo_route = ja_route.getJSONObject(i);

                routeBean = new RouteBean(
                        jo_route.optString("fr_state_text"),
                        jo_route.optString("fr_district_text"),
                        jo_route.optString("fr_tehsil_text"),
                        jo_route.optString("to_state_text"),
                        jo_route.optString("to_district_text"),
                        jo_route.optString("to_tehsil_text"),
                        jo_route.optString("tr_mode_text"),
                        jo_route.optString("distance"),
                        jo_route.optString("status"),
                        "",
                        ""
                );


                if (dataHelper.checkRecord(DatabaseHelper.TABLE_ROUTE,
                        DatabaseHelper.KEY_FR_TEHSIL_TEXT,
                        routeBean.getFr_tehsil(),
                        DatabaseHelper.KEY_TO_TEHSIL_TEXT,
                        routeBean.getTo_tehsil())) {

                    dataHelper.insertRouteData(dataHelper.KEY_UPDATE_ROUTE_FROM_SAP, routeBean);
                } else {
                    dataHelper.insertRouteData(dataHelper.KEY_INSERT_ROUTE, routeBean);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressBarStatus;
    }

    public int getRfqData(Context context) {
        int progressBarStatus = 0;
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        final ArrayList<NameValuePair> param_rfq = new ArrayList<NameValuePair>();
        param_rfq.add(new BasicNameValuePair("pernr", userid));
        param_rfq.add(new BasicNameValuePair("objs", usertype));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.RFQ_DATA, param_rfq);
            JSONArray ja_rfq = new JSONArray(obj);
            for (int i = 0; i < ja_rfq.length(); i++) {
                JSONObject jo_rfq = ja_rfq.getJSONObject(i);
                RfqBean rfqBean = new RfqBean(
                        jo_rfq.optString("rfq_doc"),
                        jo_rfq.optString("rfq_doc_manual"),
                        jo_rfq.optString("res_name"),
                        jo_rfq.optString("fr_location"),
                        jo_rfq.optString("to_location"),
                        jo_rfq.optString("fr_latlong"),
                        jo_rfq.optString("to_latlong"),
                        jo_rfq.optString("via_address"),
                        jo_rfq.optString("via_address_latlong"),
                        jo_rfq.optString("tr_mode"),
                        jo_rfq.optString("distance"),
                        jo_rfq.optString("vehicle_type"),
                        jo_rfq.optString("vehicle_weight"),
                        jo_rfq.optString("final_rate"),
                        jo_rfq.optString("delivery_place"),
                        jo_rfq.optString("transit_time"),
                        jo_rfq.optString("rfq_date"),
                        jo_rfq.optString("rfq_time"),
                        jo_rfq.optString("expiry_date"),
                        jo_rfq.optString("expiry_time"),
                        jo_rfq.optString("dala"),
                        jo_rfq.optString("height"),
                        jo_rfq.optString("sync"),
                        jo_rfq.optString("rfq_completed"),
                        jo_rfq.optString("rfq_status"),
                        jo_rfq.optString("res_pernr"),
                        jo_rfq.optString("res_pernr_type"),
                        jo_rfq.optString("vehicle_rc"),
                        jo_rfq.optString("confirmed"));

                if (dataHelper.isRecordExist(DatabaseHelper.TABLE_RFQ, DatabaseHelper.KEY_RFQ_DOC, rfqBean.getRfq_doc())) {
                    dataHelper.updateRfqData(dataHelper.KEY_RFQ_UPDATE_FROM_SAP, rfqBean);
                } else {
                    dataHelper.updateRfqData(dataHelper.KEY_INSERT_RFQ, rfqBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressBarStatus;
    }

    public int getQuotationData(Context context) {
        int progressBarStatus = 0;
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        final ArrayList<NameValuePair> param_quote = new ArrayList<NameValuePair>();
        param_quote.add(new BasicNameValuePair("pernr", userid));
        param_quote.add(new BasicNameValuePair("objs", usertype));
        try {
            String obj_quote = CustomHttpClient.executeHttpPost1(WebURL.QUOTATION_DATA, param_quote);
            JSONArray ja_quote = new JSONArray(obj_quote);
            for (int i = 0; i < ja_quote.length(); i++) {
                JSONObject jo_quote = ja_quote.getJSONObject(i);
                QuotationBean quoteBean = new QuotationBean(
                        jo_quote.optString("rfq_doc"),
                        jo_quote.optString("rate"),
                        jo_quote.optString("remark"),
                        jo_quote.optString("vehicle_rc"),
                        jo_quote.optString("vehicle_make"),
                        jo_quote.optString("puc"),
                        jo_quote.optString("insurance"),
                        jo_quote.optString("licence"),
                        jo_quote.optString("quote_completed"),
                        jo_quote.optString("quote_status"),
                        jo_quote.optString("res_pernr"),
                        jo_quote.optString("res_pernr_type"),
                        jo_quote.optString("sync")
                );
                if (dataHelper.isRecordExist(DatabaseHelper.TABLE_QUOTATION, DatabaseHelper.KEY_RFQ_DOC, quoteBean.getRfq_doc())) {
                    dataHelper.updateQuotationData(dataHelper.KEY_QUOTATION_UPDATE_FROM_SAP, quoteBean);
                } else {
                    dataHelper.updateQuotationData(dataHelper.KEY_INSERT_QUOTATION, quoteBean);
                }
            }
        } catch (Exception e) {

        }
        return progressBarStatus;
    }

    public int getApprovaData(Context context) {

        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);

        final ArrayList<NameValuePair> param_quote = new ArrayList<NameValuePair>();

        param_quote.add(new BasicNameValuePair("pernr", userid));
        param_quote.add(new BasicNameValuePair("objs", usertype));

        try {

            String obj_quote = CustomHttpClient.executeHttpPost1(WebURL.QUOTATION_DATA, param_quote);

            JSONArray ja_quote = new JSONArray(obj_quote);


            for (int i = 0; i < ja_quote.length(); i++) {

                JSONObject jo_quote = ja_quote.getJSONObject(i);

                QuotationBean quoteBean = new QuotationBean(
                        jo_quote.optString("rfq_doc"),
                        jo_quote.optString("rate"),
                        jo_quote.optString("remark"),
                        jo_quote.optString("vehicle_rc"),
                        jo_quote.optString("vehicle_make"),
                        jo_quote.optString("puc"),
                        jo_quote.optString("insurance"),
                        jo_quote.optString("licence"),
                        jo_quote.optString("quote_completed"),
                        jo_quote.optString("quote_status"),
                        jo_quote.optString("res_pernr"),
                        jo_quote.optString("res_pernr_type"),
                        jo_quote.optString("sync")
                );


                if (dataHelper.isRecordExist(DatabaseHelper.TABLE_QUOTATION, DatabaseHelper.KEY_RFQ_DOC, quoteBean.getRfq_doc())) {

                    dataHelper.updateQuotationData(dataHelper.KEY_QUOTATION_UPDATE_FROM_SAP, quoteBean);
                } else {
                    dataHelper.updateQuotationData(dataHelper.KEY_INSERT_QUOTATION, quoteBean);
                }

            }
        } catch (Exception e) {

        }
        return progressBarStatus;
    }


    public int SyncOfflineDataToSap(Context context) {

        int progressBarStatus = 0;

        DatabaseHelper db = new DatabaseHelper(context);
        String enqdocno_sap = null;
        String manual_enqdocno = null;
        String manual_enqdocno_flag = null;
        String survey_done = null;


        LoginBean loginBean = new LoginBean();

        String usertype = loginBean.getUsertype().trim();

        if ("Vendor".equals(usertype)) {

////////////////////////////////////////////Quotation Data/////////////////////////////////////////////////////////////
            ArrayList<QuotationBean> param_quote = new ArrayList<QuotationBean>();
            param_quote = db.getQuoteList(DatabaseHelper.KEY_QUOTE_COMPLETED);

            if (param_quote.size() > 0) {

                JSONArray ja_quote_data = new JSONArray();

                for (int i = 0; i < param_quote.size(); i++) {

                    JSONObject jsonObj_quote = new JSONObject();

                    try {


                        jsonObj_quote.put(DatabaseHelper.KEY_RFQ_DOC, param_quote.get(i).getRfq_doc());
                        jsonObj_quote.put(DatabaseHelper.KEY_VEHICLE_MAKE, param_quote.get(i).getVehicle_make());
                        jsonObj_quote.put(DatabaseHelper.KEY_VEHICLE_RC, param_quote.get(i).getVehicle_rc());
                        jsonObj_quote.put(DatabaseHelper.KEY_RATE, param_quote.get(i).getRate());
                        jsonObj_quote.put(DatabaseHelper.KEY_REMARK, param_quote.get(i).getRemark());
                        jsonObj_quote.put(DatabaseHelper.KEY_PUC, param_quote.get(i).getPuc());
                        jsonObj_quote.put(DatabaseHelper.KEY_INSURANCE, param_quote.get(i).getInsurance());
                        jsonObj_quote.put(DatabaseHelper.KEY_LICENCE, param_quote.get(i).getLicence());
                        jsonObj_quote.put(DatabaseHelper.KEY_RFQ_COMPLETED, param_quote.get(i).getQuote_completed());
                        jsonObj_quote.put(DatabaseHelper.KEY_RES_PERNR, param_quote.get(i).getRes_pernr());
                        jsonObj_quote.put(DatabaseHelper.KEY_RES_PERNR_TYPE, param_quote.get(i).getRes_pernr_type());

                        ja_quote_data.put(jsonObj_quote);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                final ArrayList<NameValuePair> param_quote1 = new ArrayList<NameValuePair>();
                param_quote1.add(new BasicNameValuePair("QUOTE_DATA", String.valueOf(ja_quote_data)));


                try {
                    String obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param_quote1);
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


        } else {
////////////////////////////////////////////RFQ Data/////////////////////////////////////////////////////////////

            ArrayList<RfqBean> param_rfq = new ArrayList<RfqBean>();
            param_rfq = db.getRfqList(DatabaseHelper.KEY_RFQ_COMPLETED);

            if (param_rfq.size() > 0) {

                JSONArray ja_rfq_data = new JSONArray();

                for (int i = 0; i < param_rfq.size(); i++) {

                    JSONObject jsonObj_rfq = new JSONObject();

                    try {


                        jsonObj_rfq.put(DatabaseHelper.KEY_RFQ_DOC, param_rfq.get(i).getRfq_doc());
                        jsonObj_rfq.put(DatabaseHelper.KEY_RFQ_DOC_MANUAL, param_rfq.get(i).getRfq_doc_manual());
                        jsonObj_rfq.put(DatabaseHelper.KEY_FR_LOCATION, param_rfq.get(i).getFr_location());
                        jsonObj_rfq.put(DatabaseHelper.KEY_FR_LATLONG, param_rfq.get(i).getFr_latlong());
                        jsonObj_rfq.put(DatabaseHelper.KEY_TO_LOCATION, param_rfq.get(i).getTo_location());
                        jsonObj_rfq.put(DatabaseHelper.KEY_TO_LATLONG, param_rfq.get(i).getTo_latlong());
                        jsonObj_rfq.put(DatabaseHelper.KEY_DISTANCE, param_rfq.get(i).getDistance());
                        jsonObj_rfq.put(DatabaseHelper.KEY_TR_MODE, param_rfq.get(i).getTr_mode());
                        jsonObj_rfq.put(DatabaseHelper.KEY_VEHICLE_TYPE, param_rfq.get(i).getVehicle_type());
                        jsonObj_rfq.put(DatabaseHelper.KEY_VEHICLE_WEIGHT, param_rfq.get(i).getVehicle_weight());
                        jsonObj_rfq.put(DatabaseHelper.KEY_DELIVERY_PLACE, param_rfq.get(i).getDelivery_place());
                        jsonObj_rfq.put(DatabaseHelper.KEY_TRANSIT_TIME, param_rfq.get(i).getTransit_time());
                        jsonObj_rfq.put(DatabaseHelper.KEY_RFQ_DATE, param_rfq.get(i).getRfq_date());
                        jsonObj_rfq.put(DatabaseHelper.KEY_EXPIRY_DATE, param_rfq.get(i).getExpiry_date());
                        jsonObj_rfq.put(DatabaseHelper.KEY_EXPIRY_TIME, param_rfq.get(i).getExpiry_time());
                        jsonObj_rfq.put("dala", param_rfq.get(i).getDala());
                        jsonObj_rfq.put("height", param_rfq.get(i).getHeight());

                        jsonObj_rfq.put(DatabaseHelper.KEY_RFQ_TIME, param_rfq.get(i).getRfq_time());

                        jsonObj_rfq.put(DatabaseHelper.KEY_RFQ_STATUS, param_rfq.get(i).getRfq_status());
                        jsonObj_rfq.put(DatabaseHelper.KEY_RFQ_COMPLETED, param_rfq.get(i).getRfq_completed());
                        jsonObj_rfq.put(DatabaseHelper.KEY_RES_PERNR, param_rfq.get(i).getRes_pernr());
                        jsonObj_rfq.put(DatabaseHelper.KEY_RES_PERNR_TYPE, param_rfq.get(i).getRes_pernr_type());

                        ja_rfq_data.put(jsonObj_rfq);

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
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////


        return progressBarStatus;
    }

}



