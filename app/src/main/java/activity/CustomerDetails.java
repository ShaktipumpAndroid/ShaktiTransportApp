package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.administrator.shaktiTransportApp.R;
import com.kyanogen.signatureview.SignatureView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import activity.languagechange.LocaleHelper;
import adapter.Controller;
import bean.DocumentBean;
import bean.LoginBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CameraUtils;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class CustomerDetails extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    private static final String IMAGE_DIRECTORY = "/signdemo";
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static String imageStoragePath;
    String rfq_no = null;
    String lati = null;
    String longi = null;
    String fullAddress = null;
    SimpleDateFormat simpleDateFormat = null;
    String current_date;
    String usertype;
    SignatureView signatureView;
    String path;
    Bitmap bitmap;
    double inst_latitude_double,
            inst_longitude_double;
    CustomUtility customUtility = new CustomUtility();
    Context context;
    TextView photo_1,
            photo_2,
            photo_3,
            photo_4,
            photo_5,
            photo_6,
            save_exit;
    EditText et_doc_date = null;
    String photo_1_text = "",
            photo_2_text = "",
            photo_3_text = "",
            photo_4_text = "",
            photo_5_text = "",
            photo_6_text = "",
            photo_7_text = "",
            photo_8_text = "",
            photo_9_text = "",
            photo_10_text = "";
    String key_ind = null;
    String doc_date = null;
    String time = null;
    DatabaseHelper db;
    DocumentBean documentBean;
    ImageView inst_location, geoIndigation;
    boolean photo_1_flag = false,
            photo_2_flag = false,
            photo_3_flag = false,
            photo_4_flag = false,
            photo_5_flag = false;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(CustomerDetails.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;
    private Uri fileUri; // file url to store image
    private EditText sapno;

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Controller.IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }


        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        context = this;

        db = new DatabaseHelper(context);


        getLayout();
        LoginBean loginBean = new LoginBean();

        rfq_no = loginBean.getUserrfq().trim();

        saveDat();

        Log.e("RFQNUM", "&&&&" + rfq_no);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.Employee_Details));

        setData();

        if (!TextUtils.isEmpty(rfq_no)) {
            sapno.setText(rfq_no);
        }


        et_doc_date.setOnClickListener(v -> startActivityForResult(DatePickeActivity.getIntent(context, "", "", "", false), 301));


        photo_1.setOnClickListener(view -> {
            if (photo_1_text == null || photo_1_text.isEmpty()) {
                showConfirmationGallery(DatabaseHelper.KEY_PHOTO_1);
            } else {
                showConfirmationAlert(DatabaseHelper.KEY_PHOTO_1);
            }
        });

        photo_2.setOnClickListener(view -> {
            if (photo_2_text == null || photo_2_text.isEmpty()) {
                showConfirmationGallery(DatabaseHelper.KEY_PHOTO_2);
            } else {
                showConfirmationAlert(DatabaseHelper.KEY_PHOTO_2);
            }
        });

        photo_3.setOnClickListener(view -> {
            if (photo_3_text == null || photo_3_text.isEmpty()) {
                showConfirmationGallery(DatabaseHelper.KEY_PHOTO_3);
            } else {
                showConfirmationAlert(DatabaseHelper.KEY_PHOTO_3);
            }
        });

        photo_4.setOnClickListener(view -> {
            if (photo_4_text == null || photo_4_text.isEmpty()) {
                showConfirmationGallery(DatabaseHelper.KEY_PHOTO_4);
            } else {
                showConfirmationAlert(DatabaseHelper.KEY_PHOTO_4);
            }
        });

        photo_5.setOnClickListener(view -> {
            if (photo_5_text == null || photo_5_text.isEmpty()) {
                showConfirmationGallery(DatabaseHelper.KEY_PHOTO_5);
            } else {
                showConfirmationAlert(DatabaseHelper.KEY_PHOTO_5);
            }
        });

        photo_6.setOnClickListener(view -> {
            if (CustomUtility.getSharedPreferences(context, "capture").equalsIgnoreCase("1")) {
                showConfirmationAlert1();
            } else {
                Intent i = new Intent(context, Signature_View.class);
                startActivity(i);
            }
        });

        save_exit.setOnClickListener(view -> {
            if (CustomUtility.isInternetOn()) {
                progressDialog = ProgressDialog.show(CustomerDetails.this, "", getResources().getString(R.string.server));
                saveData();
                saveData1();
            } else {
                saveData();
                Toast.makeText(context, getResources().getString(R.string.Internet), Toast.LENGTH_SHORT).show();
            }
        });

        inst_location.setOnClickListener(v -> {
            if (TextUtils.isEmpty(documentBean.getLatitude()) ||
                    documentBean.getLatitude().equals("0.0") ||
                    documentBean.getLatitude().equals("null")) {
                getGpsLocation();
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle(getResources().getString(R.string.Confirmation));
                alertDialog.setMessage(getResources().getString(R.string.location));
                alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getGpsLocation();
                    }
                });
                alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_signout:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveData();
        super.onBackPressed();
    }

    public void openCamera() {
        if (CameraUtils.checkPermissions(context)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
            }
            Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        Bitmap bitmap = null;
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] byteArray = stream.toByteArray();


                if (photo_1_flag == true) {
                    photo_1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    setIcon(DatabaseHelper.KEY_PHOTO_1);
                }

                if (photo_2_flag == true) {
                    photo_2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    setIcon(DatabaseHelper.KEY_PHOTO_2);
                }

                if (photo_3_flag == true) {
                    photo_3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    setIcon(DatabaseHelper.KEY_PHOTO_3);
                }

                if (photo_4_flag == true) {
                    photo_4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    setIcon(DatabaseHelper.KEY_PHOTO_4);
                }

                if (photo_5_flag == true) {
                    photo_5_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    setIcon(DatabaseHelper.KEY_PHOTO_5);
                }


// delete file from phone
                File file = new File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
                }
            }
        } else {
            if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {

                    if (data != null) {

                        Uri selectedImageUri = data.getData();
                        String selectedImagePath = getImagePath(selectedImageUri);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;

                        try {
                            Log.e("IMAGEURI", "&&&&" + selectedImageUri);
                            if (selectedImageUri != null) {

                                Bitmap bm = BitmapFactory.decodeFile(selectedImagePath, options);
                                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                byte[] byteArray = byteArrayBitmapStream.toByteArray();

                                Log.e("BYTEARRAY", "&&&&" + byteArray.toString());

                                if (photo_1_flag == true) {
                                    photo_1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    setIcon(DatabaseHelper.KEY_PHOTO_1);
                                }

                                if (photo_2_flag == true) {
                                    photo_2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    setIcon(DatabaseHelper.KEY_PHOTO_2);
                                }

                                if (photo_3_flag == true) {
                                    photo_3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    setIcon(DatabaseHelper.KEY_PHOTO_3);
                                }

                                if (photo_4_flag == true) {
                                    photo_4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    setIcon(DatabaseHelper.KEY_PHOTO_4);
                                }

                                if (photo_5_flag == true) {
                                    photo_5_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    setIcon(DatabaseHelper.KEY_PHOTO_5);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == 301) {

                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                String finaldate = year + "-" + month + "-" + date;
                finaldate = CustomUtility.formateDate(finaldate);
                et_doc_date.setText(finaldate);
            }
        }
    }

    public void setIcon(String key) {
        switch (key) {
            case DatabaseHelper.KEY_PHOTO_1:
                if (photo_1_text == null || photo_1_text.isEmpty()) {
                    photo_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case DatabaseHelper.KEY_PHOTO_2:
                if (photo_2_text == null || photo_2_text.isEmpty()) {
                    photo_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case DatabaseHelper.KEY_PHOTO_3:
                if (photo_3_text == null || photo_3_text.isEmpty()) {

                    photo_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);

                } else {
                    photo_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case DatabaseHelper.KEY_PHOTO_4:
                if (photo_4_text == null || photo_4_text.isEmpty()) {

                    photo_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);

                } else {
                    photo_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case DatabaseHelper.KEY_PHOTO_5:
                if (photo_5_text == null || photo_5_text.isEmpty()) {

                    photo_5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);

                } else {
                    photo_5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;
        }

    }

    public void saveData() {

        doc_date = et_doc_date.getText().toString();
        lati = String.valueOf(inst_latitude_double);
        longi = String.valueOf(inst_longitude_double);

        LoginBean loginBean = new LoginBean();

        rfq_no = loginBean.getUserrfq().trim();

        DocumentBean documentBean = new DocumentBean(rfq_no,
                doc_date,
                lati,
                longi,
                photo_1_text,
                photo_2_text,
                photo_3_text,
                photo_4_text,
                photo_5_text,
                photo_6_text,
                photo_7_text,
                photo_8_text,
                photo_9_text,
                photo_10_text
        );


        db.updateDocumentData(documentBean);

    }

    private void saveDat() {

        DocumentBean documentBean = new DocumentBean(rfq_no,
                doc_date,
                lati,
                longi,
                photo_1_text,
                photo_2_text,
                photo_3_text,
                photo_4_text,
                photo_5_text,
                photo_6_text,
                photo_7_text,
                photo_8_text,
                photo_9_text,
                photo_10_text
        );

        db.insertDocumentData(documentBean);

    }

    public void saveData1() {
        LoginBean loginBean = new LoginBean();

        rfq_no = loginBean.getUserrfq().trim();

        documentBean = new DocumentBean();
        documentBean = db.getDocumentInformation(rfq_no);

        String pht1 = documentBean.getPhoto_1();
        String pht2 = documentBean.getPhoto_2();
        String pht3 = documentBean.getPhoto_3();
        String pht4 = documentBean.getPhoto_4();
        String pht5 = documentBean.getPhoto_5();


        if (!TextUtils.isEmpty(documentBean.getLatitude()) && !documentBean.getLatitude().equals("0.0") && !documentBean.getLatitude().equals("null")) {
            if (!TextUtils.isEmpty(pht1)) {
                if (!TextUtils.isEmpty(pht2)) {
                    if (!TextUtils.isEmpty(pht3)) {
                        if (!TextUtils.isEmpty(pht4)) {
                            if (CustomUtility.getSharedPreferences(context, "capture").equalsIgnoreCase("1")) {
                                if (!TextUtils.isEmpty(pht5)) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            SyncInvoiceDataToSap(context, rfq_no);

                                        }
                                    }).start();
                                    //employeeDetails(lifnr, pernr, docdate, doctime, lat, lon, pht1, pht2, pht3, pht4, pht5, address);
                                } else {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    Toast.makeText(this, getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                Toast.makeText(this, getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(this, getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Toast.makeText(this, getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    Toast.makeText(this, getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
                }

            } else {
                if (progressDialog != null)
                    progressDialog.dismiss();
                Toast.makeText(this, getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (progressDialog != null)
                progressDialog.dismiss();
            Toast.makeText(this, getResources().getString(R.string.please_fill), Toast.LENGTH_SHORT).show();
        }
    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();
            if (inst_latitude_double == 0.0) {
                CustomUtility.ShowToast(getResources().getString(R.string.LatLong_captured), context);
            } else {
                geoIndigation.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));
                getAddress(context, inst_latitude_double, inst_longitude_double);
                CustomUtility.ShowToast(getResources().getString(R.string.Latitude) + inst_latitude_double + "     " + getResources().getString(R.string.Longitude) + inst_longitude_double, context);
            }
        } else {
            gps.showSettingsAlert();
        }
    }

    private void displayImage(String key) {
        Intent i_display_image = new Intent(context, ShowDocument.class);
        Bundle extras = new Bundle();


        saveData();
        extras.putString("docno", rfq_no);
        extras.putString("key_table", DatabaseHelper.TABLE_VERIFICATION);
        extras.putString("key_where", DatabaseHelper.KEY_INST_DOC);
        extras.putString("key_ind", key_ind);
        extras.putString("key_field", key);


        i_display_image.putExtras(extras);
        startActivity(i_display_image);
    }

    public void getLayout() {

        sapno = (EditText) findViewById(R.id.sapno);

        signatureView = (SignatureView) findViewById(R.id.signature_view);
        inst_location = (ImageView) findViewById(R.id.loaction);

        geoIndigation = (ImageView) findViewById(R.id.geoIndigation);

        et_doc_date = (EditText) findViewById(R.id.doc_date);
        photo_1 = (TextView) findViewById(R.id.photo_1);
        photo_2 = (TextView) findViewById(R.id.photo_2);
        photo_3 = (TextView) findViewById(R.id.photo_3);
        photo_4 = (TextView) findViewById(R.id.photo_4);
        photo_5 = (TextView) findViewById(R.id.photo_5);
        photo_6 = (TextView) findViewById(R.id.photo_6);
        save_exit = (TextView) findViewById(R.id.save_exit);
    }

    private void setdata1() {
        if (CustomUtility.getSharedPreferences(context, "capture").equalsIgnoreCase("0")) {
            photo_6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
        } else {
            photo_6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
        }
    }

    private void setData() {

        LoginBean loginBean = new LoginBean();

        rfq_no = loginBean.getUserrfq().trim();

        documentBean = new DocumentBean();
        documentBean = db.getDocumentInformation(rfq_no);


        if (!TextUtils.isEmpty(documentBean.getLatitude()) && !documentBean.getLatitude().equals("0.0") && !documentBean.getLatitude().equals("null")) {
            geoIndigation.setImageDrawable(getResources().getDrawable(R.drawable.right_mark_icn_green));

            inst_latitude_double = Double.valueOf(documentBean.getLatitude());
            inst_longitude_double = Double.valueOf(documentBean.getLongitude());

            getAddress(context, inst_latitude_double, inst_longitude_double);
        } else {
            geoIndigation.setImageDrawable(getResources().getDrawable(R.drawable.red_icn));
        }


        photo_1_text = documentBean.getPhoto_1();
        photo_2_text = documentBean.getPhoto_2();
        photo_3_text = documentBean.getPhoto_3();
        photo_4_text = documentBean.getPhoto_4();
        photo_5_text = documentBean.getPhoto_5();


        if (TextUtils.isEmpty(documentBean.getDoc_date()) || documentBean.getDoc_date().equals("null")) {
            documentBean.setDoc_date(customUtility.getCurrentDate());
        }

        Log.e("DATE", "%%%%" + customUtility.getCurrentDate());

        doc_date = documentBean.getDoc_date();
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());

        et_doc_date.setText(current_date);
        setIcon(DatabaseHelper.KEY_PHOTO_1);
        setIcon(DatabaseHelper.KEY_PHOTO_2);
        setIcon(DatabaseHelper.KEY_PHOTO_3);
        setIcon(DatabaseHelper.KEY_PHOTO_4);
        setIcon(DatabaseHelper.KEY_PHOTO_5);

    }

    public void setFlag(String key) {
        photo_1_flag = false;
        photo_2_flag = false;
        photo_3_flag = false;
        photo_4_flag = false;
        photo_5_flag = false;

        switch (key) {
            case DatabaseHelper.KEY_PHOTO_1:
                photo_1_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO_2:
                photo_2_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO_3:
                photo_3_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO_4:
                photo_4_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO_5:
                photo_5_flag = true;
                break;
        }

    }

    public void showConfirmationAlert1() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.Confirmation));
        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.Signature));
        // On pressing Settings button

        alertDialog.setNegativeButton(getResources().getString(R.string.change), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(context, Signature_View.class);
                startActivity(i);

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showConfirmationAlert(final String keyimage) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.Confirmation));
        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.Signature));
        // On pressing Settings button
        alertDialog.setPositiveButton(getResources().getString(R.string.display), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                displayImage(keyimage);
            }
        });

        alertDialog.setNegativeButton(getResources().getString(R.string.change), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showConfirmationGallery(keyimage);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showConfirmationGallery(final String keyimage) {
        final CustomUtility customUtility = new CustomUtility();
        final CharSequence[] items = {getResources().getString(R.string.camera), getResources().getString(R.string.gallery), getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = customUtility.checkPermission(context);
                if (items[item].equals("Take Photo")) {
                    if (result) {
                        openCamera();
                        setFlag(keyimage);
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    if (result) {
                        openGallery();
                        setFlag(keyimage);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Photo"), GALLERY_IMAGE_REQUEST_CODE);
    }

    public String getImagePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        } else {
            String[] projection = {String.valueOf(MediaStore.Images.Media.DATA)};


            Cursor cursor1 = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
            Cursor cursor2 = getContentResolver().query(uri, projection, null, null, null);

            Log.e("CUR1", "&&&&" + cursor1);
            Log.e("CUR2", "&&&&" + cursor2);

            if (cursor1 == null && cursor2 == null) {
                return null;
            } else {

                int column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int column_index1 = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor1.moveToFirst();
                cursor2.moveToFirst();

                String s = null;
                if (cursor1.moveToFirst()) {
                    s = cursor1.getString(column_index);
                } else if (cursor2.moveToFirst()) {
                    s = cursor2.getString(column_index1);
                }

                cursor1.close();
                cursor2.close();
                return s;
            }
        }
    }

    public void SyncInvoiceDataToSap(final Context context, String enq_docno) {


        this.context = context;

        String docno_sap = null;
        String invc_done = null;


        DatabaseHelper db = new DatabaseHelper(this.context);

        ArrayList<DocumentBean> param_invc = new ArrayList<DocumentBean>();

        param_invc = db.getDocumentInformation1(enq_docno);


        if (param_invc.size() > 0) {

            JSONArray ja_invc_data = new JSONArray();

            for (int i = 0; i < param_invc.size(); i++) {


                JSONObject jsonObj = new JSONObject();

                LoginBean lb = new LoginBean();

                try {

                    jsonObj.put("pernr", param_invc.get(i).getInst_docno());
                    jsonObj.put("begda", customUtility.getCurrentDate());

                    jsonObj.put("time", customUtility.getCurrentTime());
                    jsonObj.put("lat", param_invc.get(i).getLatitude());

                    jsonObj.put("lng", param_invc.get(i).getLongitude());
                    jsonObj.put("photo1", param_invc.get(i).getPhoto_1());
                    jsonObj.put("photo2", param_invc.get(i).getPhoto_2());
                    jsonObj.put("photo3", param_invc.get(i).getPhoto_3());
                    jsonObj.put("photo4", param_invc.get(i).getPhoto_4());
                    jsonObj.put("photo5", param_invc.get(i).getPhoto_5());
                    jsonObj.put("address", fullAddress);
                    ja_invc_data.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("driver_data", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {
                String obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param1_invc);

                if (obj2 != "") {

                    JSONArray ja = new JSONArray(obj2);

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);

                        docno_sap = jo.getString("docno");
                        invc_done = jo.getString("quote_done");
                        if (invc_done.equalsIgnoreCase("Y")) {

                            progressDialog.dismiss();
                            Message msg = new Message();
                            msg.obj = getResources().getString(R.string.Successfully);
                            mHandler.sendMessage(msg);

                            LoginBean loginBean = new LoginBean();

                            usertype = loginBean.getUsertype().trim();

                            logout();

                            //finish();
                        } else {
                            progressDialog.dismiss();
                            Message msg = new Message();
                            msg.obj = getResources().getString(R.string.notSubmitted);
                            mHandler.sendMessage(msg);
                        }


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;

            addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null && addresses.size() > 0) {

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                fullAddress = address;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public void logout() {


        db.deleteTableData(DatabaseHelper.TABLE_LOGIN);

        if (usertype.equalsIgnoreCase("Vendor")) {
            db.deleteTableData(DatabaseHelper.TABLE_QUOTATION);
        } else if (usertype.equalsIgnoreCase("Driver")) {
            db.deleteTableData(DatabaseHelper.TABLE_VERIFICATION);
        } else {
            db.deleteTableData(DatabaseHelper.TABLE_RFQ);
        }

        CustomUtility.setSharedPreference(context, "capture", "0");
        String selectedFilePath = "/storage/emulated/0/signdemo/signature.jpg";
        File file = new File(selectedFilePath);
        boolean deleted = file.delete();
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setdata1();
    }
}
