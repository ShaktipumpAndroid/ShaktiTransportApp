package activity;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.administrator.shaktiTransportApp.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import activity.PodBean.AssignedDeliveryDetailResponse;
import activity.PodBean.DeliveryDeliveredInput;
import activity.PodBean.ImageModel;
import bean.LoginBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import utility.ImageManager;
import webservice.CameraUtils;
import webservice.CustomHttpClient;
import webservice.WebURL;

@SuppressWarnings("deprecation")
public class AssignedDeliveryDetailActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    DatabaseHelper db;
    List<ImageModel> imageList = new ArrayList<>();
    Context context;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private AssignedDeliveryDetailResponse assignedDeliveryResponse;
    public static final int BITMAP_SAMPLE_SIZE = 4;
    private String rfqNoValue, billNoVlaue,imageStoragePath, photo1_text, photo2_text, photo3_text, photo4_text, photo5_text, photo6_text;
    private TextView rfqNo, vehNo, confirmationDate, confirmationTime, transporterName, transporterCode, fromLocValue, toLocValue,
            viaAddress, vehicle_type, distance, delivery_place, transit_time, rfq_date, rfq_time, tvLeft, tvRight, tvFront,
            tvIrCopy, tvTop, tvMatrecp;
    private ImageView mobile_image;
    private TextView billno,Lr,book,contact,address, transport_name_value;
    private ImageView call;
    private Button btnSubmit;
    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusumUnload";
    private boolean photo1, photo2, photo3, photo4, photo5, photo6;
    RelativeLayout rlvAddDEviceViewID;
    String mImageFolderName = "/SKAPP/UNLOAD/";
    String type = "UNLOAD/";
    double inst_latitude_double, inst_longitude_double;
    LinearLayout original,layout;

    @SuppressWarnings("deprecation")
    @SuppressLint({"HandlerLeak", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detail);
        context = this;
        rlvAddDEviceViewID = findViewById(R.id.rlvAddDEviceViewID);
        WebURL.GALLERY_DIRECTORY_NAME_COMMON = "ShaktiKusumUnload";
        db = new DatabaseHelper(context);
        getGpsLocation();
        inItView();

        layout =findViewById(R.id.partialLayout);
        original =findViewById(R.id.oldlayout);
        Bundle bundle = getIntent().getExtras();
        rfqNoValue = bundle.getString("rfqNoValue");
        billNoVlaue = bundle.getString("billNo");
        Log.e("rfqNoValue",rfqNoValue);
        Log.e("billNoVlaue",billNoVlaue);
        setDataOnView();
        WebURL.CUSTOMERID_ID = LoginBean.getUseid();

        if (rfqNoValue.equalsIgnoreCase("") ){
            layout.setVisibility(View.VISIBLE);
            original.setVisibility(View.GONE);
        }else{
            layout.setVisibility(View.GONE);
            original.setVisibility(View.VISIBLE);
        }



        btnSubmit.setOnClickListener(v -> {


                if (CustomUtility.isInternetOn()) {
                    if((CustomUtility.getSharedPreferences(getApplicationContext(), "lrImages" ).equalsIgnoreCase("1"))) {

                        if (!rfqNoValue.equalsIgnoreCase("") ) {

                            submitAssignedDeliveryDeliveredData();

                    }
                    else {
                        Log.e("Images==>",CustomUtility.getSharedPreferences(getApplicationContext(), "lrImages" ));

                           sendDatatoSAP();


                    }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please select all Images", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();
                }

        });


        rlvAddDEviceViewID.setOnClickListener(V-> {
            String sapBillno = assignedDeliveryResponse.getBillNo() ;
                Intent intent = new Intent(AssignedDeliveryDetailActivity.this, AssignedDeliveryImages.class);
                intent.putExtra("sapBillNo",sapBillno );
                startActivity(intent);

        });
        RelativeLayout rlvBackViewID = findViewById(R.id.rlvBackViewID);
        rlvBackViewID.setOnClickListener(v -> finish());
    }



    private void inItView() {
        rfqNo =  findViewById(R.id.rfq_no);
        vehNo =  findViewById(R.id.veh_no);
        confirmationDate =  findViewById(R.id.confirmation_date);
        confirmationTime =  findViewById(R.id.confirmation_time);
        transporterName =  findViewById(R.id.transporter_name);
        transporterCode =  findViewById(R.id.transporter_code);
        fromLocValue =  findViewById(R.id.from_loc_value);
        toLocValue =  findViewById(R.id.to_loc_value);
        viaAddress =  findViewById(R.id.viaAddress);
        vehicle_type =  findViewById(R.id.vehicle_type);
        distance =  findViewById(R.id.distance);
        delivery_place =  findViewById(R.id.delivery_place);
        transit_time =  findViewById(R.id.transit_time);
        rfq_date =  findViewById(R.id.rfq_date);
        rfq_time =  findViewById(R.id.rfq_time);
        transport_name_value = findViewById(R.id.transport_name_value);
        mobile_image = findViewById(R.id.mobile_image);

        billno = findViewById(R.id.bill_no_value);
        contact = findViewById(R.id.contact_value);
        Lr = findViewById(R.id.Lr_no_value);
        book = findViewById(R.id.booking_value);
        address = findViewById(R.id.address_value);
        call = findViewById(R.id.mobile_image);

        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void setDataOnView() {

        if(rfqNoValue.equalsIgnoreCase("")){
            assignedDeliveryResponse = db.getAssignedDeliveryListValue(billNoVlaue);

            billno.setText(assignedDeliveryResponse.getBillNo());
            contact.setText(assignedDeliveryResponse.getzMobile());
            Lr.setText(assignedDeliveryResponse.getzLRno());
            book.setText(assignedDeliveryResponse.getzBookDate());
            address.setText(assignedDeliveryResponse.getViaAddress());
            transport_name_value.setText(assignedDeliveryResponse.getTransporterName());

            mobile_image.setOnClickListener(v -> {

                if (!TextUtils.isEmpty(assignedDeliveryResponse.getzMobile())) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 222);
                        } else {
                            Make_Call(assignedDeliveryResponse.getzMobile());
                        }

                    } else {
                        Make_Call(assignedDeliveryResponse.getzMobile());
                    }
                }
                Toast.makeText(context, "Calling....", Toast.LENGTH_SHORT).show();
            });
        }
        else {
            assignedDeliveryResponse = db.getAssignedDeliveryDetail(rfqNoValue);
            rfqNo.setText(assignedDeliveryResponse.getRfqDoc());
            vehNo.setText(assignedDeliveryResponse.getVehicleRc());
            confirmationDate.setText(assignedDeliveryResponse.getConfDate());
            confirmationTime.setText(assignedDeliveryResponse.getConfTime());
            transporterCode.setText(assignedDeliveryResponse.getTransportCode());
            transporterName.setText(assignedDeliveryResponse.getTransporterName());
            fromLocValue.setText(assignedDeliveryResponse.getFrLocation());
            toLocValue.setText(assignedDeliveryResponse.getToLocation());
            viaAddress.setText(assignedDeliveryResponse.getViaAddress());
            vehicle_type.setText(assignedDeliveryResponse.getVehicleType());
            distance.setText(assignedDeliveryResponse.getDistance());
            delivery_place.setText(assignedDeliveryResponse.getDeliveryPlace());
            transit_time.setText(assignedDeliveryResponse.getTransitTime());
            rfq_date.setText(assignedDeliveryResponse.getRfqDate());
            rfq_time.setText(assignedDeliveryResponse.getRfqTime());

            if (db.isRecordExist(DatabaseHelper.TABLE_ASSIGNED_DELIVERY_DELIVERED, DatabaseHelper.KEY_DELIVERY_RFQ_DOC, assignedDeliveryResponse.getRfqDoc())) {
                DeliveryDeliveredInput deliveryDeliveredInput = db.getDeliveredDetail(assignedDeliveryResponse.getRfqDoc());
                photo1_text = deliveryDeliveredInput.getPhoto1();
                setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO1);
                photo2_text = deliveryDeliveredInput.getPhoto2();
                setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO2);
                photo3_text = deliveryDeliveredInput.getPhoto3();
                setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO3);
                photo4_text = deliveryDeliveredInput.getPhoto4();
                setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO4);
                photo5_text = deliveryDeliveredInput.getPhoto5();
                setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO5);
                photo6_text = deliveryDeliveredInput.getPhoto6();
                setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO6);
            }

        }


    }


    private void Make_Call(String mobile) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
           // new Capture_employee_gps_location(context, "12", mobile);
            intent.setData(Uri.parse("tel:" + mobile));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();

        retriveArrayList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
     }
        return super.onOptionsItemSelected(item);
    }

    private void showConfirmationGallery(final String keyimage, final String name) {
        new CustomUtility();
        final CharSequence[] items = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            boolean result = CustomUtility.checkPermission(context);
            if (items[item].equals("Take Photo")) {
                if (result) {
                    openCamera(name);
                    setFlag(keyimage);
                }
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void openCamera(String name) {
        if (CameraUtils.checkPermissions(context)) {
            File file = new File(ImageManager.getMediaFilePath(type, name, WebURL.CUSTOMERID_ID));
            imageStoragePath = file.getAbsolutePath();
            Log.e("PATH", "&&&" + imageStoragePath);
            Intent i = new Intent(context, CameraActivity.class);
            i.putExtra("lat", String.valueOf(inst_latitude_double));
            i.putExtra("lng", String.valueOf(inst_longitude_double));
            i.putExtra("cust_name", assignedDeliveryResponse.getRfqDoc());
            i.putExtra("inst_id", WebURL.CUSTOMERID_ID);
            i.putExtra("type", type);
            i.putExtra("name", name);
            startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    private void setFlag(String key) {
        photo1 = false;
        photo2 = false;
        photo3 = false;
        photo4 = false;
        photo5 = false;
        photo6 = false;

        switch (key) {
            case DatabaseHelper.KEY_DELIVERY_PHOTO1:
                photo1 = true;
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO2:
                photo2 = true;
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO3:
                photo3 = true;
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO4:
                photo4 = true;
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO5:
                photo5 = true;
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO6:
                photo6 = true;
                break;
        }
    }

    private boolean validationCheck() {
        boolean returnValue = false;
        if (null == photo1_text || photo1_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Click Left side Image", Toast.LENGTH_SHORT).show();
        } else if (null == photo2_text || photo2_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Click Right side Image", Toast.LENGTH_SHORT).show();
        } else if (null == photo3_text || photo3_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Click Top side Image", Toast.LENGTH_SHORT).show();
        } else if (null == photo4_text || photo4_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Click Front side Image", Toast.LENGTH_SHORT).show();
        } else if (null == photo5_text || photo5_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Click Lrcopy Image", Toast.LENGTH_SHORT).show();
        } else if (null == photo6_text || photo6_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Click Matrecp Image", Toast.LENGTH_SHORT).show();
        } else {
            returnValue = true;
        }
        return returnValue;
    }

    private void saveDeliveredData() {
        db.getLogin();
        String driverMob = LoginBean.getUseid();
        DeliveryDeliveredInput deliveryDeliveredInput = new DeliveryDeliveredInput();
        deliveryDeliveredInput.setRfqDoc(assignedDeliveryResponse.getRfqDoc());
        deliveryDeliveredInput.setResPernr(assignedDeliveryResponse.getTransportCode());
        deliveryDeliveredInput.setBillNo(assignedDeliveryResponse.getBillNo());
        deliveryDeliveredInput.setTransNo(assignedDeliveryResponse.getTransNo());
        deliveryDeliveredInput.setAssignMob(assignedDeliveryResponse.getAssignMob());
        deliveryDeliveredInput.setDriverMob(driverMob);
        deliveryDeliveredInput.setPhoto1(photo1_text);
        deliveryDeliveredInput.setPhoto2(photo2_text);
        deliveryDeliveredInput.setPhoto3(photo3_text);
        deliveryDeliveredInput.setPhoto4(photo4_text);
        deliveryDeliveredInput.setPhoto5(photo5_text);
        deliveryDeliveredInput.setPhoto6(photo6_text);
        if (db.isRecordExist(db.TABLE_ASSIGNED_DELIVERY_DELIVERED, db.KEY_DELIVERY_RFQ_DOC, assignedDeliveryResponse.getRfqDoc())) {
            db.updateDeliveryDeliveredData(deliveryDeliveredInput);
        } else {
            db.insertDeliveryDeliveredData(deliveryDeliveredInput);
        }
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

    private void sendDatatoSAP() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        AssignedDeliveryDetailResponse sendAssignedDeliveryResponse = db.getAssignedDeliveryListValue(billNoVlaue);

        progressDialog = ProgressDialog.show(AssignedDeliveryDetailActivity.this, "", "Connecting to server..please wait !");
        Log.e("login",CustomUtility.getSharedPreferences(context,"Userid"));
        String mobile = CustomUtility.getSharedPreferences(context,"Userid");
        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        JSONArray ja_invc_data = new JSONArray();
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("rfq_doc", sendAssignedDeliveryResponse.getRfqDoc());
                            jsonObj.put("driver_mob", mobile);
                            jsonObj.put("res_pernr",sendAssignedDeliveryResponse.getTransportCode());
                            if (imageList.size() > 0) {

                                if (imageList.get(0).isImageSelected()) {
                                    jsonObj.put("lrcopy_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(0).getImagePath()));
                                }
                                if (1 < imageList.size() && imageList.get(1).isImageSelected()) {
                                    jsonObj.put("left_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(1).getImagePath()));
                                }
                                if (2 < imageList.size() && imageList.get(2).isImageSelected()) {
                                    jsonObj.put("right_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(2).getImagePath()));
                                }
                                if (3 < imageList.size() && imageList.get(3).isImageSelected()) {
                                    jsonObj.put("top_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(3).getImagePath()));
                                }
                                if (4 < imageList.size() && imageList.get(4).isImageSelected()) {
                                    jsonObj.put("front_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(4).getImagePath()));
                                }
                                if (5 < imageList.size() && imageList.get(5).isImageSelected()) {
                                    jsonObj.put("matrecp_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(5).getImagePath()));
                                }

                            }

                            jsonObj.put("bill_no", sendAssignedDeliveryResponse.getBillNo());
                            jsonObj.put("lrno", sendAssignedDeliveryResponse.getzLRno());
                            jsonObj.put("trans_no",sendAssignedDeliveryResponse.getTransNo());
                            jsonObj.put("assign_mob",mobile);
                            ja_invc_data.put(jsonObj);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
                        param1_invc.add(new BasicNameValuePair("final", String.valueOf(ja_invc_data)));
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.SUBMIT_ASSIGNED_DELIVERY_DELIVERED, param1_invc);
                        Log.e("Response====>",obj);

                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObject = new JSONObject(obj.trim());
                            boolean status = jsonObject.getBoolean("status");
                            Log.e("status",""+jsonObject.optString("status"));
                            if (status) {
                                CameraUtils.deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + CameraUtils.DELIVERED_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID));
                                db.deleteTableData(DatabaseHelper.TABLE_LR_IMAGES);
                            }
                            Message msg = new Message();
                            msg.obj = jsonObject.optString("message");
                            mHandler.sendMessage(msg);
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Message msg = new Message();
                        msg.obj = "No Internet Connection";
                        mHandler.sendMessage(msg);
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    @SuppressLint("SuspiciousIndentation")
    private void submitAssignedDeliveryDeliveredData() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        DeliveryDeliveredInput deliveryDeliveredInput = db.getDeliveredDetail(assignedDeliveryResponse.getRfqDoc());
        String mobile = CustomUtility.getSharedPreferences(context,"Userid");

        progressDialog = ProgressDialog.show(AssignedDeliveryDetailActivity.this, "", "Connecting to server..please wait !");

        Log.e("login",CustomUtility.getSharedPreferences(context,"Userid"));

        new Thread() {
            public void run() {
                try {
                    if (CustomUtility.isInternetOn()) {
                        JSONArray ja_invc_data = new JSONArray();
                        JSONObject jsonObj = new JSONObject();

                        try {
                            jsonObj.put("rfq_doc",   assignedDeliveryResponse.getRfqDoc());
                            jsonObj.put("driver_mob",  mobile );
                            jsonObj.put("res_pernr",   assignedDeliveryResponse.getTransportCode());
                            if (imageList.size() > 0) {

                                if (imageList.get(0).isImageSelected()) {
                                    jsonObj.put("lrcopy_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(0).getImagePath()));
                                }
                                if (1 < imageList.size() && imageList.get(1).isImageSelected()) {
                                    jsonObj.put("left_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(1).getImagePath()));
                                }
                                if (2 < imageList.size() && imageList.get(2).isImageSelected()) {
                                    jsonObj.put("right_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(2).getImagePath()));
                                }
                                if (3 < imageList.size() && imageList.get(3).isImageSelected()) {
                                    jsonObj.put("top_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(3).getImagePath()));
                                }
                                if (4 < imageList.size() && imageList.get(4).isImageSelected()) {
                                    jsonObj.put("front_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(4).getImagePath()));
                                }
                                if (5 < imageList.size() && imageList.get(5).isImageSelected()) {
                                    jsonObj.put("matrecp_photo", CustomUtility.getBase64FromBitmap(AssignedDeliveryDetailActivity.this, imageList.get(5).getImagePath()));
                                }

                            }
                            jsonObj.put("bill_no",   assignedDeliveryResponse.getBillNo());
                            jsonObj.put("trans_no",   assignedDeliveryResponse.getTransNo());
                            jsonObj.put("assign_mob",   assignedDeliveryResponse.getAssignMob());
                          /*  jsonObj.put("lrno", "");*/

                            ja_invc_data.put(jsonObj);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
                        param1_invc.add(new BasicNameValuePair("final", String.valueOf(ja_invc_data)));
                        String obj = CustomHttpClient.executeHttpPost1(WebURL.SUBMIT_ASSIGNED_DELIVERY_DELIVERED, param1_invc);

                        Log.e("Response====>",obj);

                        if (!obj.equalsIgnoreCase("")) {
                            JSONObject jsonObject = new JSONObject(obj.trim());
                            boolean status = jsonObject.getBoolean("status");
                            Log.e("status","SAP"+jsonObject.optString("status"));

                            if (status) {
                                db.deleteDeliveredData(deliveryDeliveredInput.getRfqDoc());
                                CameraUtils.deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + CameraUtils.DELIVERED_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID));
                                db.deleteDeliveryData(deliveryDeliveredInput.getRfqDoc());
                            }
                            Message msg = new Message();
                            msg.obj = jsonObject.optString("message");
                            mHandler.sendMessage(msg);
                            finish();
                            Log.e("status","SAP"+jsonObject.optString("message"));
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Message msg = new Message();
                        msg.obj = "No Internet Connection";
                        mHandler.sendMessage(msg);
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    private void resetScreen() {
        photo1_text = null;
        photo2_text = null;
        photo3_text = null;
        photo4_text = null;
        photo5_text = null;
        photo6_text = null;
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
        tvRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
        tvTop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
        tvFront.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
        tvIrCopy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
        tvMatrecp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (resultCode == RESULT_OK){
           if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
               try {
                   Log.e("Count", "&&&&&" + imageStoragePath);
                   Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
                   int count = bitmap.getByteCount();
                   Log.e("Count", "&&&&&" + count);
                   Log.e("IMAGEURI", "&&&&" + imageStoragePath);
                   ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                   bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                   byte[] byteArray = byteArrayBitmapStream.toByteArray();
                   long size = byteArray.length;
                   Log.e("SIZE1234", "&&&&" + size);
                   Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

                   if (photo1 == true) {
                       File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_1.jpg");
                       if (file.exists()) {
                           photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                           setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO1);
                       }
                   } else if (photo2 == true) {
                       File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_2.jpg");
                       if (file1.exists()) {
                           photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                           setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO2);
                       }
                   } else if (photo3 == true) {
                       File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_3.jpg");
                       if (file2.exists()) {
                           photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                           setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO3);
                       }
                   } else if (photo4 == true) {
                       File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_4.jpg");
                       if (file.exists()) {
                           photo4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                           setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO4);
                       }
                   } else if (photo5 == true) {
                       File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_5.jpg");
                       if (file1.exists()) {
                           photo5_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                           setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO5);
                       }
                   } else if (photo6 == true) {
                       File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + WebURL.CUSTOMERID_ID, "/IMG_PHOTO_6.jpg");
                       if (file2.exists()) {
                           photo6_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                           setIcon(DatabaseHelper.KEY_DELIVERY_PHOTO6);
                       }
                   }
               } catch (NullPointerException e) {
                   e.printStackTrace();
               }
           }
       }
    }

    public void setIcon(String key) {
        switch (key) {
            case DatabaseHelper.KEY_DELIVERY_PHOTO1:
                if (photo1_text == null || photo1_text.isEmpty()) {
                    tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO2:
                if (photo2_text == null || photo2_text.isEmpty()) {
                    tvRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    tvRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO3:
                if (photo3_text == null || photo3_text.isEmpty()) {
                    tvTop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    tvTop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO4:
                if (photo4_text == null || photo4_text.isEmpty()) {
                    tvFront.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    tvFront.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO5:
                if (photo5_text == null || photo5_text.isEmpty()) {
                    tvIrCopy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    tvIrCopy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
            case DatabaseHelper.KEY_DELIVERY_PHOTO6:
                if (photo6_text == null || photo6_text.isEmpty()) {
                    tvMatrecp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    tvMatrecp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
        }
    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(AssignedDeliveryDetailActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
}
