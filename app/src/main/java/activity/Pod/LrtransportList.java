package activity.Pod;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.administrator.shaktiTransportApp.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import activity.PodBean.ImageModel;
import activity.PodBean.LrInvoiceResponse;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

@SuppressWarnings("deprecation")
public class LrtransportList extends AppCompatActivity {
    private Context mContext;
    private List<LrInvoiceResponse> mLrInvoiceResponse;
    List<ImageModel> imageList = new ArrayList<>();
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiTransport";
    DatabaseHelper db;
    TextView txtSubmitePhotoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrtransport_list);
        mContext = this;
        initView();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void initView() {

        WebURL.mSapBillNumber  ="";

        List<Boolean> mLrInvoiceSelectionCheck = new ArrayList<>();
        RelativeLayout rlvAddDEviceViewID = findViewById(R.id.rlvAddDEviceViewID);
        RelativeLayout rlvBackViewID = findViewById(R.id.rlvBackViewID);
        RecyclerView rclyTranportListView = findViewById(R.id.rclyTranportListView);

        txtSubmitePhotoID =  findViewById(R.id.txtSubmitePhotoID);

        rclyTranportListView.setLayoutManager(new LinearLayoutManager(this));

        mLrInvoiceResponse = Collections.unmodifiableList((List<LrInvoiceResponse>) getIntent().getSerializableExtra("InvoiceList"));

        System.out.println("mLrInvoiceResponse.size()2==>>"+mLrInvoiceResponse.size());

        for (int i = 0; i < mLrInvoiceResponse.size(); i++) {

            mLrInvoiceSelectionCheck.add(false);

        }


        TransportListAdapter mTransportListAdapter = new TransportListAdapter(mContext, mLrInvoiceResponse, mLrInvoiceSelectionCheck);
        rclyTranportListView.setAdapter(mTransportListAdapter);

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + "/SKTR/BLT/"); //it is my root directory

        File billno = new File(root.getAbsolutePath() + "/SKTR/BLT/" + mLrInvoiceResponse.get(0).getLrno()+mLrInvoiceResponse.get(0).getMobno()); // it is my sub folder directory .. it can vary..

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!billno.exists()) {
                billno.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }


        rlvBackViewID.setOnClickListener(v -> finish());

        rlvAddDEviceViewID.setOnClickListener(V-> {
            boolean isSelected = false;
            String lrNo = null, sapBillno = null;

            for (int i = 0; i < mLrInvoiceResponse.size(); i++) {

                if(mLrInvoiceSelectionCheck.get(i)){
                    lrNo =  mLrInvoiceResponse.get(i).getLrno();
                    sapBillno = mLrInvoiceResponse.get(i).getSapBillno();
                    isSelected = true;
                }
            }


            if(isSelected){
                Intent intent = new Intent(LrtransportList.this, LrtransportImages.class);
                intent.putExtra("lrNo",lrNo);
                intent.putExtra("sapBillNo",sapBillno );

                startActivity(intent);
            }
            else {
                Toast.makeText(mContext, getResources().getString(R.string.select_Lr), Toast.LENGTH_SHORT).show();
            }

        });

        txtSubmitePhotoID.setOnClickListener(v -> {

            if (CustomUtility.isInternetOn()) {

                Log.e("Images==>",CustomUtility.getSharedPreferences(getApplicationContext(), "lrImages" ));

                if((CustomUtility.getSharedPreferences(getApplicationContext(), "lrImages" ).equalsIgnoreCase("1")))
                {
                    new SyncInstallationData1().execute();
                    db.deleteTableData(DatabaseHelper.TABLE_LR_IMAGES);
                }
                else
                {
                    Toast.makeText(mContext, "Please Select all Photos", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(mContext, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        retriveArrayList();
    }


    private void retriveArrayList() {

        imageList = new ArrayList<>();
        db = new DatabaseHelper(this);

        List<ImageModel> lrTransportImages = db.getAllLrTransportImages();
        Log.e("lrTransportImages",""+lrTransportImages.size());
        for (int i = 0; i < lrTransportImages.size(); i++) {

            ImageModel imageModel = new ImageModel();
            imageModel.setName(lrTransportImages.get(i).getName());
            imageModel.setBillNo(lrTransportImages.get(i).getBillNo());
            imageModel.setImagePath(lrTransportImages.get(i).getImagePath());
            imageModel.setImageSelected(true);
            imageList.add(imageModel);
        }
        Log.e("imageList.size()","2==="+imageList.size());

    }




    private class SyncInstallationData1 extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap;
            String invc_done;
            String obj2 = null;

            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();

            try {

                jsonObj.put("lrno", mLrInvoiceResponse.get(0).getLrno());

                jsonObj.put("sap_billno",WebURL.mSapBillNumber);

                if (imageList.size() > 0) {

                    if (imageList.get(0).isImageSelected()) {
                        jsonObj.put("PHOTO1", CustomUtility.getBase64FromBitmap(LrtransportList.this, imageList.get(0).getImagePath()));
                    }
                    if (1 < imageList.size() && imageList.get(1).isImageSelected()) {
                        jsonObj.put("PHOTO2", CustomUtility.getBase64FromBitmap(LrtransportList.this, imageList.get(1).getImagePath()));
                    }
                    if (2 < imageList.size() && imageList.get(2).isImageSelected()) {
                        jsonObj.put("PHOTO3", CustomUtility.getBase64FromBitmap(LrtransportList.this, imageList.get(2).getImagePath()));
                    }
                    if (3 < imageList.size() && imageList.get(3).isImageSelected()) {
                        jsonObj.put("PHOTO4", CustomUtility.getBase64FromBitmap(LrtransportList.this, imageList.get(3).getImagePath()));
                    }
                    if (4 < imageList.size() && imageList.get(4).isImageSelected()) {
                        jsonObj.put("PHOTO5", CustomUtility.getBase64FromBitmap(LrtransportList.this, imageList.get(4).getImagePath()));
                    }
                    if (5 < imageList.size() && imageList.get(5).isImageSelected()) {
                        jsonObj.put("PHOTO6", CustomUtility.getBase64FromBitmap(LrtransportList.this, imageList.get(5).getImagePath()));
                    }
                    if (6 < imageList.size() && imageList.get(6).isImageSelected()) {
                        jsonObj.put("PHOTO7", CustomUtility.getBase64FromBitmap(LrtransportList.this, imageList.get(6).getImagePath()));
                    }
                }

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }

            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
            param1_invc.add(new BasicNameValuePair("lr_save", String.valueOf(ja_invc_data)));///array name lr_save
            Log.e("DATA", "$$$$" + param1_invc);

            System.out.println(param1_invc);

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.API_LR_PHOTO_SAVE, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (!obj2.equals("")) {

                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_return");

                    JSONArray ja = new JSONArray(obj1);

                    System.out.println("vikas_ja.toString()"+ ja);
                    Log.e("OUTPUT2", "&&&&" + ja);

                    for (int i = 0; i < ja.length(); i++) {

                        try {
                            JSONObject jo = ja.getJSONObject(i);

                            docno_sap = jo.getString("lrno");
                            System.out.println("docno_sap==>>"+docno_sap);
                            invc_done = jo.getString("return");

                            //   if (invc_done.equalsIgnoreCase("Y"))
                            if (invc_done.equalsIgnoreCase("yes"))
                            {
                                Message msg = new Message();
                                msg.obj = "Data Submitted Successfully...";
                                mHandler.sendMessage(msg);

                                progressDialog.dismiss();
                                finish();

                                //  } else if (invc_done.equalsIgnoreCase("N"))
                            } else if (invc_done.equalsIgnoreCase("no"))
                            {

                                Message msg = new Message();
                                msg.obj = "Data Not Submitted, Please try After Sometime.";
                                mHandler.sendMessage(msg);
                                progressDialog.dismiss();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }


        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            onResume();
            progressDialog.dismiss();  // dismiss dialog


        }
    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(LrtransportList.this, mString, Toast.LENGTH_LONG).show();

        }
    };
}
